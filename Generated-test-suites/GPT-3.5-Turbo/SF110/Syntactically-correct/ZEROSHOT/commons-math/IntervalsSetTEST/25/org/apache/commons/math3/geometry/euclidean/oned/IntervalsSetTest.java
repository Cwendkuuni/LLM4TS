import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.euclidean.oned.SubOrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Before
    public void setUp() {
        // Set up any necessary dependencies or initial configurations
    }

    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testParameterizedConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testBoundaryConstructor() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new SubOrientedPoint(new Vector1D(0.0), true));
        boundary.add(new SubOrientedPoint(new Vector1D(1.0), false));
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testBuildNew() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertEquals(tree, newIntervalsSet.getTree(false));
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(0.0, intervals.get(0).getInf(), 0.0);
        assertEquals(1.0, intervals.get(0).getSup(), 0.0);
    }

}