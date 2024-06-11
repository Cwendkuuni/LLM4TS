import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Double.NaN, intervalsSet.getBarycenter().getX(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(new ArrayList<Interval>(), intervalsSet.asList());
    }
    
    @Test
    public void testConstructorWithBounds() {
        double lower = -1.0;
        double upper = 1.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertEquals(2.0, intervalsSet.getSize(), 0.0);
        assertEquals(0.0, intervalsSet.getBarycenter().getX(), 0.0);
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
        
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(lower, upper));
        assertEquals(expectedIntervals, intervalsSet.asList());
    }
    
    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Double.NaN, intervalsSet.getBarycenter().getX(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(new ArrayList<Interval>(), intervalsSet.asList());
    }
    
    @Test
    public void testConstructorWithBoundary() {
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane();
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(subHyperplane);
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(Double.NaN, intervalsSet.getSize(), 0.0);
        assertEquals(Double.NaN, intervalsSet.getBarycenter().getX(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(expectedIntervals, intervalsSet.asList());
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertSame(tree, intervalsSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        intervalsSet.computeGeometricalProperties();
        assertEquals(2.0, intervalsSet.getSize(), 0.0);
        assertEquals(0.0, intervalsSet.getBarycenter().getX(), 0.0);
        
        intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Double.NaN, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        assertEquals(-1.0, intervalsSet.getInf(), 0.0);
        
        intervalsSet = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        assertEquals(1.0, intervalsSet.getSup(), 0.0);
        
        intervalsSet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        List<Interval> expectedIntervals = new ArrayList<>();
        expectedIntervals.add(new Interval(-1.0, 1.0));
        assertEquals(expectedIntervals, intervalsSet.asList());
        
        intervalsSet = new IntervalsSet();
        expectedIntervals = new ArrayList<>();
        assertEquals(expectedIntervals, intervalsSet.asList());
    }
}