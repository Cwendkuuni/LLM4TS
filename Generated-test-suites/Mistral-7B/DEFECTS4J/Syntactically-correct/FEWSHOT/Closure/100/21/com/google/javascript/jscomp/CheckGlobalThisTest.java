package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {
  @Test
  public void testShouldTraverse() {
    // Test traverse global contexts.
    NodeTraversal traversal = new NodeTraversal(new BasicVisitor(), new CheckGlobalThis(null, CheckLevel.WARNING));
    Node globalContext = Traversing.parseJs("function foo() { this.x = 1; }");
    assertTrue(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, null, null));
    traversal.traverse(globalContext);

    // Test don't traverse functions that are constructors or have the @this annotation.
    JSDocInfo constructorAnnotation = new JSDocInfo("@constructor");
    Node constructorFunction = Traversing.parseJs("@constructor function Foo() { this.x = 1; }");
    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, null, null));
    traversal.traverse(constructorFunction);

    JSDocInfo thisAnnotation = new JSDocInfo("@this {type: 'Object'}");
    Node thisFunction = Traversing.parseJs("function Foo() { @this this.x = 1; }");
    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, null, null));
    traversal.traverse(thisFunction);

    // Test don't traverse functions unless they would normally
    // be able to have a @this annotation associated with them.
    Node notFunction = Traversing.parseJs("var a = function() { this.x = 1; }");
    assertTrue(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, null, null));
    traversal.traverse(notFunction);

    Node notFunctionWithThisAnnotation = Traversing.parseJs("function a() { @this var x = 1; }");
    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldTraverse(traversal, null, null));
    traversal.traverse(notFunctionWithThisAnnotation);
  }

  @Test
  public void testShouldReportThis() {
    NodeTraversal traversal = new NodeTraversal(new BasicVisitor(), new CheckGlobalThis(null, CheckLevel.WARNING));
    Node assignment = Traversing.parseJs("var x = this;");
    assertTrue(new CheckGlobalThis(null, CheckLevel.WARNING).shouldReportThis(assignment, null));

    Node functionAssignment = Traversing.parseJs("function foo() { var x = this; }");
    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldReportThis(functionAssignment, null));

    Node objectPropertyAssignment = Traversing.parseJs("var obj = { x: this };");
    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldReportThis(objectPropertyAssignment, null));

    Node functionPropertyAssignment = Traversing.parseJs("function Foo() { Foo.x = this; }");
    assertTrue(new CheckGlobalThis(null, CheckLevel.WARNING).shouldReportThis(functionPropertyAssignment, null));

    Node prototypePropertyAssignment = Traversing.parseJs("function Foo() { Object.prototype.x = this; }");
    assertFalse(new CheckGlobalThis(null, CheckLevel.WARNING).shouldReportThis(prototypePropertyAssignment, null));
  }

  @Test
  public void testReport() {
    NodeTraversal traversal = new NodeTraversal(new BasicVisitor(), new CheckGlobalThis(null, CheckLevel.WARNING));
    Node assignment = Traversing.parseJs("var x = this;");
    Node functionAssignment = Traversing.parseJs("function foo() { var x = this; }");
    Node objectPropertyAssignment = Traversing.parseJs("var obj = { x: this };");
    Node functionPropertyAssignment = Traversing.parseJs("function Foo() { Foo.x = this; }");
    Node prototypePropertyAssignment = Traversing.parseJs("function Foo() { Object.prototype.x = this; }");
    Node globalContext = Traversing.parseJs("function foo() { this.x = 1; }");

    traversal.traverse(assignment);
    traversal.traverse(functionAssignment);
    traversal.traverse(objectPropertyAssignment);
    traversal.traverse(functionPropertyAssignment);
    traversal.traverse(prototypePropertyAssignment);
    traversal.traverse(globalContext);

    Node error = traversal.errors.get(0);
    assertEquals(DiagnosticType.WARNING, error.getType());
    assertEquals("JSC_USED_GLOBAL_THIS", error.getCode());
    assertEquals("dangerous use of the global 'this' object", error.getMessage());
    assertEquals(1, error.getPosition().line);
    assertEquals(12, error.getPosition().column);
  }
}