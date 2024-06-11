package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.Compiler;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.compiler.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeTraversal.AbstractPostOrderCallback;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() throws Exception {
    Compiler compiler = new Compiler();
    CompilerPass pass = new MinimizeExitPoints(compiler);

    String input = "function foo() {\n" +
        "  if (true) return;\n" +
        "  else if (true) return;\n" +
        "  var x = 42;\n" +
        "}";

    Node externs = Node.externalInitializer(null);
    Node root = Node.parse(input);

    pass.process(externs, root);

    Node fooFunction = NodeUtil.getFunctionDeclaration(root, "foo");
    Node ifBlock = fooFunction.getFirstChild().getNext();

    // Check that both exits have been removed.
    assertTrue(ifBlock.isBlock());
    assertFalse(ifBlock.hasChildren());

    Node xVariable = ifBlock.getLastChild();
    assertTrue(xVariable.isVar());
    assertTrue(xVariable.hasChildren());
  }

  @Test
  public void testMinimizeExitPointsWithLabel() throws Exception {
    Compiler compiler = new Compiler();
    CompilerPass pass = new MinimizeExitPoints(compiler);

    String input = "function foo() {\n" +
        "  myLabel:\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    if (i === 5) break myLabel;\n" +
        "    console.log(i);\n" +
        "  }\n" +
        "}";

    Node externs = Node.externalInitializer(null);
    Node root = Node.parse(input);

    pass.process(externs, root);

    Node fooFunction = NodeUtil.getFunctionDeclaration(root, "foo");
    Node forStatement = fooFunction.getFirstChild();
    Node loopBlock = NodeUtil.getLoopCodeBlock(forStatement);

    // Check that the break statement has been moved into the loop body.
    Node breakStatement = loopBlock.getFirstChild();
    assertTrue(breakStatement.isBreak());
    assertTrue(breakStatement.hasChildren());
    assertTrue(breakStatement.getFirstChild().getString().equals("myLabel"));
  }

  @Test
  public void testMinimizeExitPointsWithEmptyBlock() throws Exception {
    Compiler compiler = new Compiler();
    CompilerPass pass = new MinimizeExitPoints(compiler);

    String input = "function foo() {\n" +
        "  if (true) return;\n" +
        "  if (true) return;\n" +
        "  {\n" +
        "  }\n" +
        "}";

    Node externs = Node.externalInitializer(null);
    Node root = Node.parse(input);

    pass.process(externs, root);

    Node fooFunction = NodeUtil.getFunctionDeclaration(root, "foo");
    Node ifBlock1 = fooFunction.getFirstChild().getNext();
    Node ifBlock2 = ifBlock1.getNext();

    // Check that the empty block has been removed.
    assertTrue(ifBlock2.isBlock());
    assertFalse(ifBlock2.hasChildren());
    assertTrue(fooFunction.getLastChild().isBlock());
  }

  @Test
  public void testMinimizeExitPointsWithComplexControlFlow() throws Exception {
    Compiler compiler = new Compiler();
    CompilerPass pass = new MinimizeExitPoints(compiler);

    String input = "function foo() {\n" +
        "  if (true) {\n" +
        "    if (true) return;\n" +
        "  } else if (true) {\n" +
        "    if (true) return;\n" +
        "  } else if (true) {\n" +
        "    if (true) return;\n" +
        "  } else {\n" +
        "    if (true) return;\n" +
        "  }\n" +
        "  var x = 42;\n" +
        "}";

    Node externs = Node.externalInitializer(null);
    Node root = Node.parse(input);

    pass.process(externs, root);

    Node fooFunction = NodeUtil.getFunctionDeclaration(root, "foo");
    Node ifBlock1 = fooFunction.getFirstChild();
    Node ifBlock2 = ifBlock1.getNext();
    Node ifBlock3 = ifBlock2.getNext();
    Node ifBlock4 = ifBlock3.getNext();

    // Check that only the last exit has been removed.
    assertTrue(ifBlock1.isIf());
    assertTrue(ifBlock2.isBlock());
    assertTrue(ifBlock3.isIf());
    assertTrue(ifBlock4.isBlock());
    assertTrue(ifBlock4.getLastChild().isVar());
  }
}