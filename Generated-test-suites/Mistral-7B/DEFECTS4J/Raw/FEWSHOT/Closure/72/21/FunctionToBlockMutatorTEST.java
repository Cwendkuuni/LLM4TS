package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionToBlockMutatorTest {
    @Test
    public void testMakeLocalNamesUnique() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node fnNode = NodeUtil.newFunctionNode(
            "testMakeLocalNamesUnique",
            Lists.newArrayList("a", "b", "c"),
            NodeUtil.newBlock(
                NodeUtil.newVarNode("a", null),
                NodeUtil.newVarNode("b", null),
                NodeUtil.newVarNode("c", null)
            )
        );

        LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
        argMap.put("a", NodeUtil.newNumberNode(1));
        argMap.put("b", NodeUtil.newNumberNode(2));
        argMap.put("c", NodeUtil.newNumberNode(3));

        mutator.makeLocalNamesUnique(fnNode, false);

        for (Node child : fnNode.children()) {
            for (Node c = child.getFirstChild(); c != null; c = c.getNext()) {
                String name = c.getString();
                if (name != null) {
                    assertTrue(name.startsWith("a_"));
                    assertTrue(name.length() > 3);
                }
            }
        }
    }

    @Test
    public void testReplaceReturns() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)),
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(43))
        );

        String labelName = "foo";
        String resultName = "result";

        Node result = mutator.replaceReturns(block, resultName, labelName, true);

        assertEquals(2, result.getChildCount());
        Node firstStatement = result.getFirstChild();
        assertEquals(Token.BREAK, firstStatement.getType());
        Node secondStatement = result.getChildren().get(1);
        assertEquals(Token.ASSIGN, secondStatement.getType());
        Node resultNode = secondStatement.getFirstChild();
        assertEquals(Token.NAME, resultNode.getType());
        assertEquals("result", resultNode.getString());
    }

    @Test
    public void testReplaceReturns_noResult() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42)),
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(43))
        );

        String labelName = "foo";

        Node result = mutator.replaceReturns(block, null, labelName, false);

        assertEquals(2, result.getChildCount());
        Node firstStatement = result.getFirstChild();
        assertEquals(Token.BREAK, firstStatement.getType());
        Node secondStatement = result.getChildren().get(1);
        assertEquals(Token.VOID, secondStatement.getType());
    }

    @Test
    public void testReplaceReturns_singleReturn() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))
        );

        String labelName = "foo";
        String resultName = "result";

        Node result = mutator.replaceReturns(block, resultName, labelName, true);

        assertEquals(1, result.getChildCount());
        Node statement = result.getFirstChild();
        assertEquals(Token.ASSIGN, statement.getType());
        Node resultNode = statement.getFirstChild();
        assertEquals(Token.NAME, resultNode.getType());
        assertEquals("result", resultNode.getString());
    }

    @Test
    public void testReplaceReturns_singleReturn_noResult() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))
        );

        String labelName = "foo";

        Node result = mutator.replaceReturns(block, null, labelName, false);

        assertEquals(1, result.getChildCount());
        Node statement = result.getFirstChild();
        assertEquals(Token.BREAK, statement.getType());
    }

    @Test
    public void testReplaceReturns_noReturn() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newAssignStatementNode("x", NodeUtil.newNumberNode(42))
        );

        String labelName = "foo";
        String resultName = "result";

        Node result = mutator.replaceReturns(block, resultName, labelName, true);

        assertEquals(1, result.getChildCount());
        Node statement = result.getFirstChild();
        assertEquals(Token.ASSIGN, statement.getType());
        Node resultNode = statement.getFirstChild();
        assertEquals(Token.NAME, resultNode.getType());
        assertEquals("result", resultNode.getString());
    }

    @Test
    public void testReplaceReturns_noReturn_noResult() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newAssignStatementNode("x", NodeUtil.newNumberNode(42))
        );

        String labelName = "foo";

        Node result = mutator.replaceReturns(block, null, labelName, false);

        assertEquals(1, result.getChildCount());
        Node statement = result.getFirstChild();
        assertEquals(Token.BREAK, statement.getType());
    }

    @Test
    public void testReplaceReturns_noLabel() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))
        );

        String resultName = "result";

        Node result = mutator.replaceReturns(block, resultName, null, true);

        assertEquals(1, result.getChildCount());
        Node statement = result.getFirstChild();
        assertEquals(Token.ASSIGN, statement.getType());
        Node resultNode = statement.getFirstChild();
        assertEquals(Token.NAME, resultNode.getType());
        assertEquals("result", resultNode.getString());
    }

    @Test
    public void testReplaceReturns_noLabel_noResult() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))
        );

        Node result = mutator.replaceReturns(block, null, null, false);

        assertEquals(1, result.getChildCount());
        Node statement = result.getFirstChild();
        assertEquals(Token.BREAK, statement.getType());
    }

    @Test
    public void testReplaceReturns_labelExists() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newLabelNode("foo"),
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))
        );

        String labelName = "foo";
        String resultName = "result";

        Node result = mutator.replaceReturns(block, resultName, labelName, true);

        assertEquals(2, result.getChildCount());
        Node firstStatement = result.getFirstChild();
        assertEquals(Token.BREAK, firstStatement.getType());
        Node secondStatement = result.getChildren().get(1);
        assertEquals(Token.ASSIGN, secondStatement.getType());
        Node resultNode = secondStatement.getFirstChild();
        assertEquals(Token.NAME, resultNode.getType());
        assertEquals("result", resultNode.getString());
    }

    @Test
    public void testReplaceReturns_labelExists_noResult() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newLabelNode("foo"),
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))
        );

        String labelName = "foo";

        Node result = mutator.replaceReturns(block, null, labelName, false);

        assertEquals(1, result.getChildCount());
        Node statement = result.getFirstChild();
        assertEquals(Token.BREAK, statement.getType());
    }

    @Test
    public void testReplaceReturns_labelDoesNotExist() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newLabelNode("foo"),
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))
        );

        String labelName = "bar";
        String resultName = "result";

        Node result = mutator.replaceReturns(block, resultName, labelName, true);

        assertEquals(2, result.getChildCount());
        Node firstStatement = result.getFirstChild();
        assertEquals(Token.BREAK, firstStatement.getType());
        Node secondStatement = result.getChildren().get(1);
        assertEquals(Token.ASSIGN, secondStatement.getType());
        Node resultNode = secondStatement.getFirstChild();
        assertEquals(Token.NAME, resultNode.getType());
        assertEquals("result", resultNode.getString());
    }

    @Test
    public void testReplaceReturns_labelDoesNotExist_noResult() {
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);

        Node block = NodeUtil.newBlock(
            NodeUtil.newLabelNode("foo"),
            NodeUtil.newReturnNode(NodeUtil.newNumberNode(42))
        );

        String labelName = "bar";

        Node result = mutator.replaceReturns(block, null, labelName, false);

        assertEquals(1, result.getChildCount());
        Node statement = result.getFirstChild();
        assertEquals(Token.BREAK, statement.getType());
    }
}