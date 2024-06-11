import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {

    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructorWithLowerAndUpper() {
        double lower = 1.0;
        double upper = 5.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructorWithBoundary() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(0.0, intervalsSet.getSize(), 0.0);

        double lower = 1.0;
        double upper = 5.0;
        intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(4.0, intervalsSet.getSize(), 0.0);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.0);

        double lower = 1.0;
        double upper = 5.0;
        intervalsSet = new IntervalsSet(lower, upper);
        inf = intervalsSet.getInf();
        assertEquals(lower, inf, 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.0);

        double lower = 1.0;
        double upper = 5.0;
        intervalsSet = new IntervalsSet(lower, upper);
        sup = intervalsSet.getSup();
        assertEquals(upper, sup, 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        assertNotNull(list);
        assertEquals(0, list.size());

        double lower = 1.0;
        double upper = 5.0;
        intervalsSet = new IntervalsSet(lower, upper);
        list = intervalsSet.asList();
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals(new Interval(lower, upper), list.get(0));
    }

    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = new ArrayList<Interval>();
        intervalsSet.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(0, list.size());

        double lower = 1.0;
        double upper = 5.0;
        intervalsSet = new IntervalsSet(lower, upper);
        list = new ArrayList<Interval>();
        intervalsSet.recurseList(intervalsSet.getTree(false), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertEquals(1, list.size());
        assertEquals(new Interval(lower, upper), list.get(0));
    }
}