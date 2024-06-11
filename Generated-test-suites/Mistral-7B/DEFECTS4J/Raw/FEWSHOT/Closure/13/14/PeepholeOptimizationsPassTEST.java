package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;

public class PeepholeOptimizationsPassTest {

    @Test
    public void testSimpleTraversal() {
        Node root = Node.newNode("root");
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.process(null, root);
        assertTrue(pass.traversalState.peek().traverseChildScopes);
    }

    @Test
    public void testTraversalWithFunction() {
        Node root = Node.newNode("root");
        Node functionNode = Node.newNode("function", null, null);
        root.addChild(functionNode);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.process(null, root);
        assertTrue(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.push();
        assertFalse(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.pop();
        assertTrue(pass.traversalState.peek().traverseChildScopes);
    }

    @Test
    public void testTraversalWithScript() {
        Node root = Node.newNode("root");
        Node scriptNode = Node.newNode("script", null, null);
        root.addChild(scriptNode);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.process(null, root);
        assertTrue(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.push();
        assertFalse(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.pop();
        assertTrue(pass.traversalState.peek().traverseChildScopes);
    }

    @Test
    public void testTraversalWithFunctionAndChange() {
        Node root = Node.newNode("root");
        Node functionNode = Node.newNode("function", null, null);
        Node changeNode = Node.newNode("change");
        functionNode.addChild(changeNode);
        root.addChild(functionNode);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.process(null, root);
        assertTrue(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.push();
        assertFalse(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.pop();
        assertTrue(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.peek().changed = true;
        pass.traverse(root);
        assertTrue(pass.traversalState.peek().changed);
        assertTrue(pass.traversalState.peek().traverseChildScopes);
    }

    @Test
    public void testTraversalWithScriptAndChange() {
        Node root = Node.newNode("root");
        Node scriptNode = Node.newNode("script", null, null);
        Node changeNode = Node.newNode("change");
        scriptNode.addChild(changeNode);
        root.addChild(scriptNode);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.process(null, root);
        assertTrue(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.push();
        assertFalse(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.pop();
        assertTrue(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.peek().changed = true;
        pass.traverse(root);
        assertTrue(pass.traversalState.peek().changed);
        assertTrue(pass.traversalState.peek().traverseChildScopes);
    }

    @Test
    public void testTraversalWithChange() {
        Node root = Node.newNode("root");
        Node changeNode = Node.newNode("change");
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.process(null, root);
        pass.traversalState.peek().changed = true;
        pass.traverse(root);
        assertTrue(pass.traversalState.peek().changed);
    }

    @Test(expected = AssertionError.class)
    public void testTraversalWithChangeAndRecursion() {
        Node root = Node.newNode("root");
        Node changeNode = Node.newNode("change");
        Node functionNode = Node.newNode("function", null, null);
        functionNode.addChild(changeNode);
        root.addChild(functionNode);
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.process(null, root);
        pass.traversalState.peek().changed = true;
        pass.traverse(root);
    }

    @Test
    public void testPeepholeOptimization() {
        Node root = Node.newNode("root");
        Node functionNode = Node.newNode("function", null, null);
        Node addNode = Node.newNode("Add", null, null);
        Node number1 = Node.newNode("Number", 1);
        Node number2 = Node.newNode("Number", 2);
        addNode.addChild(number1);
        addNode.addChild(number2);
        functionNode.addChild(addNode);
        Node resultNode = Node.newNode("Return", null, addNode);
        functionNode.addChild(resultNode);
        root.addChild(functionNode);

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.process(null, root);
        Node optimizedNode = pass.traverse(functionNode);
        assertTrue(optimizedNode instanceof Node
            && optimizedNode.getClass().isAssignableFrom(Node.Class.NUMBER));
        Node optimizedResultNode = pass.traverse(functionNode);
        assertTrue(optimizedResultNode instanceof Node
            && optimizedResultNode.getClass().isAssignableFrom(Node.Class.NUMBER));
        assertEquals(3, ((Node.Number)optimizedNode).intValue());
        assertEquals(3, ((Node.Number)optimizedResultNode).intValue());
    }
}