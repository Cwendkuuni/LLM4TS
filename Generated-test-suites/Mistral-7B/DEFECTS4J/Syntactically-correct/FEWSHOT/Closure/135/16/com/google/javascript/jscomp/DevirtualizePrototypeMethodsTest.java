package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilationUnit;
import com.google.javascript.jscomp.CompilationUnitTestBase;
import com.google.javascript.jscomp.DefinitionsRemover.Definition;
import com.google.javascript.jscomp.DefinitionsRemover.DefinitionSite;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SimpleDefinitionFinder;
import com.google.javascript.jscomp.SimpleDefinitionFinder.UseSite;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ObjectType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DevirtualizePrototypeMethodsTest extends CompilationUnitTestBase {

  private AbstractCompiler compiler;
  private CompilationUnit unit;
  private SimpleDefinitionFinder defFinder;
  private DevirtualizePrototypeMethods devirtualizer;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = mock(AbstractCompiler.class);
    unit = new CompilationUnit();
    unit.setCompiler(compiler);
    defFinder = new SimpleDefinitionFinder(compiler);
    defFinder.process(unit.getExterns(), unit.getRoot());
    devirtualizer = new DevirtualizePrototypeMethods(compiler);
  }

  @Test
  public void testRewritePrototypeMethod() {
    String code = "var A = function() { this.accumulate = function(value) { this.total += value; return this.total } }; A.prototype.accumulate = function(value) { this.total += value; return this.total }; var a = new A(); a.total = 0; var total = a.accumulate(2);";
    unit.setCode(code);
    devirtualizer.process(unit.getExterns(), unit.getRoot());

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    DefinitionSite definitionSite = definitionSites.get(0);
    Definition definition = definitionSite.definition;

    assertNotNull(definition);
    assertEquals("function(self, value) { self.total += value; return self.total }", definition.getCode());
    assertEquals("A", definition.getFileName());
    assertEquals(1, definition.getParameterCount());
    assertEquals("value", definition.getParameterName(0));
    assertEquals(JSTypeNative.NUMBER_TYPE, definition.getReturnType());

    Node accumulateNode = definition.getNode();
    assertEquals("function(self, value)", accumulateNode.getClass().getName());

    List<UseSite> useSites = defFinder.getUseSites(definition);
    UseSite useSite = useSites.get(0);
    Node useNode = useSite.node;

    assertEquals("total", useSite.variableName);
    assertEquals(1, useSite.arguments.size());
    assertEquals("a", useSite.variable.getName());
    assertEquals("A", useSite.variable.getFileName());

    Node node = useNode.getParent();
    assertEquals("total = accumulate(a, 2)", node.getClass().getName());
  }

  @Test
  public void testRewritePrototypeMethodWithStaticMethod() {
    String code = "var A = function() {}; A.prototype.accumulate = function(value) { this.total += value; return this.total }; A.staticAccumulate = function(instance, value) { instance.total += value; return instance.total }; var a = new A(); a.total = 0; var total = a.accumulate(2); var staticTotal = A.staticAccumulate(a, 3);";
    unit.setCode(code);
    devirtualizer.process(unit.getExterns(), unit.getRoot());

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    DefinitionSite definitionSite = definitionSites.get(0);
    Definition definition = definitionSite.definition;

    assertNotNull(definition);
    assertEquals("function(self, value) { self.total += value; return self.total }", definition.getCode());
    assertEquals("A", definition.getFileName());
    assertEquals(1, definition.getParameterCount());
    assertEquals("value", definition.getParameterName(0));
    assertEquals(JSTypeNative.NUMBER_TYPE, definition.getReturnType());

    Definition staticDefinition = definitionSites.get(1).definition;
    assertEquals("function(self, value) { self.total += value; return self.total }", staticDefinition.getCode());
    assertEquals("A", staticDefinition.getFileName());
    assertEquals("A.staticAccumulate", staticDefinition.getName());
    assertEquals(2, staticDefinition.getParameterCount());
    assertEquals("instance", staticDefinition.getParameterName(0));
    assertEquals("value", staticDefinition.getParameterName(1));
    assertEquals(JSTypeNative.NUMBER_TYPE, staticDefinition.getReturnType());

    Node accumulateNode = definition.getNode();
    assertEquals("function(self, value)", accumulateNode.getClass().getName());

    Node staticAccumulateNode = staticDefinition.getNode();
    assertEquals("function(self, value)", staticAccumulateNode.getClass().getName());

    List<UseSite> useSites = defFinder.getUseSites(definition);
    UseSite useSite = useSites.get(0);
    Node useNode = useSite.node;

    assertEquals("total", useSite.variableName);
    assertEquals(1, useSite.arguments.size());
    assertEquals("a", useSite.variable.getName());
    assertEquals("A", useSite.variable.getFileName());

    Node staticUseNode = useSites.get(1).node;
    assertEquals("staticTotal = A.staticAccumulate(a, 3)", staticUseNode.getClass().getName());
  }

  @Test
  public void testRewritePrototypeMethodWithMissingDefinition() {
    String code = "var A = function() {}; A.prototype.accumulate = function(value) { this.total += value; return this.total }; var a = new A(); a.total = 0; var total = a.accumulate(2);";
    unit.setCode(code);
    devirtualizer.process(unit.getExterns(), unit.getRoot());

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    assertNull(definitionSites.get(0).definition);
  }

  @Test
  public void testRewritePrototypeMethodWithMissingUse() {
    String code = "var A = function() {}; A.prototype.accumulate = function(value) { this.total += value; return this.total };";
    unit.setCode(code);
    devirtualizer.process(unit.getExterns(), unit.getRoot());

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    Definition definition = definitionSites.get(0).definition;
    assertNotNull(definition);
    assertEquals("function() { Error: no such variable: total }", definition.getCode());
  }

  @Test
  public void testRewritePrototypeMethodWithDuplicateDefinitions() {
    String code = "var A = function() {}; A.prototype.accumulate = function(value) { this.total += value; return this.total }; A.prototype.accumulate = function(value) { this.total += value; return this.total }; var a = new A(); a.total = 0; var total = a.accumulate(2);";
    unit.setCode(code);
    devirtualizer.process(unit.getExterns(), unit.getRoot());

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    Definition definition = definitionSites.get(0).definition;
    assertEquals("function(self, value) { self.total += value; return self.total }", definition.getCode());

    List<UseSite> useSites = defFinder.getUseSites(definition);
    assertEquals(1, useSites.size());

    Node useNode = useSites.get(0).node;
    assertEquals("total", useSite.variableName);
    assertEquals(1, useSite.arguments.size());
    assertEquals("a", useSite.variable.getName());
    assertEquals("A", useSite.variable.getFileName());

    definition = definitionSites.get(1).definition;
    assertNull(definition);
  }

  @Test
  public void testRewritePrototypeMethodInModule() {
    String code = "define(function() { var A = function() {}; A.prototype.accumulate = function(value) { this.total += value; return this.total }; return A; }); var a = new A(); a.total = 0; var total = a.accumulate(2);";
    unit.setCode(code);
    devirtualizer.process(unit.getExterns(), unit.getRoot());

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    Definition definition = definitionSites.get(0).definition;
    assertEquals("function(self, value) { self.total += value; return self.total }", definition.getCode());

    List<UseSite> useSites = defFinder.getUseSites(definition);
    UseSite useSite = useSites.get(0);
    Node useNode = useSite.node;

    assertEquals("total", useSite.variableName);
    assertEquals(1, useSite.arguments.size());
    assertEquals("a", useSite.variable.getName());
    assertEquals("A", useSite.variable.getFileName());

    Map<String, Node> externs = unit.getExterns();
    Node defineNode = externs.get("define");
    Node moduleNode = defineNode.getLastChild();
    assertEquals("A", moduleNode.getLastChild().getString());
  }

  @Test
  public void testRewritePrototypeMethodWithVarArgsFunction() {
    String code = "var A = function(foo) { this.accumulate = function() { var args = arguments; var sum = 0; for (var i = 0; i < args.length; i++) { sum += args[i]; } return sum }; this.accumulate = function(values) { var sum = 0; for (var i = 0; i < values.length; i++) { sum += values[i]; } return sum }; }";
    unit.setCode(code);
    devirtualizer.process(unit.getExterns(), unit.getRoot());

    List<DefinitionSite> definitionSites = defFinder.getDefinitionSites();
    Definition definition = definitionSites.get(0).definition;
    assertEquals("function(self) { return function() { var args = arguments; var sum = 0; for (var i = 0; i < args.length; i++) { sum += args[i]; } return sum } }", definition.getCode());
  }
}