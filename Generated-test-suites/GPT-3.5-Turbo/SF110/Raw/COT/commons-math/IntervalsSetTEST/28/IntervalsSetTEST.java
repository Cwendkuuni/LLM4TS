
import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.partitioning.*;
import java.util.*;

public class IntervalsSetTest {
    
    @Test
    public void testIntervalsSet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }
    
    @Test
    public void testIntervalsSetWithLowerAndUpper() {
        double lower = -1.0;
        double upper = 1.0;
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        assertEquals(lower, intervalsSet.getInf(), 0.0);
        assertEquals(upper, intervalsSet.getSup(), 0.0);
    }
    
    @Test
    public void testIntervalsSetWithBSTree() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        assertEquals(tree, intervalsSet.getTree(false));
    }
    
    @Test
    public void testIntervalsSetWithCollection() {
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet = new IntervalsSet(boundary);
        assertNotNull(intervalsSet);
        assertEquals(boundary, intervalsSet.getTree(false).getCut().getHyperplane());
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>();
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertNotNull(newIntervalsSet);
        assertEquals(tree, newIntervalsSet.getTree(false));
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), 0.0);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }
    
    @Test
    public void testGetInf() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        assertEquals(Double.POSITIVE_INFINITY, inf, 0.0);
    }
    
    @Test
    public void testGetSup() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        assertEquals(Double.NEGATIVE_INFINITY, sup, 0.0);
    }
    
    @Test
    public void testAsList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        assertNotNull(list);
    }
    
    @Test
    public void testRecurseList() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> node = new BSPTree<>();
        List<Interval> list = new ArrayList<>();
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet.recurseList(node, list, lower, upper);
        assertNotNull(list);
        assertEquals(0, list.size());
    }
    
}
