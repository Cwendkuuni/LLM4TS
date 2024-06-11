import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {
    
    @Test
    public void testEmptyConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(0, intervalsSet.asList().size());
    }
    
    @Test
    public void testConstructorWithBounds() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(1.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(5.0, intervalsSet.asList().get(0).getSup(), 0.0);
    }
    
    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertSame(tree, intervalsSet.getTree());
    }
    
    @Test
    public void testConstructorWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertSame(boundary, intervalsSet.getBoundary());
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertSame(tree, intervalsSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalPropertiesInfNull() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Double.NaN, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testComputeGeometricalPropertiesInfPositive() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        
        intervalsSet.computeGeometricalProperties();
        assertEquals(4.0, intervalsSet.getSize(), 0.0);
        assertEquals(3.0, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testComputeGeometricalPropertiesInfPositiveInfinite() {
        IntervalsSet intervalsSet = new IntervalsSet(Double.POSITIVE_INFINITY, 5.0);
        
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Double.NaN, intervalsSet.getBarycenter().getX(), 0.0);
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertEquals(5.0, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testAsListEmpty() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.asList().isEmpty());
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 5.0);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(1.0, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(5.0, intervalsSet.asList().get(0).getSup(), 0.0);
    }
}