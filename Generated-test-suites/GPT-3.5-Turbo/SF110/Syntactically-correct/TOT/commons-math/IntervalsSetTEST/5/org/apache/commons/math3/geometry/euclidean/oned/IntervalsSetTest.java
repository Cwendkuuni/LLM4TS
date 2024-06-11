import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = intervalsSet.asList();

        assertTrue(intervals.isEmpty());
    }

    @Test
    public void testConstructorWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 5.0);
        List<Interval> intervals = intervalsSet.asList();

        assertEquals(1, intervals.size());
        assertEquals(0.0, intervals.get(0).getInf(), 0.01);
        assertEquals(5.0, intervals.get(0).getSup(), 0.01);
    }

    @Test
    public void testConstructorWithInfiniteBounds() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> intervals = intervalsSet.asList();

        assertEquals(1, intervals.size());
        assertEquals(lower, intervals.get(0).getInf(), 0.01);
        assertEquals(upper, intervals.get(0).getSup(), 0.01);
    }

    @Test
    public void testConstructorWithBoundary() {
        OrientedPoint p1 = new OrientedPoint(new Vector1D(0.0), true);
        OrientedPoint p2 = new OrientedPoint(new Vector1D(5.0), false);
        SubHyperplane<Euclidean1D> boundary = p1.wholeHyperplane().split(p2.wholeHyperplane());
        List<SubHyperplane<Euclidean1D>> boundaryList = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundaryList.add(boundary);

        IntervalsSet intervalsSet = new IntervalsSet(boundaryList);
        List<Interval> intervals = intervalsSet.asList();

        assertEquals(1, intervals.size());
        assertEquals(0.0, intervals.get(0).getInf(), 0.01);
        assertEquals(5.0, intervals.get(0).getSup(), 0.01);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);

        assertEquals(tree, intervalsSet.getTree());
    }

    @Test
    public void testGetInf() {
        OrientedPoint p1 = new OrientedPoint(new Vector1D(-3.0), true);
        OrientedPoint p2 = new OrientedPoint(new Vector1D(5.0), false);
        SubHyperplane<Euclidean1D> boundary = p1.wholeHyperplane().split(p2.wholeHyperplane());
        List<SubHyperplane<Euclidean1D>> boundaryList = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundaryList.add(boundary);

        IntervalsSet intervalsSet = new IntervalsSet(boundaryList);
        double inf = intervalsSet.getInf();

        assertEquals(-Double.POSITIVE_INFINITY, inf, 0.01);
    }

    @Test
    public void testGetSup() {
        OrientedPoint p1 = new OrientedPoint(new Vector1D(-3.0), true);
        OrientedPoint p2 = new OrientedPoint(new Vector1D(5.0), false);
        SubHyperplane<Euclidean1D> boundary = p1.wholeHyperplane().split(p2.wholeHyperplane());
        List<SubHyperplane<Euclidean1D>> boundaryList = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundaryList.add(boundary);

        IntervalsSet intervalsSet = new IntervalsSet(boundaryList);
        double sup = intervalsSet.getSup();

        assertEquals(Double.POSITIVE_INFINITY, sup, 0.01);
    }

    @Test
    public void testAsList() {
        OrientedPoint p1 = new OrientedPoint(new Vector1D(0.0), true);
        OrientedPoint p2 = new OrientedPoint(new Vector1D(2.0), false);
        SubHyperplane<Euclidean1D> boundary = p1.wholeHyperplane().split(p2.wholeHyperplane());
        List<SubHyperplane<Euclidean1D>> boundaryList = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundaryList.add(boundary);

        IntervalsSet intervalsSet = new IntervalsSet(boundaryList);
        List<Interval> intervals = intervalsSet.asList();

        assertEquals(1, intervals.size());
        assertEquals(0.0, intervals.get(0).getInf(), 0.01);
        assertEquals(2.0, intervals.get(0).getSup(), 0.01);
    }

    @Test
    public void testComputeGeometricalPropertiesWithNullCut() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();

        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.01);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }

    @Test
    public void testComputeGeometricalPropertiesWithFiniteSize() {
        OrientedPoint p1 = new OrientedPoint(new Vector1D(0.0), true);
        OrientedPoint p2 = new OrientedPoint(new Vector1D(2.0), false);
        SubHyperplane<Euclidean1D> boundary = p1.wholeHyperplane().split(p2.wholeHyperplane());
        List<SubHyperplane<Euclidean1D>> boundaryList = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundaryList.add(boundary);

        IntervalsSet intervalsSet = new IntervalsSet(boundaryList);
        intervalsSet.computeGeometricalProperties();

        assertEquals(2.0, intervalsSet.getSize(), 0.01);
        assertEquals(new Vector1D(1.0), intervalsSet.getBarycenter());
    }

    @Test
    public void testComputeGeometricalPropertiesWithZeroSize() {
        OrientedPoint p1 = new OrientedPoint(new Vector1D(0.0), true);
        OrientedPoint p2 = new OrientedPoint(new Vector1D(0.0), false);
        SubHyperplane<Euclidean1D> boundary = p1.wholeHyperplane().split(p2.wholeHyperplane());
        List<SubHyperplane<Euclidean1D>> boundaryList = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundaryList.add(boundary);

        IntervalsSet intervalsSet = new IntervalsSet(boundaryList);
        intervalsSet.computeGeometricalProperties();

        assertEquals(0.0, intervalsSet.getSize(), 0.01);
        assertEquals(new Vector1D(0.0), intervalsSet.getBarycenter());
    }

    @Test
    public void testComputeGeometricalPropertiesWithInfiniteSize() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();

        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.01);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }

}