package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestUtils;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link CheckGlobalThis} class.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public NodeFactory getNodeFactory() {
        return nodeFactory;
      }
    };
    nodeFactory = new NodeFactory();
  }

  /**
   * Tests that the constructor is called correctly.
   */
  @Test
  public void testConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    assertNotNull(checkGlobalThis);
  }

  /**
   * Tests that the check is skipped for constructors.
   */
  @Test
  public void testConstructorSkipped() {
    String jsCode = "function MyConstructor() {}";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that the check is skipped for functions annotated with {@code @constructor}.
   */
  @Test
  public void testConstructorAnnotationSkipped() {
    String jsCode = "/** @constructor */ function MyConstructor() {}";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that the check is skipped for functions annotated with {@code @this}.
   */
  @Test
  public void testThisAnnotationSkipped() {
    String jsCode = "/** @this {MyType} */ function MyFunction() {}";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that the check is skipped for functions where there's no logical place
   * to put a {@code this} annotation.
   */
  @Test
  public void testNoThisAnnotationNeeded() {
    String jsCode = "function MyFunction() {}";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that a simple usage of {@code this} is reported as a warning.
   */
  @Test
  public void testSimpleUsageOfThis() {
    String jsCode = "function MyFunction() { this.property = 42; }";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  /**
   * Tests that a usage of {@code this} inside a function is reported as a warning.
   */
  @Test
  public void testUsageOfThisInsideFunction() {
    String jsCode = "function MyOuterFunction() { function MyInnerFunction() { this.property = 42; } }";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  /**
   * Tests that a usage of {@code this} inside a prototype method is not reported
   * as a warning.
   */
  @Test
  public void testUsageOfThisInsidePrototypeMethod() {
    String jsCode = "function MyConstructor() { this.property = 42; } MyConstructor.prototype.prototypeMethod = function() { this.property = 43; }";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that a usage of {@code this} inside a constructor is not reported as a warning.
   */
  @Test
  public void testUsageOfThisInsideConstructor() {
    String jsCode = "function MyConstructor() { this.property = 42; }";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that a usage of {@code this} inside a function with a {@code @this} annotation is not reported as a warning.
   */
  @Test
  public void testUsageOfThisInsideFunctionWithThisAnnotation() {
    String jsCode = "/** @this {MyType} */ function MyFunction() { this.property = 42; }";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that a usage of {@code this} inside a function with a {@code @constructor} annotation is not reported as a warning.
   */
  @Test
  public void testUsageOfThisInsideFunctionWithConstructorAnnotation() {
    String jsCode = "/** @constructor */ function MyConstructor() { this.property = 42; }";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that a usage of {@code this} inside a function with no logical place for a {@code this} annotation is not reported as a warning.
   */
  @Test
  public void testUsageOfThisInsideFunctionWithNoThisAnnotation() {
    String jsCode = "function MyFunction() { this.property = 42; }";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  /**
   * Tests that a usage of {@code this} inside an assignment is reported as a warning.
   */
  @Test
  public void testUsageOfThisInsideAssignment() {
    String jsCode = "var a = this; a.property = 42;";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  /**
   * Tests that a usage of {@code this} inside a getter is reported as a warning.
   */
  @Test
  public void testUsageOfThisInsideGetter() {
    String jsCode = "var obj = { get x() { return this.property; } };";
    CompilerOptions options = ParserTestUtils.createCompilerOptions();
    Node ast = ParserTestUtils.parseJsCode(jsCode, options);
    NodeTraversal traversal = new NodeTraversal(ast, null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    checkGlobalThis.visitAll(traversal);
    assertEquals(1, traversal.getDiagnostics().size());
    DiagnosticType diagnosticType = traversal.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }
}