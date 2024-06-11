import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class IntervalsSetTest {

    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testBuildTree() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 5.0);
        assertNotNull(tree);
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(1.0, 5.0);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // Add assertion based on expected behavior
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        // Add assertion based on expected behavior
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        // Add assertion based on expected behavior
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> intervalList = intervalsSet.asList();
        assertNotNull(intervalList);
    }
}