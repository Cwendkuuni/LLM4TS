import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class IntervalsSetTest {
    
    @Test
    public void testBuildNew() {
        // Create a test tree
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newSet = intervalsSet.buildNew(tree);
        
        assertEquals(tree, newSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        // Add test cases for computing geometrical properties
    }
    
    @Test
    public void testGetInf() {
        // Add test cases for getting lower bounds
    }
    
    @Test
    public void testGetSup() {
        // Add test cases for getting upper bounds
    }
    
    @Test
    public void testAsList() {
        // Add test cases for converting to list
    }
    
    // Add more test cases for other methods as needed
    
}