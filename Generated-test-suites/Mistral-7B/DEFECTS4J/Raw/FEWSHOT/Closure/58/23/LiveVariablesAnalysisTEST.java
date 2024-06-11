package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;

import java.util.BitSet;
import java.util.Set;

public class LiveVariablesAnalysisTest {

    @Test
    public void testIsLive() {
        Scope jsScope = new Scope();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, jsScope, null);
        Var v1 = jsScope.declareLocal("v1");
        Var v2 = jsScope.declareLocal("v2");
        Var v3 = jsScope.declareLocal("v3");

        analysis.getEntryLattice().liveSet.set(v1.index);
        analysis.getEntryLattice().liveSet.set(v2.index);
        analysis.getEntryLattice().liveSet.set(v3.index);

        LiveVariablesAnalysis.LiveVariableLattice expectedResult = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
        expectedResult.liveSet.set(v1.index);
        expectedResult.liveSet.set(v2.index);

        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(new Node(), expectedResult);

        assertTrue(analysis.isLive(v1, result));
        assertTrue(analysis.isLive(v2, result));
        assertFalse(analysis.isLive(v3, result));
    }

    @Test
    public void testIsLiveWithEscapedLocals() {
        Scope jsScope = new Scope();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, jsScope, null);
        Var v1 = jsScope.declareLocal("v1");
        Var v2 = jsScope.declareLocal("v2");
        Var v3 = jsScope.declareLocal("v3");
        analysis.escaped.add(v3);

        analysis.getEntryLattice().liveSet.set(v1.index);
        analysis.getEntryLattice().liveSet.set(v2.index);
        analysis.getEntryLattice().liveSet.set(v3.index);

        LiveVariablesAnalysis.LiveVariableLattice expectedResult = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
        expectedResult.liveSet.set(v1.index);
        expectedResult.liveSet.set(v2.index);

        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(new Node(), expectedResult);

        assertTrue(analysis.isLive(v1, result));
        assertTrue(analysis.isLive(v2, result));
        assertTrue(analysis.isLive(v3, result));
    }

    @Test
    public void testIsLiveWithConditionalAssignments() {
        Scope jsScope = new Scope();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, jsScope, null);
        Var v1 = jsScope.declareLocal("v1");
        Var v2 = jsScope.declareLocal("v2");

        analysis.getEntryLattice().liveSet.set(v1.index);
        analysis.getEntryLattice().liveSet.set(v2.index);

        Node assignment1 = new Node();
        assignment1.setType(Token.ASSIGN);
        Node lhs1 = new Node();
        lhs1.setType(Token.NAME);
        lhs1.setString("v1");
        Node rhs1 = new Node();
        rhs1.setType(Token.NAME);
        rhs1.setString("v2");
        analysis.getEntryLattice().liveSet.set(lhs1.index);
        analysis.getEntryLattice().liveSet.set(rhs1.index);
        assignment1.setFirstChild(lhs1);
        assignment1.setLastChild(rhs1);

        Node assignment2 = new Node();
        assignment2.setType(Token.ASSIGN);
        Node lhs2 = new Node();
        lhs2.setType(Token.NAME);
        lhs2.setString("v1");
        Node cond = new Node();
        cond.setType(Token.IF);
        Node condBranchTrue = new Node();
        condBranchTrue.setType(Token.BANG);
        Node condExpression = new Node();
        condExpression.setType(Token.NAME);
        condExpression.setString("condition");
        condBranchTrue.setFirstChild(condExpression);
        Node rhs2 = new Node();
        rhs2.setType(Token.NAME);
        rhs2.setString("v2");
        analysis.getEntryLattice().liveSet.set(lhs2.index);
        analysis.getEntryLattice().liveSet.set(condBranchTrue.index);
        analysis.getEntryLattice().liveSet.set(rhs2.index);
        cond.setFirstChild(condExpression);
        cond.setLastChild(rhs2);
        analysis.getEntryLattice().liveSet.set(cond.index);
        assignment2.setFirstChild(lhs2);
        assignment2.setLastChild(cond);

        LiveVariablesAnalysis.LiveVariableLattice expectedResult = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
        expectedResult.liveSet.set(v1.index);
        expectedResult.liveSet.set(v2.index);

        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(assignment1, expectedResult);

        assertTrue(analysis.isLive(v1, result));
        assertTrue(analysis.isLive(v2, result));

        result = analysis.flowThrough(assignment2, result);

        assertTrue(analysis.isLive(v1, result));
        assertTrue(analysis.isLive(v2, result));
    }

    @Test
    public void testIsLiveWithLoops() {
        Scope jsScope = new Scope();
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, jsScope, null);
        Var v1 = jsScope.declareLocal("v1");
        Var v2 = jsScope.declareLocal("v2");

        analysis.getEntryLattice().liveSet.set(v1.index);
        analysis.getEntryLattice().liveSet.set(v2.index);

        Node loop = new Node();
        loop.setType(Token.WHILE);
        Node condition = new Node();
        condition.setType(Token.NAME);
        condition.setString("condition");
        loop.setFirstChild(condition);

        Node loopBody = new Node();
        loopBody.setType(Token.BLOCK);

        Node assignment1 = new Node();
        assignment1.setType(Token.ASSIGN);
        Node lhs1 = new Node();
        lhs1.setType(Token.NAME);
        lhs1.setString("v1");
        Node rhs1 = new Node();
        rhs1.setType(Token.NAME);
        rhs1.setString("v2");
        analysis.getEntryLattice().liveSet.set(lhs1.index);
        analysis.getEntryLattice().liveSet.set(rhs1.index);
        assignment1.setFirstChild(lhs1);
        assignment1.setLastChild(rhs1);

        Node assignment2 = new Node();
        assignment2.setType(Token.ASSIGN);
        Node lhs2 = new Node();
        lhs2.setType(Token.NAME);
        lhs2.setString("v2");
        Node rhs2 = new Node();
        rhs2.setType(Token.NAME);
        rhs2.setString("v1");
        analysis.getEntryLattice().liveSet.set(lhs2.index);
        analysis.getEntryLattice().liveSet.set(rhs2.index);
        assignment2.setFirstChild(lhs2);
        assignment2.setLastChild(rhs2);

        loopBody.setFirstChild(assignment1);
        loopBody.setLastChild(assignment2);

        loop.setFirstChild(condition);
        loop.setLastChild(loopBody);

        analysis.getEntryLattice().liveSet.set(condition.index);
        analysis.getEntryLattice().liveSet.set(loopBody.index);

        LiveVariablesAnalysis.LiveVariableLattice expectedResult = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
        expectedResult.liveSet.set(v1.index);
        expectedResult.liveSet.set(v2.index);

        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(new Node(), expectedResult);

        assertTrue(analysis.isLive(v1, result));
        assertTrue(analysis.isLive(v2, result));

        result = analysis.flowThrough(loop, result);

        assertTrue(analysis.isLive(v1, result));
        assertTrue(analysis.isLive(v2, result));
    }
}