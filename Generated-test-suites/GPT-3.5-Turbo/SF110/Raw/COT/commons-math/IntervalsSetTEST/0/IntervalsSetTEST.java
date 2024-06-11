import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        assertEquals(intervalsSet.getInf(), 0, 0);
        assertEquals(intervalsSet.getSup(), 10, 0);
    }
    
    @Test
    public void testIntervalsSetWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        intervalsSet.computeGeometricalProperties();
        assertEquals(intervalsSet.getSize(), 10, 0);
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        assertEquals(intervalsSet.getInf(), 0, 0);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        assertEquals(intervalsSet.getSup(), 10, 0);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        List<Interval> intervalList = intervalsSet.asList();
        assertEquals(intervalList.size(), 1);
        assertEquals(intervalList.get(0).getInf(), 0, 0);
        assertEquals(intervalList.get(0).getSup(), 10, 0);
    }
    
    // Additional test cases for potential edge cases and exceptions
    
    @Test
    public void testIntervalsSetWithNegativeBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(-10, 0);
        assertEquals(intervalsSet.getInf(), -10, 0);
        assertEquals(intervalsSet.getSup(), 0, 0);
    }
    
    @Test
    public void testIntervalsSetWithInfinityBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(intervalsSet.getInf(), Double.NEGATIVE_INFINITY, 0);
        assertEquals(intervalsSet.getSup(), Double.POSITIVE_INFINITY, 0);
    }
    
    @Test
    public void testIntervalsSetWithInvertedBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(10, 0);
        assertEquals(intervalsSet.getInf(), 0, 0);
        assertEquals(intervalsSet.getSup(), 10, 0);
    }
    
    @Test
    public void testAsListWithMultipleIntervals() {
        IntervalsSet intervalsSet = new IntervalsSet(0, 10);
        intervalsSet.union(new IntervalsSet(20, 30));
        List<Interval> intervalList = intervalsSet.asList();
        assertEquals(intervalList.size(), 2);
        assertEquals(intervalList.get(0).getInf(), 0, 0);
        assertEquals(intervalList.get(0).getSup(), 10, 0);
        assertEquals(intervalList.get(1).getInf(), 20, 0);
        assertEquals(intervalList.get(1).getSup(), 30, 0);
    }
    
    @Test
    public void testAsListWithEmptyIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervalList = intervalsSet.asList();
        assertEquals(intervalList.size(), 0);
    }

}