package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeTraversal.AbstractShallowCallback;
import com.google.javascript.jscomp.NodeTraversal.FunctionCallback;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testRemoveDeadCodesAfterReturns() {
    UnreachableCodeElimination unreachableCodeElimination =
        new UnreachableCodeElimination(null, true);

    String jsCode = "function test() {" +
        "if (true) {" +
        "return;" +
        "alert('unreachable');" +
        "}" +
        "}";

    Node root = AbstractCompiler.parseTree(jsCode, null);

    unreachableCodeElimination.process(null, root);

    assertTrue(root.getLastChild().isReturn());
    assertFalse(root.getLastChild().getNext().isNode());
  }

  @Test
  public void testRemoveDeadCodeWithNoSideEffects() {
    UnreachableCodeElimination unreachableCodeElimination =
        new UnreachableCodeElimination(null, true);

    String jsCode = "function test() {" +
        "var x = a.b.MyClass.prototype.propertyName;" +
        "}";

    Node root = AbstractCompiler.parseTree(jsCode, null);

    unreachableCodeElimination.process(null, root);

    assertTrue(root.isFunction());
    assertTrue(root.getLastChild().isEndFunction());
  }

  @Test
  public void testRemoveDeadCodeWithSideEffects() {
    UnreachableCodeElimination unreachableCodeElimination =
        new UnreachableCodeElimination(null, false);

    String jsCode = "function test() {" +
        "var x = a.b.MyClass.prototype.propertyName;" +
        "document.body.innerHTML += 'Test';" +
        "}";

    Node root = AbstractCompiler.parseTree(jsCode, null);

    unreachableCodeElimination.process(null, root);

    assertTrue(root.isFunction());
    assertTrue(root.getLastChild().isEndFunction());
    assertTrue(document.body.innerHTML.contains("Test"));
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    UnreachableCodeElimination unreachableCodeElimination =
        new UnreachableCodeElimination(null, true);

    String jsCode = "function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        "if (i === 5) {" +
        "break;" +
        "}" +
        "if (i === 5) {" +
        "break;" +
        "}" +
        "}" +
        "}";

    Node root = AbstractCompiler.parseTree(jsCode, null);

    unreachableCodeElimination.process(null, root);

    Node breakNode1 = null;
    Node breakNode2 = null;
    for (Node node = root; node != null; node = node.getNext()) {
      if (node.getType() == Token.BREAK) {
        if (breakNode1 == null) {
          breakNode1 = node;
        } else {
          breakNode2 = node;
          break;
        }
      }
    }

    assertTrue(breakNode1.getNext() == breakNode2);
    assertFalse(root.getLastChild().isNode());
  }

  @Test
  public void testRemoveDeadNodeInTryCatch() {
    UnreachableCodeElimination unreachableCodeElimination =
        new UnreachableCodeElimination(null, true);

    String jsCode = "function test() {" +
        "try {" +
        "  throw new Error('Test');" +
        "  var x = a.b.MyClass.prototype.propertyName;" +
        "}" +
        "catch (e) {" +
        "}" +
        "}";

    Node root = AbstractCompiler.parseTree(jsCode, null);

    unreachableCodeElimination.process(null, root);

    assertTrue(root.isFunction());
    assertTrue(root.getLastChild().isEndFunction());
  }
}