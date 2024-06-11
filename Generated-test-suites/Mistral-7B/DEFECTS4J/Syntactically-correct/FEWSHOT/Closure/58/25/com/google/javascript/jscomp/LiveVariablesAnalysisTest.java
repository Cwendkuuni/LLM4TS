package org.apache.commons.cli; // for testing package imports
import static org.junit.Assert.*;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;

import java.util.BitSet;
import java.util.Set;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariablesAnalysis() {
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new NoOpCompiler(); // A dummy compiler for testing
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

        // Add some sample code for testing
        Node rootNode = NodeUtil.createRootNode();
        rootNode.setSourcePositionForDebug(0, 0);
        Node functionNode = NodeUtil.createFunctionNode("testFunction", rootNode);
        functionNode.setSourcePositionForDebug(1, 0);
        Node argNode = NodeUtil.createNameNode("arg1", Token.NAME, functionNode);
        argNode.setSourcePositionForDebug(2, 0);
        Node assignNode1 = NodeUtil.createAssignNode(Token.ADD, argNode,
                NodeUtil.createNumberNode(1, argNode), functionNode);
        argNode.setSourcePositionForDebug(3, 0);
        assignNode1.setSourcePositionForDebug(4, 0);
        Node ifNode = NodeUtil.createIfNode(Token.IF, functionNode, argNode,
                NodeUtil.createBooleanLiteralNode(true, functionNode));
        Node assignNode2 = NodeUtil.createAssignNode(Token.ASSIGN, argNode,
                NodeUtil.createNumberNode(2, argNode), ifNode);
        argNode.setSourcePositionForDebug(5, 0);
        assignNode2.setSourcePositionForDebug(6, 0);
        Node returnNode = NodeUtil.createReturnNode(functionNode);
        returnNode.setSourcePositionForDebug(7, 0);

        cfg.addNode(rootNode);
        cfg.addNode(functionNode);
        cfg.addEdge(rootNode, functionNode, null);
        cfg.addEdge(functionNode, ifNode, null);
        cfg.addEdge(functionNode, assignNode1, null);
        cfg.addEdge(ifNode, assignNode2, null);
        cfg.addEdge(assignNode1, ifNode, Branch.ON_FALSE);
        cfg.addEdge(assignNode2, returnNode, null);

        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

        LiveVariableLattice entryLattice = analysis.createEntryLattice();
        assertFalse(entryLattice.isLive(argNode));

        LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
        assertFalse(initialEstimateLattice.isLive(argNode));

        LiveVariableLattice resultLattice = analysis.flowThrough(rootNode, initialEstimateLattice);
        assertTrue(resultLattice.isLive(argNode));

        resultLattice = analysis.flowThrough(functionNode, resultLattice);
        assertTrue(resultLattice.isLive(argNode));

        resultLattice = analysis.flowThrough(assignNode1, resultLattice);
        assertTrue(resultLattice.isLive(argNode));

        resultLattice = analysis.flowThrough(ifNode, resultLattice);
        assertTrue(resultLattice.isLive(argNode));

        resultLattice = analysis.flowThrough(assignNode2, resultLattice);
        assertTrue(resultLattice.isLive(argNode));

        resultLattice = analysis.flowThrough(returnNode, resultLattice);
        assertTrue(resultLattice.isLive(argNode));

        analysis.markAllParametersEscaped();
        Set<Var> escapedLocals = analysis.getEscapedLocals();
        assertTrue(escapedLocals.contains(jsScope.getVar("arg1")));
    }
}