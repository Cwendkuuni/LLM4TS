import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;

public class IntervalsSetTest {

    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(lower, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(upper, intervalsSet.asList().get(0).getSup(), 0.0);
        assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testConstructorWithBoundary() {
        SubHyperplane<Euclidean1D> boundary = new OrientedPoint(new Vector1D(0.0), true).wholeHyperplane();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet1 = new IntervalsSet(tree);
        IntervalsSet intervalsSet2 = intervalsSet1.buildNew(tree);
        assertSame(tree, intervalsSet2.getTree());
    }

    @Test
    public void testComputeGeometricalPropertiesWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertTrue(intervalsSet.getBarycenter().isNaN());
    }

    @Test
    public void testComputeGeometricalPropertiesWithNonEmptyTree() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        assertEquals(upper - lower, intervalsSet.getSize(), 0.0);
        assertEquals((upper + lower) / 2, intervalsSet.getBarycenter().getX(), 0.0);
    }

    @Test
    public void testGetInfWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetInfWithNonEmptyTree() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertEquals(lower, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSupWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testGetSupWithNonEmptyTree() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsListWithEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testAsListWithNonEmptySet() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(lower, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(upper, intervalsSet.asList().get(0).getSup(), 0.0);
    }

    @Test
    public void testRecurseListWithEmptyTree() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.recurseList(intervalsSet.getTree(), intervalsSet.asList(), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(intervalsSet.asList().isEmpty());
    }

    @Test
    public void testRecurseListWithNonEmptyTree() {
        double lower = -5.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.recurseList(intervalsSet.getTree(), intervalsSet.asList(), lower, upper);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(lower, intervalsSet.asList().get(0).getInf(), 0.0);
        assertEquals(upper, intervalsSet.asList().get(0).getSup(), 0.0);
    }
}