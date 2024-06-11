package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest {
    @SuppressWarnings("unchecked")
    static <T extends AbstractPeepholeOptimization> T newPeepholeOptimizationInstance(Class<T> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class TestCompiler extends AbstractCompiler {}

    @Test
    public void testPeepholeOptimizationsPass() {
        TestCompiler compiler = new TestCompiler();
        AbstractPeepholeOptimization optimization1 = newPeepholeOptimizationInstance(ConstantFoldingOptimization.class);
        AbstractPeepholeOptimization optimization2 = newPeepholeOptimizationInstance(DeadCodeEliminationOptimization.class);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

        Node rootNode = NodeOperations.parseTemplate("function foo() { var x = 1; var y = 1; return x + y; }");
        Node resultNode = NodeOperations.parseTemplate("function foo() { return 2; }");

        pass.process(null, rootNode);

        Node actualResultNode = pass.getCompiler().getOutput();
        Node expectedResultNode = resultNode;

        NodeUtils.assertNodesEqual(expectedResultNode, actualResultNode);
    }

    @Test
    public void testPeepholeOptimizationsPass_Optimization1Skipped() {
        TestCompiler compiler = new TestCompiler();
        AbstractPeepholeOptimization optimization1 = newPeepholeOptimizationInstance(ConstantFoldingOptimization.class);
        optimization1.setSkip(true);
        AbstractPeepholeOptimization optimization2 = newPeepholeOptimizationInstance(DeadCodeEliminationOptimization.class);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

        Node rootNode = NodeOperations.parseTemplate("function foo() { var x = 1; var y = 1; return x + y; }");
        Node resultNode = NodeOperations.parseTemplate("function foo() { var x = 1; var y = 1; return x + y; }");

        pass.process(null, rootNode);

        Node actualResultNode = pass.getCompiler().getOutput();
        Node expectedResultNode = resultNode;

        NodeUtils.assertNodesEqual(expectedResultNode, actualResultNode);
    }

    @Test
    public void testPeepholeOptimizationsPass_Optimization2Skipped() {
        TestCompiler compiler = new TestCompiler();
        AbstractPeepholeOptimization optimization1 = newPeepholeOptimizationInstance(ConstantFoldingOptimization.class);
        AbstractPeepholeOptimization optimization2 = newPeepholeOptimizationInstance(DeadCodeEliminationOptimization.class);
        optimization2.setSkip(true);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

        Node rootNode = NodeOperations.parseTemplate("function foo() { var x = 1; return x; }");
        Node resultNode = NodeOperations.parseTemplate("function foo() { var x = 1; return x; }");

        pass.process(null, rootNode);

        Node actualResultNode = pass.getCompiler().getOutput();
        Node expectedResultNode = resultNode;

        NodeUtils.assertNodesEqual(expectedResultNode, actualResultNode);
    }

    @Test
    public void testPeepholeOptimizationsPass_TwoOptimizationsApplied() {
        TestCompiler compiler = new TestCompiler();
        AbstractPeepholeOptimization optimization1 = newPeepholeOptimizationInstance(ConstantFoldingOptimization.class);
        AbstractPeepholeOptimization optimization2 = newPeepholeOptimizationInstance(DeadCodeEliminationOptimization.class);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

        Node rootNode = NodeOperations.parseTemplate("function foo() { var x = 1; var y = 1; return x + y; }");
        Node resultNode = NodeOperations.parseTemplate("function foo() { return 1; }");

        pass.process(null, rootNode);

        Node actualResultNode = pass.getCompiler().getOutput();
        Node expectedResultNode = resultNode;

        NodeUtils.assertNodesEqual(expectedResultNode, actualResultNode);
    }

    @Test
    public void testPeepholeOptimizationsPass_TraversalStateReset() {
        TestCompiler compiler = new TestCompiler();
        AbstractPeepholeOptimization optimization1 = newPeepholeOptimizationInstance(ConstantFoldingOptimization.class);
        AbstractPeepholeOptimization optimization2 = newPeepholeOptimizationInstance(DeadCodeEliminationOptimization.class);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

        Node rootNode1 = NodeOperations.parseTemplate("function foo() { var x = 1; var y = 1; return x + y; }");
        Node resultNode1 = NodeOperations.parseTemplate("function foo() { return 1; }");

        Node rootNode2 = NodeOperations.parseTemplate("function foo() { var x = 1; var y = 1; return x + y; }");
        Node resultNode2 = NodeOperations.parseTemplate("function foo() { var x = 1; var y = 1; return x + y; }");

        pass.process(null, rootNode1);
        Node actualResultNode1 = pass.getCompiler().getOutput();
        Node expectedResultNode1 = resultNode1;

        NodeUtils.assertNodesEqual(expectedResultNode1, actualResultNode1);

        pass.process(null, rootNode2);
        Node actualResultNode2 = pass.getCompiler().getOutput();
        Node expectedResultNode2 = resultNode2;

        NodeUtils.assertNodesEqual(expectedResultNode2, actualResultNode2);

        ScopeState state = pass.getTraversalState().peek();
        assertThat(state.changed).isFalse();
        assertThat(state.traverseChildScopes).isTrue();

        pass.getTraversalState().push();
        state = pass.getTraversalState().peek();
        assertThat(state.changed).isFalse();
        assertThat(state.traverseChildScopes).isFalse();
    }

    @Test
    public void testPeepholeOptimizationsPass_NullTraversalState() {
        TestCompiler compiler = new TestCompiler();
        AbstractPeepholeOptimization optimization1 = newPeepholeOptimizationInstance(ConstantFoldingOptimization.class);
        AbstractPeepholeOptimization optimization2 = newPeepholeOptimizationInstance(DeadCodeEliminationOptimization.class);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
        pass.setTraversalState(null);

        Node rootNode = NodeOperations.parseTemplate("function foo() { var x = 1; var y = 1; return x + y; }");
        Node resultNode = NodeOperations.parseTemplate("function foo() { return 1; }");

        assertNull(pass.getTraversalState());

        pass.process(null, rootNode);

        Node actualResultNode = pass.getCompiler().getOutput();
        Node expectedResultNode = resultNode;

        NodeUtils.assertNodesEqual(expectedResultNode, actualResultNode);
    }
}

class NodeUtils {
    static void assertNodesEqual(Node a, Node b) {
        if (a == b) {
            return;
        }

        if (a.getNodeType() != b.getNodeType()) {
            throw new RuntimeException("Expected nodes to have the same type: " + a.getNodeType() + " != " + b.getNodeType());
        }

        switch (a.getNodeType()) {
            case Node.ABSTRACT:
            case Node.ACCESSOR:
            case Node.ARRAY_LITERAL:
            case Node.ASGN:
            case Node.ASSIGNMENT:
            case Node.ASYNCHRONOUS:
            case Node.BLOCK:
            case Node.BooleanLiteral:
            case Node.Call:
            case Node.Catch:
            case Node.CatchAny:
            case Node.CharLiteral:
            case Node.ContinueStatement:
            case Node.DebuggerStatement:
            case Node.DeleteProperty:
            case Node.DoWhileStatement:
            case Node.EmptyStatement:
            case Node.ExportDefaultDeclaration:
            case Node.ExportNamedDeclaration:
            case Node.ExportStatement:
            case Node.ForStatement:
            case Node.Function:
            case Node.FunctionDeclaration:
            case Node.FunctionExpression:
            case Node.Identifier:
            case Node.IfStatement:
            case Node.ImportDeclaration:
            case Node.ImportMeta:
            case Node.ImportNamespace:
            case Node.IntegerLiteral:
            case Node.Interface:
            case Node.LabeledStatement:
            case Node.Lambdap:
            case Node.Lambdar:
            case Node.LogicalOR:
            case Node.LogicalAND:
            case Node.MemberExpression:
            case Node.Method:
            case Node.MethodDeclaration:
            case Node.Name:
            case Node.NewExpression:
            case Node.ObjectLiteral:
            case Node.ReturnStatement:
            case Node.Script:
            case Node.SequenceExpression:
            case Node.SetProperty:
            case Node.StringLiteral:
            case Node.SwitchStatement:
            case Node.SynchronizedStatement:
            case Node.ThisKeyword:
            case Node.ThrowStatement:
            case Node.TryStatement:
            case Node.TypeOf:
            case Node.UniqueIdentifier:
            case Node.UnaryMinus:
            case Node.UnaryPlus:
            case Node.VariableDeclarationList:
            case Node.VariableDeclaration:
            case Node.WhileStatement:
            case Node.WithStatement:
            case Node.YieldStatement:
            case Node.YieldExpression:
                List<Node> aChildren = Arrays.asList(a.getChildren());
                List<Node> bChildren = Arrays.asList(b.getChildren());
                assertNodesEqualLists(aChildren, bChildren);
                break;
            case Node.Array:
                List<Node> aElements = Arrays.asList(a.getFirstChild().getChildren());
                List<Node> bElements = Arrays.asList(b.getFirstChild().getChildren());
                assertNodesEqualLists(aElements, bElements);
                break;
            case Node.Object:
                List<NodePair> aProperties = new ArrayList<>();
                List<NodePair> bProperties = new ArrayList<>();
                for (Node child = a.getFirstChild().getNext(); child != null; child = child.getNext()) {
                    aProperties.add(new NodePair(child.getFirstChild(), child.getLastChild()));
                }
                for (Node child = b.getFirstChild().getNext(); child != null; child = child.getNext()) {
                    bProperties.add(new NodePair(child.getFirstChild(), child.getLastChild()));
                }
                assertNodesEqualLists(aProperties, bProperties);
                break;
            default:
                throw new RuntimeException("Unsupported node type: " + a.getNodeType());
        }
    }

    static void assertNodesEqualLists(List<Node> a, List<Node> b) {
        if (a.size() != b.size()) {
            throw new RuntimeException("Expected lists to have the same size: " + a.size() + " != " + b.size());
        }

        for (int i = 0; i < a.size(); i++) {
            Node aNode = a.get(i);
            Node bNode = b.get(i);
            assertNodesEqual(aNode, bNode);
        }
    }

    static class NodePair {
        Node key;
        Node value;

        NodePair(Node key, Node value) {
            this.key = key;
            this.value = value;
        }
    }
}