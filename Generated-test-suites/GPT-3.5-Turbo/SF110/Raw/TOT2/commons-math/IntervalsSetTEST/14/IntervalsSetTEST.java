import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
        // Add more tests if needed
    }

    @Test
    public void testIntervalsSetDoubleConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertNotNull(intervalsSet);
        // Add more tests if needed
    }

    @Test
    public void testIntervalsSetTreeConstructor() {
        // Create BSPTree and test constructor
        // Add more tests if needed
    }

    @Test
    public void testIntervalsSetBoundaryConstructor() {
        // Create Collection<SubHyperplane<Euclidean1D>> and test constructor
        // Add more tests if needed
    }

    @Test
    public void testBuildNew() {
        // Create BSPTree and test buildNew method
        // Add more tests if needed
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test computeGeometricalProperties method
        // Add more tests if needed
    }

    @Test
    public void testGetInf() {
        // Test getInf method
        // Add more tests if needed
    }

    @Test
    public void testGetSup() {
        // Test getSup method
        // Add more tests if needed
    }

    @Test
    public void testAsList() {
        // Test asList method
        // Add more tests if needed
    }

    // Add more test cases for edge cases and exceptions

}