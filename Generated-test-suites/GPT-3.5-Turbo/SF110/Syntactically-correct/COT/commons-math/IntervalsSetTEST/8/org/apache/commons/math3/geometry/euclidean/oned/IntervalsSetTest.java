import org.junit.Test;
import org.apache.commons.math3.geometry.partitioning.sub.Test.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testConstructor_emptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> expected = new ArrayList<Interval>();
        List<Interval> actual = intervalsSet.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testConstructor_singleInterval() {
        double lower = 1.0;
        double upper = 2.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> expected = new ArrayList<Interval>();
        expected.add(new Interval(lower, upper));

        List<Interval> actual = intervalsSet.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testConstructor_collectionOfSubHyperplanes() {
        double lower1 = 1.0;
        double upper1 = 2.0;
        double lower2 = 3.0;
        double upper2 = 4.0;
        SubHyperplane<Euclidean1D> subHyperplane1 = new OrientedPoint(new Vector1D(lower1), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subHyperplane2 = new OrientedPoint(new Vector1D(lower2), true).wholeHyperplane();
        List<SubHyperplane<Euclidean1D>> boundary = Arrays.asList(subHyperplane1, subHyperplane2);

        IntervalsSet intervalsSet = new IntervalsSet(boundary);

        List<Interval> expected = new ArrayList<Interval>();
        expected.add(new Interval(lower1, upper1));
        expected.add(new Interval(lower2, upper2));

        List<Interval> actual = intervalsSet.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalSet = intervalsSet.buildNew(tree);

        assertEquals(tree, newIntervalSet.getTree());
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();

        intervalsSet.computeGeometricalProperties();

        // assert geometrical properties
    }

    @Test
    public void testGetInf_singleInterval() {
        double lower = 1.0;
        double upper = 2.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        double expected = lower;
        double actual = intervalsSet.getInf();

        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetInf_multipleIntervals() {
        double lower1 = 1.0;
        double upper1 = 2.0;
        double lower2 = 3.0;
        double upper2 = 4.0;
        SubHyperplane<Euclidean1D> subHyperplane1 = new OrientedPoint(new Vector1D(lower1), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subHyperplane2 = new OrientedPoint(new Vector1D(lower2), true).wholeHyperplane();
        List<SubHyperplane<Euclidean1D>> boundary = Arrays.asList(subHyperplane1, subHyperplane2);

        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        double expected = lower1;
        double actual = intervalsSet.getInf();

        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetSup_singleInterval() {
        double lower = 1.0;
        double upper = 2.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        double expected = upper;
        double actual = intervalsSet.getSup();

        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetSup_multipleIntervals() {
        double lower1 = 1.0;
        double upper1 = 2.0;
        double lower2 = 3.0;
        double upper2 = 4.0;
        SubHyperplane<Euclidean1D> subHyperplane1 = new OrientedPoint(new Vector1D(lower1), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subHyperplane2 = new OrientedPoint(new Vector1D(lower2), true).wholeHyperplane();
        List<SubHyperplane<Euclidean1D>> boundary = Arrays.asList(subHyperplane1, subHyperplane2);

        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        double expected = upper2;
        double actual = intervalsSet.getSup();

        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testAsList_singleInterval() {
        double lower = 1.0;
        double upper = 2.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        List<Interval> expected = new ArrayList<Interval>();
        expected.add(new Interval(lower, upper));

        List<Interval> actual = intervalsSet.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testAsList_multipleIntervals() {
        double lower1 = 1.0;
        double upper1 = 2.0;
        double lower2 = 3.0;
        double upper2 = 4.0;
        SubHyperplane<Euclidean1D> subHyperplane1 = new OrientedPoint(new Vector1D(lower1), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subHyperplane2 = new OrientedPoint(new Vector1D(lower2), true).wholeHyperplane();
        List<SubHyperplane<Euclidean1D>> boundary = Arrays.asList(subHyperplane1, subHyperplane2);

        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        List<Interval> expected = new ArrayList<Interval>();
        expected.add(new Interval(lower1, upper1));
        expected.add(new Interval(lower2, upper2));

        List<Interval> actual = intervalsSet.asList();

        assertEquals(expected, actual);
    }

    @Test
    public void testRecurseList() {
        // add test case code here
    }

}