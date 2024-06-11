import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetConstructorWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertNotNull(intervalsSet);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetConstructorWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // test specific properties based on input data, cannot be generalized
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 6.0);
        assertEquals(2.0, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 6.0);
        assertEquals(6.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(1, intervalsSet.asList().size());
        // test more scenarios for list generation
    }

    // Add more test cases for edge cases and error scenarios

}