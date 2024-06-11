import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class IntervalsSetTest {
    
    @Test
    public void testBuildTree() {
        // Edge case: lower and upper both infinite
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        // Verify that the tree is built correctly
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildNew() {
        // Typical case: build a new tree
        IntervalsSet intervalsSet = new IntervalsSet();
        // Create BSPTree for testing
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        // Build a new IntervalsSet with the BSPTree
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        // Verify that the new IntervalsSet is created correctly
        assertNotNull(newIntervalsSet);
    }
    
    @Test
    public void testGetInf() {
        // Typical case: Get the lower bound of the IntervalsSet
        IntervalsSet intervalsSet = new IntervalsSet(-5.0, 10.0);
        // Get the lower bound
        double lowerBound = intervalsSet.getInf();
        // Verify that the lower bound is correct
        assertEquals(-5.0, lowerBound, 0);
    }
    
    @Test
    public void testGetSup() {
        // Typical case: Get the upper bound of the IntervalsSet
        IntervalsSet intervalsSet = new IntervalsSet(-2.0, 7.0);
        // Get the upper bound
        double upperBound = intervalsSet.getSup();
        // Verify that the upper bound is correct
        assertEquals(7.0, upperBound, 0);
    }
    
    @Test
    public void testAsList() {
        // Typical case: Get the list of intervals in the IntervalsSet
        IntervalsSet intervalsSet = new IntervalsSet(-3.0, 5.0);
        // Get the list of intervals
        List<Interval> intervalList = intervalsSet.asList();
        // Verify that the list is not empty
        assertFalse(intervalList.isEmpty());
    }
    
    // Add more test cases for other methods as needed
    
}