package weka.classifiers.bayes.net.search.ci;
import org.junit.Test;
import static org.junit.Assert.*;

public class ICSSearchAlgorithmTest {

    @Test
    public void testSetName() {
        ICSSearchAlgorithm icsSearchAlgorithm = new ICSSearchAlgorithm();
        String name = icsSearchAlgorithm.name(0);
        assertEquals("Test Name", name);
    }

    @Test
    public void testMaxn() {
        ICSSearchAlgorithm icsSearchAlgorithm = new ICSSearchAlgorithm();
        int maxn = icsSearchAlgorithm.maxn();
        assertEquals(10, maxn);
    }

    @Test
    public void testSetMaxCardinality() {
        ICSSearchAlgorithm icsSearchAlgorithm = new ICSSearchAlgorithm();
        icsSearchAlgorithm.setMaxCardinality(3);
        int maxCardinality = icsSearchAlgorithm.getMaxCardinality();
        assertEquals(3, maxCardinality);
    }

    @Test
    public void testSearch() {
        // Test for search method
    }

    @Test
    public void testCalcDependencyGraph() {
        // Test for calcDependencyGraph method
    }

    @Test
    public void testExistsSepSet() {
        // Test for existsSepSet method
    }

    @Test
    public void testNext() {
        // Test for next method
    }

    @Test
    public void testCalcVeeNodes() {
        // Test for calcVeeNodes method
    }

    @Test
    public void testCalcArcDirections() {
        // Test for calcArcDirections method
    }

    @Test
    public void testListOptions() {
        // Test for listOptions method
    }

    @Test
    public void testSetOptions() {
        // Test for setOptions method
    }

    @Test
    public void testGetOptions() {
        // Test for getOptions method
    }

    @Test
    public void testMaxCardinalityTipText() {
        // Test for maxCardinalityTipText method
    }

    @Test
    public void testGlobalInfo() {
        // Test for globalInfo method
    }

    @Test
    public void testGetRevision() {
        // Test for getRevision method
    }

    @Test
    public void testMain() {
        // Test for main method
    }

    @Test
    public void testContains() {
        ICSSearchAlgorithm.SeparationSet separationSet = new ICSSearchAlgorithm().new SeparationSet();
        boolean contains = separationSet.contains(3);
        assertFalse(contains);
    }
}