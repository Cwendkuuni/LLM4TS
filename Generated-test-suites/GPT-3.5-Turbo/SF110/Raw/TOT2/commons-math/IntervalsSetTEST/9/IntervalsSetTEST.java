import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testBuildTree() {
        // Test building tree with lower and upper bounds
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        // Test building new interval set
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(new BSPTree<Euclidean1D>());
        assertNotNull(newIntervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test computing geometrical properties
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        intervalsSet.computeGeometricalProperties();
        assertNotNull(intervalsSet.getBarycenter());
        assertEquals(1.5, intervalsSet.getBarycenter().getX(), 0.001);
    }

    @Test
    public void testGetInf() {
        // Test getting lower bound of the interval set
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.001);
    }

    @Test
    public void testGetSup() {
        // Test getting upper bound of the interval set
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        assertEquals(2.0, intervalsSet.getSup(), 0.001);
    }

    @Test
    public void testAsList() {
        // Test converting interval set to list of intervals
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 2.0);
        List<Interval> intervalList = intervalsSet.asList();
        assertNotNull(intervalList);
        assertEquals(1, intervalList.size());
        assertEquals(1.0, intervalList.get(0).getInf(), 0.001);
        assertEquals(2.0, intervalList.get(0).getSup(), 0.001);
    }
}