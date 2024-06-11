
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;

    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.001);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.001);
        assertEquals(0, intervalsSet.asList().size());
    }

    @Test
    public void testConstructorWithBounds() {
        double lower = -2.5;
        double upper = 3.8;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);

        assertEquals(lower, intervalsSet.getInf(), 0.001);
        assertEquals(upper, intervalsSet.getSup(), 0.001);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(lower, intervalsSet.asList().get(0).getInf(), 0.001);
        assertEquals(upper, intervalsSet.asList().get(0).getSup(), 0.001);
    }

    @Test
    public void testConstructorWithTree() {
        // Test by using a dummy BSPTree<Euclidean1D> object
        BSPTree<Euclidean1D> dummyTree = new BSPTree<>(null);
        IntervalsSet intervalsSet = new IntervalsSet(dummyTree);

        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.001);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.001);
        assertEquals(0, intervalsSet.asList().size());
    }

    @Test
    public void testConstructorWithBoundary() {
        // Test by using a dummy Collection<SubHyperplane<Euclidean1D>> object
        Collection<SubHyperplane<Euclidean1D>> dummyBoundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(dummyBoundary);

        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.001);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.001);
        assertEquals(0, intervalsSet.asList().size());
    }

    @Test
    public void testBuildNew() {
        // Test by using a dummy BSPTree<Euclidean1D> object
        BSPTree<Euclidean1D> dummyTree = new BSPTree<>(null);
        IntervalsSet intervalsSet = intervalsSet.buildNew(dummyTree);

        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.001);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.001);
        assertEquals(0, intervalsSet.asList().size());
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test with an empty IntervalsSet
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.001);
        assertTrue(intervalsSet.getBarycenter().isNaN());

        // Test with an IntervalsSet containing a single interval
        intervalsSet = new IntervalsSet(-1.5, 3.7);
        intervalsSet.computeGeometricalProperties();
        assertEquals(5.2, intervalsSet.getSize(), 0.001);
        assertEquals(1.1, intervalsSet.getBarycenter().getX(), 0.001);

        // Test with an IntervalsSet containing infinite intervals
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.001);
        assertTrue(intervalsSet.getBarycenter().isNaN());
    }

    @Test
    public void testGetInf() {
        // Test with an empty IntervalsSet
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.001);

        // Test with an IntervalsSet containing a single interval
        intervalsSet = new IntervalsSet(-3.5, 2.9);
        assertEquals(-3.5, intervalsSet.getInf(), 0.001);

        // Test with an IntervalsSet containing infinite intervals
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.001);
    }

    @Test
    public void testGetSup() {
        // Test with an empty IntervalsSet
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.001);

        // Test with an IntervalsSet containing a single interval
        intervalsSet = new IntervalsSet(-3.5, 2.9);
        assertEquals(2.9, intervalsSet.getSup(), 0.001);

        // Test with an IntervalsSet containing infinite intervals
        intervalsSet = new IntervalsSet(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.001);
    }

    @Test
    public void testAsList() {
        // Test with an empty IntervalsSet
        assertTrue(intervalsSet.asList().isEmpty());

        // Test with an IntervalsSet containing a single interval
        intervalsSet = new IntervalsSet(-1.0, 3.5);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(-1.0, intervalsSet.asList().get(0).getInf(), 0.001);
        assertEquals(3.5, intervalsSet.asList().get(0).getSup(), 0.001);

        // Test with an IntervalsSet containing multiple intervals
        intervalsSet = new IntervalsSet(-1.0, 3.5);
        intervalsSet.getRoot().getMinus().insertCut(new OrientedPoint(new Vector1D(2.0), true));
        intervalsSet.computeGeometricalProperties();
        assertEquals(2, intervalsSet.asList().size());
        assertEquals(-1.0, intervalsSet.asList().get(0).getInf(), 0.001);
        assertEquals(2.0, intervalsSet.asList().get(0).getSup(), 0.001);
        assertEquals(2.0, intervalsSet.asList().get(1).getInf(), 0.001);
        assertEquals(3.5, intervalsSet.asList().get(1).getSup(), 0.001);
    }

    @Test
    public void testRecurseList() {
        // Test with an empty BSPTree<Euclidean1D>
        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(new BSPTree<>(null), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(list.isEmpty());

        // Test with a non-empty BSPTree<Euclidean1D>
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.FALSE);
        tree.insertCut(new OrientedPoint(new Vector1D(-2.0), false));
        tree.insertCut(new OrientedPoint(new Vector1D(1.0), true));
        tree.insertCut(new OrientedPoint(new Vector1D(3.5), true));

        intervalsSet.recurseList(tree, list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(2, list.size());
        assertEquals(Double.NEGATIVE_INFINITY, list.get(0).getInf(), 0.001);
        assertEquals(-2.0, list.get(0).getSup(), 0.001);
        assertEquals(-2.0, list.get(1).getInf(), 0.001);
        assertEquals(Double.POSITIVE_INFINITY, list.get(1).getSup(), 0.001);
    }
}
