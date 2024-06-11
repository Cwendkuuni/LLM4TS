import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.Vector;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetNoParameters() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        
        // Check if the tree is empty
        assertNull(tree.getCut());
    }

    @Test
    public void testIntervalsSetWithLowerAndUpper() {
        double lower = -2.0;
        double upper = 2.0;

        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        
        // Check if the tree is not null
        assertNotNull(tree);
        
        // Check if the tree has two cuts
        assertNotNull(tree.getCut());
        assertNotNull(tree.getMinus().getCut());
        assertNotNull(tree.getPlus().getCut());
        
        // Check if the tree has the right attributes
        assertFalse(tree.getAttribute());
        assertTrue(tree.getMinus().getAttribute());
        assertTrue(tree.getPlus().getAttribute());
        
        // Check if the cuts location are correct
        Vector<Euclidean1D> minusLocation = tree.getMinus().getCut().getHyperplane().getLocation();
        Vector<Euclidean1D> plusLocation = tree.getPlus().getCut().getHyperplane().getLocation();

        assertEquals(lower, minusLocation.getX(), 1e-15);
        assertEquals(upper, plusLocation.getX(), 1e-15);
    }

    @Test
    public void testIntervalsSetWithEmptyBoundary() {
        IntervalsSet intervalsSet = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>());
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        
        // Check if the tree is empty
        assertNull(tree.getCut());
    }

    @Test
    public void testIntervalsSetWithBoundary() {
        double lower = -1.0;
        double upper = 1.0;
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector(upper), true).wholeHyperplane();
        ArrayList<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        boundary.add(upperCut);

        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        
        // Check if the tree is not null
        assertNotNull(tree);
        
        // Check if the tree has one cut
        assertNotNull(tree.getCut());
        
        // Check if the tree has the right attribute
        assertTrue(tree.getAttribute());
    }

    @Test
    public void testBuildNew() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        BSPTree<Euclidean1D> newTree = intervalsSet.getTree();
        
        // Check if the new intervals set has the same tree
        assertEquals(tree, newTree);
    }

    @Test
    public void testComputeGeometricalPropertiesWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        
        // Check if the size is 0
        assertEquals(0.0, intervalsSet.getSize(), 1e-15);
        
        // Check if the barycenter is NaN
        assertTrue(Double.isNaN(intervalsSet.getBarycenter().getX()));
    }

    // More test cases can be added

    @Test
    public void testGetInfWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        
        // Check if the inf value is negative infinity
        assertEquals(Double.NEGATIVE_INFINITY, inf, 1e-15);
    }

    @Test
    public void testGetSupWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        
        // Check if the sup value is positive infinity
        assertEquals(Double.POSITIVE_INFINITY, sup, 1e-15);
    }

    @Test
    public void testAsListWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervals = intervalsSet.asList();
        
        // Check if the intervals list is empty
        assertTrue(intervals.isEmpty());
    }

    // More test cases can be added

}