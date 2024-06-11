import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis.LiveVariableJoinOp joinOp;
    private LiveVariablesAnalysis.LiveVariableLattice lattice;
    private LiveVariablesAnalysis analysis;

    @Before
    public void setUp() {
        joinOp = new LiveVariablesAnalysis.LiveVariableJoinOp();
        lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        analysis = new LiveVariablesAnalysis(null, null, null);
    }

    @Test
    public void testLiveVariableJoinOp() {
        LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);

        lattice1.liveSet.set(0);
        lattice1.liveSet.set(1);
        lattice2.liveSet.set(2);
        lattice2.liveSet.set(3);

        assertEquals(lattice1.liveSet, joinOp.apply(Sets.newArrayList(lattice1, lattice1)).liveSet);

        lattice = joinOp.apply(Sets.newArrayList(lattice1, lattice2));
        assertTrue(lattice.liveSet.get(0));
        assertTrue(lattice.liveSet.get(1));
        assertTrue(lattice.liveSet.get(2));
        assertTrue(lattice.liveSet.get(3));
        assertFalse(lattice.liveSet.get(4));
    }

    @Test
    public void testLiveVariableLattice() {
        assertEquals("", lattice.toString());

        lattice.liveSet.set(0);
        lattice.liveSet.set(1);
        lattice.liveSet.set(2);

        assertTrue(lattice.isLive(0));
        assertTrue(lattice.isLive(1));
        assertTrue(lattice.isLive(2));
        assertFalse(lattice.isLive(3));
        assertFalse(lattice.isLive(4));

        Node node = new Node(1);
        assertTrue(lattice.equals(node));

        assertFalse(lattice.equals(null));
        assertFalse(lattice.equals(new LiveVariablesAnalysis.LiveVariableLattice(5)));

        assertEquals(lattice.hashCode(), node.hashCode());
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(1);
        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, lattice);

        assertEquals(lattice.liveSet, result.liveSet);
        assertFalse(result.isLive(0));
        assertFalse(result.isLive(1));
        assertFalse(result.isLive(2));
        assertFalse(result.isLive(3));
        assertFalse(result.isLive(4));
    }

    @Test
    public void testMarkAllParametersEscaped() {
        analysis.markAllParametersEscaped();
        assertEquals(0, analysis.getEscapedLocals().size());

        analysis.getJsScope().declare("x");
        analysis.getJsScope().declare("y");

        analysis.markAllParametersEscaped();
        assertEquals(2, analysis.getEscapedLocals().size());
    }

    @Test
    public void testAddToSetIfLocal() {
        Node node = new Node(1);
        analysis.addToSetIfLocal(node, lattice.liveSet);

        assertFalse(lattice.isLive(0));

        analysis.getEscaped().add(new Var());
        analysis.addToSetIfLocal(node, lattice.liveSet);

        assertTrue(lattice.isLive(0));
    }

    @Test
    public void testIsArgumentsName() {
        Node node = new Node(1);
        assertFalse(analysis.isArgumentsName(node));

        node.setString("arguments");
        assertFalse(analysis.isArgumentsName(node));

        analysis.getJsScope().declare("arguments");
        assertTrue(analysis.isArgumentsName(node));
    }
}