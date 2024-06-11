import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.BSPTree;

public class IntervalsSetTest {

    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testConstructorWithBounds() {
        double lower = 5.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        assertEquals(lower, intervalsSet.getInf(), 0.0001);
        assertEquals(upper, intervalsSet.getSup(), 0.0001);
        assertEquals(1, intervalsSet.asList().size());
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(lower, interval.getInf(), 0.0001);
        assertEquals(upper, interval.getSup(), 0.0001);
        assertEquals((upper - lower), interval.getSize(), 0.0001);
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        tree.setAttribute(true);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0001);
        assertEquals(1, intervalsSet.asList().size());
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, interval.getSize(), 0.0001);
    }

    @Test
    public void testConstructorWithBoundary() {
        SubHyperplane<Euclidean1D> boundary = new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane();
        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(boundary));
        assertNotNull(intervalsSet);
        assertEquals(1, intervalsSet.asList().size());
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0001);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        tree.setAttribute(true);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0001);
        assertEquals(1, intervalsSet.asList().size());
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0001);
    }

    @Test
    public void testComputeGeometricalPropertiesWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals((boolean) intervalsSet.getTree(false).getAttribute(), true);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0001);
    }

    @Test
    public void testComputeGeometricalPropertiesWithNonEmptyTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        tree.setAttribute(true);
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane();
        tree.insertCut(subHyperplane);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.computeGeometricalProperties();
        assertEquals(5.0, intervalsSet.getBarycenter().getX(), 0.0001);
        assertEquals(0.0, intervalsSet.getSize(), 0.0001);
    }

    @Test
    public void testGetInfWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        assertEquals(Double.POSITIVE_INFINITY, inf, 0.0001);
    }

    @Test
    public void testGetInfWithNonEmptyTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        tree.setAttribute(true);
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(5.5), true).wholeHyperplane();
        tree.insertCut(subHyperplane);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double inf = intervalsSet.getInf();
        assertEquals(5.5, inf, 0.0001);
    }

    @Test
    public void testGetSupWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        assertEquals(Double.NEGATIVE_INFINITY, sup, 0.0001);
    }

    @Test
    public void testGetSupWithNonEmptyTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        tree.setAttribute(true);
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(8.5), true).wholeHyperplane();
        tree.insertCut(subHyperplane);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double sup = intervalsSet.getSup();
        assertEquals(8.5, sup, 0.0001);
    }

    @Test
    public void testAsListWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(0, intervalsSet.asList().size());
    }

    @Test
    public void testAsListWithNonEmptyTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        tree.setAttribute(true);
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(0.5), true).wholeHyperplane();
        tree.insertCut(subHyperplane);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(1, intervalsSet.asList().size());
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0001);
    }

    @Test
    public void testRecurseListWithEmptyTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.recurseList(tree, intervalsSet.asList(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(0, intervalsSet.asList().size());
    }

    @Test
    public void testRecurseListWithNonEmptyTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        tree.setAttribute(true);
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(0.5), true).wholeHyperplane();
        tree.insertCut(subHyperplane);
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.recurseList(tree, intervalsSet.asList(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(1, intervalsSet.asList().size());
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(Double.NEGATIVE_INFINITY, interval.getInf(), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, interval.getSup(), 0.0001);
    }

}