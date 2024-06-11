import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
        assertEquals(1, intervalsSet.asList().size());
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testConstructorWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
        assertEquals(1, intervalsSet.asList().size());
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertTrue(Double.isInfinite(intervalsSet.getSize()));
        assertNull(intervalsSet.getBarycenter());
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(1.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(2.0, intervalsSet.asList().get(0).getSup(), 0.0);
    }
}