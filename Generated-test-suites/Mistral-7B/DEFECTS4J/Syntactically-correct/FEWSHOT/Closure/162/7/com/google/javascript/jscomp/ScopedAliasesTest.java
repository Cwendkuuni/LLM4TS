package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformationHandler;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ScopedAliasesTest {
  private static final String TEST_FILE = "TestScopedAliases.java";
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private ScopedAliases scopedAliases;

  @Before
  public void setUp() {
    // Setup the mock objects
    compiler = new MockCompiler();
    preprocessorSymbolTable = new MockSymbolTable();
    transformationHandler = new MockAliasTransformationHandler();
    scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable,
        transformationHandler);
  }

  @Test
  public void testEmptyScopedAliases() {
    Node rootNode = Node.createString("// empty scoped aliases block");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testSingleAliasedName() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  var " + aliasName + " = " + aliasName + ";" +
        "});");
    scopedAliases.process(null, rootNode);
    assertFalse(scopedAliases.hasErrors());
    Var aliasVar = preprocessorSymbolTable.getVariable(aliasName);
    assertSame(aliasVar, scopedAliases.aliases.get(aliasName));
  }

  @Test
  public void testMultipleAliasedNames() {
    String aliasName1 = "Alias1";
    String aliasName2 = "Alias2";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  var " + aliasName1 + " = " + aliasName1 + ";" +
        "  var " + aliasName2 + " = " + aliasName2 + ";" +
        "});");
    scopedAliases.process(null, rootNode);
    assertFalse(scopedAliases.hasErrors());
    Var aliasVar1 = preprocessorSymbolTable.getVariable(aliasName1);
    Var aliasVar2 = preprocessorSymbolTable.getVariable(aliasName2);
    assertSame(aliasVar1, scopedAliases.aliases.get(aliasName1));
    assertSame(aliasVar2, scopedAliases.aliases.get(aliasName2));
  }

  @Test
  public void testAliasedNameWithSameNameInInitializer() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  var " + aliasName + " = " + aliasName + ";" +
        "  " + aliasName + " = 42;" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testAliasedNameWithNonAliasInitializer() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  var " + aliasName + " = 42;" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testGoogScopeWithBadParent() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "var " + aliasName + " = 42;" +
        "goog.scope(function() {" +
        "  var " + aliasName + ";" + // should not redefine
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testGoogScopeWithThis() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  this." + aliasName + " = " + aliasName + ";" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testGoogScopeWithReturn() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  return " + aliasName + ";" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testGoogScopeWithThrow() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  throw " + aliasName + ";" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testGoogScopeWithInvalidParameters() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function(" + aliasName + ") {" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testGoogScopeWithExtraStatement() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  var " + aliasName + " = " + aliasName + ";" +
        "  // extra statement" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testApplyAliases() {
    String aliasName1 = "Alias1";
    String aliasName2 = "Alias2";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  var " + aliasName1 + " = " + aliasName1 + ";" +
        "  var " + aliasName2 + " = " + aliasName2 + ";" +
        "});");
    scopedAliases.process(null, rootNode);
    // No errors, so continue

    Node expectedNode1 = Node.createString("var Alias1 = goog.Alias1;");
    Node expectedNode2 = Node.createString("var Alias2 = goog.Alias2;");
    List<Node> aliasDefinitions = Lists.newArrayList(
        expectedNode1, expectedNode2);
    List<Node> aliasUsages = Lists.newArrayList();

    assertTrue(scopedAliases.hasErrors() == false);
    assertEquals(aliasDefinitions, scopedAliases.aliasDefinitionsInOrder);
    assertEquals(aliasUsages, scopedAliases.aliasUsages);
    assertEquals(Lists.newArrayList(expectedNode1, expectedNode2),
        scopedAliases.scopeCalls);
  }

  @Test
  public void testApplyAliasesWithNonAliasedLocal() {
    String aliasName = "MyAlias";
    String nonAliasedLocalName = "NonAliasedLocal";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  var " + aliasName + " = " + aliasName + ";" +
        "  var " + nonAliasedLocalName + " = 42;" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testApplyAliasesWithRedefinedAlias() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "goog.scope(function() {" +
        "  var " + aliasName + " = " + aliasName + ";" +
        "  var " + aliasName + " = 42;" +
        "});");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testApplyAliasesWithAliasDefinitionInsideFunction() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "function MyFunction() {" +
        "  goog.scope(function() {" +
        "    var " + aliasName + " = " + aliasName + ";" +
        "  });" +
        "  //function body" +
        "}" +
        "MyFunction();");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @Test
  public void testApplyAliasesWithAliasDefinitionInsideConditional() {
    String aliasName = "MyAlias";
    Node rootNode = Node.createString(
        "if (true) {" +
        "  goog.scope(function() {" +
        "    var " + aliasName + " = " + aliasName + ";" +
        "  });" +
        "}" +
        "//rest of the code");
    scopedAliases.process(null, rootNode);
    assertTrue(scopedAliases.hasErrors());
  }

  @After
  public void tearDown() {
    // Reset the mocks
    compiler = null;
    preprocessorSymbolTable = null;
    transformationHandler = null;
    scopedAliases = null;
  }
}

class MockCompiler implements AbstractCompiler {
  @Override
  public void report(NodeTraversal t, DiagnosticType error, String... arguments) {
    // Do nothing
  }

  @Override
  public void reportCodeChange() {
    // Do nothing
  }
}

class MockSymbolTable implements PreprocessorSymbolTable {
  private Map<String, Var> variables = Maps.newHashMap();

  @Override
  public void addReference(Node node) {
    // Do nothing
  }

  @Override
  public void addVariable(Var var) {
    variables.put(var.getName(), var);
  }

  @Override
  public Var getVariable(String name) {
    return variables.get(name);
  }
}

class MockAliasTransformationHandler implements AliasTransformationHandler {
  private List<AliasTransformation> aliasTransformations = Lists.newArrayList();

  @Override
  public void logAliasTransformation(String fileName, SourcePosition position) {
    aliasTransformations.add(new AliasTransformation(fileName, position));
  }
}