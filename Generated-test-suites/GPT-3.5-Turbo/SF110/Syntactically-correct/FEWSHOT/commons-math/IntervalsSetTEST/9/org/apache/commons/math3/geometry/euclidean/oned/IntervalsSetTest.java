
package org.apache.commons.math3.geometry.euclidean.oned;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.partitioning.AbstractRegion;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Before;
import org.junit.Test;

public class IntervalsSetTest {
    
    private IntervalsSet intervalsSet;

    @Before
    public void setup() {
        intervalsSet = new IntervalsSet(1.0, 10.0);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(10.0, intervalsSet.getSup(), 0.0);
        assertEquals(9.0, intervalsSet.getSize(), 0.0);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertTrue(newIntervalsSet.getTree(false).equals(tree));
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        double expectedSize = 0.0;
        double expectedSum = 0.0;
        
        List<Interval> intervals = intervalsSet.asList();
        
        for (Interval interval : intervals) {
            expectedSize += interval.getSize();
            expectedSum += interval.getSize() * interval.getBarycenter();
        }
        
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(expectedSize, intervalsSet.getSize(), 0.0);
        
        if (Double.isInfinite(expectedSize)) {
            assertTrue(intervalsSet.getBarycenter().isNaN());
        } else if (expectedSize >= Precision.SAFE_MIN) {
            assertEquals(expectedSum / expectedSize, intervalsSet.getBarycenter().getX(), 0.0);
        } else {
            assertEquals(((OrientedPoint)intervalsSet.getTree(false).getCut().getHyperplane()).getLocation().getX(), intervalsSet.getBarycenter().getX(), 0.0);
        }
    }
    
    @Test
    public void testGetInf() {
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        
        BSPTree<Euclidean1D> node = intervalsSet.getTree(false);
        node.setAttribute(Boolean.TRUE);
        
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup() {
        assertEquals(10.0, intervalsSet.getSup(), 0.0);
        
        BSPTree<Euclidean1D> node = intervalsSet.getTree(false);
        node.setAttribute(Boolean.TRUE);
        
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsList() {
        List<Interval> expectedIntervals = new ArrayList<>();
        
        expectedIntervals.add(new Interval(1.0, 10.0));
        
        List<Interval> intervals = intervalsSet.asList();
        
        assertEquals(expectedIntervals.size(), intervals.size());
        
        for (int i = 0; i < expectedIntervals.size(); i++) {
            assertEquals(expectedIntervals.get(i).getInf(), intervals.get(i).getInf(), 0.0);
            assertEquals(expectedIntervals.get(i).getSup(), intervals.get(i).getSup(), 0.0);
            assertEquals(expectedIntervals.get(i).getSize(), intervals.get(i).getSize(), 0.0);
            assertEquals(expectedIntervals.get(i).getBarycenter(), intervals.get(i).getBarycenter());
        }
    }
}
