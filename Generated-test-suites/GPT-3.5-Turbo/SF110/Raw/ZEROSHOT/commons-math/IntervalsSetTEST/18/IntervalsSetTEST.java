import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import org.apache.commons.math3.geometry.Vector1D;
import org.apache.commons.math3.geometry.euclidean.oned.BSPTree;
import org.apache.commons.math3.geometry.euclidean.oned.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;

import java.util.ArrayList;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testBuildTree() {
        double lower = -5.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertNotNull(tree);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.buildNew(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervalList = intervalsSet.asList();
        assertNotNull(intervalList);
        assertTrue(intervalList.isEmpty());
    }

    @Test
    public void testRecurseList() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        List<Interval> intervalList = new ArrayList<Interval>();
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.recurseList(tree, intervalList, lower, upper);
        assertNotNull(intervalList);
    }

    @Test
    public void testIntervalsSetConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetConstructorWithArgs() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetConstructorWithBoundary() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }
}