import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class IntervalsSetTest {
    
    private IntervalsSet intervalsSet;
    
    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();
    }
    
    @Test
    public void testBuildTree() {
        double lower = 1.0;
        double upper = 3.0;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        assertNotNull(tree);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(newIntervalsSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        intervalsSet.computeGeometricalProperties();
        // Add assertion based on expected behavior
    }
    
    @Test
    public void testGetInf() {
        double inf = intervalsSet.getInf();
        // Add assertion based on expected behavior
    }
    
    @Test
    public void testGetSup() {
        double sup = intervalsSet.getSup();
        // Add assertion based on expected behavior
    }
    
    @Test
    public void testAsList() {
        List<Interval> intervalList = intervalsSet.asList();
        assertNotNull(intervalList);
    }
}