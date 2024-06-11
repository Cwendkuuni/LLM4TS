import static org.junit.Assert.*;
import org.junit.Test;

public class IntervalsSetTest {
    
    @Test
    public void testIntervalsSetConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetConstructor2() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(newIntervalsSet);
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        intervalsSet.computeGeometricalProperties();
        // Add assertions based on the expected behavior
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        double inf = intervalsSet.getInf();
        // Add assertions based on the expected behavior
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        double sup = intervalsSet.getSup();
        // Add assertions based on the expected behavior
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(2.0, 5.0);
        List<Interval> list = intervalsSet.asList();
        // Add assertions based on the expected behavior
    }
    
}