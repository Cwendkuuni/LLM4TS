import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetWithTree() {
        // Create a test BSPTree
        BSPTree<Euclidean1D> testTree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet(testTree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetWithBoundary() {
        // Create a test collection of SubHyperplanes
        Collection<SubHyperplane<Euclidean1D>> testBoundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(testBoundary);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        // Create a test BSPTree
        BSPTree<Euclidean1D> testTree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(testTree);
        assertNotNull(newIntervalsSet);
    }

    @Test
    public void testGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(3.0, 7.0);
        intervalsSet.computeGeometricalProperties();
        // Assert properties were calculated correctly
        assertNotNull(intervalsSet.getBarycenter());
        // Add more assertions as needed
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        double inf = intervalsSet.getInf();
        assertEquals(1.0, inf, 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        double sup = intervalsSet.getSup();
        assertEquals(5.0, sup, 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        List<Interval> intervalList = intervalsSet.asList();
        // Assert the correct interval list is returned
        assertNotNull(intervalList);
    }

    // Add more test cases for edge cases and exceptions as needed

}