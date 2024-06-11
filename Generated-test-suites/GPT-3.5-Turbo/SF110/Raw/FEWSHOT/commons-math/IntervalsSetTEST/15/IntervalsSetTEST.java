import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import java.util.*;
import org.junit.Test;

public class IntervalsSetTest {
    @Test
    public void testConstructorEmpty() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0001);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0001);
        assertEquals(0, intervalsSet.asList().size());
    }
    
    @Test
    public void testConstructorWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        
        assertEquals(0, intervalsSet.getInf(), 0.0001);
        assertEquals(10, intervalsSet.getSup(), 0.0001);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(0, intervalsSet.asList().get(0).getInf(), 0.0001);
        assertEquals(10, intervalsSet.asList().get(0).getSup(), 0.0001);
    }
    
    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0001);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0001);
        assertEquals(0, intervalsSet.asList().size());
    }
    
    @Test
    public void testConstructorWithBoundary() {
        SubHyperplane<Euclidean1D> subHyperplane1 = new OrientedPoint(new Vector1D(0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subHyperplane2 = new OrientedPoint(new Vector1D(10), true).wholeHyperplane();
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundary.add(subHyperplane1);
        boundary.add(subHyperplane2);
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        
        assertEquals(0, intervalsSet.getInf(), 0.0001);
        assertEquals(10, intervalsSet.getSup(), 0.0001);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(0, intervalsSet.asList().get(0).getInf(), 0.0001);
        assertEquals(10, intervalsSet.asList().get(0).getSup(), 0.0001);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        
        assertEquals(tree, newIntervalsSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(10, intervalsSet.getSize(), 0.0001);
        assertEquals(new Vector1D(5), intervalsSet.getBarycenter());
        
        IntervalsSet emptyIntervalsSet = new IntervalsSet();
        
        emptyIntervalsSet.computeGeometricalProperties();
        
        assertEquals(0, emptyIntervalsSet.getSize(), 0.0001);
        assertEquals(Vector1D.NaN, emptyIntervalsSet.getBarycenter());
    }
    
    @Test
    public void testGetInf() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0001);
        
        tree = new BSPTree<Euclidean1D>(Boolean.FALSE);
        intervalsSet = new IntervalsSet(tree);
        
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0001);
    }
    
    @Test
    public void testGetSup() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0001);
        
        tree = new BSPTree<Euclidean1D>(Boolean.FALSE);
        intervalsSet = new IntervalsSet(tree);
        
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0001);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        
        List<Interval> intervalList = intervalsSet.asList();
        
        assertEquals(1, intervalList.size());
        assertEquals(0, intervalList.get(0).getInf(), 0.0001);
        assertEquals(10, intervalList.get(0).getSup(), 0.0001);
    }
    
    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        List<Interval> intervalList = new ArrayList<Interval>();
        
        intervalsSet.recurseList(intervalsSet.getTree(), intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        assertEquals(1, intervalList.size());
        assertEquals(0, intervalList.get(0).getInf(), 0.0001);
        assertEquals(10, intervalList.get(0).getSup(), 0.0001);
    }
}