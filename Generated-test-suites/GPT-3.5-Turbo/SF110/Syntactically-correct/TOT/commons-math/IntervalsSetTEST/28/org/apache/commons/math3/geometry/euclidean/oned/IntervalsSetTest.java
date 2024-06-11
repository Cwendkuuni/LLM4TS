import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testConstructor() {
        // Test typical use case
        IntervalsSet intervalsSet1 = new IntervalsSet();
        assertNotNull(intervalsSet1);
        
        // Test edge case
        IntervalsSet intervalsSet2 = new IntervalsSet(1.0, 2.0);
        assertNotNull(intervalsSet2);
        
        // Test error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        assertNotNull(intervalsSet3);
    }
    
    @Test
    public void testBuildNew() {
        // Test typical use case
        IntervalsSet intervalsSet1 = new IntervalsSet();
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.FALSE);
        IntervalsSet intervalsSet2 = intervalsSet1.buildNew(tree);
        assertNotNull(intervalsSet2);
        
        // Test edge case
        IntervalsSet intervalsSet3 = new IntervalsSet();
        BSPTree<Euclidean1D> treeNull = null;
        IntervalsSet intervalsSet4 = intervalsSet3.buildNew(treeNull);
        assertNotNull(intervalsSet4);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        // Test typical use case
        IntervalsSet intervalsSet1 = new IntervalsSet(1.0, 2.0);
        intervalsSet1.computeGeometricalProperties();
        
        // Test edge case
        IntervalsSet intervalsSet2 = new IntervalsSet(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        intervalsSet2.computeGeometricalProperties();
        
        // Test error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet();
        intervalsSet3.computeGeometricalProperties();
    }
    
    @Test
    public void testGetInf() {
        // Test typical use case
        IntervalsSet intervalsSet1 = new IntervalsSet(1.0, 2.0);
        double inf1 = intervalsSet1.getInf();
        assertEquals(1.0, inf1, 0);
        
        // Test edge case
        IntervalsSet intervalsSet2 = new IntervalsSet();
        double inf2 = intervalsSet2.getInf();
        assertEquals(Double.POSITIVE_INFINITY, inf2, 0);
        
        // Test error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        double inf3 = intervalsSet3.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf3, 0);
    }
    
    @Test
    public void testGetSup() {
        // Test typical use case
        IntervalsSet intervalsSet1 = new IntervalsSet(1.0, 2.0);
        double sup1 = intervalsSet1.getSup();
        assertEquals(2.0, sup1, 0);
        
        // Test edge case
        IntervalsSet intervalsSet2 = new IntervalsSet();
        double sup2 = intervalsSet2.getSup();
        assertEquals(Double.NEGATIVE_INFINITY, sup2, 0);
        
        // Test error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        double sup3 = intervalsSet3.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup3, 0);
    }
    
    @Test
    public void testAsList() {
        // Test typical use case
        IntervalsSet intervalsSet1 = new IntervalsSet(1.0, 3.0);
        List<Interval> list1 = intervalsSet1.asList();
        assertEquals(1, list1.size());
        assertEquals(1.0, list1.get(0).getInf(), 0);
        assertEquals(3.0, list1.get(0).getSup(), 0);
        
        // Test edge case
        IntervalsSet intervalsSet2 = new IntervalsSet();
        List<Interval> list2 = intervalsSet2.asList();
        assertEquals(0, list2.size());
        
        // Test error scenario
        IntervalsSet intervalsSet3 = new IntervalsSet(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        List<Interval> list3 = intervalsSet3.asList();
        assertEquals(1, list3.size());
        assertEquals(Double.NEGATIVE_INFINITY, list3.get(0).getInf(), 0);
        assertEquals(Double.POSITIVE_INFINITY, list3.get(0).getSup(), 0);
    }
}