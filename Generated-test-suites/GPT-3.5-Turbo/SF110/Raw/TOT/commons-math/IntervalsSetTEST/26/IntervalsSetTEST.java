import org.junit.*;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;

public class IntervalsSetTest {

    @Test
    public void testIntervalsSetDefaultConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        assertNotNull(intervalsSet);
    }

    @Test
    public void testIntervalsSetConstructorWithLowerAndUpper() {
        double lower = 0.0;
        double upper = 10.0;
        
        IntervalsSet intervalsSet = new IntervalsSet(lower, upper);
        assertNotNull(intervalsSet);
        
        double inf = intervalsSet.getInf();
        double sup = intervalsSet.getSup();
        assertEquals(lower, inf, 0.01);
        assertEquals(upper, sup, 0.01);
    }

    @Test
    public void testIntervalsSetConstructorWithTree() {
        BSPTree<Euclidean1D> tree = buildTestTree();
        
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        assertNotNull(intervalsSet);
        
        double inf = intervalsSet.getInf();
        double sup = intervalsSet.getSup();
        assertEquals(-5.0, inf, 0.01);
        assertEquals(5.0, sup, 0.01);
    }
    
    @Test
    public void testIntervalsSetConstructorWithBoundary() {
        Interval interval1 = new Interval(-10.0, -5.0);
        Interval interval2 = new Interval(5.0, 10.0);
        
        SubHyperplane<Euclidean1D> subHyperplane1 = new OrientedPoint(new Vector1D(-5.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subHyperplane2 = new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane();

        interval1.reuseSubHyperplane(subHyperplane1);
        interval2.reuseSubHyperplane(subHyperplane2);

        IntervalsSet intervalsSet = new IntervalsSet(Arrays.asList(subHyperplane1, subHyperplane2));
        assertNotNull(intervalsSet);
        
        double inf = intervalsSet.getInf();
        double sup = intervalsSet.getSup();
        assertEquals(-10.0, inf, 0.01);
        assertEquals(10.0, sup, 0.01);
    }

    private BSPTree<Euclidean1D> buildTestTree() {
        SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(-5.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(5.0), true).wholeHyperplane();
        
        return new BSPTree<>(lowerCut, new BSPTree<>(Boolean.FALSE), new BSPTree<>(upperCut, new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null), null);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = buildTestTree();
        
        IntervalsSet intervalsSet = new IntervalsSet();
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        
        assertNotNull(newIntervalsSet);
        assertNotSame(intervalsSet, newIntervalsSet);
        assertEquals(tree, newIntervalsSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalPropertiesWithEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        
        double size = intervalsSet.getSize();
        Vector1D barycenter = intervalsSet.getBarycenter();
        
        assertEquals(0.0, size, 0.01);
        assertEquals(Vector1D.NaN, barycenter);
    }
    
    @Test
    public void testGetInfWithEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double inf = intervalsSet.getInf();
        
        assertEquals(Double.NEGATIVE_INFINITY, inf, 0.01);
    }
    
    @Test
    public void testGetSupWithEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        double sup = intervalsSet.getSup();
        
        assertEquals(Double.POSITIVE_INFINITY, sup, 0.01);
    }
    
    @Test
    public void testAsListWithEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = intervalsSet.asList();
        
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testGetInfWithNonEmptySet() {
        BSPTree<Euclidean1D> tree = buildTestTree();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        double inf = intervalsSet.getInf();
        
        assertEquals(-5.0, inf, 0.01);
    }
    
    @Test
    public void testGetSupWithNonEmptySet() {
        BSPTree<Euclidean1D> tree = buildTestTree();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        double sup = intervalsSet.getSup();
        
        assertEquals(5.0, sup, 0.01);
    }
    
    @Test
    public void testAsListWithNonEmptySet() {
        BSPTree<Euclidean1D> tree = buildTestTree();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        List<Interval> list = intervalsSet.asList();
        
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
        assertEquals(-5.0, list.get(0).getInf(), 0.01);
        assertEquals(-5.0, list.get(0).getSup(), 0.01);
        assertEquals(5.0, list.get(1).getInf(), 0.01);
        assertEquals(5.0, list.get(1).getSup(), 0.01);
    }

    @Test
    public void testRecurseListWithEmptySet() {
        IntervalsSet intervalsSet = new IntervalsSet();
        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(list.isEmpty());
    }
    
    @Test
    public void testRecurseListWithNonEmptySet() {
        BSPTree<Euclidean1D> tree = buildTestTree();
        IntervalsSet intervalsSet = new IntervalsSet(tree);
        
        List<Interval> list = new ArrayList<>();
        intervalsSet.recurseList(intervalsSet.getTree(), list, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertEquals(2, list.size());
        assertEquals(-5.0, list.get(0).getInf(), 0.01);
        assertEquals(-5.0, list.get(0).getSup(), 0.01);
        assertEquals(5.0, list.get(1).getInf(), 0.01);
        assertEquals(5.0, list.get(1).getSup(), 0.01);
    }

}