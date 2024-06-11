import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;

import java.util.*;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;

    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();
    }

    @Test
    public void testConstructorWithDefaultValues() {
        Assert.assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        double lower = -1.0;
        double upper = 1.0;

        intervalsSet = new IntervalsSet(lower, upper);

        Assert.assertNotNull(intervalsSet);
        Assert.assertEquals(lower, intervalsSet.getInf(), 0.01);
        Assert.assertEquals(upper, intervalsSet.getSup(), 0.01);
    }

    @Test
    public void testConstructorWithBSPtree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);

        intervalsSet = new IntervalsSet(tree);

        Assert.assertNotNull(intervalsSet);
        Assert.assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.01);
        Assert.assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.01);
    }

    // Additional test cases for other constructors can be added similarly

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);

        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);

        Assert.assertNotNull(newIntervalsSet);
        Assert.assertEquals(tree, newIntervalsSet.getTree());
    }

    @Test
    public void testComputeGeometricalPropertiesWithEmptyTree() {
        intervalsSet.computeGeometricalProperties();

        Assert.assertNull(intervalsSet.getBarycenter());
        Assert.assertEquals(0.0, intervalsSet.getSize(), 0.01);
    }

    @Test
    public void testComputeGeometricalPropertiesWithNonEmptyTree() {
        // Create a non-empty intervalsSet with specific intervals

        double lower = -1.0;
        double upper = 1.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        intervalsSet.computeGeometricalProperties();

        Assert.assertNotNull(intervalsSet.getBarycenter());
        Assert.assertEquals(upper-lower, intervalsSet.getSize(), 0.01);
    }

    @Test
    public void testGetInfWithEmptyIntervalsSet() {
        double expectedInf = Double.NEGATIVE_INFINITY;
        Assert.assertEquals(expectedInf, intervalsSet.getInf(), 0.01);
    }

    @Test
    public void testGetInfWithNonEmptyIntervalsSet() {
        // Create a non-empty intervalsSet with specific intervals

        double lower = -1.0;
        double upper = 1.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        double expectedInf = lower;
        Assert.assertEquals(expectedInf, intervalsSet.getInf(), 0.01);
    }

    @Test
    public void testGetSupWithEmptyIntervalsSet() {
        double expectedSup = Double.POSITIVE_INFINITY;
        Assert.assertEquals(expectedSup, intervalsSet.getSup(), 0.01);
    }

    @Test
    public void testGetSupWithNonEmptyIntervalsSet() {
        // Create a non-empty intervalsSet with specific intervals

        double lower = -1.0;
        double upper = 1.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        double expectedSup = upper;
        Assert.assertEquals(expectedSup, intervalsSet.getSup(), 0.01);
    }

    @Test
    public void testAsListWithEmptyIntervalsSet() {
        List<Interval> expectedList = new ArrayList<>();
        List<Interval> intervalsList = intervalsSet.asList();

        Assert.assertEquals(expectedList, intervalsList);
    }

    @Test
    public void testAsListWithNonEmptyIntervalsSet() {
        // Create a non-empty intervalsSet with specific intervals

        double lower = -3.0;
        double upper = 3.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        List<Interval> expectedList = Arrays.asList(new Interval(-3.0, 3.0));
        List<Interval> intervalsList = intervalsSet.asList();

        Assert.assertEquals(expectedList, intervalsList);
    }

    // Additional test cases can be added for each method in IntervalsSet class

}