import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;

    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();
    }

    @Test
    public void testDefaultConstructor() {
        assertNotNull(intervalsSet);
        assertThat(intervalsSet.getAsList().isEmpty(), is(true));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        intervalsSet = new IntervalsSet(-1.0, 1.0);
        assertFalse(intervalsSet.getAsList().isEmpty());
        assertEquals(-1.0, intervalsSet.getInf(), 0.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testConstructorWithBSTTree() {
        BSPTree<Vector1D> tree = new BSPTree<>(false);
        SubHyperplane<Vector1D> subHyperplane = new Vector1D(1.0);
        tree.setAttribute(subHyperplane);
        intervalsSet = new IntervalsSet(tree);
        assertFalse(intervalsSet.getAsList().isEmpty());
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testConstructorWithBoundary() {
        Collection<SubHyperplane<Vector1D>> boundary = new ArrayList<>();
        intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        assertThat(intervalsSet.getAsList().isEmpty(), is(true));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }

    @Test
    public void testBuildNew() {
        BSPTree<Vector1D> tree = new BSPTree<>(false);
        intervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(intervalsSet);
        assertThat(intervalsSet.getAsList().isEmpty(), is(true));
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }

    @Test
    public void testComputeGeometricalProperties() {
        intervalsSet.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testGetInf() {
        intervalsSet = new IntervalsSet(-1.0, 1.0);
        assertEquals(-1.0, intervalsSet.getInf(), 0.0);
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        intervalsSet = new IntervalsSet(Double.POSITIVE_INFINITY, 1.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        intervalsSet = new IntervalsSet(-1.0, 1.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
        intervalsSet = new IntervalsSet(1.0, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        intervalsSet = new IntervalsSet(1.0, Double.NEGATIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        List<IntervalsSet.Interval> intervalList = intervalsSet.asList();
        assertNotNull(intervalList);
        assertThat(intervalList.isEmpty(), is(true));

        intervalsSet = new IntervalsSet(-1.0, 1.0);
        intervalList = intervalsSet.asList();
        assertFalse(intervalList.isEmpty());
        assertEquals(-1.0, intervalList.get(0).getLower(), 0.0);
        assertEquals(1.0, intervalList.get(0).getUpper(), 0.0);
    }

    @Test
    public void testRecurseList() {
        List<IntervalsSet.Interval> intervalList = new ArrayList<>();
        intervalsSet.recurseList(new BSPTree<>(false), intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertFalse(intervalList.isEmpty());
        assertEquals(Double.NEGATIVE_INFINITY, intervalList.get(0).getLower(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalList.get(0).getUpper(), 0.0);
    }

    public static class Interval {
        private final double lower;
        private final double upper;

        public Interval(double lower, double upper) {
            this.lower = lower;
            this.upper = upper;
        }

        public double getLower() {
            return lower;
        }

        public double getUpper() {
            return upper;
        }
    }
}