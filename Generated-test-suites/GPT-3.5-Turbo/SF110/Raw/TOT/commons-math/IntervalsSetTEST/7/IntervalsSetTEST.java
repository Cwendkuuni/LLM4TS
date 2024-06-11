import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.partitioning.*;
import java.util.*;

public class IntervalsSetTest {

    private static final double EPSILON = 1e-10;
    
    @Test
    public void testDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        
        assertEquals(Double.NEGATIVE_INFINITY, intervalsSet.getInf(), EPSILON);
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSup(), EPSILON);
        assertTrue(intervalsSet.asList().isEmpty());
    }
    
    @Test
    public void testConstructorWithLowerAndUpper() {
        double lower = -5;
        double upper = 5;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        
        assertEquals(lower, intervalsSet.getInf(), EPSILON);
        assertEquals(upper, intervalsSet.getSup(), EPSILON);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(lower, intervalsSet.asList().get(0).getInf(), EPSILON);
        assertEquals(upper, intervalsSet.asList().get(0).getSup(), EPSILON);
    }
    
    @Test
    public void testConstructorWithTree() {
        double lower = -5;
        double upper = 5;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        assertEquals(lower, intervalsSet.getInf(), EPSILON);
        assertEquals(upper, intervalsSet.getSup(), EPSILON);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(lower, intervalsSet.asList().get(0).getInf(), EPSILON);
        assertEquals(upper, intervalsSet.asList().get(0).getSup(), EPSILON);
    }
    
    @Test
    public void testConstructorWithBoundary() {
        double lower = -5;
        double upper = 5;
        SubHyperplane<Euclidean1D> boundary = new OrientedPoint(new Vector1D(lower), false).wholeHyperplane();
        List<SubHyperplane<Euclidean1D>> boundaryList = new ArrayList<>();
        boundaryList.add(boundary);
        
        IntervalsSet intervalsSet = new IntervalsSet(boundaryList);
        
        assertEquals(lower, intervalsSet.getInf(), EPSILON);
        assertEquals(upper, intervalsSet.getSup(), EPSILON);
        assertEquals(1, intervalsSet.asList().size());
        assertEquals(lower, intervalsSet.asList().get(0).getInf(), EPSILON);
        assertEquals(upper, intervalsSet.asList().get(0).getSup(), EPSILON);
    }
    
    @Test
    public void testBuildNew() {
        double lower = -5;
        double upper = 5;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        
        assertEquals(intervalsSet.getInf(), newIntervalsSet.getInf(), EPSILON);
        assertEquals(intervalsSet.getSup(), newIntervalsSet.getSup(), EPSILON);
        assertEquals(intervalsSet.asList().size(), newIntervalsSet.asList().size());
        for (int i = 0; i < intervalsSet.asList().size(); i++) {
            assertEquals(intervalsSet.asList().get(i).getInf(), newIntervalsSet.asList().get(i).getInf(), EPSILON);
            assertEquals(intervalsSet.asList().get(i).getSup(), newIntervalsSet.asList().get(i).getSup(), EPSILON);
        }
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        // Test when tree cut is null
        IntervalsSet intervalsSet = new IntervalsSet();
        
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), EPSILON);
        
        // Test when tree cut is not null
        double lower = -5;
        double upper = 5;
        IntervalsSet intervalsSet2 = new IntervalsSet(lower, upper);
        
        intervalsSet2.computeGeometricalProperties();
        
        double expectedSize = upper - lower;
        assertEquals(new Vector1D((lower + upper) / 2), intervalsSet2.getBarycenter());
        assertEquals(expectedSize, intervalsSet2.getSize(), EPSILON);
    }
    
    @Test
    public void testGetInf() {
        // Test when attribute is false
        double lower = -5;
        double upper = 5;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        double inf = intervalsSet.getInf();
        
        assertEquals(lower, inf, EPSILON);
        
        // Test when attribute is true
        BSPTree<Euclidean1D> tree2 = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet2 = new IntervalsSet(tree2);
        
        double inf2 = intervalsSet2.getInf();
        
        assertEquals(Double.NEGATIVE_INFINITY, inf2, EPSILON);
    }
    
    @Test
    public void testGetSup() {
        // Test when attribute is false
        double lower = -5;
        double upper = 5;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        double sup = intervalsSet.getSup();
        
        assertEquals(upper, sup, EPSILON);
        
        // Test when attribute is true
        BSPTree<Euclidean1D> tree2 = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet2 = new IntervalsSet(tree2);
        
        double sup2 = intervalsSet2.getSup();
        
        assertEquals(Double.POSITIVE_INFINITY, sup2, EPSILON);
    }
    
    @Test
    public void testAsList() {
        double lower = -5;
        double upper = 5;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        List<Interval> intervalList = intervalsSet.asList();
        
        assertEquals(1, intervalList.size());
        assertEquals(lower, intervalList.get(0).getInf(), EPSILON);
        assertEquals(upper, intervalList.get(0).getSup(), EPSILON);
    }
    
    @Test
    public void testRecurseList() {
        double lower = -5;
        double upper = 5;
        BSPTree<Euclidean1D> tree = IntervalsSet.buildTree(lower, upper);
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        List<Interval> intervalList = new ArrayList<>();
        intervalsSet.recurseList(tree, intervalList, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        assertEquals(lower, intervalList.get(0).getInf(), EPSILON);
        assertEquals(upper, intervalList.get(0).getSup(), EPSILON);
    }
}