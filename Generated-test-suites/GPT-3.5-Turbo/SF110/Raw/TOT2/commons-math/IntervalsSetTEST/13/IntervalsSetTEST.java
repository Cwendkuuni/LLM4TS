import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import java.util.Collection;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetDefaultConstructor() {
        IntervalsSet interval = new IntervalsSet();
        assertNotNull(interval);
    }

    @Test
    public void testIntervalsSetConstructorWithBounds() {
        IntervalsSet interval = new IntervalsSet(1.0, 5.0);
        assertNotNull(interval);
        assertEquals(1.0, interval.getInf(), 0.0);
        assertEquals(5.0, interval.getSup(), 0.0);
    }

    @Test
    public void testIntervalsSetConstructorWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet interval = new IntervalsSet(tree);
        assertNotNull(interval);
    }

    @Test
    public void testIntervalsSetConstructorWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet interval = new IntervalsSet(boundary);
        assertNotNull(interval);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet interval = new IntervalsSet();
        IntervalsSet newInterval = interval.buildNew(tree);
        assertNotNull(newInterval);
    }

    // Additional test cases for other public methods can be added here

}