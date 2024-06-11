import org.apache.commons.math3.geometry.PartitioningTestUtils;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testConstructor() {

        // typical use case
        IntervalsSet intervalsSet = new IntervalsSet();
        Assert.assertNotNull(intervalsSet);

        // edge case
        IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 2.0);
        List<Interval> intervals = intervalsSet2.asList();
        Assert.assertEquals(1, intervals.size());
        Assert.assertEquals(1.0, intervals.get(0).getInf(), Precision.EPSILON);
        Assert.assertEquals(2.0, intervals.get(0).getSup(), Precision.EPSILON);

        // error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(2.0, 1.0);
        List<Interval> intervals3 = intervalsSet3.asList();
        Assert.assertNull(intervals3);
    }

    @Test
    public void testBuildNew() {
        // typical use case
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet newSet = intervalsSet.buildNew(tree);
        Assert.assertNotNull(newSet);

        // edge case
        tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane(),
                new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null);
        IntervalsSet newSet2 = intervalsSet.buildNew(tree);
        List<Interval> intervals = newSet2.asList();
        Assert.assertEquals(1, intervals.size());
        Assert.assertEquals(1.0, intervals.get(0).getInf(), Precision.EPSILON);
        Assert.assertEquals(Double.POSITIVE_INFINITY, intervals.get(0).getSup(), Precision.EPSILON);

        // error scenario
        IntervalsSet newSet3 = intervalsSet.buildNew(null);
        List<Interval> intervals3 = newSet3.asList();
        Assert.assertNull(intervals3);
    }

    @Test
    public void testComputeGeometricalProperties() {
        // typical use case
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();

        // edge case
        IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 2.0);
        intervalsSet2.computeGeometricalProperties();

        // error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(2.0, 1.0);
        intervalsSet3.computeGeometricalProperties();
    }

    @Test
    public void testGetInf() {
        // typical use case
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        Assert.assertEquals(Double.POSITIVE_INFINITY, inf, Precision.EPSILON);

        // edge case
        IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 2.0);
        double inf2 = intervalsSet2.getInf();
        Assert.assertEquals(1.0, inf2, Precision.EPSILON);

        // error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(2.0, 1.0);
        double inf3 = intervalsSet3.getInf();
        Assert.assertEquals(Double.POSITIVE_INFINITY, inf3, Precision.EPSILON);
    }

    @Test
    public void testGetSup() {
        // typical use case
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        Assert.assertEquals(Double.NEGATIVE_INFINITY, sup, Precision.EPSILON);

        // edge case
        IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 2.0);
        double sup2 = intervalsSet2.getSup();
        Assert.assertEquals(2.0, sup2, Precision.EPSILON);

        // error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(2.0, 1.0);
        double sup3 = intervalsSet3.getSup();
        Assert.assertEquals(Double.NEGATIVE_INFINITY, sup3, Precision.EPSILON);
    }

    @Test
    public void testAsList() {
        // typical use case
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = intervalsSet.asList();
        Assert.assertNotNull(intervals);
        Assert.assertEquals(0, intervals.size());

        // edge case
        IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 2.0);
        intervalsSet2.computeGeometricalProperties();
        List<Interval> intervals2 = intervalsSet2.asList();
        Assert.assertNotNull(intervals2);
        Assert.assertEquals(1, intervals2.size());
        Assert.assertEquals(1.0, intervals2.get(0).getInf(), Precision.EPSILON);
        Assert.assertEquals(2.0, intervals2.get(0).getSup(), Precision.EPSILON);

        // error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(2.0, 1.0);
        List<Interval> intervals3 = intervalsSet3.asList();
        Assert.assertNull(intervals3);
    }

    @Test
    public void testRecurseList() {
        // typical use case
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = new ArrayList<>();
        intervalsSet.recurseList(intervalsSet.getTree(false), intervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Assert.assertNotNull(intervals);
        Assert.assertEquals(0, intervals.size());

        // edge case
        IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 2.0);
        intervalsSet2.computeGeometricalProperties();
        List<Interval> intervals2 = new ArrayList<>();
        intervalsSet2.recurseList(intervalsSet2.getTree(false), intervals2, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Assert.assertNotNull(intervals2);
        Assert.assertEquals(1, intervals2.size());
        Assert.assertEquals(1.0, intervals2.get(0).getInf(), Precision.EPSILON);
        Assert.assertEquals(2.0, intervals2.get(0).getSup(), Precision.EPSILON);

        // error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(2.0, 1.0);
        intervalsSet3.computeGeometricalProperties();
        List<Interval> intervals3 = new ArrayList<>();
        intervalsSet3.recurseList(intervalsSet3.getTree(false), intervals3, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        Assert.assertNull(intervals3);
    }
}