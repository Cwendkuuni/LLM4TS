import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class IntervalsSetTest {

    @Test
    public void testConstructor() {  
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructorWithBounds() {
        double lower = 5.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        
        // Check if the bounds were set correctly
        assertEquals(lower, intervalsSet.getInf());
        assertEquals(upper, intervalsSet.getSup());
    }
    
    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        assertEquals(tree, intervalsSet.getTree());
    }
    
    @Test
    public void testConstructorWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        assertEquals(boundary, intervalsSet.getBoundary());
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newSet = intervalsSet.buildNew(tree);
        assertNotNull(newSet);
        assertEquals(tree, newSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(5.0, 10.0);
        intervalsSet.computeGeometricalProperties();
        
        // Check if the size and barycenter were computed correctly
        assertEquals(5.0, intervalsSet.getSize());
        assertEquals(7.5, intervalsSet.getBarycenter().getX());
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(5.0, 10.0);
        double inf = intervalsSet.getInf();
        assertEquals(5.0, inf);
        
        // Test case with infinite lower bound
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, 10.0);
        inf = intervalsSet.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(5.0, 10.0);
        double sup = intervalsSet.getSup();
        assertEquals(10.0, sup);
        
        // Test case with infinite upper bound
        intervalsSet = new IntervalsSet(5.0, Double.POSITIVE_INFINITY);
        sup = intervalsSet.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(5.0, 10.0);
        List<Interval> list = intervalsSet.asList();
        assertNotNull(list);
        
        // Check if the correct intervals are in the list
        assertEquals(1, list.size());
        Interval interval = list.get(0);
        assertEquals(5.0, interval.getInf());
        assertEquals(10.0, interval.getSup());
    }
    
    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet(5.0, 10.0);
        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        // Check if the correct intervals were added to the list
        assertEquals(1, list.size());
        Interval interval = list.get(0);
        assertEquals(5.0, interval.getInf());
        assertEquals(10.0, interval.getSup());
    }
}