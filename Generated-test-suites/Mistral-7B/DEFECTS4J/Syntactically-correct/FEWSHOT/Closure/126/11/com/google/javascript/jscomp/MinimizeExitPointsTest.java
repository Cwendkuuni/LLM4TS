package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TernaryValue;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    NodeFactory nodeFactory = new NodeFactory();
    AbstractCompiler compiler = new DummyCompiler();
    CompilerPass minimizer = new MinimizeExitPoints(compiler);

    Node root = NodeUtil.createFunction(nodeFactory, "test", null, false, null, null);
    Node body = NodeUtil.createBlock(nodeFactory, root);
    Node ifNode = NodeUtil.createIf(nodeFactory, body, null, null, nodeFactory.createBooleanLiteral(true));
    Node ifBlock = ifNode.getFirstChild();
    Node returnNode = NodeUtil.createReturn(nodeFactory, ifBlock, null);
    Node elseBlock = NodeUtil.createBlock(nodeFactory, ifBlock);
    Node elseIfNode = NodeUtil.createIf(nodeFactory, elseBlock, null, null, nodeFactory.createBooleanLiteral(false));
    Node elseStatement = NodeUtil.createPrint(nodeFactory, elseBlock, null, null, null, null, null);

    Node rootWithExit = NodeUtil.createFunction(nodeFactory, "testWithExit", null, false, null, null);
    Node rootWithExitBody = NodeUtil.createBlock(nodeFactory, rootWithExit);
    Node rootWithExitIfNode = NodeUtil.createIf(nodeFactory, rootWithExitBody, null, null, nodeFactory.createBooleanLiteral(true));
    Node rootWithExitIfBlock = rootWithExitIfNode.getFirstChild();
    Node rootWithExitReturnNode = NodeUtil.createReturn(nodeFactory, rootWithExitIfBlock, null);
    Node rootWithExitElseBlock = NodeUtil.createBlock(nodeFactory, rootWithExitIfBlock);
    Node rootWithExitElseIfNode = NodeUtil.createIf(nodeFactory, rootWithExitElseBlock, null, null, nodeFactory.createBooleanLiteral(false));
    Node rootWithExitElseStatement = NodeUtil.createPrint(nodeFactory, rootWithExitElseBlock, null, null, null, null, null);

    Node rootWithLabel = NodeUtil.createFunction(nodeFactory, "testWithLabel", null, false, null, null);
    Node rootWithLabelBody = NodeUtil.createBlock(nodeFactory, rootWithLabel);
    Node rootWithLabelLabelNode = NodeUtil.createLabel(nodeFactory, rootWithLabelBody, "label");
    Node rootWithLabelIfNode = NodeUtil.createIf(nodeFactory, rootWithLabelBody, null, null, nodeFactory.createBooleanLiteral(true));
    Node rootWithLabelIfBlock = rootWithLabelIfNode.getFirstChild();
    Node rootWithLabelReturnNode = NodeUtil.createReturn(nodeFactory, rootWithLabelIfBlock, null);
    Node rootWithLabelElseBlock = NodeUtil.createBlock(nodeFactory, rootWithLabelIfBlock);
    Node rootWithLabelElseIfNode = NodeUtil.createIf(nodeFactory, rootWithLabelElseBlock, null, null, nodeFactory.createBooleanLiteral(false));
    Node rootWithLabelElseStatement = NodeUtil.createPrint(nodeFactory, rootWithLabelElseBlock, null, null, null, null, null);
    Node rootWithLabelBreakNode = NodeUtil.createBreak(nodeFactory, rootWithLabelElseBlock, "label");

    Node rootWithTry = NodeUtil.createFunction(nodeFactory, "testWithTry", null, false, null, null);
    Node rootWithTryBody = NodeUtil.createBlock(nodeFactory, rootWithTry);
    Node tryNode = NodeUtil.createTry(nodeFactory, rootWithTryBody);
    Node catchNode = NodeUtil.createCatch(nodeFactory, tryNode, null, null);
    Node catchCodeBlock = catchNode.getLastChild();
    Node returnNodeInCatch = NodeUtil.createReturn(nodeFactory, catchCodeBlock, null);
    Node finallyNode = NodeUtil.createFinally(nodeFactory, tryNode, null);
    Node finallyCodeBlock = finallyNode.getLastChild();
    Node returnNodeInFinally = NodeUtil.createReturn(nodeFactory, finallyCodeBlock, null);

    Node rootWithSwitch = NodeUtil.createFunction(nodeFactory, "testWithSwitch", null, false, null, null);
    Node rootWithSwitchBody = NodeUtil.createBlock(nodeFactory, rootWithSwitch);
    Node switchNode = NodeUtil.createSwitch(nodeFactory, rootWithSwitchBody, null, null);
    Node caseNode1 = NodeUtil.createCase(nodeFactory, switchNode, null, null, null, null);
    Node caseBlock1 = caseNode1.getNext();
    Node returnNodeInCase1 = NodeUtil.createReturn(nodeFactory, caseBlock1, null);
    Node caseNode2 = NodeUtil.createCase(nodeFactory, switchNode, null, null, null, null);
    Node caseBlock2 = caseNode2.getNext();

    Node[] exitNodesToRemove = new Node[] {
        returnNode,
        rootWithExitReturnNode,
        rootWithLabelReturnNode,
        returnNodeInCatch,
        returnNodeInFinally,
        returnNodeInCase1
    };

    Node[] exitNodesExpectedParents = new Node[] {
        ifBlock,
        rootWithExitIfBlock,
        rootWithLabelIfBlock,
        catchCodeBlock,
        finallyCodeBlock,
        caseBlock1
    };

    Node[] exitNodesExpectedNewParents = new Node[] {
        null,
        ifNode,
        rootWithLabelIfNode,
        null,
        null,
        null
    };

    for (int i = 0; i < exitNodesToRemove.length; ++i) {
      Node exitNode = exitNodesToRemove[i];
      Node exitNodeParentExpected = exitNodesExpectedParents[i];
      Node exitNodeParentActual = exitNode.getParent();
      assertEquals("Unexpected exit node parent for exit node: " + exitNode, exitNodeParentExpected, exitNodeParentActual);

      if (exitNodeParentExpected != null) {
        Node exitNodeNewParentExpected = exitNodesExpectedNewParents[i];
        Node exitNodeNewParentActual = null;

        minimizer.process(null, root);
        for (Node n = root; n != null; n = n.getNext()) {
          if (n.isBlock() && exitNode.isChildOf(n)) {
            exitNodeNewParentActual = n;
            break;
          }
        }

        assertEquals("Unexpected new parent for exit node: " + exitNode, exitNodeNewParentExpected, exitNodeNewParentActual);
      }

      NodeUtil.removeChild(exitNodeParentExpected, exitNode);
      compiler.reportCodeChange();
      minimizer.process(null, rootWithExit);
      assertTrue("Exit node was not removed", !NodeUtil.hasChild(exitNodeParentExpected, exitNode));
      assertNull("Unexpected new parent for exit node after removal: " + exitNode, exitNode.getParent());
    }

    // Ensure no side effects.
    minimizer.process(null, root);
    minimizer.process(null, rootWithExit);
    minimizer.process(null, rootWithLabel);
    minimizer.process(null, rootWithTry);
    minimizer.process(null, rootWithSwitch);
  }

  private static class DummyCompiler implements AbstractCompiler {
    public void reportCodeChange() {
      // No-op.
    }
  }
}