import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructor2() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 4.0);
        assertEquals(2.0, intervalsSet.getInf(), 0.00001);
        assertEquals(4.0, intervalsSet.getSup(), 0.00001);
    }

    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(true);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree());
    }

    @Test
    public void testConstructor4() {
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        IntervalsSet intervalsSet = new IntervalsSet(Collections.singletonList(subHyperplane));
        assertEquals(subHyperplane, intervalsSet.getBoundary().get(0));
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(true);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newSet = intervalsSet.buildNew(tree);
        assertEquals(tree, newSet.getTree());
    }

    @Test
    public void testComputeGeometricalProperties1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.00001);
    }

    @Test
    public void testComputeGeometricalProperties2() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        intervalsSet.computeGeometricalProperties();
        assertEquals(new Vector1D(1.5), intervalsSet.getBarycenter());
        assertEquals(1.0, intervalsSet.getSize(), 0.00001);
    }

    // More test cases for the remaining methods...

}