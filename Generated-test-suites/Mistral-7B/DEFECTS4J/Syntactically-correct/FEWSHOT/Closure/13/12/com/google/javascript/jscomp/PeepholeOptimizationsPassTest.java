package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PeepholeOptimizationsPassTest {
    private PeepholeOptimizationsPass pass;

    @Before
    public void setUp() {
        AbstractCompiler compiler = new Compiler();
        AbstractPeepholeOptimization constantFolding = new ConstantFolding();
        AbstractPeepholeOptimization uselessCodeRemoval = new UselessCodeRemoval();

        pass = new PeepholeOptimizationsPass(compiler, constantFolding, uselessCodeRemoval);
    }

    @Test
    public void testProcess() {
        Node root = new Node_Object();
        pass.process(null, root);
    }

    @Test
    public void testTraverse() {
        Node root = new Node_Object();
        Node node = new Node_Number(1);
        node.setParent(root);

        pass.traverse(root);
    }

    @Test
    public void testShouldVisitFunction() {
        Node root = new Node_Object();
        Node functionNode = new Node_Function("foo", Lists.newArrayList(), null);
        Node childNode = new Node_Number(1);
        functionNode.setParent(root);
        childNode.setParent(functionNode);

        assertTrue(pass.shouldVisit(functionNode));
        assertFalse(pass.shouldVisit(childNode));
    }

    @Test
    public void testShouldRetraverseFunctionWithChangedChild() {
        Node root = new Node_Object();
        Node functionNode = new Node_Function("foo", Lists.newArrayList(), null);
        Node childNode1 = new Node_Number(1);
        Node childNode2 = new Node_Number(2);
        functionNode.setParent(root);
        childNode1.setParent(functionNode);
        childNode2.setParent(functionNode);

        pass.traverse(functionNode);
        pass.traverse(childNode1);

        assertTrue(pass.shouldRetraverse(functionNode));
        assertFalse(pass.shouldRetraverse(childNode2));
    }

    @Test
    public void testVisit() {
        Node root = new Node_Object();
        Node constantNode1 = new Node_Number(1);
        Node constantNode2 = new Node_Number(2);
        Node nonConstantNode = new Node_Name("foo");
        Node assignmentNode = new Node_SetProperty(nonConstantNode, null, constantNode1);
        Node functionNode = new Node_Function("foo", Lists.newArrayList(), null);
        Node functionReturnNode = new Node_Call(functionNode, Lists.newArrayList(), null);
        Node concatenationNode = new Node_BinaryOp(Node.OP_PLUS, constantNode1, constantNode2);
        Node expressionNode = new Node_BinaryOp(Node.OP_MINUS, constantNode1, constantNode2);

        Node visitedNode;

        // Test constant folding
        visitedNode = pass.visit(constantNode1);
        assertSame(constantNode1, visitedNode);

        visitedNode = pass.visit(concatenationNode);
        assertSame(new Node_Number(3), visitedNode);

        visitedNode = pass.visit(expressionNode);
        assertSame(new Node_Number(-1), visitedNode);

        // Test useless code removal
        visitedNode = pass.visit(nonConstantNode);
        assertNull(visitedNode);

        visitedNode = pass.visit(functionNode);
        assertNull(visitedNode);

        visitedNode = pass.visit(functionReturnNode);
        assertNull(visitedNode);

        visitedNode = pass.visit(assignmentNode);
        assertNull(visitedNode);
    }

    @Test
    public void testBeginAndEndTraversal() {
        for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
            optimization.beginTraversal(pass.compiler);
            optimization.endTraversal(pass.compiler);
        }
    }
}