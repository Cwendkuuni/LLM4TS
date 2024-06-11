import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    private IntervalsSet intervalsSet;

    @Test
    public void testConstructor() {
        intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructorWithBounds() {
        intervalsSet = new IntervalsSet(1.0, 2.0);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testConstructorWithBoundary() {
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // Add assertions to test the functionality
    }

    @Test
    public void testGetInf() {
        intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        // Add assertions to test the functionality
    }

    @Test
    public void testGetSup() {
        intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        // Add assertions to test the functionality
    }

    @Test
    public void testAsList() {
        intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        // Add assertions to test the functionality
    }
}

