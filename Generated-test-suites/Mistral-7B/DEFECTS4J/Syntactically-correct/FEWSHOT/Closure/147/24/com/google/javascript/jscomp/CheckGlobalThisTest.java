package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @org.junit.Test
  public void testShouldTraverseGlobalContext() {
    Node rootNode = Node.newTextNode("function foo() { this.bar = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertThat(checker.shouldTraverse(traversal, rootNode, parentNode)).isTrue();
  }

  @org.junit.Test
  public void testShouldNotTraverseLocalContext() {
    Node rootNode = Node.newTextNode("function foo() { var x = this; }");
    Node parentNode = Node.newTextNode("function foo() { }");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertThat(checker.shouldTraverse(traversal, rootNode, parentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructor() {
    Node rootNode = Node.newTextNode("function Foo() { }");
    Node parentNode = Node.newTextNode("function Foo() { }");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertThat(checker.shouldTraverse(traversal, rootNode, parentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node rootNode = Node.newTextNode("function Foo@this() { }");
    Node parentNode = Node.newTextNode("function Foo() { }");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertThat(checker.shouldTraverse(traversal, rootNode, parentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node rootNode = Node.newTextNode("function Foo() {}");
    Node parentNode = Node.newTextNode("function Foo() { @constructor }");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertThat(checker.shouldTraverse(traversal, rootNode, parentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    Node rootNode = Node.newTextNode("function Foo() {}");
    Node parentNode = Node.newTextNode("function Foo() { @interface }");

    when(compiler.getTraversal()).thenReturn(traversal);

    assertThat(checker.shouldTraverse(traversal, rootNode, parentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldReportGlobalThis() {
    Node rootNode = Node.newTextNode("function foo() { this.bar = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(compiler.getTraversal().getCurrentNode()).thenReturn(rootNode);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).hasSize(1);
    assertThat(compiler.reportedErrors().get(0).getErrorType()).isEqualTo(GLOBAL_THIS);
    assertThat(compiler.reportedErrors().get(0).getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  @org.junit.Test
  public void testShouldNotReportLocalThis() {
    Node rootNode = Node.newTextNode("function foo() { var x = this; }");
    Node parentNode = Node.newTextNode("function foo() { }");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(compiler.getTraversal().getCurrentNode()).thenReturn(rootNode);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).isEmpty();
  }

  @org.junit.Test
  public void testShouldReportThisWithPropertyAccess() {
    Node rootNode = Node.newTextNode("function foo() { this.bar.baz = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(compiler.getTraversal().getCurrentNode()).thenReturn(rootNode);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).hasSize(1);
    assertThat(compiler.reportedErrors().get(0).getErrorType()).isEqualTo(GLOBAL_THIS);
    assertThat(compiler.reportedErrors().get(0).getMessage())
        .isEqualTo("dangerous use of the global 'this' object");
  }

  @org.junit.Test
  public void testShouldNotReportFunctionWithThisAnnotation() {
    Node rootNode = Node.newTextNode("function Foo@this() { }");
    Node parentNode = Node.newTextNode("function Foo() { }");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(compiler.getTraversal().getCurrentNode()).thenReturn(rootNode);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).isEmpty();
  }

  @org.junit.Test
  public void testShouldNotReportConstructorFunction() {
    Node rootNode = Node.newTextNode("function Foo() { @constructor }");
    Node parentNode = Node.newTextNode("function Foo() { }");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(compiler.getTraversal().getCurrentNode()).thenReturn(rootNode);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).isEmpty();
  }

  @org.junit.Test
  public void testShouldNotReportInterfaceFunction() {
    Node rootNode = Node.newTextNode("function Foo() { @interface }");
    Node parentNode = Node.newTextNode("function Foo() { }");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(compiler.getTraversal().getCurrentNode()).thenReturn(rootNode);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).isEmpty();
  }

  @org.junit.Test
  public void testShouldNotReportFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node rootNode = Node.newTextNode("function foo() { }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);
    when(compiler.getTraversal().getCurrentNode()).thenReturn(rootNode);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).isEmpty();
  }

  @org.junit.Test
  public void testShouldReportNestedAssignment() {
    Node rootNode = Node.newTextNode("function foo() { (this.a = this).b = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode.getFirstChild();
    assertThat(compiler.reportedErrors()).hasSize(1);
    assertThat(compiler.reportedErrors().get(0).getErrorType()).isEqualTo(GLOBAL_THIS);
    assertThat(compiler.reportedErrors().get(0).getMessage())
        .isEqualTo("dangerous use of the global 'this' object");
  }

  @org.junit.Test
  public void testShouldReportNestedAssignmentWithPrototypeProperty() {
    Node rootNode = Node.newTextNode("function foo() { (this.prototype.a = this).b = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode.getFirstChild();
    Node prototypeNode = errorNode.getFirstChild();
    Node thisNode = prototypeNode.getLastChild();

    assertThat(compiler.reportedErrors()).hasSize(1);
    assertThat(compiler.reportedErrors().get(0).getErrorType()).isEqualTo(GLOBAL_THIS);
    assertThat(compiler.reportedErrors().get(0).getMessage())
        .isEqualTo("dangerous use of the global 'this' object");
  }

  @org.junit.Test
  public void testShouldReportNestedAssignmentWithPrototypeSubProperty() {
    Node rootNode = Node.newTextNode("function foo() { (this.prototype.a.b = this).c = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode.getFirstChild();
    Node prototypeNode = errorNode.getFirstChild();
    Node thisNode = prototypeNode.getLastChild();
    Node bNode = thisNode.getNext();

    assertThat(compiler.reportedErrors()).hasSize(1);
    assertThat(compiler.reportedErrors().get(0).getErrorType()).isEqualTo(GLOBAL_THIS);
    assertThat(compiler.reportedErrors().get(0).getMessage())
        .isEqualTo("dangerous use of the global 'this' object");
  }

  @org.junit.Test
  public void testShouldNotReportNestedAssignmentWithNonPrototypeProperty() {
    Node rootNode = Node.newTextNode("function foo() { (this.bar = this).baz = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).isEmpty();
  }

  @org.junit.Test
  public void testShouldNotReportNestedAssignmentWithPrototypePropertyGet() {
    Node rootNode = Node.newTextNode("function foo() { this.prototype.getA().b = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).isEmpty();
  }

  @org.junit.Test
  public void testShouldNotReportNestedAssignmentWithPrototypeSubPropertyGet() {
    Node rootNode = Node.newTextNode("function foo() { this.prototype.getA().getB().c = 1; }");
    Node parentNode = Node.newTextNode("script");

    when(compiler.getTraversal()).thenReturn(traversal);

    checker.visit(traversal, rootNode, parentNode);

    Node errorNode = rootNode;
    assertThat(compiler.reportedErrors()).isEmpty();
  }

  private JSDocInfo getFunctionJsDocInfo(Node n) {
    return checker.getFunctionJsDocInfo(n);
  }
}