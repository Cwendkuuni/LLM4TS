import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {
    
    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0);
    }
    
    @Test
    public void testConstructor2() {
        double lower = 0.0;
        double upper = 10.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        assertEquals(lower, intervalsSet.getInf(), 0);
        assertEquals(upper, intervalsSet.getSup(), 0);
    }
    
    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = buildTree(0.0, 10.0);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        assertEquals(0.0, intervalsSet.getInf(), 0);
        assertEquals(10.0, intervalsSet.getSup(), 0);
    }
    
    @Test
    public void testConstructor4() {
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(10.0), true).wholeHyperplane();
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(lowerCut);
        boundary.add(upperCut);
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        assertEquals(0.0, intervalsSet.getInf(), 0);
        assertEquals(10.0, intervalsSet.getSup(), 0);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = buildTree(0.0, 10.0);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(newIntervalsSet);
        assertEquals(0.0, newIntervalsSet.getInf(), 0);
        assertEquals(10.0, newIntervalsSet.getSup(), 0);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        // Test when tree cut is null
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        
        // Test when tree cut is not null and size is not infinite
        BSPTree<Euclidean1D> tree = buildTree(0.0, 10.0);
        intervalsSet = new IntervalsSet(tree);
        intervalsSet.computeGeometricalProperties();
        assertEquals(10.0, intervalsSet.getSize(), 0);
        assertEquals(new Vector1D(5.0), intervalsSet.getBarycenter());
        
        // Test when tree cut is not null and size is infinite
        tree = buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        intervalsSet = new IntervalsSet(tree);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }
    
    @Test
    public void testGetInf() {
        BSPTree<Euclidean1D> tree = buildTree(0.0, 10.0);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(0.0, intervalsSet.getInf(), 0);
        
        tree = buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        intervalsSet = new IntervalsSet(tree);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0);
    }
    
    @Test
    public void testGetSup() {
        BSPTree<Euclidean1D> tree = buildTree(0.0, 10.0);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(10.0, intervalsSet.getSup(), 0);
        
        tree = buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        intervalsSet = new IntervalsSet(tree);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0);
    }
    
    @Test
    public void testAsList() {
        BSPTree<Euclidean1D> tree = buildTree(0.0, 10.0);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervalList = intervalsSet.asList();
        assertEquals(1, intervalList.size());
        assertEquals(0.0, intervalList.get(0).getInf(), 0);
        assertEquals(10.0, intervalList.get(0).getSup(), 0);
    }
    
    private BSPTree<Euclidean1D> buildTree(double lower, double upper) {
        // Test implementation of buildTree() method
    }
}