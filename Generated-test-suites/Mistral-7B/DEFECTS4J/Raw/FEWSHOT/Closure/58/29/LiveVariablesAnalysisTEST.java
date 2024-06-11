package org.apache.commons.cli; // Change the package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariableLattice entryLattice = analysis.createEntryLattice();
        assertEquals(0, entryLattice.liveSet.cardinality());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
        assertEquals(0, initialEstimateLattice.liveSet.cardinality());
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        int varIndex = analysis.getVarIndex("x");
        assertEquals(-1, varIndex);
    }

    @Test
    public void testIsForward() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        assertFalse(analysis.isForward());
    }

    @Test
    public void testFlowThroughSimpleAssignment() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = Node.newIntLiteral(1);
        LiveVariableLattice input = new LiveVariableLattice(1);
        input.liveSet.set(0);
        LiveVariableLattice output = analysis.flowThrough(node, input);
        assertTrue(output.liveSet.get(0));
    }

    @Test
    public void testFlowThroughSimpleAssignmentWithDeadVariable() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = Node.newIntLiteral(1);
        LiveVariableLattice input = new LiveVariableLattice(1);
        input.liveSet.set(1);
        LiveVariableLattice output = analysis.flowThrough(node, input);
        assertFalse(output.liveSet.get(1));
    }

    @Test
    public void testFlowThroughConditionalAssignment() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node condition = Node.newBoolLiteral(true);
        Node assignment1 = Node.newName("x");
        Node assignment2 = Node.newName("y");
        Node conditionalAssignment = Node.newBinaryOp("?:", assignment1, Node.newAssign(assignment2, Node.newIntLiteral(1)), condition);
        LiveVariableLattice input = new LiveVariableLattice(2);
        input.liveSet.set(0);
        LiveVariableLattice output = analysis.flowThrough(conditionalAssignment, input);
        assertTrue(output.liveSet.get(0));
        assertTrue(output.liveSet.get(1));
    }

    @Test
    public void testFlowThroughConditionalAssignmentWithDeadVariable() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node condition = Node.newBoolLiteral(true);
        Node assignment1 = Node.newName("x");
        Node assignment2 = Node.newName("y");
        Node deadVariable = Node.newName("z");
        Node conditionalAssignment = Node.newBinaryOp("?:", assignment1, Node.newAssign(assignment2, Node.newIntLiteral(1)), condition);
        LiveVariableLattice input = new LiveVariableLattice(3);
        input.liveSet.set(0);
        input.liveSet.set(1);
        input.liveSet.set(2);
        LiveVariableLattice output = analysis.flowThrough(conditionalAssignment, input);
        assertTrue(output.liveSet.get(0));
        assertFalse(output.liveSet.get(2));
    }

    @Test
    public void testGetEscapedLocals() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Scope jsScope = new Scope();
        Var variableX = jsScope.addVar("x");
        Var variableY = jsScope.addVar("y");
        analysis.escaped.add(variableX);
        analysis.escaped.add(variableY);
        Set<Var> escapedLocals = analysis.getEscapedLocals();
        assertEquals(2, escapedLocals.size());
        assertTrue(escapedLocals.contains(variableX));
        assertTrue(escapedLocals.contains(variableY));
    }
}