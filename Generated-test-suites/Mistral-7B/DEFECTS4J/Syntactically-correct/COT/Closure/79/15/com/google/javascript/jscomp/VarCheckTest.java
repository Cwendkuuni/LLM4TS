package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VarCheckTest {

  private static final String TEST_INPUT = "test.js";
  private AbstractCompiler compiler;
  private VarCheck varCheck;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = Mockito.spy(new AbstractCompiler() {});
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testValidVariableDeclaration() {
    Node root = Node.externalInputToRoot(compiler.parseTree(TEST_INPUT));
    varCheck.process(null, root);
    // No errors or warnings expected
  }

  @Test
  public void testUndefinedVariable() {
    Node root = Node.externalInputToRoot(compiler.parseTree(TEST_INPUT + "\nvar x;"));
    varCheck.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isError());
    assertEquals(UNDEFINED_VAR_ERROR, diagnosticType);
  }

  @Test
  public void testVariableAccessAcrossModules() {
    String testInput = "module1.js:\nvar x = 1;\nfunction foo() {}\n" +
                       "module2.js:\nvar y = x;";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isError());
    assertEquals(VIOLATED_MODULE_DEP_ERROR, diagnosticType);
  }

  @Test
  public void testMissingModuleDependency() {
    String testInput = "module1.js:\nimport x from 'module2';\nfunction foo() { x++; }";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isWarning());
    assertEquals(MISSING_MODULE_DEP_ERROR, diagnosticType);
  }

  @Test
  public void testStrictModuleDependency() {
    String testInput = "module1.js:\nimport x from 'module2';\nfunction foo() { x++; }";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck = new VarCheck(compiler, true);
    varCheck.process(null, root);
    assertEquals(0, compiler.getDiagnostics().size());
  }

  @Test
  public void testEmptyFunctionDeclaration() {
    String testInput = "var foo = function() {};";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isError());
    assertEquals(INVALID_FUNCTION_DECL, diagnosticType);
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    String testInput = "var x;\nfunction foo() {\nvar x;\n}";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isWarning());
    // TODO: Check that the duplicate variable is marked with a suppression.
  }

  @Test
  public void testVariableDeclarationInExterns() {
    String testInput = "// externs:\nvar x = 1;\n" +
                       "module1.js:\nimport x from 'externs';\nfunction foo() { x++; }";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck.process(null, root);
    // No errors or warnings expected
  }

  @Test
  public void testVariableAccessInExterns() {
    String testInput = "// externs:\nvar x = 1;\n" +
                       "module1.js:\nfunction foo() { console.log(x); }";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck.process(null, root);
    // No errors or warnings expected
  }

  @Test
  public void testNameReferenceInExterns() {
    String testInput = "// externs:\nvar x = 1;\n" +
                       "module1.js:\nfunction foo() { console.log(x); }";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck = new VarCheck(compiler);
    varCheck.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isWarning());
    assertEquals(NAME_REFERENCE_IN_EXTERNS_ERROR, diagnosticType);
  }

  @Test
  public void testUndefinedExternalVariable() {
    String testInput = "// externs:\nvar x = 1;\n" +
                       "module1.js:\nfunction foo() { console.log(x); }";
    Node root = Node.externalInputToRoot(compiler.parseTree(testInput));
    varCheck = new VarCheck(compiler, true);
    varCheck.process(null, root);
    assertEquals(1, compiler.getDiagnostics().size());
    DiagnosticType diagnosticType = compiler.getDiagnostics().get(0).getType();
    assertTrue(diagnosticType.isError());
    assertEquals(UNDEFINED_EXTERN_VAR_ERROR, diagnosticType);
  }

  private interface DiagnosticType {}

  private static final DiagnosticType UNDEFINED_VAR_ERROR = new DiagnosticType() {};
  private static final DiagnosticType VIOLATED_MODULE_DEP_ERROR = new DiagnosticType() {};
  private static final DiagnosticType MISSING_MODULE_DEP_ERROR = new DiagnosticType() {};
  private static final DiagnosticType STRICT_MODULE_DEP_ERROR = new DiagnosticType() {};
  private static final DiagnosticType INVALID_FUNCTION_DECL = new DiagnosticType() {};
  private static final DiagnosticType NAME_REFERENCE_IN_EXTERNS_ERROR = new DiagnosticType() {};
}