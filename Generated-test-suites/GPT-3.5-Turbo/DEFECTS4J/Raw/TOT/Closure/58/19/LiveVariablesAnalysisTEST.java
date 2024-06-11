import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

    private LiveVariablesAnalysis.LiveVariableLattice lattice;
    private LiveVariablesAnalysis.Analysis analysis;

    @Before
    public void setup() {
        lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
        analysis = new LiveVariablesAnalysis.Analysis();
    }

    @Test
    public void testLiveVariableLatticeIsLive_PositiveCase() {
        int index = 3;
        lattice.liveSet.set(index);

        Assert.assertTrue(lattice.isLive(index));
    }

    @Test
    public void testLiveVariableLatticeIsLive_NegativeCase() {
        int index = 3;
        lattice.liveSet.clear(index);

        Assert.assertFalse(lattice.isLive(index));
    }

    @Test
    public void testLiveVariableLatticeEquals_EqualCase() {
        LiveVariablesAnalysis.LiveVariableLattice other = new LiveVariablesAnalysis.LiveVariableLattice(lattice);

        Assert.assertEquals(lattice, other);
    }

    @Test
    public void testLiveVariableLatticeEquals_NotEqualCase() {
        LiveVariablesAnalysis.LiveVariableLattice other = new LiveVariablesAnalysis.LiveVariableLattice(5);
        other.liveSet.set(1);

        Assert.assertNotEquals(lattice, other);
    }

    @Test
    public void testLiveVariableLatticeToString() {
        lattice.liveSet.set(1);
        lattice.liveSet.set(4);

        String expected = "{1, 4}";
        String result = lattice.toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testLiveVariableLatticeHashCode() {
        LiveVariablesAnalysis.LiveVariableLattice other = new LiveVariablesAnalysis.LiveVariableLattice(lattice);

        Assert.assertEquals(lattice.hashCode(), other.hashCode());
    }

    @Test
    public void testIsForward() {
        Assert.assertFalse(analysis.isForward());
    }

    @Test
    public void testCreateEntryLattice() {
        LiveVariablesAnalysis.LiveVariableLattice entryLattice = analysis.createEntryLattice();

        Assert.assertNotNull(entryLattice);
        Assert.assertEquals(5, entryLattice.liveSet.size());
    }

    @Test
    public void testCreateInitialEstimateLattice() {
        LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

        Assert.assertNotNull(initialEstimateLattice);
        Assert.assertEquals(5, initialEstimateLattice.liveSet.size());
    }

    @Test
    public void testFlowThrough() {
        Node node = new Node(Token.SCRIPT);
        LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);

        LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);

        Assert.assertNotNull(result);
        Assert.assertEquals(5, result.liveSet.size());
    }

    @Test
    public void testComputeGenKill() {
        Node node = new Node(Token.BLOCK);
        BitSet gen = new BitSet(5);
        BitSet kill = new BitSet(5);
        boolean conditional = true;

        analysis.computeGenKill(node, gen, kill, conditional);

        Assert.assertNotNull(gen);
        Assert.assertNotNull(kill);
    }

    @Test
    public void testAddToSetIfLocal_ExistsInScope() {
        Node node = new Node(Token.NAME, "x");
        BitSet set = new BitSet(5);
        analysis.addedVariables.add("x", null);

        analysis.addToSetIfLocal(node, set);

        Assert.assertTrue(set.get(0));
    }

    @Test
    public void testAddToSetIfLocal_DoesNotExistInScope() {
        Node node = new Node(Token.NAME, "y");
        BitSet set = new BitSet(5);
        analysis.addedVariables.add("x", null);

        analysis.addToSetIfLocal(node, set);

        Assert.assertFalse(set.get(0));
    }

    @Test
    public void testMarkAllParametersEscaped() {
        analysis.markAllParametersEscaped();

        Assert.assertEquals(2, analysis.escaped.size());
    }

    @Test
    public void testIsArgumentsName_IsArgumentsName() {
        Node node = new Node(Token.NAME, "arguments");
        node.putProp(Node.ORIGINALNAME_PROP, "arguments");

        Assert.assertTrue(analysis.isArgumentsName(node));
    }

    @Test
    public void testIsArgumentsName_NotArgumentsName() {
        Node node = new Node(Token.NAME, "args");

        Assert.assertFalse(analysis.isArgumentsName(node));
    }
}