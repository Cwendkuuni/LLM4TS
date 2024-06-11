package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest {
    @Test
    public void testPeepholeOptimizationsPass() {
        Node rootNode = new Node_();
        AbstractCompiler compiler = new Compiler();
        PeepholeOptimizationsPass optimizationsPass = new PeepholeOptimizationsPass(compiler,
                new ConstantFoldingOptimization(), new UselessCodeRemovalOptimization());

        optimizationsPass.process(null, rootNode);

        // Basic test for traverse method
        ScopeState rootState = optimizationsPass.traversalState.peek();
        assertTrue(rootState.traverseChildScopes);

        Node functionNode = new NodeFunctionExpression(null, "function", Lists.newArrayList("a"), null,
                new NodeIdentifier("result"), null, Arrays.asList(new NodeNumber(1)), null);
        Node functionCallNode1 = new NodeCallExpression(functionNode, Arrays.asList(new NodeNumber(2)));
        Node functionCallNode2 = new NodeCallExpression(functionNode, Arrays.asList(new NodeNumber(3)));
        Node scriptNode = new NodeScript(null, null, null, Arrays.asList(functionCallNode1, functionCallNode2));

        optimizationsPass.traverse(scriptNode);

        rootState = optimizationsPass.traversalState.peek();
        assertFalse(rootState.changed);
        assertFalse(rootState.traverseChildScopes);

        // Test for visit method
        List<Node> expectedNodes = Lists.newArrayList(functionCallNode1, functionCallNode2);
        Node currentVersionOfNode = functionCallNode1;
        boolean somethingChanged = false;

        do {
            somethingChanged = false;
            for (AbstractPeepholeOptimization optimization : optimizationsPass.peepholeOptimizations) {
                Node newVersionOfNode = optimization.optimizeSubtree(currentVersionOfNode);

                if (newVersionOfNode != currentVersionOfNode) {
                    somethingChanged = true;

                    currentVersionOfNode = newVersionOfNode;
                }

                if (currentVersionOfNode == null) {
                    break;
                }
            }
            if (!somethingChanged) {
                List<Node> actualNodes = Lists.newArrayList();
                Node child = currentVersionOfNode.getFirstChild();
                while (child != null) {
                    actualNodes.add(child);
                    child = child.getNext();
                }
                assertSame(expectedNodes, actualNodes);
            }
        } while (somethingChanged);

        // Test for beginTraversal and endTraversal methods
        optimizationsPass.beginTraversal();
        optimizationsPass.endTraversal();

        for (AbstractPeepholeOptimization optimization : optimizationsPass.peepholeOptimizations) {
            optimization.beginTraversal(null);
            optimization.endTraversal(null);
        }

        // Test for shouldVisit, shouldRetraverse, and exitNode methods
        Node nodeToBeTraversed = new NodeIdentifier("test");
        Node parentNode = new NodeIdentifier("parent");
        parentNode.setParent(new NodeIdentifier("grandparent"));

        ScopeState grandparentState = optimizationsPass.traversalState.peek();
        assertFalse(optimizationsPass.shouldVisit(nodeToBeTraversed));
        assertSame(grandparentState, optimizationsPass.traversalState.peek());

        optimizationsPass.traversalState.push();
        assertTrue(optimizationsPass.shouldVisit(nodeToBeTraversed));
        assertSame(optimizationsPass.traversalState.peek(), optimizationsPass.traversalState.peek());

        optimizationsPass.exitNode(parentNode);
        assertSame(grandparentState, optimizationsPass.traversalState.peek());

        optimizationsPass.traversalState.pop();
        assertSame(grandparentState, optimizationsPass.traversalState.peek());

        optimizationsPass.traverse(nodeToBeTraversed.getParent());
        assertSame(grandparentState, optimizationsPass.traversalState.peek());

        // Test for peepholeChangeHandler in process method
        Multimap<Node, Node> nodesBeforeChange = ArrayListMultimap.create();
        Multimap<Node, Node> nodesAfterChange = ArrayListMultimap.create();
        optimizationsPass.compiler.addChangeHandler((ChangeEvent e) -> {
            nodesBeforeChange.put(e.getOriginalNode(), e.getReplacedNode());
            nodesAfterChange.put(e.getOriginalNode(), e.getReplacedNode());
        });

        optimizationsPass.process(null, rootNode);

        for (Node node : Arrays.asList(rootNode, scriptNode, functionNode, functionCallNode1, functionCallNode2)) {
            if (node == null) {
                continue;
            }
            Node before = nodesBeforeChange.get(node);
            Node after = nodesAfterChange.get(node);
            if (before == null && after == null) {
                continue;
            }
            if (before == null && after != null) {
                assertTrue(somethingChanged);
                assertSame(currentVersionOfNode, after);
            } else if (before != null && after == null) {
                assertNull(currentVersionOfNode);
            } else {
                assertSame(currentVersionOfNode, after);
            }
        }

        optimizationsPass.compiler.removeChangeHandler((ChangeEvent e) -> {});
    }
}

class Node_ {
}

class NodeFunctionExpression extends Node {
    NodeFunctionExpression(Node parent, String type, List<Node> parameters, List<Node> body,
            Node name, List<Node> formals, List<Node> decls, Node prototype) {
        super(type);
        setParent(parent);
        this.parameters = parameters;
        this.body = body;
        this.name = name;
        this.formals = formals;
        this.decls = decls;
        this.prototype = prototype;
    }

    @Override
    public List<Node> getChildNodes() {
        List<Node> children = new ArrayList<Node>();
        Node child = this.parameters;
        while (child != null) {
            children.add(child);
            child = child.getNext();
        }
        child = this.body.get(0);
        children.add(child);
        return children;
    }
}

class NodeIdentifier extends Node {
    NodeIdentifier(Node parent, String name) {
        super("Identifier");
        setParent(parent);
        this.name = name;
    }

    @Override
    public List<Node> getChildNodes() {
        return null;
    }
}

class NodeCallExpression extends Node {
    NodeCallExpression(Node callee, List<Node> arguments) {
        super("CallExpression");
        this.callee = callee;
        this.arguments = arguments;
    }

    @Override
    public List<Node> getChildNodes() {
        List<Node> children = new ArrayList<Node>();
        children.add(this.callee);
        Node child = this.arguments.get(0);
        while (child != null) {
            children.add(child);
            child = child.getNext();
        }
        return children;
    }

    Node callee;
    List<Node> arguments;
}

class NodeScript extends Node {
    NodeScript(Node parent, List<Node> body, Node sourceNode) {
        super("ExpressionStatement");
        this.body = body;
        setParent(parent);
        this.sourceNode = sourceNode;
    }

    @Override
    public List<Node> getChildNodes() {
        return this.body;
    }

    List<Node> body;
    Node sourceNode;
}