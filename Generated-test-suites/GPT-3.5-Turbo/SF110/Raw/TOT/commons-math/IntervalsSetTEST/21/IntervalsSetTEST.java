import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IntervalsSetTest {
    
    @Test
    public void testConstructor1() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testConstructor2() {
        double lower = 0.0;
        double upper = 10.0;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        assertEquals(lower, intervalsSet.getInf(), 0.01);
        assertEquals(upper, intervalsSet.getSup(), 0.01);
    }
    
    @Test
    public void testConstructor3() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        assertTrue(tree == intervalsSet.getTree(false));
    }
    
    @Test
    public void testConstructor4() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<SubHyperplane<Euclidean1D>>();
        
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        assertTrue(boundary == intervalsSet.getBoundaryLoops());
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<Euclidean1D>(Boolean.TRUE);
        
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(newIntervalsSet);
        assertTrue(tree == newIntervalsSet.getTree(false));
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(10.0, intervalsSet.getSize(), 0.01);
        assertEquals(new Vector1D(5.0), intervalsSet.getBarycenter());
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
        double inf = intervalsSet.getInf();
        
        assertEquals(0.0, inf, 0.01);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
        double sup = intervalsSet.getSup();
        
        assertEquals(10.0, sup, 0.01);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet(0.0, 10.0);
        List<Interval> intervalList = intervalsSet.asList();
        
        assertNotNull(intervalList);
        assertEquals(1, intervalList.size());
        assertEquals(0.0, intervalList.get(0).getInf(), 0.01);
        assertEquals(10.0, intervalList.get(0).getSup(), 0.01);
    }
}