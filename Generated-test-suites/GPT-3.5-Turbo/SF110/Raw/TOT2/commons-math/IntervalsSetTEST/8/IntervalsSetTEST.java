import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {
    
    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructorWithLowerAndUpper() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertNotNull(intervalsSet);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(3.0, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testBuildNew() {
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<>());
        assertNotNull(newIntervalsSet);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        assertEquals(1, intervalsSet.asList().size());
    }
    
    // Add more test cases for other methods
    
}