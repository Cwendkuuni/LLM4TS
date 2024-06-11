package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.AnnotationConfigClassLoader;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.Error;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerTestHelper;
import com.google.javascript.rhino.JSDocInfo;

/**
 * Test case for the CheckGlobalThis class.
 */
@RunWith(PowerMockRunner.class)
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions(true);
    options.setWarningLevel(DiagnosticType.ALL, Error.ERROR);
    AnnotationConfigClassLoader classLoader = new AnnotationConfigClassLoader();
    classLoader.register(CheckGlobalThis.class);

    compiler = new AbstractCompiler(classLoader, options);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  /**
   * Test for the constructor.
   */
  @Test
  public void testConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    assertNull(checker.assignLhsChild);
  }

  /**
   * Test shouldTraverse method with null parent.
   */
  @Test
  public void testShouldTraverseWithNullParent() {
    assertTrue(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), PowerMockito.mock(Node.class), null));
  }

  /**
   * Test shouldTraverse method with non-function parent.
   */
  @Test
  public void testShouldTraverseWithNonFunctionParent() {
    Node parent = PowerMockito.mock(Node.class);
    PowerMockito.when(parent.getType()).thenReturn(Token.NUMBER);
    assertFalse(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), PowerMockito.mock(Node.class), parent));
  }

  /**
   * Test shouldTraverse method with constructor function.
   */
  @Test
  public void testShouldTraverseWithConstructorFunction() {
    Node constructor = PowerMockito.mock(Node.class);
    JSDocInfo jsDoc = PowerMockito.mock(JSDocInfo.class);
    PowerMockito.when(constructor.getJSDocInfo()).thenReturn(jsDoc);
    PowerMockito.when(jsDoc.isConstructor()).thenReturn(true);

    assertTrue(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), constructor, null));
  }

  /**
   * Test shouldTraverse method with @this function.
   */
  @Test
  public void testShouldTraverseWithThisFunction() {
    Node function = PowerMockito.mock(Node.class);
    JSDocInfo jsDoc = PowerMockito.mock(JSDocInfo.class);
    PowerMockito.when(function.getJSDocInfo()).thenReturn(jsDoc);
    PowerMockito.when(jsDoc.hasThisType()).thenReturn(true);

    assertTrue(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), function, null));
  }

  /**
   * Test shouldTraverse method with function that can't have a @this annotation.
   */
  @Test
  public void testShouldTraverseWithFunctionThatCantHaveThisAnnotation() {

    Node function = PowerMockito.mock(Node.class);

    assertTrue(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), function, null));
  }

  /**
   * Test shouldTraverse method with assignment node.
   */
  @Test
  public void testShouldTraverseWithAssignmentNode() {
    Node assign = PowerMockito.mock(Node.class);
    Node lhs = PowerMockito.mock(Node.class);
    Node rhs = PowerMockito.mock(Node.class);

    PowerMockito.when(assign.getType()).thenReturn(Token.ASSIGN);
    PowerMockito.when(lhs.getType()).thenReturn(Token.NAME);
    PowerMockito.when(lhs.getLastChild().getString()).thenReturn("a");

    assertTrue(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), assign, lhs));
  }

  /**
   * Test shouldTraverse method with assign node and a global this.
   */
  @Test
  public void testShouldTraverseWithAssignmentNodeAndGlobalThis() {
    Node assign = PowerMockito.mock(Node.class);
    Node lhs = PowerMockito.mock(Node.class);
    Node rhs = PowerMockito.mock(Node.class);

    PowerMockito.when(assign.getType()).thenReturn(Token.ASSIGN);
    PowerMockito.when(lhs.getType()).thenReturn(Token.THIS);

    assertFalse(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), assign, lhs));
  }

  /**
   * Test shouldTraverse method with assign node and a prototype property.
   */
  @Test
  public void testShouldTraverseWithAssignmentNodeAndPrototypeProperty() {
    Node assign = PowerMockito.mock(Node.class);
    Node lhs = PowerMockito.mock(Node.class);
    Node rhs = PowerMockito.mock(Node.class);

    PowerMockito.when(assign.getType()).thenReturn(Token.ASSIGN);
    PowerMockito.when(lhs.getType()).thenReturn(Token.GETPROP);
    PowerMockito.when(lhs.getLastChild().getString()).thenReturn("prototype");

    assertFalse(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), assign, lhs));
  }

  /**
   * Test shouldTraverse method with assign node and a sub-property.
   */
  @Test
  public void testShouldTraverseWithAssignmentNodeAndSubProperty() {
    Node assign = PowerMockito.mock(Node.class);
    Node lhs = PowerMockito.mock(Node.class);
    Node rhs = PowerMockito.mock(Node.class);

    PowerMockito.when(assign.getType()).thenReturn(Token.ASSIGN);
    PowerMockito.when(lhs.getType()).thenReturn(Token.GETPROP);
    PowerMockito.when(lhs.getLastChild().getString()).thenReturn("a");
    PowerMockito.when(lhs.getLastChild().getLastChild().getString()).thenReturn("subProperty");

    assertTrue(checker.shouldTraverse(PowerMockito.mock(NodeTraversal.class), assign, lhs));
  }

  /**
   * Test visit method with global this.
   */
  @Test
  public void testVisitWithGlobalThis() {
    Node node = PowerMockito.mock(Node.class);
    Node parent = PowerMockito.mock(Node.class);

    PowerMockito.when(node.getType()).thenReturn(Token.THIS);
    PowerMockito.when(parent.getType()).thenReturn(Token.ASSIGN);

    checker.visit(PowerMockito.mock(NodeTraversal.class), node, parent);

    // The visit method should not throw any exceptions, so no need to add an assert statement here.
  }

  /**
   * Test visit method with a non-global this.
   */
  @Test
  public void testVisitWithNonGlobalThis() {
    Node node = PowerMockito.mock(Node.class);
    Node parent = PowerMockito.mock(Node.class);

    PowerMockito.when(node.getType()).thenReturn(Token.THIS);
    PowerMockito.when(parent.getType()).thenReturn(Token.FUNCTION);

    checker.visit(PowerMockito.mock(NodeTraversal.class), node, parent);

    // The visit method should not throw any exceptions, so no need to add an assert statement here.
  }

  /**
   * Test getFunctionJsDocInfo method with function that has a JSDoc.
   */
  @Test
  public void testGetFunctionJsDocInfoWithFunctionWithJSDoc() {
    Node constructor = PowerMockito.mock(Node.class);
    JSDocInfo jsDoc = PowerMockito.mock(JSDocInfo.class);

    PowerMockito.when(constructor.getJSDocInfo()).thenReturn(jsDoc);
    PowerMockito.when(jsDoc.isConstructor()).thenReturn(true);

    JSDocInfo result = checker.getFunctionJsDocInfo(constructor);

    assertTrue(result.isConstructor());
  }

  /**
   * Test getFunctionJsDocInfo method with function that doesn't have a JSDoc.
   */
  @Test
  public void testGetFunctionJsDocInfoWithFunctionWithoutJSDoc() {
    Node function = PowerMockito.mock(Node.class);

    JSDocInfo result = checker.getFunctionJsDocInfo(function);

    assertNull(result);
  }
}