package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
  }

  @Test
  public void testShouldTraverse_globalFunction() {
    Node globalFunction = Node.newFunction("f", null);
    Node parent = Node.newBlock();

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, globalFunction, null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_constructorFunction() {
    Node constructor = Node.newFunction("constructor", null, true);
    Node parent = Node.newBlock();

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, constructor, null);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithThisAnnotation() {
    Node function = Node.newFunction("foo", null);
    Node jsDocInfo = Node.newObjectLiteral();
    jsDocInfo.setProperty("type", Node.newString("this"));
    function.setJSDocInfo(jsDocInfo);
    Node parent = Node.newBlock();

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, function, null);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithNoAnnotation() {
    Node function = Node.newFunction("foo", null);
    Node parent = Node.newBlock();

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, function, null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_functionWithParentVariable() {
    Node parentVariable = Node.newVar("parent");
    Node function = Node.newFunction("foo", null);
    Node parent = Node.newBlock();
    parent.addChildToEnd(parentVariable);
    parent.addChildToEnd(function);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, function, null);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldTraverse_functionWithParentVariableAnnotation() {
    Node parentVariable = Node.newVar("parent");
    Node function = Node.newFunction("foo", null);
    Node jsDocInfo = Node.newObjectLiteral();
    jsDocInfo.setProperty("type", Node.newString("this"));
    function.setJSDocInfo(jsDocInfo);
    Node parent = Node.newBlock();
    parent.addChildToEnd(parentVariable);
    parent.addChildToEnd(function);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, function, null);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithParentPrototypeProperty() {
    Node parentPrototype = Node.newName("foo.prototype");
    Node function = Node.newFunction("foo", null);
    Node parent = Node.newBlock();
    parent.addChildToEnd(parentPrototype);
    parent.addChildToEnd(function);

    Node getPrototypeProperty = Node.newGetProp(parentPrototype, "prototype");
    Node lhs = Node.newGetProp(getPrototypeProperty, "x");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, function, lhs);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldTraverse_functionWithParentPrototypePropertyChain() {
    Node parentPrototype = Node.newName("foo.prototype");
    Node parentPrototypeChain = Node.newName("parentPrototype.prototype");
    Node function = Node.newFunction("foo", null);
    Node parent = Node.newBlock();
    parent.addChildToEnd(parentPrototype);
    parent.addChildToEnd(function);

    Node getPrototypeProperty = Node.newGetProp(parentPrototype, "prototype");
    Node getPrototypePropertyChain = Node.newGetProp(getPrototypeProperty, "prototype");
    Node lhs = Node.newGetProp(getPrototypePropertyChain, "x");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    boolean result = checker.shouldTraverse(traversal, function, lhs);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThis() {
    Node thisKeyword = Node.newThis();
    Node parent = Node.newBlock();

    boolean result = checker.shouldReportThis(thisKeyword, parent);

    assertThat(result).isFalse();
  }

  @Test
  public void testShouldReportThis_leftSideOfAssignment() {
    Node assign = Node.newAssign();
    Node lhs = Node.newThis();
    Node rhs = Node.newString("foo");
    assign.addChildToEnd(lhs);
    assign.addChildToEnd(rhs);
    Node parent = Node.newBlock();
    parent.addChildToEnd(assign);

    boolean result = checker.shouldReportThis(lhs, parent);

    assertThat(result).isTrue();
  }

  @Test
  public void testShouldReportThis_propertyAccess() {
    Node parent = Node.newBlock();
    Node getProperty = Node.newGetProp(Node.newThis(), "x");

    boolean result = checker.shouldReportThis(getProperty, parent);

    assertThat(result).isTrue();
  }

  @Test
  public void testVisit_globalFunction() {
    Node globalFunction = Node.newFunction("f", null);
    Node parent = Node.newBlock();

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, globalFunction, parent);

    // No error should be reported.
  }

  @Test
  public void testVisit_constructorFunction() {
    Node constructor = Node.newFunction("constructor", null, true);
    Node parent = Node.newBlock();

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, constructor, parent);

    // No error should be reported.
  }

  @Test
  public void testVisit_functionWithThisAnnotation() {
    Node function = Node.newFunction("foo", null);
    Node jsDocInfo = Node.newObjectLiteral();
    jsDocInfo.setProperty("type", Node.newString("this"));
    function.setJSDocInfo(jsDocInfo);
    Node parent = Node.newBlock();

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, function, parent);

    // No error should be reported.
  }

  @Test
  public void testVisit_functionWithNoAnnotation() {
    Node function = Node.newFunction("foo", null);
    Node parent = Node.newBlock();

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, function, parent);

    // No error should be reported.
  }

  @Test
  public void testVisit_functionWithParentVariable() {
    Node parentVariable = Node.newVar("parent");
    Node function = Node.newFunction("foo", null);
    Node parent = Node.newBlock();
    parent.addChildToEnd(parentVariable);
    parent.addChildToEnd(function);

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, function, parent);

    // No error should be reported.
  }

  @Test
  public void testVisit_functionWithParentPrototypeProperty() {
    Node parentPrototype = Node.newName("foo.prototype");
    Node function = Node.newFunction("foo", null);
    Node parent = Node.newBlock();
    parent.addChildToEnd(parentPrototype);
    parent.addChildToEnd(function);

    Node getPrototypeProperty = Node.newGetProp(parentPrototype, "prototype");
    Node lhs = Node.newGetProp(getPrototypeProperty, "x");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, function, lhs);

    // No error should be reported.
  }

  @Test
  public void testVisit_functionWithParentPrototypePropertyChain() {
    Node parentPrototype = Node.newName("foo.prototype");
    Node parentPrototypeChain = Node.newName("parentPrototype.prototype");
    Node function = Node.newFunction("foo", null);
    Node parent = Node.newBlock();
    parent.addChildToEnd(parentPrototype);
    parent.addChildToEnd(function);

    Node getPrototypeProperty = Node.newGetProp(parentPrototype, "prototype");
    Node getPrototypePropertyChain = Node.newGetProp(getPrototypeProperty, "prototype");
    Node lhs = Node.newGetProp(getPrototypePropertyChain, "x");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, function, lhs);

    // No error should be reported.
  }

  @Test
  public void testVisit_functionWithLeftSideOfAssignment() {
    Node assign = Node.newAssign();
    Node lhs = Node.newThis();
    Node rhs = Node.newString("foo");
    assign.addChildToEnd(lhs);
    assign.addChildToEnd(rhs);
    Node parent = Node.newBlock();
    parent.addChildToEnd(assign);

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, assign, parent);

    checkGlobalThisError(assign, CheckLevel.WARNING);
  }

  @Test
  public void testVisit_functionWithPropertyAccess() {
    Node parent = Node.newBlock();
    Node getProperty = Node.newGetProp(Node.newThis(), "x");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, getProperty, parent);

    checkGlobalThisError(getProperty, CheckLevel.WARNING);
  }

  private void checkGlobalThisError(Node node, CheckLevel level) {
    Node error = compiler.report(traversal.makeError(node, level, CheckGlobalThis.GLOBAL_THIS));
    String errorMessage = error.getString();

    assertThat(errorMessage).isEqualTo("dangerous use of the global 'this' object");
  }
}