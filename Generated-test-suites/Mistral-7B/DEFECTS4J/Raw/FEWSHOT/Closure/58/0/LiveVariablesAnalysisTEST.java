package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

    @Test
    public void testLiveVariablesAnalysis() {
        // Test the LiveVariablesAnalysis class with a simple Node
        Scope jsScope = new Scope();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

        Node nodeA = Node.newLiteral(Token.NAME, "a");
        Node nodeB = Node.newLiteral(Token.NAME, "b");
        Node nodeC = Node.newLiteral(Token.NAME, "c");
        Node nodeAssign = Node.newBinaryOp("=", Node.newName("a"), Node.newName("b"));
        Node nodeIf = Node.newBinaryOp("?", Node.newName("x"), Node.newLiteral(Token.INT_LITERAL, 0));
        Node nodeElse = Node.newKeyword("else");
        Node nodeReturn = Node.newKeyword("return");

        cfg.addNode(nodeA);
        cfg.addNode(nodeB);
        cfg.addNode(nodeC);
        cfg.addNode(nodeAssign);
        cfg.addNode(nodeIf);
        cfg.addNode(nodeElse);
        cfg.addNode(nodeReturn);

        cfg.addEdge(nodeA, nodeAssign);
        cfg.addEdge(nodeB, nodeAssign);
        cfg.addEdge(nodeAssign, nodeIf);
        cfg.addEdge(NodeUtil.getElseBranch(nodeIf), nodeElse);
        cfg.addEdge(nodeElse, nodeReturn);

        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        LiveVariablesAnalysis.LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();

        LiveVariablesAnalysis.LiveVariableLattice latticeA = liveVariablesAnalysis.flowThrough(nodeA, initialLattice);
        LiveVariablesAnalysis.LiveVariableLattice latticeB = liveVariablesAnalysis.flowThrough(nodeB, latticeA);
        LiveVariablesAnalysis.LiveVariableLattice latticeAssign = liveVariablesAnalysis.flowThrough(nodeAssign, latticeB);
        LiveVariablesAnalysis.LiveVariableLattice latticeIf = liveVariablesAnalysis.flowThrough(nodeIf, latticeAssign);
        LiveVariablesAnalysis.LiveVariableLattice latticeElse = liveVariablesAnalysis.flowThrough(nodeElse, latticeIf);
        LiveVariablesAnalysis.LiveVariableLattice latticeReturn = liveVariablesAnalysis.flowThrough(nodeReturn, latticeElse);

        Set<Integer> expectedLiveVars = Sets.newHashSet(0, 1); // a and b

        for (int i = 0; i < jsScope.getVarCount(); i++) {
            Var var = jsScope.getVar(i);
            int index = var.index;

            if (expectedLiveVars.contains(index)) {
                assertTrue(latticeReturn.isLive(index));
            } else {
                assertFalse(latticeReturn.isLive(index));
            }
        }
    }

    @Test
    public void testDeadVariables() {
        // Test the LiveVariablesAnalysis class with a Node that has dead variables
        Scope jsScope = new Scope();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

        Node nodeA = Node.newLiteral(Token.NAME, "a");
        Node nodeB = Node.newLiteral(Token.NAME, "b");
        Node nodeC = Node.newLiteral(Token.NAME, "c");
        Node nodeAssign = Node.newBinaryOp("=", Node.newName("a"), Node.newName("b"));
        Node nodeIf = Node.newBinaryOp("?", Node.newName("x"), Node.newLiteral(Token.INT_LITERAL, 0));
        Node nodeElse = Node.newKeyword("else");
        Node nodeReturn = Node.newKeyword("return");

        cfg.addNode(nodeA);
        cfg.addNode(nodeB);
        cfg.addNode(nodeC);
        cfg.addNode(nodeAssign);
        cfg.addNode(nodeIf);
        cfg.addNode(nodeElse);
        cfg.addNode(nodeReturn);

        cfg.addEdge(nodeA, nodeAssign);
        cfg.addEdge(nodeB, nodeAssign);
        cfg.addEdge(nodeAssign, nodeIf);
        cfg.addEdge(NodeUtil.getElseBranch(nodeIf), nodeElse);
        cfg.addEdge(nodeElse, nodeReturn);

        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        LiveVariablesAnalysis.LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();

        LiveVariablesAnalysis.LiveVariableLattice latticeA = liveVariablesAnalysis.flowThrough(nodeA, initialLattice);
        LiveVariablesAnalysis.LiveVariableLattice latticeB = liveVariablesAnalysis.flowThrough(nodeB, latticeA);
        LiveVariablesAnalysis.LiveVariableLattice latticeAssign = liveVariablesAnalysis.flowThrough(nodeAssign, latticeB);
        LiveVariablesAnalysis.LiveVariableLattice latticeIf = liveVariablesAnalysis.flowThrough(nodeIf, latticeAssign);
        LiveVariablesAnalysis.LiveVariableLattice latticeElse = liveVariablesAnalysis.flowThrough(nodeElse, latticeIf);
        LiveVariablesAnalysis.LiveVariableLattice latticeReturn = liveVariablesAnalysis.flowThrough(nodeReturn, latticeElse);

        Set<Integer> expectedLiveVars = Sets.newHashSet(0, 1); // a and b

        for (int i = 0; i < jsScope.getVarCount(); i++) {
            Var var = jsScope.getVar(i);
            int index = var.index;

            if (expectedLiveVars.contains(index)) {
                assertTrue(latticeReturn.isLive(index));
            } else {
                assertFalse(latticeReturn.isLive(index));
            }
        }

        // Test a dead variable
        Node nodeD = Node.newLiteral(Token.NAME, "d");
        Node nodeDeadAssign = Node.newBinaryOp("=", Node.newName("d"), Node.newName("a"));

        cfg.addNode(nodeD);
        cfg.addEdge(nodeAssign, nodeDeadAssign);

        LiveVariablesAnalysis.LiveVariableLattice latticeDeadAssign = liveVariablesAnalysis.flowThrough(nodeDeadAssign, latticeAssign);

        assertFalse(latticeDeadAssign.isLive(2)); // d should not be live
    }

    @Test
    public void testEscapedVariables() {
        // Test the LiveVariablesAnalysis class with escaped variables
        Scope jsScope = new Scope();
        ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);

        Node nodeFunctionDeclaration = Node.newKeyword("function");
        Node nodeFunctionName = Node.newName("myFunction");
        Node nodeArgumentsNode = Node.newKeyword("arguments");
        Node nodeBody = Node.newBlock();
        Node nodeReturn = Node.newKeyword("return");

        cfg.addNode(nodeFunctionDeclaration);
        cfg.addNode(nodeFunctionName);
        cfg.addNode(nodeArgumentsNode);
        cfg.addNode(nodeBody);
        cfg.addNode(nodeReturn);

        cfg.addEdge(nodeFunctionDeclaration, nodeFunctionName);
        cfg.addEdge(nodeFunctionName, nodeArgumentsNode);
        cfg.addEdge(nodeArgumentsNode, nodeBody);
        cfg.addEdge(nodeBody, nodeReturn);

        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        LiveVariablesAnalysis.LiveVariableLattice initialLattice = liveVariablesAnalysis.createInitialEstimateLattice();

        LiveVariablesAnalysis.LiveVariableLattice latticeFunctionDeclaration = liveVariablesAnalysis.flowThrough(nodeFunctionDeclaration, initialLattice);
        LiveVariablesAnalysis.LiveVariableLattice latticeFunctionName = liveVariablesAnalysis.flowThrough(nodeFunctionName, latticeFunctionDeclaration);
        LiveVariablesAnalysis.LiveVariableLattice latticeArgumentsNode = liveVariablesAnalysis.flowThrough(nodeArgumentsNode, latticeFunctionName);
        LiveVariablesAnalysis.LiveVariableLattice latticeBody = liveVariablesAnalysis.flowThrough(nodeBody, latticeArgumentsNode);
        LiveVariablesAnalysis.LiveVariableLattice latticeReturn = liveVariablesAnalysis.flowThrough(nodeReturn, latticeBody);

        Set<Var> expectedEscapedVars = Sets.newHashSet(jsScope.getVar(jsScope.getVarCount() - 1)); // arguments

        Set<Var> escapedLocalVars = liveVariablesAnalysis.getEscapedLocals();

        for (Var var : jsScope.getVars()) {
            if (expectedEscapedVars.contains(var)) {
                assertTrue(escapedLocalVars.contains(var));
            } else {
                assertFalse(escapedLocalVars.contains(var));
            }
        }
    }
}