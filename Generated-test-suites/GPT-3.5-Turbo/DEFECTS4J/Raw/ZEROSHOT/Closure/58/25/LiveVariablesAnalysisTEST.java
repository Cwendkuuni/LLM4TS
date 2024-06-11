import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis.LiveVariableLattice lattice;
    private LiveVariablesAnalysis.LiveVariableJoinOp joinOp;

    @Before
    public void setUp() {
        lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
        joinOp = new LiveVariablesAnalysis.LiveVariableJoinOp();
    }

    @Test
    public void testEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(lattice);
        assertTrue(lattice.equals(lattice2));
    }

    @Test
    public void testNotEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        assertFalse(lattice.equals(lattice2));
    }

    @Test
    public void testToString() {
        assertEquals("{}", lattice.toString());
    }

    @Test
    public void testHashCode() {
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(lattice);
        assertEquals(lattice.hashCode(), lattice2.hashCode());
    }

    @Test
    public void testIsLiveVar() {
        Var var = new Var("x", 0);
        lattice.liveSet.set(var.index);
        assertTrue(lattice.isLive(var));
    }

    @Test
    public void testIsLiveIndex() {
        Var var = new Var("x", 0);
        lattice.liveSet.set(var.index);
        assertTrue(lattice.isLive(var.index));
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createEntryLattice();
        for (int i = 0; i < liveVariablesAnalysis.jsScope.getVarCount(); i++) {
            assertFalse(lattice.isLive(i));
        }
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createInitialEstimateLattice();
        for (int i = 0; i < liveVariablesAnalysis.jsScope.getVarCount(); i++) {
            assertFalse(lattice.isLive(i));
        }
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(Token.NAME, new Node(Token.EMPTY));
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
        LiveVariablesAnalysis.LiveVariableLattice result = lattice.flowThrough(node, input);
        for (int i = 0; i < lattice.liveSet.size(); i++) {
            assertEquals(lattice.liveSet.get(i), result.liveSet.get(i));
        }
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        liveVariablesAnalysis.markAllParametersEscaped();
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        for(Node arg = liveVariablesAnalysis.jsScope.getRootNode().getFirstChild().getNext().getFirstChild(); arg != null; arg = arg.getNext()) {
            assertTrue(escapedLocals.contains(liveVariablesAnalysis.jsScope.getVar(arg.getString())));
        }
    }

    @Test
    public void testGetVarIndex() {
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, null, null);
        int varIndex = liveVariablesAnalysis.getVarIndex("x");
        assertEquals(liveVariablesAnalysis.jsScope.getVar("x").index, varIndex);
    }
}