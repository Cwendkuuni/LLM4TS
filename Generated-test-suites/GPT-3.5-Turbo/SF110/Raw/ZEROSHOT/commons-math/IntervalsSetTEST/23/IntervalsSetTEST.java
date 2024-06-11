import org.junit.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.* ;
import org.apache.commons.math3.geometry.partitioning.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testBuildTree() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        BSPTree<Euclidean1D> result = IntervalsSet.buildTree(lower, upper);
        Assert.assertNotNull(result);
        // add more assertions here
    }

    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet result = new IntervalsSet().buildNew(tree);
        Assert.assertNotNull(result);
        // add more assertions here
    }

    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // add assertions here
    }

    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double result = intervalsSet.getInf();
        // add assertions here
    }

    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double result = intervalsSet.getSup();
        // add assertions here
    }

    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> result = intervalsSet.asList();
        Assert.assertNotNull(result);
        // add more assertions here
    }
}