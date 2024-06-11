import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest extends TestCase {

    private LiveVariablesAnalysis.LiveVariableLattice lattice;

    @Before
    public void setUp() {
        lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
    }

    @Test
    public void testEquals() {
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(lattice);
        assertTrue(lattice.equals(lattice2));
    }

    @Test
    public void testIsLiveWithVar() {
        LiveVariablesAnalysis.Var var = new LiveVariablesAnalysis.Var(0, "variable");
        lattice.liveSet.set(var.index);
        assertTrue(lattice.isLive(var));
    }

    @Test
    public void testIsLiveWithIndex() {
        int index = 0;
        lattice.liveSet.set(index);
        assertTrue(lattice.isLive(index));
    }

    @Test
    public void testToString() {
        lattice.liveSet.set(0);
        lattice.liveSet.set(1);
        assertEquals("{0, 1}", lattice.toString());
    }

    @Test
    public void testHashCode() {
        LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(lattice);
        assertEquals(lattice.hashCode(), lattice2.hashCode());
    }

    @Test
    public void testIsForward() {
        assertFalse(lattice.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");
        LiveVariablesAnalysis.Var var3 = new LiveVariablesAnalysis.Var(2, "variable3");
        lattice.liveSet.set(var1.index);
        lattice.liveSet.set(var2.index);
        lattice.liveSet.set(var3.index);

        LiveVariablesAnalysis.Var[] vars = { var1, var2, var3 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
        assertEquals(lattice, entryLattice);
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");
        LiveVariablesAnalysis.Var var3 = new LiveVariablesAnalysis.Var(2, "variable3");
        lattice.liveSet.set(var1.index);
        lattice.liveSet.set(var2.index);
        lattice.liveSet.set(var3.index);

        LiveVariablesAnalysis.Var[] vars = { var1, var2, var3 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
        assertEquals(lattice, initialEstimateLattice);
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(Token.NAME, "variable1");
        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");
        lattice.liveSet.set(var2.index);

        LiveVariablesAnalysis.Var[] vars = { var1, var2 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        LiveVariablesAnalysis.LiveVariableLattice newLattice = liveVariablesAnalysis.flowThrough(node, lattice);
        assertTrue(newLattice.isLive(var2));
        assertTrue(newLattice.isLive(var1));
    }

    @Test
    public void testComputeGenKillWithVar() {
        Node n = new Node(Token.NAME, "variable1");
        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");

        LiveVariablesAnalysis.Var[] vars = { var1, var2 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        BitSet gen = new BitSet(lattice.liveSet.size());
        BitSet kill = new BitSet(lattice.liveSet.size());
        liveVariablesAnalysis.computeGenKill(n, gen, kill, false);

        assertTrue(gen.get(var1.index));
        assertFalse(gen.get(var2.index));
        assertFalse(kill.get(var1.index));
        assertFalse(kill.get(var2.index));
    }

    @Test
    public void testComputeGenKillWithoutVar() {
        Node n = new Node(Token.ADD, new Node(Token.NUMBER, "1"), new Node(Token.NUMBER, "2"));
        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");

        LiveVariablesAnalysis.Var[] vars = { var1, var2 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        BitSet gen = new BitSet(lattice.liveSet.size());
        BitSet kill = new BitSet(lattice.liveSet.size());
        liveVariablesAnalysis.computeGenKill(n, gen, kill, false);

        assertFalse(gen.get(var1.index));
        assertFalse(gen.get(var2.index));
        assertFalse(kill.get(var1.index));
        assertFalse(kill.get(var2.index));
    }

    @Test
    public void testAddToSetIfLocalWithLocalVar() {
        Node node = new Node(Token.NAME, "variable1");
        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");
        lattice.liveSet.set(var1.index);

        LiveVariablesAnalysis.Var[] vars = { var1, var2 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        BitSet set = new BitSet(lattice.liveSet.size());
        liveVariablesAnalysis.addToSetIfLocal(node, set);

        assertTrue(set.get(var1.index));
        assertFalse(set.get(var2.index));
    }

    @Test
    public void testAddToSetIfLocalWithGlobalVar() {
        Node node = new Node(Token.NAME, "variable3");
        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");
        lattice.liveSet.set(var1.index);

        LiveVariablesAnalysis.Var[] vars = { var1, var2 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        BitSet set = new BitSet(lattice.liveSet.size());
        liveVariablesAnalysis.addToSetIfLocal(node, set);

        assertFalse(set.get(var1.index));
        assertFalse(set.get(var2.index));
    }

    @Test
    public void testMarkAllParametersEscaped() {
        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");
        LiveVariablesAnalysis.Var var3 = new LiveVariablesAnalysis.Var(2, "variable3");

        LiveVariablesAnalysis.Var[] vars = { var1, var2, var3 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        liveVariablesAnalysis.markAllParametersEscaped();

        assertTrue(liveVariablesAnalysis.getEscapedLocals().contains(var1));
        assertTrue(liveVariablesAnalysis.getEscapedLocals().contains(var2));
        assertTrue(liveVariablesAnalysis.getEscapedLocals().contains(var3));
    }

    @Test
    public void testIsArgumentsName() {
        Node node1 = new Node(Token.NAME, "arguments");
        Node node2 = new Node(Token.NAME, "variable");

        LiveVariablesAnalysis.Var var1 = new LiveVariablesAnalysis.Var(0, "variable1");
        LiveVariablesAnalysis.Var var2 = new LiveVariablesAnalysis.Var(1, "variable2");
        LiveVariablesAnalysis.Var[] vars = { var1, var2 };
        Scope jsScope = new Scope(null, vars);
        LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(null, jsScope, null);

        assertTrue(liveVariablesAnalysis.isArgumentsName(node1));
        assertFalse(liveVariablesAnalysis.isArgumentsName(node2));
    }
}