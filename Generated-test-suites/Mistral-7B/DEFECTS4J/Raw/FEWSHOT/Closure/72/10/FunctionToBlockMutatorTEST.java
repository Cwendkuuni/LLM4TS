package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class FunctionToBlockMutatorTest {
    @Test
    public void testMutateFunction() {
        // Assuming you have a valid AbstractCompiler and Supplier instance.
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
        Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, NodeUtil.newBlockNode(null));
        Node callNode = NodeUtil.newCallNode("test", NodeUtil.newArrayNode(new Object[]{}));
        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;
        Node mutatedBlock = mutator.mutate("test", fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        assertNotNull(mutatedBlock);
    }

    @Test
    public void testFixUnitializedVarDeclarations() {
        // Test fixing unitialized VAR declarations
        Node node = NodeUtil.newVarNode("name", NodeUtil.newStringNode(""));
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
        mutator.fixUnitializedVarDeclarations(node);
        Node initializedName = node.getFirstChild();
        assertNotNull(initializedName);
        assertNotNull(initializedName.getValue());
    }

    @Test
    public void testMakeLocalNamesUnique() {
        // Test local name uniqueness
        Node fnNode = NodeUtil.newFunctionNode("test", null, null, null, NodeUtil.newBlockNode(null));
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
        mutator.makeLocalNamesUnique(fnNode, false);
        NodeTraversal.traverse(null, fnNode, new NodeNameVisitor());
        assertFalse(fnNode.getFirstChild().getName().equals("name1"));
        assertFalse(fnNode.getFirstChild().getName().equals("name2"));
    }

    @Test
    public void testReplaceReturns() {
        // Test replacing returns with breaks
        Node block = NodeUtil.newBlockNode(null);
        Node retNode = NodeUtil.newReturnNode(null);
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
        String labelName = "testLabel";
        mutator.replaceReturns(block, "result", labelName, true);
        Node breakNode = block.getLastChild();
        assertNotNull(breakNode);
        assertEquals(Token.BREAK, breakNode.getType());
    }

    @Test
    public void testAliasAndInlineArguments() {
        // Test aliasing and inlining arguments
        Node fnTemplateRoot = NodeUtil.newFunctionNode("test", null, null, null, NodeUtil.newBlockNode(null));
        LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
        argMap.put("arg1", NodeUtil.newVarNode("arg1", NodeUtil.newStringNode("")));
        Set<String> namesToAlias = Set.of("arg1");
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
        Node result = mutator.aliasAndInlineArguments(fnTemplateRoot, argMap, namesToAlias);
        assertNotNull(result);
        assertTrue(fnTemplateRoot.equals(result));
    }

    @Test
    public void testReplaceReturnsWithResult() {
        // Test replacing returns with result assignments
        Node block = NodeUtil.newBlockNode(null);
        Node retNode = NodeUtil.newReturnNode(null);
        String resultName = "result";
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
        mutator.replaceReturns(block, resultName, null, true);
        Node assignNode = block.getLastChild();
        assertNotNull(assignNode);
        assertEquals(Token.ASSIGN, assignNode.getType());
        assertEquals(resultName, assignNode.getFirstChild().getName());
    }

    @Test
    public void testReplaceReturnsWithEmptyBlock() {
        // Test replacing returns with empty blocks
        Node block = NodeUtil.newBlockNode(null);
        Node retNode = NodeUtil.newReturnNode(null);
        FunctionToBlockMutator mutator = new FunctionToBlockMutator(null, null);
        mutator.replaceReturns(block, null, null, true);
        assertTrue(block.getLastChild() == NodeUtil.newEmptyStatementNode());
    }

    private static class NodeNameVisitor
            extends NodeTraversal.AbstractNodeVisitor<Void> {
        private int nameCounter = 0;

        @Override
        public Void visit(Node node) {
            if (node.getType() == Token.NAME) {
                node.setName("name" + nameCounter++);
            }
            return null;
        }
    }
}