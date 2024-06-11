import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetLowerAndUpperConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        
        // Check lower and upper values
        assertEquals(0.0, intervalsSet.getInf(), 0.00001);
        assertEquals(1.0, intervalsSet.getSup(), 0.00001);
        
        // Check number of intervals
        assertEquals(1, intervalsSet.asList().size());
        
        // Check interval properties
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(0.0, interval.getInf(), 0.00001);
        assertEquals(1.0, interval.getSup(), 0.00001);
        assertEquals(1.0, interval.getSize(), 0.00001);
        assertEquals(0.5, interval.getBarycenter(), 0.00001);
    }
    
    @Test
    public void testIntervalsSetTreeConstructor() {
        // Create a BSPTree for testing
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetBoundaryConstructor() {
        // Create a collection of subhyperplanes for testing
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        SubHyperplane<Euclidean1D> subHyperplane = new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane();
        boundary.add(subHyperplane);
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildNew() {
        // Create a BSPTree for testing
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        
        // Check if the new IntervalsSet is created correctly
        assertSame(tree, newIntervalsSet.getTree(false));
    }
    
    @Test
    public void testComputeGeometricalPropertiesEmptyTree() {
        // Create an empty tree
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.computeGeometricalProperties();
        
        // Check if the properties are set correctly for empty tree
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.00001);
    }
    
    @Test
    public void testComputeGeometricalPropertiesNonEmptyTree() {
        // Create a non-empty tree with two intervals
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        
        BSPTree<Euclidean1D> lowerTree = new BSPTree<Euclidean1D>(lowerCut, null, null, null);
        BSPTree<Euclidean1D> upperTree = new BSPTree<Euclidean1D>(upperCut, null, null, null);
        
        tree.insertInTree(lowerTree);
        tree.insertInTree(upperTree);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.computeGeometricalProperties();
        
        // Check if the properties are set correctly for non-empty tree
        assertEquals(2.0, intervalsSet.getSize(), 0.00001);
        assertEquals(0.5, intervalsSet.getBarycenter().getX(), 0.00001);
    }
    
    @Test
    public void testGetInf() {
        // Create a tree with two intervals
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        
        BSPTree<Euclidean1D> lowerTree = new BSPTree<Euclidean1D>(lowerCut, null, null, null);
        BSPTree<Euclidean1D> upperTree = new BSPTree<Euclidean1D>(upperCut, null, null, null);
        
        tree.insertInTree(lowerTree);
        tree.insertInTree(upperTree);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        // Check if the correct inf value is returned
        assertEquals(0.0, intervalsSet.getInf(), 0.00001);
    }
    
    @Test
    public void testGetSup() {
        // Create a tree with two intervals
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        
        BSPTree<Euclidean1D> lowerTree = new BSPTree<Euclidean1D>(lowerCut, null, null, null);
        BSPTree<Euclidean1D> upperTree = new BSPTree<Euclidean1D>(upperCut, null, null, null);
        
        tree.insertInTree(lowerTree);
        tree.insertInTree(upperTree);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        // Check if the correct sup value is returned
        assertEquals(1.0, intervalsSet.getSup(), 0.00001);
    }
    
    @Test
    public void testAsList() {
        // Create a tree with two intervals
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        
        BSPTree<Euclidean1D> lowerTree = new BSPTree<Euclidean1D>(lowerCut, null, null, null);
        BSPTree<Euclidean1D> upperTree = new BSPTree<Euclidean1D>(upperCut, null, null, null);
        
        tree.insertInTree(lowerTree);
        tree.insertInTree(upperTree);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        // Check if the correct list of intervals is returned
        assertEquals(2, intervalsSet.asList().size());
        
        Interval interval1 = intervalsSet.asList().get(0);
        assertEquals(0.0, interval1.getInf(), 0.00001);
        assertEquals(1.0, interval1.getSup(), 0.00001);
        assertEquals(1.0, interval1.getSize(), 0.00001);
        
        Interval interval2 = intervalsSet.asList().get(1);
        assertEquals(1.0, interval2.getInf(), 0.00001);
        assertEquals(Double.POSITIVE_INFINITY, interval2.getSup(), 0.00001);
        assertEquals(Double.NaN, interval2.getSize(), 0.00001);
    }
    
    @Test
    public void testRecurseListEmptyTree() {
        // Create an empty tree
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.asList();
        
        // Check if the list is empty
        assertEquals(0, intervalsSet.asList().size());
    }
    
    @Test
    public void testRecurseListNonEmptyTree() {
        // Create a non-empty tree with two intervals
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(0.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(1.0), true).wholeHyperplane();
        
        BSPTree<Euclidean1D> lowerTree = new BSPTree<Euclidean1D>(lowerCut, null, null, null);
        BSPTree<Euclidean1D> upperTree = new BSPTree<Euclidean1D>(upperCut, null, null, null);
        
        tree.insertInTree(lowerTree);
        tree.insertInTree(upperTree);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.asList();
        
        // Check if the intervals are correct
        assertEquals(1, intervalsSet.asList().size());
        
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(0.0, interval.getInf(), 0.00001);
        assertEquals(1.0, interval.getSup(), 0.00001);
        assertEquals(1.0, interval.getSize(), 0.00001);
    }
}