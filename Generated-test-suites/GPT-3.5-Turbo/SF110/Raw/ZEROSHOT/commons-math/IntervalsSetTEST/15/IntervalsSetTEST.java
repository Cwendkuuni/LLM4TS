import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.OrientedPoint;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testConstructor2() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> bspTree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(bspTree);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> bspTree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newSet = intervalsSet.buildNew(bspTree);
        assertEquals(bspTree, newSet.getTree());
    }

    @Test
    public void testComputeGeometricalProperties1() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        intervalsSet.computeGeometricalProperties();
        assertEquals(4.0, intervalsSet.getSize(), 0.0);
        assertEquals(new Vector1D(3.0), intervalsSet.getBarycenter());
    }

    @Test
    public void testComputeGeometricalProperties2() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        double inf = intervalsSet.getInf();
        assertEquals(1.0, inf, 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        double sup = intervalsSet.getSup();
        assertEquals(5.0, sup, 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        List<Interval> list = intervalsSet.asList();
        assertEquals(1.0, list.get(0).getInf(), 0.0);
        assertEquals(5.0, list.get(0).getSup(), 0.0);
    }

    private static class Interval {
        private double lower;
        private double upper;

        public Interval(double lower, double upper) {
            this.lower = lower;
            this.upper = upper;
        }

        public double getInf() {
            return lower;
        }

        public double getSup() {
            return upper;
        }
    }
}