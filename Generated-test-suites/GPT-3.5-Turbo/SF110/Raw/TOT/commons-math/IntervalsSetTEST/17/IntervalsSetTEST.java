import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        
        // Check that the tree is empty
        assertNull(tree.getCut());
    }
    
    @Test
    public void testConstructor2() {
        double lower = 0.0;
        double upper = 10.0;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        
        // Check that the tree is constructed correctly
        assertNotNull(tree.getCut());
        assertTrue(tree.getAttribute());
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
        assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        
        // Check that the tree contains the correct boundary
        SubHyperplane<Euclidean1D> cut = tree.getCut();
        assertEquals(lower, ((OrientedPoint) cut.getHyperplane()).getLocation().getX(), 0.0);
        
        BSPTree<Euclidean1D> minus = tree.getMinus();
        assertTrue(minus.getAttribute());
        
        BSPTree<Euclidean1D> plus = tree.getPlus();
        assertFalse(plus.getAttribute());
        
        assertNull(minus.getCut());
        SubHyperplane<Euclidean1D> secondCut = plus.getCut();
        assertEquals(upper, ((OrientedPoint) secondCut.getHyperplane()).getLocation().getX(), 0.0);
        
        assertNull(plus.getMinus().getCut());
        assertTrue(plus.getMinus().getAttribute());
        
        assertNull(plus.getPlus().getCut());
        assertTrue(plus.getPlus().getAttribute());
    }
    
    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        assertEquals(tree, intervalsSet.getTree(false));
    }
    
    @Test
    public void testConstructor4() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(10.0), false).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        
        // Check that the tree is constructed correctly
        assertNotNull(tree.getCut());
        assertFalse(tree.getAttribute());
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(10.0, intervalsSet.getSup(), 0.0);
        assertEquals(10.0, intervalsSet.getSize(), 0.0);
        
        // Check that the tree contains the correct boundary
        SubHyperplane<Euclidean1D> cut = tree.getCut();
        assertEquals(0.0, ((OrientedPoint) cut.getHyperplane()).getLocation().getX(), 0.0);
        
        BSPTree<Euclidean1D> minus = tree.getMinus();
        assertFalse(minus.getAttribute());
        
        BSPTree<Euclidean1D> plus = tree.getPlus();
        assertTrue(plus.getAttribute());
        
        assertNull(minus.getCut());
        SubHyperplane<Euclidean1D> secondCut = plus.getCut();
        assertEquals(10.0, ((OrientedPoint) secondCut.getHyperplane()).getLocation().getX(), 0.0);
        
        assertNull(plus.getMinus().getCut());
        assertFalse(plus.getMinus().getAttribute());
        
        assertNull(plus.getPlus().getCut());
        assertTrue(plus.getPlus().getAttribute());
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        
        assertEquals(tree, newIntervalsSet.getTree(false));
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        // Create intervals and add to intervals set
        List<Interval> intervals = new ArrayList<>();
        intervals.add(new Interval(0.0, 1.0));
        intervals.add(new Interval(2.0, 3.0));
        intervalsSet.asList().addAll(intervals);
        
        // Compute geometrical properties
        intervalsSet.computeGeometricalProperties();
        
        // Check that the size and barycenter are computed correctly
        assertEquals(2.0, intervalsSet.getSize(), 0.0);
        assertEquals(1.5, intervalsSet.getBarycenter().getX(), 0.0);
        
        // Remove intervals and check that the size and barycenter are computed correctly
        intervalsSet.asList().removeAll(intervals);
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
        assertTrue(intervalsSet.getBarycenter().isNaN());
    }
    
    @Test
    public void testGetInf() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(10.0), false).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        
        double inf = intervalsSet.getInf();
        
        assertEquals(0.0, inf, 0.0);
    }
    
    @Test
    public void testGetSup() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(10.0), false).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        
        double sup = intervalsSet.getSup();
        
        assertEquals(10.0, sup, 0.0);
    }
    
    @Test
    public void testAsList() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(10.0), false).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        
        List<Interval> intervals = intervalsSet.asList();
        
        // Check that the intervals list is equal to the original boundary
        assertEquals(boundary.size(), intervals.size());
        for (int i = 0; i < boundary.size(); i++) {
            SubHyperplane<Euclidean1D> boundarySubHyperplane = boundary.get(i);
            Interval interval = intervals.get(i);
            assertEquals(((OrientedPoint) boundarySubHyperplane.getHyperplane()).getLocation().getX(), interval.getInf(), 0.0);
            assertEquals(((OrientedPoint) boundarySubHyperplane.getHyperplane()).getReverse().getLocation().getX(), interval.getSup(), 0.0);
        }
    }
    
    @Test
    public void testRecurseList1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = new ArrayList<>();
        
        intervalsSet.recurseList(intervalsSet.getTree(false), intervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        // Check that the intervals list is empty
        assertEquals(0, intervals.size());
    }
    
    @Test
    public void testRecurseList2() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane());
        boundary.add(new OrientedPoint(new Vector1D(10.0), false).wholeHyperplane());
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        List<Interval> intervals = new ArrayList<>();
        
        intervalsSet.recurseList(intervalsSet.getTree(false), intervals, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        // Check that the intervals list is equal to the original boundary
        assertEquals(boundary.size(), intervals.size());
        for (int i = 0; i < boundary.size(); i++) {
            SubHyperplane<Euclidean1D> boundarySubHyperplane = boundary.get(i);
            Interval interval = intervals.get(i);
            assertEquals(((OrientedPoint) boundarySubHyperplane.getHyperplane()).getLocation().getX(), interval.getInf(), 0.0);
            assertEquals(((OrientedPoint) boundarySubHyperplane.getHyperplane()).getReverse().getLocation().getX(), interval.getSup(), 0.0);
        }
    }

}