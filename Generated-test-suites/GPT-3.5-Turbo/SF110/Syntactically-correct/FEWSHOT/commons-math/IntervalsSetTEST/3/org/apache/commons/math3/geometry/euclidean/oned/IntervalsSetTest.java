
//Corresponding JUnit 4 Test Cases:
package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.BSPTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testBuildNew() {
        // Test buildNew(BSPTree<Euclidean1D> tree)
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertEquals(tree, intervalsSet.getTree());
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test computeGeometricalProperties()

        // Set up a simple IntervalsSet with a single interval
        double lower = 0.0;
        double upper = 1.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        // Test the properties of the IntervalsSet
        intervalsSet.computeGeometricalProperties();
        assertEquals(1.0, intervalsSet.getSize(), 0.0);
        assertEquals(0.5, intervalsSet.getBarycenter().getX(), 0.0);
    }

    @Test
    public void testGetInf() {
        // Test getInf()

        // Set up a simple IntervalsSet with a single interval
        double lower = 1.0;
        double upper = 3.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        // Test the lower bound
        assertEquals(1.0, intervalsSet.getInf(), 0.0);

        // Test an infinite lower bound
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 3.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);

        // Test a negative infinite lower bound
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        // Test getSup()

        // Set up a simple IntervalsSet with a single interval
        double lower = 1.0;
        double upper = 3.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        // Test the upper bound
        assertEquals(3.0, intervalsSet.getSup(), 0.0);

        // Test an infinite upper bound
        intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);

        // Test a negative infinite upper bound
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        // Test asList()

        // Set up a simple IntervalsSet with multiple intervals
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.getTree().insertCut(new OrientedPoint(new Vector1D(1.0), true));
        intervalsSet.getTree().insertCut(new OrientedPoint(new Vector1D(3.0), true));
        intervalsSet.getTree().insertCut(new OrientedPoint(new Vector1D(5.0), true));

        // Create the expected list
        List<Interval> expectedList = new ArrayList<>(Arrays.asList(new Interval(1.0, 3.0), new Interval(3.0, 5.0)));

        // Test the asList() method
        assertEquals(expectedList, intervalsSet.asList());
    }
}
