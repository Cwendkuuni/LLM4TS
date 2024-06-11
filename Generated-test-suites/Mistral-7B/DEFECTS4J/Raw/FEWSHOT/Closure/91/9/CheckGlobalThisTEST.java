package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.DiagnosticType.warning;
import static com.google.javascript.jscomp.NodeTraversal.Callback.SCOPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.DiagnosticType;

/**
 * Test cases for {@link CheckGlobalThis}.
 */
public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private Diagnostics diagnostics;
  private CheckGlobalThis checker;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    diagnostics = new Diagnostics();
    checker = new CheckGlobalThis(compiler, WARNING);
  }

  @org.junit.Test
  public void testShouldTraverseFunctionWithPrototypeProperty() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node prototypeNode = Node.newName("prototype");
    Node prototypePropertyNode = Node.newGetProp(functionNode, prototypeNode);
    Node parentNode = Node.newBlock();
    parentNode.addChild(prototypePropertyNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.shouldTraverse(traversal, null, prototypePropertyNode);
    assertEquals(true, checker.shouldTraverse(traversal, null, prototypePropertyNode));
  }

  @org.junit.Test
  public void testShouldTraverseFunctionWithPrototypeSubProperty() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node prototypeNode = Node.newName("prototype");
    Node aNode = Node.newName("a");
    Node prototypeAPINode = Node.newGetProp(prototypeNode, aNode);
    Node parentNode = Node.newBlock();
    parentNode.addChild(prototypeAPINode);

    NodeTraversal traversal = new NodeTraversals(parentNode, checker, null, null, SCOPE);
    checker.shouldTraverse(traversal, null, prototypeAPINode);
    assertEquals(true, checker.shouldTraverse(traversal, null, prototypeAPINode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithNoParent() {
    Node functionNode = Node.newFunction("function Foo() {}");

    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, SCOPE);
    assertEquals(false, checker.shouldTraverse(traversal, null, functionNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithNoPrototypeProperty() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node parentNode = Node.newBlock();

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    assertEquals(false, checker.shouldTraverse(traversal, null, functionNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithNoParentOrPrototypeProperty() {
    Node functionNode = Node.newFunction("function Foo() {}");

    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, SCOPE);
    assertEquals(false, checker.shouldTraverse(traversal, null, functionNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithPrototypeMethod() {
    Node functionNode = Node.newFunction("function Foo() { this.bar = 1; }");
    Node prototypeNode = Node.newName("prototype");
    Node barNode = Node.newName("bar");
    Node prototypeBarNode = Node.newSetProp(prototypeNode, functionNode, barNode);

    Node parentNode = Node.newBlock();
    parentNode.addChild(prototypeBarNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.shouldTraverse(traversal, null, prototypeBarNode);
    assertEquals(false, checker.shouldTraverse(traversal, null, prototypeBarNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithConstructorAnnotation() {
    Node functionNode = Node.newFunction("@constructor function Foo() {}");

    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, SCOPE);
    assertEquals(false, checker.shouldTraverse(traversal, null, functionNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node functionNode = Node.newFunction("function Foo() { @this this.bar = 1; }");

    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, SCOPE);
    assertEquals(false, checker.shouldTraverse(traversal, null, functionNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node functionNode = Node.newFunction("function Foo() { @override this.bar = 1; }");

    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, SCOPE);
    assertEquals(false, checker.shouldTraverse(traversal, null, functionNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
    Node functionNode = Node.newFunction("function Foo() { this.bar = 1; var x = function() { this.baz = 2; } }");

    NodeTraversal traversal = new NodeTraversal(null, checker, null, null, SCOPE);
    assertEquals(false, checker.shouldTraverse(traversal, null, functionNode));
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node assignNode = Node.newAssign(Node.newName("this"), functionNode);
    Node parentNode = Node.newBlock();
    parentNode.addChild(assignNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.visit(traversal, null, assignNode);

    assertEquals(1, diagnostics.getDiagnostics().size());
    DiagnosticType diagnosticType = diagnostics.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_GLOBAL_THIS"), diagnosticType);
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithPropertyAccess() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node assignNode = Node.newAssign(Node.newGetProp(Node.newName("this"), "bar"), Node.newNumber(1));
    Node parentNode = Node.newBlock();
    parentNode.addChild(assignNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.visit(traversal, null, assignNode);

    assertEquals(1, diagnostics.getDiagnostics().size());
    DiagnosticType diagnosticType = diagnostics.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_GLOBAL_THIS"), diagnosticType);
  }

  @org.junit.Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node assignNode = Node.newAssign(Node.newName("this"), Node.newNumber(1));
    Node parentNode = Node.newBlock();
    parentNode.addChild(functionNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.visit(traversal, null, assignNode);

    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithPrototypePropertyAccess() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node prototypeNode = Node.newName("prototype");
    Node assignNode = Node.newAssign(Node.newGetProp(Node.newGetProp(Node.newName("this"), "prototype"), "bar"), Node.newNumber(1));
    Node parentNode = Node.newBlock();
    parentNode.addChild(assignNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.visit(traversal, null, assignNode);

    assertEquals(1, diagnostics.getDiagnostics().size());
    DiagnosticType diagnosticType = diagnostics.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_GLOBAL_THIS"), diagnosticType);
  }

  @org.junit.Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithPrototypeSubPropertyAccess() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node aNode = Node.newName("a");
    Node prototypeNode = Node.newName("prototype");
    Node assignNode = Node.newAssign(Node.newGetProp(Node.newGetProp(Node.newName("this"), "prototype"), aNode), Node.newNumber(1));
    Node parentNode = Node.newBlock();
    parentNode.addChild(assignNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.visit(traversal, null, assignNode);

    assertEquals(1, diagnostics.getDiagnostics().size());
    DiagnosticType diagnosticType = diagnostics.getDiagnostics().get(0).getType();
    assertEquals(warning("JSC_USED_GLOBAL_THIS"), diagnosticType);
  }

  @org.junit.Test
  public void testShouldNotReportThisOnRightSideOfAssignmentWithPrototypeProperty() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node prototypeNode = Node.newName("prototype");
    Node assignNode = Node.newAssign(Node.newGetProp(prototypeNode, "bar"), Node.newNumber(1));
    Node parentNode = Node.newBlock();
    parentNode.addChild(functionNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.visit(traversal, null, assignNode);

    assertEquals(0, diagnostics.getDiagnostics().size());
  }

  @org.junit.Test
  public void testShouldNotReportThisOnRightSideOfAssignmentWithPrototypeSubProperty() {
    Node functionNode = Node.newFunction("function Foo() {}");
    Node aNode = Node.newName("a");
    Node prototypeNode = Node.newName("prototype");
    Node assignNode = Node.newAssign(prototypeNode, Node.newGetProp(prototypeNode, aNode));
    Node parentNode = Node.newBlock();
    parentNode.addChild(functionNode);

    NodeTraversal traversal = new NodeTraversal(parentNode, checker, null, null, SCOPE);
    checker.visit(traversal, null, assignNode);

    assertEquals(0, diagnostics.getDiagnostics().size());
  }
}