import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    // Test constructor: IntervalsSet()
    // No input arguments, verify that the tree has no cuts
    @Test
    public void testConstructorNoArgs() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNull(intervalsSet.getTree(false).getCut());
    }

    // Test constructor: IntervalsSet(lower, upper)
    // Verify that lower and upper cuts are correctly constructed for finite values
    @Test
    public void testConstructorWithArgsFinite() {
        double lower = 3.0;
        double upper = 7.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertTrue(((OrientedPoint)intervalsSet.getTree(false).getCut().getHyperplane()).getLocation().getX() == lower);
        assertTrue(((OrientedPoint)intervalsSet.getTree(false).getPlus().getCut().getHyperplane()).getLocation().getX() == upper);
    }

    // Test constructor: IntervalsSet(lower, upper)
    // Verify that lower and upper cuts are correctly constructed for infinite values
    @Test
    public void testConstructorWithArgsInfinite() {
        double lower = Double.POSITIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet.getTree(false).getCut());
        assertTrue(((OrientedPoint)intervalsSet.getTree(false).getPlus().getCut().getHyperplane()).isDirect());
        assertTrue(((OrientedPoint)intervalsSet.getTree(false).getMinus().getCut().getHyperplane()).isDirect());
    }

    // Test constructor: IntervalsSet(tree)
    // Create a tree and verify that it is set correctly
    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertEquals(tree, intervalsSet.getTree(false));
    }

    // Test constructor: IntervalsSet(boundary)
    // Create a boundary and verify that it is set correctly
    @Test
    public void testConstructorWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertEquals(boundary, intervalsSet.getBoundaryLoops());
    }

    // Test method: buildNew(tree)
    // Create a tree and verify that the new instance of IntervalsSet has the same tree
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertEquals(tree, intervalsSet.getTree(false));
    }

    // Test method: computeGeometricalProperties()
    // Verify that the geometrical properties are computed correctly for a tree with no cut
    @Test
    public void testComputeGeometricalPropertiesNoCut() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        intervalsSet.computeGeometricalProperties();
        assertNull(intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);
    }

    // Test method: computeGeometricalProperties()
    // Verify that the geometrical properties are computed correctly for a tree with cuts
    @Test
    public void testComputeGeometricalPropertiesWithCuts() {
        double lower = 3.0;
        double upper = 7.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        assertNotNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.getSize() > 0.0);
    }

    // Test method: getInf()
    // Verify that the lower bound is computed correctly for a tree with cuts
    @Test
    public void testGetInfWithCuts() {
        double lower = 3.0;
        double upper = 7.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        double inf = intervalsSet.getInf();
        assertTrue(inf == lower);
    }

    // Test method: getInf()
    // Verify that the lower bound is computed correctly for a tree with no cuts
    @Test
    public void testGetInfNoCuts() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double inf = intervalsSet.getInf();
        assertTrue(Double.isInfinite(inf));
    }


    // Test method: getSup()
    // Verify that the upper bound is computed correctly for a tree with cuts
    @Test
    public void testGetSupWithCuts() {
        double lower = 3.0;
        double upper = 7.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        double sup = intervalsSet.getSup();
        assertTrue(sup == upper);
    }

    // Test method: getSup()
    // Verify that the upper bound is computed correctly for a tree with no cuts
    @Test
    public void testGetSupNoCuts() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        double sup = intervalsSet.getSup();
        assertTrue(Double.isInfinite(sup));
    }

    // Test method: asList()
    // Verify that the intervals are correctly listed for a tree with cuts
    @Test
    public void testAsListWithCuts() {
        double lower = 3.0;
        double upper = 7.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> intervalList = intervalsSet.asList();
        assertTrue(intervalList.size() == 1);
        Interval interval = intervalList.get(0);
        assertTrue(interval.getInf() == lower);
        assertTrue(interval.getSup() == upper);
    }

    // Test method: asList()
    // Verify that no intervals are listed for a tree with no cuts
    @Test
    public void testAsListNoCuts() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervalList = intervalsSet.asList();
        assertTrue(intervalList.size() == 0);
    }

    // Test method: recurseList(node, list, lower, upper)
    // Verify that correct intervals are added to the list for a tree with cuts
    @Test
    public void testRecurseListWithCuts() {
        double lower = 3.0;
        double upper = 7.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> intervalList = new ArrayList<Interval>();
        intervalsSet.recurseList(intervalsSet.getTree(false), intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(intervalList.size() == 1);
        Interval interval = intervalList.get(0);
        assertTrue(interval.getInf() == lower);
        assertTrue(interval.getSup() == upper);
    }

    // Test method: recurseList(node, list, lower, upper)
    // Verify that no intervals are added to the list for a tree with no cuts
    @Test
    public void testRecurseListNoCuts() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        List<Interval> intervalList = new ArrayList<Interval>();
        intervalsSet.recurseList(intervalsSet.getTree(false), intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(intervalList.size() == 0);
    }
}