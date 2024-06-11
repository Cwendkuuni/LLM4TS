package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;

/**
 * Tests for the MinimizeExitPoints class.
 *
 * @author johnlenz@google.com (John Lenz)
 */
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    String jsCode = "function foo() {"
        + "if (true) return; "
        + "if (true) return; "
        + "};";

    AbstractCompiler compiler = new Compiler() {};
    Node root = IR.parseLabeledStatement(jsCode, "foo");
    NodeTraversal.traverse(compiler, root, new MinimizeExitPoints(compiler));
    Node expectedResult = IR.block()
        .addChild(IR.ifStatement(Token.TRUE, IR.block()))
        .addChild(IR.ifStatement(Token.TRUE, IR.block()));

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testMinimizeExitPointsWithLabel() {
    String jsCode = "function foo() {"
        + "label: {"
        + "if (true) break label;"
        + "if (true) break label;"
        + "};";

    AbstractCompiler compiler = new Compiler() {};
    Node root = IR.parseLabeledStatement(jsCode, "foo");
    NodeTraversal.traverse(compiler, root, new MinimizeExitPoints(compiler));
    Node expectedResult = IR.block()
        .addChild(IR.labelStatement("label", IR.block()))
        .addChild(IR.ifStatement(Token.TRUE, IR.block()))
        .addChild(IR.ifStatement(Token.TRUE, IR.block()))
        .addChild(IR.ifStatement(Token.TRUE, IR.block()))
        .addChild(IR.ifStatement(Token.TRUE, IR.block()))
        .addChild(IR.returnStatement(Token.VOID));

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testMinimizeExitPointsWithConditionalStatement() {
    String jsCode = "function foo() {"
        + "if (condition1) return; "
        + "if (condition2) return; "
        + "};";

    AbstractCompiler compiler = new Compiler() {};
    Node root = IR.parseLabeledStatement(jsCode, "foo");
    Node cond1 = NodeUtil.createConditionalExpression(Token.TRUE, Node.newBoolean(condition1), Node.newBoolean(condition2));
    Node expectedResult = IR.block()
        .addChild(IR.ifStatement(cond1, IR.block()))
        .addChild(IR.block());

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testMinimizeExitPointsWithForLoop() {
    String jsCode = "function foo() {"
        + "for (var i = 0; i < 10; i++) {"
        + "  if (i === 5) return;"
        + "}"
        + "};";

    AbstractCompiler compiler = new Compiler() {};
    Node root = IR.parseLabeledStatement(jsCode, "foo");
    Node forNode = NodeUtil.getForNode(root);
    Node expectedResult = IR.block()
        .addChild(IR.labeledStatement("foo", IR.block()
            .addChild(IR.forStatement(forNode.getFirstChild(), forNode.getLastChild().getNext(), forNode.getLastChild()))
            .addChild(IR.ifStatement(NodeUtil.newIdentExpression("i"), Node.newNumber(5), IR.block()))))
        .addChild(IR.returnStatement(Token.VOID));

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testMinimizeExitPointsWithWhileLoop() {
    String jsCode = "function foo() {"
        + "while (true) {"
        + "  if (condition) return;"
        + "}"
        + "};";

    AbstractCompiler compiler = new Compiler() {};
    Node root = IR.parseLabeledStatement(jsCode, "foo");
    Node whileNode = NodeUtil.getWhileNode(root);
    Node expectedResult = IR.block()
        .addChild(IR.labeledStatement("foo", IR.block()
            .addChild(IR.whileStatement(whileNode.getCondition(), whileNode.getBody()))
            .addChild(IR.ifStatement(NodeUtil.newIdentExpression("condition"), Node.newBoolean(true), IR.block()))))
        .addChild(IR.returnStatement(Token.VOID));

    assertEquals(expectedResult.toSource(), root.toSource());
  }

  @Test
  public void testMinimizeExitPointsWithDoWhileLoop() {
    String jsCode = "function foo() {"
        + "do {"
        + "  if (condition) return;"
        + "} while (true);";

    AbstractCompiler compiler = new Compiler() {};
    Node root = IR.parseLabeledStatement(jsCode, "foo");
    Node doNode = NodeUtil.getDoWhileNode(root);
    Node expectedResult = IR.block()
        .addChild(IR.labeledStatement("foo", IR.block()
            .addChild(IR.doWhileStatement(doNode.getBody(), doNode.getCondition()))
            .addChild(IR.ifStatement(NodeUtil.newIdentExpression("condition"), Node.newBoolean(true), IR.block()))))
        .addChild(IR.returnStatement(Token.VOID));

    assertEquals(expectedResult.toSource(), root.toSource());
  }
}