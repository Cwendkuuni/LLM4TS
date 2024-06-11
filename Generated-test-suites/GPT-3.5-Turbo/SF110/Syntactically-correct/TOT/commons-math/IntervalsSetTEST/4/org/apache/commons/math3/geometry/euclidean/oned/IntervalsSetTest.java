import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        // Check if intervalsSet object is not null
        assertNotNull(intervalsSet);
        
        // Check if intervalsSet has an empty tree
        assertNull(intervalsSet.getTree(false).getCut());
    }
    
    // Test case for constructor with lower and upper bounds
    @Test
    public void testConstructorWithBounds() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        // Check if intervalsSet object is not null
        assertNotNull(intervalsSet);
        
        // Check if intervalsSet has a tree with a proper cut
        assertNotNull(intervalsSet.getTree(false).getCut());
        
        // Check if the lower bound is set correctly
        assertEquals(lower, intervalsSet.getInf(), 0.0001);
        
        // Check if the upper bound is set correctly
        assertEquals(upper, intervalsSet.getSup(), 0.0001);
    }
    
    // Test case for constructor with BSPTree
    @Test
    public void testConstructorWithTree() {
        double lower = -5.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = buildTree(lower, upper);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        // Check if intervalsSet object is not null
        assertNotNull(intervalsSet);
        
        // Check if intervalsSet has a tree with a proper cut
        assertNotNull(intervalsSet.getTree(false).getCut());
        
        // Check if the lower bound is set correctly
        assertEquals(lower, intervalsSet.getInf(), 0.0001);
        
        // Check if the upper bound is set correctly
        assertEquals(upper, intervalsSet.getSup(), 0.0001);
    }
    
    // Test case for constructor with collection of sub-hyperplanes
    @Test
    public void testConstructorWithBoundary() {
        // TODO: add test case for the constructor with boundary collection
    }
    
    // Test case for buildNew() method
    @Test
    public void testBuildNew() {
        double lower = -5.0;
        double upper = 5.0;
        BSPTree<Euclidean1D> tree = buildTree(lower, upper);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        // Check if buildNew() returns a new IntervalsSet object with the same tree
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertSame(tree, newIntervalsSet.getTree(false));
    }
    
    // Test case for computeGeometricalProperties() method - when tree cut is null
    @Test
    public void testComputeGeometricalPropertiesWithNullCut() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        
        // Check if the tree cut is null
        assertNull(intervalsSet.getTree(false).getCut());
        
        // Check if the barycenter is NaN
        assertTrue(Double.isNaN(intervalsSet.getBarycenter().getX()));
        
        // Check if the size is infinity
        assertTrue(Double.isInfinite(intervalsSet.getSize()));
    }
    
    // Test case for computeGeometricalProperties() method - when tree cut is not null and size is not infinite
    @Test
    public void testComputeGeometricalPropertiesWithNonInfiniteSize() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        
        // Check if the barycenter is set correctly
        assertEquals(0.0, intervalsSet.getBarycenter().getX(), 0.0001);
        
        // Check if the size is set correctly
        assertEquals(10.0, intervalsSet.getSize(), 0.0001);
    }
    
    // Test case for computeGeometricalProperties() method - when tree cut is not null and size is infinite
    @Test
    public void testComputeGeometricalPropertiesWithInfiniteSize() {
        // TODO: add test case for infinite size
    }
    
    // Test case for getInf() method
    @Test
    public void testGetInf() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        // Check if the lower bound is returned correctly
        assertEquals(lower, intervalsSet.getInf(), 0.0001);
    }
    
    // Test case for getSup() method
    @Test
    public void testGetSup() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        // Check if the upper bound is returned correctly
        assertEquals(upper, intervalsSet.getSup(), 0.0001);
    }
    
    // Test case for asList() method
    @Test
    public void testAsList() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        List<Interval> intervalList = intervalsSet.asList();
        
        // Check if the interval list is not empty
        assertFalse(intervalList.isEmpty());
        
        // Check if the size of the interval list is 1, as there is only one interval in the set
        assertEquals(1, intervalList.size());
        
        // Check if the interval in the list has the correct lower bound
        assertEquals(lower, intervalList.get(0).getInf(), 0.0001);
        
        // Check if the interval in the list has the correct upper bound
        assertEquals(upper, intervalList.get(0).getSup(), 0.0001);
    }
    
    // Helper method to build a BSPTree
    private static BSPTree<Euclidean1D> buildTree(final double lower, final double upper) {
        // TODO: implement the buildTree method
    }
}