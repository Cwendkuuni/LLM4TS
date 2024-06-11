package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.jscomp.FunctionToBlockMutator.MakeDeclaredNamesUnique.InlineRenamer;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class FunctionToBlockMutatorTest {

    private static Node createTestFunctionNode(String functionName) {
        Node functionNode = new Node(Token.FUNCTION);
        functionNode.addChild(new Node(Token.FUNCTION_DECLARATION, null, functionName, null, null, null));
        Node body = new Node(Token.BLOCK);
        Node returnNode = new Node(Token.RETURN, NodeUtil.newNumberNode(42));
        body.addChildToBack(returnNode);
        functionNode.setBody(body);
        return functionNode;
    }

    @Test
    public void testMutate() {
        AbstractCompiler compiler = new TestCompiler();
        Supplier<String> safeNameIdSupplier = new Supplier<String>() {
            private int id = 0;

            @Override
            public String get() {
                return Integer.toString(id++);
            }
        };
        FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

        String functionName = "testFunction";
        Node functionNode = createTestFunctionNode(functionName);
        Node callNode = NodeUtil.newCallNode(functionName, Lists.newArrayList(NodeUtil.newNumberNode(1)));
        String resultName = "result";
        boolean needsDefaultResult = false;
        boolean isCallInLoop = false;
        Node mutatedNode = functionToBlockMutator.mutate(functionName, functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Check that the function body has been cloned and mutated
        assertThat(mutatedNode).isInstanceOf(Node.class);
        Node mutatedFunctionBody = NodeUtil.getFunctionBody(mutatedNode);
        assertThat(mutatedFunctionBody).isInstanceOf(Node.class);
        assertThat(mutatedFunctionBody).isNotNull();
        assertThat(mutatedFunctionBody.getFirstChild()).isNotNull();
        assertThat(mutatedFunctionBody.getType()).isEqualTo(Token.BLOCK);
        assertThat(mutatedNode).isNotSameInstanceAs(functionNode);

        // Check that local names are unique
        makeLocalNamesUnique(mutatedNode, isCallInLoop);
        Map<String, Node> localNames = new LinkedHashMap<>();
        NodeTraversal.traverse(compiler, mutatedNode, new NameCollector(localNames));
        Set<String> uniqueNames = localNames.keySet();
        for (String name : uniqueNames) {
            assertNameIsUnique(functionName, name, mutatedNode);
        }

        // Check that argument names are injected
        LinkedHashMap<String, Node> argMap = FunctionArgumentInjector.getFunctionCallParameterMap(functionNode, callNode, safeNameIdSupplier);
        FunctionArgumentInjector.inject(mutatedFunctionBody, null, argMap);
        for (Entry<String, Node> entry : argMap.entrySet()) {
            String argumentName = entry.getKey();
            Node argumentNode = entry.getValue();
            Node argNode = mutatedNode.findChildWithFunctionName("findArgumentByName");
            Node injectedNode = argNode.getChildAtIndex(argumentName.hashCode());
            assertThat(injectedNode).isNotNull();
            assertThat(injectedNode.getFirstChild()).isNotNull();
            assertThat(injectedNode.getFirstChild().getFirstChild().getValue()).isEqualTo(argumentNode.getValue().toString());
        }

        // Check that returns are replaced with breaks
        Node labelNameSupplier = new LabelNameSupplier(safeNameIdSupplier);
        String labelName = labelNameSupplier.get();
        replaceReturns(mutatedNode, resultName, labelName, needsDefaultResult);
        Node labelNode = mutatedNode.findChildWithFunctionName("getLabelNodeByName");
        Node block = labelNode.getNextSibling();
        assertThat(block).isInstanceOf(Node.class);
        assertThat(block.getType()).isEqualTo(Token.BLOCK);
        Node breakNode = block.getLastChild();
        assertThat(breakNode).isInstanceOf(Node.class);
        assertThat(breakNode.getType()).isEqualTo(Token.BREAK);

        // Check that result is set if needed
        if (needsDefaultResult) {
            Node resultNode = mutatedNode.findChildWithFunctionName("findResultByName");
            assertThat(resultNode).isNotNull();
            assertThat(resultNode.getFirstChild()).isNotNull();
            assertThat(resultNode.getFirstChild().getType()).isEqualTo(Token.NAME);
            assertThat(resultNode.getFirstChild().getValue()).isEqualTo(resultName);
        } else {
            assertNull(mutatedNode.findChildWithFunctionName("findResultByName"));
        }
    }

    @Test
    public void testReplaceReturns() {
        Node block = new Node(Token.BLOCK);
        Node returnNode1 = new Node(Token.RETURN, NodeUtil.newNameNode("a"));
        Node returnNode2 = new Node(Token.RETURN, NodeUtil.newNameNode("b"));
        block.addChildToBack(returnNode1);
        block.addChildToBack(returnNode2);
        String labelName = "foo";
        String resultName = "result";
        boolean resultMustBeSet = true;
        Node replacedBlock = replaceReturns(block, resultName, labelName, resultMustBeSet);

        // Check that returns have been replaced with breaks
        Node labelNode = NodeUtil.newLabelNode(labelName);
        Node breakNode1 = new Node(Token.BREAK, labelNode);
        Node breakNode2 = new Node(Token.BREAK, labelNode);
        Node newBlock = new Node(Token.BLOCK);
        newBlock.addChildToBack(labelNode);
        newBlock.addChildToBack(returnNode1);
        newBlock.addChildToBack(breakNode1);
        newBlock.addChildToBack(returnNode2);
        newBlock.addChildToBack(breakNode2);
        assertThat(replacedBlock).isInstanceOf(Node.class);
        assertThat(replacedBlock).isNotNull();
        assertThat(replacedBlock).isEqualTo(newBlock);

        // Check that result is set if needed
        if (resultMustBeSet) {
            Node resultNode = NodeUtil.newAssignStatementNode(resultName, NodeUtil.newNameNode("resultValue"));
            newBlock.addChildToBack(resultNode);
            Node expectedBlock = new Node(Token.BLOCK, NodeUtil.newAssignStatementNode(resultName, NodeUtil.newNameNode("resultValue")));
            assertThat(replacedBlock).isEqualTo(expectedBlock);
        } else {
            Node expectedBlock = new Node(Token.BLOCK, returnNode1, returnNode2);
            assertThat(replacedBlock).isEqualTo(expectedBlock);
        }
    }

    @Test
    public void testReplaceReturnsWithNullResult() {
        Node block = new Node(Token.BLOCK);
        Node returnNode = new Node(Token.RETURN);
        block.addChildToBack(returnNode);
        String labelName = "foo";
        String resultName = null;
        boolean resultMustBeSet = true;
        Node replacedBlock = replaceReturns(block, resultName, labelName, resultMustBeSet);

        // Check that no changes have been made to the block
        Node expectedBlock = block;
        assertThat(replacedBlock).isEqualTo(expectedBlock);
    }

    @Test
    public void testReplaceReturnsWithoutLabel() {
        Node block = new Node(Token.BLOCK);
        Node returnNode = new Node(Token.RETURN, NodeUtil.newNameNode("a"));
        block.addChildToBack(returnNode);
        String labelName = null;
        String resultName = "result";
        boolean resultMustBeSet = true;
        replaceReturns(block, resultName, labelName, resultMustBeSet);

        // Check that no changes have been made to the block
        Node expectedBlock = block;
        assertThat(block).isEqualTo(expectedBlock);
    }

    private void assertNameIsUnique(String functionName, String name, Node node) {
        Node nameNode = node.findChildWithFunctionName("findNameByName");
        Node nameNode2 = node.findChildWithFunctionName("findNameByName2");
        assertThat(nameNode).isNotNull();
        assertThat(nameNode2).isNull();
        Node functionNameNode = node.findChildWithFunctionName("findFunctionName");
        Node functionNameNode2 = node.findChildWithFunctionName("findFunctionName2");
        assertThat(functionNameNode).isNotNull();
        assertThat(functionNameNode2).isNull();
        Node firstChild = node.getFirstChild();
        for (Node c = firstChild; c != null; c = c.getNext()) {
            assertNameIsUnique(functionName, name, c);
        }
    }

    private static class NameCollector extends NodeVisitorEx<Map<String, Node>> {
        private Map<String, Node> localNames;

        NameCollector(Map<String, Node> localNames) {
            this.localNames = localNames;
        }

        @Override
        public void visitNode(Node node) {
            if (NodeUtil.isVar(node)) {
                String name = node.getFirstChild().getValue();
                localNames.put(name, node);
            }
            traverseChildren(node);
        }
    }
}