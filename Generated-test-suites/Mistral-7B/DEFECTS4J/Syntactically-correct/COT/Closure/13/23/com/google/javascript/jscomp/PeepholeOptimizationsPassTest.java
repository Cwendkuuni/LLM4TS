package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest {
    private Compiler compiler;
    private PeepholeOptimizationsPass optimizationsPass;

    @Before
    public void setUp() {
        compiler = new Compiler();
        optimizationsPass = new PeepholeOptimizationsPass(compiler, new DummyPeepholeOptimization());
    }

    @Test
    public void testProcess_ExternsIsNull_ThrowsException() {
        try {
            optimizationsPass.process(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testProcess_RootIsNull_ThrowsException() {
        try {
            optimizationsPass.process(compiler.getExterns(), null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testProcess_EmptyAST_DoesNotChangeAST() {
        Node root = Node.NULL();
        optimizationsPass.process(compiler.getExterns(), root);
        assertNull(compiler.getChangeLog());
    }

    @Test
    public void testProcess_SimpleAST_OptimizesAST() {
        Node root = NodeFactory.createProgram(
            NodeFactory.createVar("x", Node.NUMBER_LITERAL(42)),
            NodeFactory.createReturn(NodeFactory.createIdent("x"))
        );
        optimizationsPass.process(compiler.getExterns(), root);
        Node expected = NodeFactory.createProgram(
            NodeFactory.createVar("x", Node.NUMBER_LITERAL(42)),
            NodeFactory.createReturn(NodeFactory.createNumber(42))
        );
        Node actual = compiler.getNodeFromChangeLog(expected);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testTraverse_NullNode_DoesNotTraverse() {
        optimizationsPass.traverse(null);
    }

    @Test
    public void testShouldVisit_FunctionNode_VisitsChildren() {
        Node root = NodeFactory.createProgram(
            NodeFactory.createFunction("func", new ArrayList<Node>(), NodeFactory.createBlock(
                NodeFactory.createVar("x", Node.NUMBER_LITERAL(42)),
                NodeFactory.createReturn(NodeFactory.createIdent("x"))
            ))
        );
        optimizationsPass.traverse(root);
        ScopeState state = optimizationsPass.traversalState.peek();
        assertTrue(state.traverseChildScopes);
    }

    @Test
    public void testShouldVisit_ScriptNode_VisitsChildren() {
        Node root = NodeFactory.createScript("func() { var x = 42; }");
        optimizationsPass.traverse(root);
        ScopeState state = optimizationsPass.traversalState.peek();
        assertTrue(state.traverseChildScopes);
    }

    @Test
    public void testShouldVisit_NonFunctionNonScriptNode_Visits() {
        Node root = NodeFactory.createIntLiteral(42);
        optimizationsPass.traverse(root);
        assertTrue(optimizationsPass.shouldVisit(root));
    }

    @Test
    public void testShouldRetraverse_NonFunctionNonScriptNode_DoesNotRetransverse() {
        Node root = NodeFactory.createIntLiteral(42);
        assertFalse(optimizationsPass.shouldRetransverse(root));
    }

    @Test
    public void testShouldRetransverse_FunctionOrScriptNode_Retransverses() {
        Node root = NodeFactory.createFunction("func", new ArrayList<Node>(), NodeFactory.createBlock());
        optimizationsPass.traverse(root);
        ScopeState state = optimizationsPass.traversalState.peek();
        assertTrue(state.changed);
        state.reset();
        assertFalse(optimizationsPass.shouldRetransverse(root));

        root = NodeFactory.createScript("func() { var x = 42; }");
        optimizationsPass.traverse(root);
        state = optimizationsPass.traversalState.peek();
        assertTrue(state.changed);
        state.reset();
        assertTrue(optimizationsPass.shouldRetransverse(root));
    }

    @Test
    public void testVisit_SimpleNode_DoesNotChangeNode() {
        Node root = NodeFactory.createIntLiteral(42);
        optimizationsPass.visit(root);
        assertEquals(Node.INT_LITERAL, root.getType());
    }

    @Test
    public void testVisit_FunctionNode_OptimizesFunction() {
        Node root = NodeFactory.createFunction("func", new ArrayList<Node>(), NodeFactory.createBlock(
            NodeFactory.createVar("x", Node.NUMBER_LITERAL(42)),
            NodeFactory.createReturn(NodeFactory.createIdent("x"))
        ));
        optimizationsPass.visit(root);
        Node expected = NodeFactory.createFunction("func", new ArrayList<Node>(), NodeFactory.createBlock(
            NodeFactory.createVar("x", Node.NUMBER_LITERAL(42)),
            NodeFactory.createReturn(NodeFactory.createNumber(42))
        ));
        Node optimized = compiler.getNodeFromChangeLog(expected);
        assertNotNull(optimized);
        assertEquals(expected, optimized);
    }

    @Test
    public void testVisit_ScriptNode_OptimizesScript() {
        Node root = NodeFactory.createScript("func() { var x = 42; }");
        optimizationsPass.visit(root);
        Node expected = NodeFactory.createScript("func() { var x = 42; }");
        Node optimized = compiler.getNodeFromChangeLog(expected);
        assertNotNull(optimized);
        assertEquals(expected, optimized);
    }

    @Test
    public void testVisit_FunctionWithChangedChild_Retransverses() {
        Node root = NodeFactory.createFunction("func", new ArrayList<Node>(), NodeFactory.createBlock(
            NodeFactory.createVar("x", Node.NUMBER_LITERAL(42)),
            NodeFactory.createReturn(NodeFactory.createIdent("x"))
        ));
        optimizationsPass.traverse(root);
        optimizationsPass.traversalState.peek().reset();
        ScopeState state = optimizationsPass.traversalState.peek();
        assertFalse(state.traverseChildScopes);
        Node child = NodeFactory.createIdent("x");
        Node parent = NodeFactory.createFunction("func", new ArrayList<Node>(), NodeFactory.createBlock(child, NodeFactory.createReturn(child)));
        optimizationsPass.visit(parent);
        Node expected = NodeFactory.createFunction("func", new ArrayList<Node>(), NodeFactory.createBlock(
            NodeFactory.createVar("x", Node.NUMBER_LITERAL(42)),
            NodeFactory.createReturn(NodeFactory.createNumber(42))
        ));
        Node optimized = compiler.getNodeFromChangeLog(expected);
        assertNotNull(optimized);
        assertEquals(expected, optimized);
    }

    @Test
    public void testExitNode_FunctionNode_PopTraversalState() {
        Node root = NodeFactory.createFunction("func", new ArrayList<Node>(), NodeFactory.createBlock(
            NodeFactory.createVar("x", Node.NUMBER_LITERAL(42)),
            NodeFactory.createReturn(NodeFactory.createIdent("x"))
        ));
        optimizationsPass.traverse(root);
        optimizationsPass.traverse(NodeFactory.createIntLiteral(42));
        optimizationsPass.exitNode(root);
        assertEquals(1, optimizationsPass.traversalState.getCurrentDepth());
    }

    @Test
    public void testExitNode_ScriptNode_PopTraversalState() {
        Node root = NodeFactory.createScript("func() { var x = 42; }");
        optimizationsPass.traverse(root);
        optimizationsPass.traverse(NodeFactory.createIntLiteral(42));
        optimizationsPass.exitNode(root);
        assertEquals(1, optimizationsPass.traversalState.getCurrentDepth());
    }

    @Test
    public void testBeginTraversal_OptimizationsAreRegistered() {
        PeepholeChangeHandler changeHandler = new PeepholeChangeHandler();
        optimizationsPass.beginTraversal();
        for (AbstractPeepholeOptimization optimization : optimizationsPass.peepholeOptimizations) {
            optimization.registerTraversalHandler(changeHandler);
        }
        assertNotNull(changeHandler.getChangeLog());
        optimizationsPass.endTraversal();
    }

    @Test(expected = AssertionError.class)
    public void testBeginTraversal_OptimizationsAreNotRegistered() {
        optimizationsPass.beginTraversal();
        PeepholeChangeHandler changeHandler = new PeepholeChangeHandler();
        for (AbstractPeepholeOptimization optimization : optimizationsPass.peepholeOptimizations) {
            optimization.registerTraversalHandler(changeHandler);
        }
        optimizationsPass.endTraversal();
        changeHandler.assertChangeLogIsEmpty();
        fail("Expected AssertionError");
    }

    @Test
    public void testEndTraversal_OptimizationsAreDeregistered() {
        PeepholeChangeHandler changeHandler = new PeepholeChangeHandler();
        optimizationsPass.beginTraversal();
        for (AbstractPeepholeOptimization optimization : optimizationsPass.peepholeOptimizations) {
            optimization.registerTraversalHandler(changeHandler);
        }
        optimizationsPass.endTraversal();
        for (AbstractPeepholeOptimization optimization : optimizationsPass.peepholeOptimizations) {
            optimization.deregisterTraversalHandler(changeHandler);
        }
        assertNull(changeHandler.getChangeLog());
    }

    @Test(expected = AssertionError.class)
    public void testEndTraversal_OptimizationsAreNotDeregistered() {
        optimizationsPass.beginTraversal();
        PeepholeChangeHandler changeHandler = new PeepholeChangeHandler();
        for (AbstractPeepholeOptimization optimization : optimizationsPass.peepholeOptimizations) {
            optimization.registerTraversalHandler(changeHandler);
        }
        optimizationsPass.endTraversal();
        for (AbstractPeepholeOptimization optimization : optimizationsPass.peepholeOptimizations) {
            optimization.deregisterTraversalHandler(changeHandler);
            fail("Expected AssertionError");
        }
    }

    private static NodeFactory nodeFactory = new NodeFactory();

    private static class DummyPeepholeOptimization implements AbstractPeepholeOptimization {
        @Override
        public Node optimizeSubtree(Node node) {
            return node;
        }

        @Override
        public void beginTraversal(Compiler compiler) {
        }

        @Override
        public void endTraversal(Compiler compiler) {
        }

        @Override
        public void registerTraversalHandler(PeepholeChangeHandler handler) {
        }

        @Override
        public void deregisterTraversalHandler(PeepholeChangeHandler handler) {
        }
    }
}

class NodeFactory {
    NodeFactory() {}

    Node createIntLiteral(int value) {
        Node node = Node.newIntLiteral(value);
        node.setType(Node.INT_LITERAL);
        return node;
    }

    Node createNumber(double value) {
        Node node = Node.newNumber(value);
        node.setType(Node.NUMBER_LITERAL);
        return node;
    }

    Node createIdent(String name) {
        Node node = Node.newIdent(name);
        node.setType(Node.NAME);
        return node;
    }

    Node createFunction(String name, List<Node> formals, Node body) {
        Node node = Node.newFunction(name, formals, body);
        node.setType(Node.FUNCTION);
        return node;
    }

    Node createBlock(Node... statements) {
        Node node = Node.newBlock(Arrays.asList(statements));
        node.setType(Node.BLOCK);
        return node;
    }

    Node createProgram(Node... statements) {
        Node node = Node.newProgram(Arrays.asList(statements));
        node.setType(Node.PROGRAM);
        return node;
    }

    Node createScript(String script) {
        Node node = Node.parseJs(script);
        node.setType(Node.SCRIPT);
        return node;
    }
}