package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(true, null, null);
    compiler = new AbstractCompiler(options);
    passManager = new CompilerPassManager(compiler, options);
  }

  @Test
  public void testUnreachableCodeElimination() {
    String sourceCode = "function foo() {"
        + "if (true) {"
        + " return;"
        + "} else {"
        + " alert('unreachable');"
        + "}"
        + "}";

    Node root = NodeFactory.createScript(sourceCode);
    NodeTraversal traversal = new NodeTraversal(root, null, null);

    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    unreachableCodeElimination.process(null, root);

    NodeTraversal.traverse(passManager, root, unreachableCodeElimination);
    Node foo = NodeUtil.getFunctionByName(root, "foo");

    Node fooBody = foo.getBody();
    assertEquals(0, fooBody.getChildCount());
    assertNull(NodeUtil.getChildByType(fooBody, Token.ALERT));
  }

  @Test
  public void testNoOpStatements() {
    String sourceCode = "function foo() {"
        + "a.b.MyClass.prototype.propertyName;"
        + "return 42;"
        + "}"
        + "function bar() {"
        + "return true;"
        + "}"
        + "foo();"
        + "bar();";

    Node root = NodeFactory.createScript(sourceCode);
    NodeTraversal traversal = new NodeTraversal(root, null, null);

    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    unreachableCodeElimination.process(null, root);

    NodeTraversal.traverse(passManager, root, unreachableCodeElimination);

    Node foo = NodeUtil.getFunctionByName(root, "foo");
    Node bar = NodeUtil.getFunctionByName(root, "bar");

    Node fooBody = foo.getBody();
    assertEquals(1, fooBody.getChildCount());
    assertNull(fooBody.getChild(0).getNext());

    Node barBody = bar.getBody();
    assertEquals(0, barBody.getChildCount());
  }

  @Test
  public void testRemoveBreaksAndContinues() {
    String sourceCode = "function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + " if (i > 5) {"
        + " break;"
        + "}"
        + "}"
        + "}"
        + "function bar() {"
        + "for (var i = 0; i < 10; i++) {"
        + " if (i > 5) {"
        + " continue;"
        + "}"
        + "}"
        + "}"
        + "foo();"
        + "bar();";

    Node root = NodeFactory.createScript(sourceCode);
    NodeTraversal traversal = new NodeTraversal(root, null, null);

    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    unreachableCodeElimination.process(null, root);

    NodeTraversal.traverse(passManager, root, unreachableCodeElimination);

    Node foo = NodeUtil.getFunctionByName(root, "foo");
    Node bar = NodeUtil.getFunctionByName(root, "bar");

    Node fooBody = foo.getBody();
    assertEquals(0, fooBody.getChildCount());

    Node barBody = bar.getBody();
    assertEquals(1, barBody.getChildCount());
    assertEquals(Token.FOR, barBody.getChild(0).getType());
  }

  @Test(expected = AssertionError.class)
  public void testThrowException() {
    String sourceCode = "function foo() {"
        + "try {"
        + " throw new Error('message');"
        + "} catch (e) {"
        + "}"
        + "}"
        + "foo();";

    Node root = NodeFactory.createScript(sourceCode);
    NodeTraversal traversal = new NodeTraversal(root, null, null);

    UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(compiler, true);
    unreachableCodeElimination.process(null, root);

    NodeTraversal.traverse(passManager, root, unreachableCodeElimination);

    Node foo = NodeUtil.getFunctionByName(root, "foo");
    Node body = foo.getBody();

    assertEquals(3, body.getChildCount());
    assertEquals(Token.TRY, body.getChild(0).getType());
    assertEquals(Token.CATCH, body.getChild(1).getType());
    assertEquals(Token.EMPTY, body.getChild(2).getType());
  }
}