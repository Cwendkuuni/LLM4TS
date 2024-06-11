import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetWithBoundsConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetWithTreeConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(new BSPTree<Euclidean1D>());
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetWithBoundaryConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(new ArrayList<SubHyperplane<Euclidean1D>>());
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(1.0, intervalsSet.getInf(), 0.0);
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(3.0, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(1.0, 3.0);
        assertEquals(1, intervalsSet.asList().size());
    }
}