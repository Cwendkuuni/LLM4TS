import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.util.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class IntervalsSetTest {
    
    @Test
    public void testIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetWithLimits() {
        double lower = 10.0;
        double upper = 20.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        assertNotNull(tree);
        
        double inf = intervalsSet.getInf();
        assertEquals(lower, inf, 0.0);
        
        double sup = intervalsSet.getSup();
        assertEquals(upper, sup, 0.0);
    }
    
    @Test
    public void testIntervalsSetWithTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        
        BSPTree<Euclidean1D> result = intervalsSet.getTree();
        assertNotNull(result);
        assertEquals(tree, result);
    }
    
    @Test
    public void testIntervalsSetWithBoundary() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        
        BSPTree<Euclidean1D> tree = intervalsSet.getTree();
        assertNotNull(tree);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        
        IntervalsSet result = intervalsSet.buildNew(tree);
        assertNotNull(result);
        assertEquals(tree, result.getTree());
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        intervalsSet.computeGeometricalProperties();
        // Assertions for properties
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        double inf = intervalsSet.getInf();
        // Assertions for inf
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        double sup = intervalsSet.getSup();
        // Assertions for sup
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        List<Interval> list = intervalsSet.asList();
        // Assertions for list
    }
    
    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> node = intervalsSet.getTree();
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        
        List<Interval> list = new ArrayList<Interval>();
        intervalsSet.recurseList(node, list, lower, upper);
        // Assertions for list
    }
}