package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.REPLACE;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.SourceFile;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.Nullable;

@RunWith(JUnit4.class)
public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() throws Exception {
    String testCode =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation(REPLACE));
    options.setAliasTransformationHandler(new DummyAliasTransformationHandler());

    SourceFile sourceFile = new SourceFile("test.js", testCode);
    Compiler compiler = new Compiler();
    compiler.compile(sourceFile, options);

    Node rootNode = sourceFile.getAST();
    NodeTraversal traversal = new NodeTraversal();
    new ScopedAliases(compiler, null, null).process(null, rootNode);
    NodeUtil.tryMergeBlocks(rootNode);

    Var domVar = findVariableByName("dom", rootNode);
    Var divVar = findVariableByName("DIV", rootNode);

    assertThat(domVar.getInitialValue().isThis()).isFalse();
    assertThat(domVar.getInitialValue().getQualifiedName()).isEqualTo("goog.dom");

    assertThat(divVar.getInitialValue().isThis()).isFalse();
    assertThat(divVar.getInitialValue().getQualifiedName()).isEqualTo("goog.dom.TagName.DIV");

    Node domElementNode = findNodeByKind(rootNode, EXPR_STMT);
    Node createElementNode = domElementNode.getFirstChild();
    Node elementTypeNode = createElementNode.getLastChild();

    assertThat(elementTypeNode.isQualifiedName()).isTrue();
    assertThat(elementTypeNode.getQualifiedName().getName()).isEqualTo("DIV");
  }

  @Test
  public void testScopedAliasesWithJSDocs() throws Exception {
    String testCode =
        "/**\n" +
        " * @const\n" +
        " */\n" +
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  dom.createElement(DIV);" +
        "});";

    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation(REPLACE));
    options.setAliasTransformationHandler(new DummyAliasTransformationHandler());

    SourceFile sourceFile = new SourceFile("test.js", testCode);
    Compiler compiler = new Compiler();
    compiler.compile(sourceFile, options);

    Node rootNode = sourceFile.getAST();
    NodeTraversal traversal = new NodeTraversal();
    new ScopedAliases(compiler, null, null).process(null, rootNode);
    NodeUtil.tryMergeBlocks(rootNode);

    Var domVar = findVariableByName("dom", rootNode);
    Var divVar = findVariableByName("DIV", rootNode);

    assertThat(domVar.getInitialValue().isThis()).isFalse();
    assertThat(domVar.getInitialValue().getQualifiedName()).isEqualTo("goog.dom");

    JSDocInfo domJSDocInfo = domVar.getInitialValue().getJSDocInfo();
    assertThat(domJSDocInfo.isConstant()).isTrue();

    assertThat(divVar.getInitialValue().isThis()).isFalse();
    assertThat(divVar.getInitialValue().getQualifiedName())
        .isEqualTo("goog.dom.TagName.DIV");

    Node domElementNode = findNodeByKind(rootNode, EXPR_STMT);
    Node createElementNode = domElementNode.getFirstChild();
    Node elementTypeNode = createElementNode.getLastChild();

    assertThat(elementTypeNode.isQualifiedName()).isTrue();
    assertThat(elementTypeNode.getQualifiedName().getName()).isEqualTo("DIV");

    JSDocInfo elementJSDocInfo = elementTypeNode.getJSDocInfo();
    assertThat(elementJSDocInfo != null).isTrue();
  }

  private Node findVariableByName(String name, Node rootNode) {
    Scope globalScope = rootNode.getFirstChild();
    for (Var variable : globalScope.getVarIterable()) {
      if (variable.getName().equals(name)) {
        return variable.getNode();
      }
    }
    throw new AssertionError("Variable not found: " + name);
  }

  private Node findNodeByKind(Node rootNode, int nodeKind) {
    for (Node child = rootNode.getFirstChild(); child != null;
         child = child.getNext()) {
      if (child.getKind() == nodeKind) {
        return child;
      }
    }
    throw new AssertionError("Node not found: " + nodeKind);
  }

  private static class DummyAliasTransformationHandler
      implements AliasTransformationHandler {
    @Override
    public void logAliasTransformation(
        String fileName, SourcePosition position,
        String aliasName, String qualifiedName) {
      // no-op
    }
  }
}