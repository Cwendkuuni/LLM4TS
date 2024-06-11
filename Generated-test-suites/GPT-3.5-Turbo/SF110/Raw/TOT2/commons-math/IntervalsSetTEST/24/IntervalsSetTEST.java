import org.junit.Test;
import static org.junit.Assert.*;

public class IntervalsSetTest {

    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>();
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        intervalsSet.computeGeometricalProperties();
        assertNotNull(intervalsSet.getBarycenter());
        assertTrue(intervalsSet.getSize() == 1.0);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        double inf = intervalsSet.getInf();
        assertEquals(0.0, inf, 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 1.0);
        double sup = intervalsSet.getSup();
        assertEquals(1.0, sup, 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        List<Interval> intervalList = intervalsSet.asList();
        assertEquals(2, intervalList.size());
    }
}