import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetDefaultConstructor() {
        IntervalsSet set = new IntervalsSet();
        assertNotNull(set);
    }
  
    @Test
    public void testIntervalsSetConstructorWithBounds() {
        IntervalsSet set = new IntervalsSet(1.0, 5.0);
        assertNotNull(set);
    }
    
    @Test
    public void testBuildNew() {
        IntervalsSet set = new IntervalsSet();
        IntervalsSet newSet = set.buildNew(new BSPTree<Euclidean1D>(Boolean.TRUE));
        assertNotNull(newSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet set = new IntervalsSet();
        set.computeGeometricalProperties();
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet set = new IntervalsSet();
        double inf = set.getInf();
        assertEquals(Double.POSITIVE_INFINITY, inf, 0.0);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet set = new IntervalsSet();
        double sup = set.getSup();
        assertEquals(Double.NEGATIVE_INFINITY, sup, 0.0);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet set = new IntervalsSet();
        assertNotNull(set.asList());
    }

    @Test
    public void testRecurseList() {
        IntervalsSet set = new IntervalsSet();
        set.recurseList(new BSPTree<Euclidean1D>(Boolean.TRUE), new ArrayList<Interval>(), 0.0, 10.0);
    }
}