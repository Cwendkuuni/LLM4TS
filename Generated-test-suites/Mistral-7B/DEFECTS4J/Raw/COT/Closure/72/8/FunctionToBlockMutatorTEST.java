package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.rhino.Token;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FunctionToBlockMutatorTest {

  FunctionToBlockMutator testee;

  @org.junit.Before
  public void setUp() {
    testee = new FunctionToBlockMutator(new AbstractCompiler(), () -> "");
  }

  @org.junit.Test
  public void testMutate_SimpleFunction() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, fnName, null, null, null);
    Node callNode = new Node(Token.CALL, fnNode);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node result = testee.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertNotNull(result);
    assertTrue(NodeUtil.isStatementBlock(result));
  }

  @org.junit.Test
  public void testMutate_FunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION, fnName, null, null, null);
    Node callNode = Node.newFunctionCall(fnName);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> args = Maps.newLinkedHashMap();
    args.put("arg1", Node.newString("arg1"));
    args.put("arg2", Node.newNumber(10));

    // Test without aliases
    Node result1 = testee.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertNotNull(result1);
    assertTrue(NodeUtil.isStatementBlock(result1));

    // Test with aliases
    Set<String> namesToAlias = Sets.newHashSet("arg1");
    Node result2 = testee.mutate(fnName, fnNode, callNode, resultName,
        needsDefaultResult, isCallInLoop);

    assertNotNull(result2);
    assertTrue(NodeUtil.isStatementBlock(result2));

    // Check if arguments have been aliased
    for (Node c = result2.getFirstChild(); c != null; c = c.getNext()) {
      if (c.getType() == Token.VAR) {
        Node name = c.getFirstChild();
        String nameValue = name.getValue();
        assertTrue(namesToAlias.contains(nameValue));
      }
    }
  }

  @org.junit.Test
  public void testReplaceReturns() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode();
    String resultName = "result";
    boolean resultMustBeSet = true;

    Node result = FunctionToBlockMutator.replaceReturns(block, resultName,
        labelName, resultMustBeSet);

    if (resultMustBeSet) {
      assertTrue(NodeUtil.isStatementBlock(result));
      Node resultNode = null;
      for (Node c = result.getFirstChild(); c != null; c = c.getNext()) {
        if (c.getType() == Token.VAR && "result".equals(c.getFirstChild().getValue())) {
          resultNode = c;
          break;
        }
      }
      assertNotNull(resultNode);
      assertEquals(NodeUtil.newUndefinedNode(resultNode.getLocation()),
          resultNode.getLastChild());
    } else {
      assertNull(result);
    }
  }

  @org.junit.Test
  public void testReplaceReturns_WithLabel() {
    String labelName = "testLabel";
    String resultName = "result";
    Node block = NodeUtil.newBlockNode();
    Node returnNode = Node.newReturnNode(NodeUtil.newNumber(10));
    Node labelNode = new Node(Token.LABEL).copyInformationFrom(block);
    Node labelNameNode = Node.newString(Token.LABEL_NAME, labelName)
        .copyInformationFrom(block);
    labelNode.addChildToFront(labelNameNode);

    // Add return node to block
    block.addChildrenToBack(returnNode);

    Node result = FunctionToBlockMutator.replaceReturns(block, resultName,
        labelName, resultMustBeSet(true));

    if (resultMustBeSet(true)) {
      assertTrue(NodeUtil.isStatementBlock(result));
      Node resultNode = null;
      for (Node c = result.getFirstChild(); c != null; c = c.getNext()) {
        if (c.getType() == Token.VAR && "result".equals(c.getFirstChild().getValue())) {
          resultNode = c;
          break;
        }
      }
      assertNotNull(resultNode);
      assertEquals(NodeUtil.newNumberNode(10), resultNode.getLastChild());

      // Check if break statement was added
      Node breakNode = null;
      for (Node c = result.getFirstChild(); c != null; c = c.getNext()) {
        if (c.getType() == Token.BREAK) {
          breakNode = c;
          break;
        }
      }
      assertNotNull(breakNode);
      assertEquals(labelName, breakNode.getFirstChild().getValue());
    } else {
      assertNull(result);
    }
  }

  @org.junit.Test
  public void testReplaceReturns_NoResult() {
    String labelName = "testLabel";
    Node block = NodeUtil.newBlockNode();
    boolean resultMustBeSet = false;

    Node result = FunctionToBlockMutator.replaceReturns(block, null,
        labelName, resultMustBeSet);

    if (resultMustBeSet(false)) {
      assertNull(result);
    } else {
      assertNotNull(result);
      assertTrue(NodeUtil.isStatementBlock(result));

      // Check if break statement was added
      Node breakNode = null;
      for (Node c = result.getFirstChild(); c != null; c = c.getNext()) {
        if (c.getType() == Token.BREAK) {
          breakNode = c;
          break;
        }
      }
      assertNotNull(breakNode);
      assertEquals(labelName, breakNode.getFirstChild().getValue());
    }
  }

  private boolean resultMustBeSet(boolean value) {
    return value;
  }
}