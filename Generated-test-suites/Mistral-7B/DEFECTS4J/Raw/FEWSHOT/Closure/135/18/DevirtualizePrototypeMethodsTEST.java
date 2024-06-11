package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.ErrorReporter;
import com.google.javascript.jscomp.JSType;
import com.google.javascript.jscomp.JSTypeNative;
import com.google.javascript.jscomp.JSTypeRegistry;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.UseSite;
import com.google.javascript.jscomp.compiler.NodeUtil;
import com.google.javascript.rhino.NodeUtilJava8;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.List;

@RunWith(JUnit4.class)
public class DevirtualizePrototypeMethodsTest {
  private AbstractCompiler compiler;
  private Node rootNode;
  private ErrorReporter errorReporter;

  @Before
  public void setup() {
    compiler = new CompilerOptions();
    errorReporter = new ErrorReporter() {};
    rootNode = NodeUtilJava8.parse("class A {prototype.accumulate = function(value) {this.total += value; return this.total}}");
  }

  @Test
  public void testDevirtualizePrototypeMethods() {
    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);
    CompilerPassManager passManager = new CompilerPassManager(compiler, errorReporter);
    passManager.addPass(pass);
    passManager.process(rootNode, null, null);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, rootNode);
    DefinitionSite definitionSite = defFinder.getDefinitionSites().get(0);

    String rewrittenMethodName = pass.getRewrittenMethodName(
        definitionSite.definition.getRValue().getLastChild().getString());

    List<UseSite> useSites = defFinder.getUseSites(definitionSite.definition);
    for (UseSite site : useSites) {
      Node newNameNode = site.node.getFirstChild();
      assertThat(newNameNode.getString()).isEqualTo(rewrittenMethodName);
    }
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithMultipleDefinitions() {
    String source = "class A {prototype.accumulate = function(value) {this.total += value; return this.total}}" +
                    "class B {prototype.accumulate = function(value) {this.total += value; return this.total}}";
    Node rootNode = NodeUtilJava8.parse(source);

    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);
    CompilerPassManager passManager = new CompilerPassManager(compiler, errorReporter);
    passManager.addPass(pass);
    passManager.process(rootNode, null, null);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, rootNode);

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    Preconditions.checkState(definitionSites.size() == 2);

    String firstMethodName = pass.getRewrittenMethodName(
        definitionSites.get(0).definition.getRValue().getLastChild().getString());
    String secondMethodName = pass.getRewrittenMethodName(
        definitionSites.get(1).definition.getRValue().getLastChild().getString());

    List<UseSite> useSites = defFinder.getUseSites(definitionSites.get(0).definition);
    for (UseSite site : useSites) {
      Node newNameNode = site.node.getFirstChild();
      assertThat(newNameNode.getString()).isEqualTo(firstMethodName);
    }

    useSites = defFinder.getUseSites(definitionSites.get(1).definition);
    for (UseSite site : useSites) {
      Node newNameNode = site.node.getFirstChild();
      assertThat(newNameNode.getString()).isEqualTo(secondMethodName);
    }
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithNoDefinitionSites() {
    String source = "class A {}";
    Node rootNode = NodeUtilJava8.parse(source);

    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);
    CompilerPassManager passManager = new CompilerPassManager(compiler, errorReporter);
    passManager.addPass(pass);
    passManager.process(rootNode, null, null);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, rootNode);

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    assertNull(definitionSites);
  }

  @Test
  public void testDevirtualizePrototypeMethodsWithNoUseSites() {
    String source = "class A {prototype.accumulate = function(value) {this.total += value; return this.total}}";
    Node rootNode = NodeUtilJava8.parse(source);

    CompilerPass pass = new DevirtualizePrototypeMethods(compiler);
    CompilerPassManager passManager = new CompilerPassManager(compiler, errorReporter);
    passManager.addPass(pass);

    // Remove all use sites for the definition.
    for (DefinitionSite definitionSite : pass.defFinder.getDefinitionSites()) {
      if (definitionSite.definition.equals(pass.defFinder.getDefinitionSites().get(0).definition)) {
        List<UseSite> useSites = defFinder.getUseSites(definitionSite.definition);
        for (UseSite site : useSites) {
          Node parent = site.node.getParent();
          parent.removeChild(site.node);
        }
      }
    }

    passManager.process(rootNode, null, null);

    SimpleDefinitionFinder defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(null, rootNode);

    List<UseSite> useSites = defFinder.getUseSites(definitionSite.definition);
    assertNull(useSites);
  }
}