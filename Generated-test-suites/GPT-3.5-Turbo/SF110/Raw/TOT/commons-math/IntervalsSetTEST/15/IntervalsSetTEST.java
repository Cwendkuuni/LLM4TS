import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.*;

public class IntervalsSetTest {

    @Test
    public void testEmptyConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        // Assert statements
    }
    
    @Test
    public void testLowerUpperConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet(-10.0, 10.0);
        // Assert statements
    }
    
    @Test
    public void testTreeConstructor() {
        // Create a test BSPTree<Euclidean1D> object
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        // Assert statements
    }
    
    @Test
    public void testBoundaryConstructor() {
        // Create a test Collection<SubHyperplane<Euclidean1D>> object
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        // Assert statements
    }
    
    @Test
    public void testBuildNew() {
        // Create a test BSPTree<Euclidean1D> object
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        // Assert statements
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        // Assert statements
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        // Assert statements
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        // Assert statements
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        // Assert statements
    }
}