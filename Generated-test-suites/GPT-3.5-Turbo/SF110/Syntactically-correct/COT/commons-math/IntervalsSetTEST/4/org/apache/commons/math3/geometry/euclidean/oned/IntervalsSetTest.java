import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(0, intervalsSet.asList().size());
    }

    @Test
    public void testConstructorWithLimits() {
        double lower = -1.0;
        double upper = 1.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(1, intervalsSet.asList().size());

        lower = Double.NEGATIVE_INFINITY;
        upper = Double.POSITIVE_INFINITY;
        intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(1, intervalsSet.asList().size());
    }

    @Test
    public void testConstructorWithBoundary() {
        // Test with boundary
        double lower = -1.0;
        double upper = 1.0;
        Interval interval = new Interval(lower, upper);
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        boundary.add(interval.wholeHyperplane());
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(1, intervalsSet.asList().size());
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
        assertSame(tree, intervalsSet.getTree());
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test with empty tree
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertTrue(intervalsSet.getBarycenter().isNaN());

        // Test with non-empty tree
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        assertEquals(0.0, intervalsSet.getBarycenter().getX(), 0.0);
    }

    @Test
    public void testGetInf() {
        // Test with empty tree
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);

        // Test with non-empty tree
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        inf = intervalsSet.getInf();
        assertEquals(lower, inf, 0.0);
    }

    @Test
    public void testGetSup() {
        // Test with empty tree
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);

        // Test with non-empty tree
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        sup = intervalsSet.getSup();
        assertEquals(upper, sup, 0.0);
    }

    @Test
    public void testAsList() {
        // Test with empty tree
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        assertNotNull(list);
        assertEquals(0, list.size());

        // Test with non-empty tree
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        list = intervalsSet.asList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(lower, list.get(0).getInf(), 0.0);
        assertEquals(upper, list.get(0).getSup(), 0.0);
    }
}

