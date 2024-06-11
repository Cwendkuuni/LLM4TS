package org.apache.commons.cli; // for consistency with the given example

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

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
    public void testFlowThroughNodeWithNoAssignments() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = Node.newIntLiteral(1);
        LiveVariableLattice input = new LiveVariableLattice(0);
        LiveVariableLattice output = analysis.flowThrough(node, input);
        assertEquals(input, output);
    }

    @Test
    public void testFlowThroughAssignmentNode() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = NodeFactory.createAssign(NodeFactory.createVar("x"), NodeFactory.createIntLiteral(1));
        LiveVariableLattice input = new LiveVariableLattice(1);
        LiveVariableLattice output = analysis.flowThrough(node, input);
        assertTrue(output.liveSet.get(0));
    }

    @Test
    public void testFlowThroughAssignmentNodeWithDeadVariable() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = NodeFactory.createAssign(NodeFactory.createVar("dead"), NodeFactory.createIntLiteral(1));
        LiveVariableLattice input = new LiveVariableLattice(1);
        LiveVariableLattice output = analysis.flowThrough(node, input);
        assertFalse(output.liveSet.get(0));
    }

    @Test
    public void testFlowThroughConditionalNode() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node condition = NodeFactory.createConditionalExpression();
        Node trueBranch = NodeFactory.createAssign(NodeFactory.createVar("x"), NodeFactory.createIntLiteral(1));
        Node falseBranch = NodeFactory.createAssign(NodeFactory.createVar("y"), NodeFactory.createIntLiteral(2));
        condition.setFirstChild(trueBranch);
        condition.setLastChild(falseBranch);
        LiveVariableLattice input = new LiveVariableLattice(2);
        LiveVariableLattice output = analysis.flowThrough(condition, input);
        assertTrue(output.liveSet.get(0));
        assertTrue(output.liveSet.get(1));
    }

    @Test
    public void testFlowThroughConditionalNodeWithDeadVariable() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node condition = NodeFactory.createConditionalExpression();
        Node trueBranch = NodeFactory.createAssign(NodeFactory.createVar("dead"), NodeFactory.createIntLiteral(1));
        Node falseBranch = NodeFactory.createAssign(NodeFactory.createVar("y"), NodeFactory.createIntLiteral(2));
        condition.setFirstChild(trueBranch);
        condition.setLastChild(falseBranch);
        LiveVariableLattice input = new LiveVariableLattice(2);
        LiveVariableLattice output = analysis.flowThrough(condition, input);
        assertFalse(output.liveSet.get(0));
        assertTrue(output.liveSet.get(1));
    }

    @Test
    public void testFlowThroughFunctionCallNode() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node functionCall = NodeFactory.createCall("func", NodeFactory.createArrayLiteral());
        LiveVariableLattice input = new LiveVariableLattice(2);
        LiveVariableLattice output = analysis.flowThrough(functionCall, input);
        assertEquals(input, output);
    }

    @Test
    public void testComputeGenKill() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        Node node = NodeFactory.createAssign(NodeFactory.createVar("x"), NodeFactory.createIntLiteral(1));
        BitSet gen = new BitSet(1);
        BitSet kill = new BitSet(1);
        analysis.computeGenKill(node, gen, kill, false);
        assertTrue(gen.get(0));
        assertFalse(kill.get(0));

        Node node2 = NodeFactory.createAssign(NodeFactory.createVar("x"), NodeFactory.createIntLiteral(1));
        Node condition = NodeFactory.createConditionalExpression();
        Node trueBranch = NodeFactory.createAssign(NodeFactory.createVar("x"), NodeFactory.createIntLiteral(2));
        Node falseBranch = NodeFactory.createAssign(NodeFactory.createVar("x"), NodeFactory.createIntLiteral(1));
        condition.setFirstChild(trueBranch);
        condition.setLastChild(falseBranch);
        analysis.computeGenKill(condition, gen, kill, false);
        assertFalse(gen.get(0));
        assertTrue(kill.get(0));
    }

    @Test
    public void testJoinLattices() {
        LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariableLattice lattice1 = new LiveVariableLattice(3);
        LiveVariableLattice lattice2 = new LiveVariableLattice(3);
        LiveVariableLattice result = analysis.join(List.of(lattice1, lattice2));
        assertEquals(lattice1, result);

        lattice1.liveSet.set(1);
        lattice2.liveSet.set(2);
        result = analysis.join(List.of(lattice1, lattice2));
        assertTrue(result.liveSet.get(1));
        assertTrue(result.liveSet.get(2));
    }
}