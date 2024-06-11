import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetConstructorWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newSet = intervalsSet.buildNew(null);
        assertNull(newSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test for when tree cut is null
        // Implementation of the test depends on data structure and logic of the code

        // Test for when tree cut is not null
        // Implementation of the test depends on data structure and logic of the code
    }

    @Test
    public void testGetInf() {
        // Add test cases for various scenarios of calculating inf
    }

    @Test
    public void testGetSup() {
        // Add test cases for various scenarios of calculating sup
    }

    @Test
    public void testAsList() {
        // Add test cases for converting intervals to list
    }

}