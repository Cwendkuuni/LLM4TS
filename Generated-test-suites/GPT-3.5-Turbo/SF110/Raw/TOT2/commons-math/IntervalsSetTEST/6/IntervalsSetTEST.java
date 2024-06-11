import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0); // Assuming intervals are within a valid range
        intervalsSet.computeGeometricalProperties();
        // Add assertions based on expected results
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0); // Assuming intervals are within a valid range
        double inf = intervalsSet.getInf();
        // Add assertions based on expected results
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0); // Assuming intervals are within a valid range
        double sup = intervalsSet.getSup();
        // Add assertions based on expected results
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0); // Assuming intervals are within a valid range
        List<Interval> intervalList = intervalsSet.asList();
        assertNotNull(intervalList);
    }
}