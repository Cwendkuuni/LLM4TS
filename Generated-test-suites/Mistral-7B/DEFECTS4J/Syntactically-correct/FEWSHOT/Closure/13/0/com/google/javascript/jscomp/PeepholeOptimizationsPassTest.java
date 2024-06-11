package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest {
    private Node rootNode;
    private AbstractCompiler compilerMock;
    private PeepholeOptimizationsPass optimizationsPass;

    @Before
    public void setUp() {
        compilerMock = mock(AbstractCompiler.class);
        optimizationsPass = new PeepholeOptimizationsPass(compilerMock, new AbstractPeepholeOptimization[] {});
        rootNode = Node.newObject();
    }

    @Test
    public void testProcess_NoOptimizations_DoesNotModifyTree() {
        optimizationsPass.process(Node.newObject(), rootNode);
        assertFalse(rootNodeHasChanged(rootNode));
    }

    @Test
    public void testProcess_OneOptimization_OptimizesSubtree() {
        AbstractPeepholeOptimization optimizationMock = mock(AbstractPeepholeOptimization.class);
        when(optimizationMock.optimizeSubtree(Node.newObject())).thenReturn(Node.newObject());

        optimizationsPass = new PeepholeOptimizationsPass(compilerMock, optimizationMock);
        Node inputNode = Node.newObject();
        Node expectedOutputNode = Node.newObject();

        optimizationsPass.process(Node.newObject(), inputNode);

        Node actualOutputNode = rootNode;
        while (actualOutputNode != null) {
            if (actualOutputNode.equals(inputNode)) {
                actualOutputNode = optimizationsPass.compiler.getCurrentNode();
            }
            actualOutputNode = actualOutputNode.getParent();
        }

        assertTrue(actualOutputNode.equals(expectedOutputNode));
        assertFalse(rootNodeHasChanged(rootNode));
    }

    @Test
    public void testProcess_MultipleOptimizations_AppliesEachInOrder() {
        AbstractPeepholeOptimization optimization1Mock = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization optimization2Mock = mock(AbstractPeepholeOptimization.class);

        when(optimization1Mock.optimizeSubtree(Node.newObject())).thenReturn(Node.newObject());
        when(optimization2Mock.optimizeSubtree(Node.newObject())).thenReturn(Node.newObject());

        optimizationsPass = new PeepholeOptimizationsPass(compilerMock, optimization1Mock, optimization2Mock);
        Node inputNode = Node.newObject();
        Node expectedOutputNode = Node.newObject();

        optimizationsPass.process(Node.newObject(), inputNode);

        Node actualOutputNode = rootNode;
        while (actualOutputNode != null) {
            if (actualOutputNode.equals(inputNode)) {
                actualOutputNode = optimizationsPass.compiler.getCurrentNode();
            }
            actualOutputNode = actualOutputNode.getParent();
        }

        assertTrue(actualOutputNode.equals(expectedOutputNode));
        assertFalse(rootNodeHasChanged(rootNode));
    }

    @Test
    public void testTraverse_DoesNotRetraverseIfShouldNot() {
        Node nodeToSkip = Node.newObject();
        Node parentNode = Node.newObject();
        parentNode.setFirstChild(nodeToSkip);

        optimizationsPass.traverse(parentNode);
        assertTrue(nodeToSkip.getParent() == parentNode);
    }

    @Test
    public void testTraverse_RetraversesIfShould() {
        Node nodeToRetraverse = Node.newObject();
        Node parentNode = Node.newObject();
        parentNode.setFirstChild(nodeToRetraverse);

        optimizationsPass.traverse(parentNode);
        optimizationsPass.traverse(parentNode);

        assertTrue(nodeToRetraverse.getParent() != parentNode);
    }

    @Test
    public void testShouldVisit_VisitsFunctionOrScriptNodes() {
        Node functionNode = Node.newFunction("test", null);
        assertTrue(optimizationsPass.shouldVisit(functionNode));
    }

    @Test
    public void testShouldVisit_DoesNotVisitOtherNodes() {
        Node nonFunctionNode = Node.newIdentifier("test");
        assertFalse(optimizationsPass.shouldVisit(nonFunctionNode));
    }

    private boolean rootNodeHasChanged(Node rootNode) {
        Node originalRootNode = rootNode.cloneTree();
        optimizationsPass.process(Node.newObject(), rootNode);
        return !rootNode.equals(originalRootNode);
    }
}