import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;

public class IntervalsSetTest {
    
    private IntervalsSet intervalsSet;
    
    @Before
    public void setUp() {
        intervalsSet = new IntervalsSet();
    }
    
    @Test
    public void testIntervalsSetEmptyConstructor() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        assertTrue(tree.getCut() == null);
        assertTrue(tree.getAttribute() == Boolean.FALSE);
    }
    
    @Test
    public void testIntervalsSetConstructorWithLowerUpper() {
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        
        assertEquals(tree.getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getCut().getSize(), 1);
        assertEquals(tree.getAttribute(), Boolean.FALSE);
        assertEquals(tree.getPlus().getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getPlus().getCut().getSize(), 0);
        assertEquals(tree.getPlus().getAttribute(), Boolean.TRUE);
        assertNull(tree.getPlus().getPlus());
        assertNull(tree.getPlus().getMinus());
        
        OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), lower, 0);
        assertFalse(op.isDirect());
        
        op = (OrientedPoint) tree.getPlus().getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), upper, 0);
        assertTrue(op.isDirect());
    }
    
    @Test
    public void testIntervalsSetConstructorWithTree() {
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        IntervalsSet newIntervalsSet = new IntervalsSet(tree);
        
        assertEquals(tree.getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getCut().getSize(), 1);
        assertEquals(tree.getAttribute(), Boolean.FALSE);
        assertEquals(tree.getPlus().getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getPlus().getCut().getSize(), 0);
        assertEquals(tree.getPlus().getAttribute(), Boolean.TRUE);
        assertNull(tree.getPlus().getPlus());
        assertNull(tree.getPlus().getMinus());
        
        OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), lower, 0);
        assertFalse(op.isDirect());
        
        op = (OrientedPoint) tree.getPlus().getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), upper, 0);
        assertTrue(op.isDirect());
        
        tree = newIntervalsSet.getTree(false);
        assertNotNull(tree);
    }
    
    @Test
    public void testIntervalsSetConstructorWithBoundary() {
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        Collection<SubHyperplane<Euclidean1D>> boundary = tree.getBoundary();
        IntervalsSet newIntervalsSet = new IntervalsSet(boundary);
        
        assertEquals(tree.getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getCut().getSize(), 1);
        assertEquals(tree.getAttribute(), Boolean.FALSE);
        assertEquals(tree.getPlus().getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getPlus().getCut().getSize(), 0);
        assertEquals(tree.getPlus().getAttribute(), Boolean.TRUE);
        assertNull(tree.getPlus().getPlus());
        assertNull(tree.getPlus().getMinus());
        
        OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), lower, 0);
        assertFalse(op.isDirect());
        
        op = (OrientedPoint) tree.getPlus().getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), upper, 0);
        assertTrue(op.isDirect());
        
        tree = newIntervalsSet.getTree(false);
        assertNotNull(tree);
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        
        assertEquals(tree, newIntervalsSet.getTree(false));
    }
    
    @Test
    public void testComputeGeometricalPropertiesNoCut() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        intervalsSet.computeGeometricalProperties();
        
        assertTrue(tree.getCut() == null);
        assertEquals(intervalsSet.getBarycenter(), Vector1D.NaN);
        assertEquals(intervalsSet.getSize(), 0.0, 0);
    }
    
    @Test
    public void testComputeGeometricalPropertiesWithCut() {
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(intervalsSet.getSize(), 2.0, 0);
        assertFalse(intervalsSet.getBarycenter().isNaN());
        
        lower = -1.0;
        upper = Double.POSITIVE_INFINITY;
        intervalsSet = new IntervalsSet(lower, upper);
        tree = intervalsSet.getTree(false);
        intervalsSet.computeGeometricalProperties();
        
        assertEquals(intervalsSet.getSize(), Double.POSITIVE_INFINITY, 0);
        assertTrue(intervalsSet.getBarycenter().isNaN());
    }
    
    @Test
    public void testGetInfNoCut() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        double inf = intervalsSet.getInf();
        
        assertTrue(tree.getCut() == null);
        assertFalse(tree.getAttribute());
        assertEquals(inf, Double.NEGATIVE_INFINITY, 0);
    }
    
    @Test
    public void testGetInfWithCut() {
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        double inf = intervalsSet.getInf();
        
        assertEquals(tree.getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getCut().getSize(), 1);
        assertEquals(tree.getAttribute(), Boolean.FALSE);
        assertEquals(tree.getPlus().getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getPlus().getCut().getSize(), 0);
        assertEquals(tree.getPlus().getAttribute(), Boolean.TRUE);
        assertNull(tree.getPlus().getPlus());
        assertNull(tree.getPlus().getMinus());
        
        OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), lower, 0);
        assertFalse(op.isDirect());
        
        op = (OrientedPoint) tree.getPlus().getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), upper, 0);
        assertTrue(op.isDirect());
        
        assertEquals(inf, lower, 0);
    }
    
    @Test
    public void testGetSupNoCut() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        double sup = intervalsSet.getSup();
        
        assertTrue(tree.getCut() == null);
        assertFalse(tree.getAttribute());
        assertEquals(sup, Double.POSITIVE_INFINITY, 0);
    }
    
    @Test
    public void testGetSupWithCut() {
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        double sup = intervalsSet.getSup();
        
        assertEquals(tree.getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getCut().getSize(), 1);
        assertEquals(tree.getAttribute(), Boolean.FALSE);
        assertEquals(tree.getPlus().getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getPlus().getCut().getSize(), 0);
        assertEquals(tree.getPlus().getAttribute(), Boolean.TRUE);
        assertNull(tree.getPlus().getPlus());
        assertNull(tree.getPlus().getMinus());
        
        OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), lower, 0);
        assertFalse(op.isDirect());
        
        op = (OrientedPoint) tree.getPlus().getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), upper, 0);
        assertTrue(op.isDirect());
        
        assertEquals(sup, upper, 0);
    }
    
    @Test
    public void testAsListEmpty() {
        List<Interval> list = intervalsSet.asList();
        
        assertEquals(list.size(), 0);
    }
    
    @Test
    public void testAsListNotEmpty() {
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        List<Interval> list = intervalsSet.asList();
        
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getInf(), lower, 0);
        assertEquals(list.get(0).getSup(), upper, 0);
    }
    
    @Test
    public void testRecurseListNoCut() {
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        List<Interval> list = new ArrayList<Interval>();
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        intervalsSet.recurseList(tree, list, lower, upper);
        
        assertTrue(tree.getCut() == null);
        assertFalse(tree.getAttribute());
        assertEquals(list.size(), 0);
    }
    
    @Test
    public void testRecurseListWithCut() {
        double lower = -1.0;
        double upper = 1.0;
        intervalsSet = new IntervalsSet(lower, upper);
        BSPTree<Euclidean1D> tree = intervalsSet.getTree(false);
        List<Interval> list = new ArrayList<Interval>();
        intervalsSet.recurseList(tree, list, lower, upper);
        
        assertEquals(tree.getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getCut().getSize(), 1);
        assertEquals(tree.getAttribute(), Boolean.FALSE);
        assertEquals(tree.getPlus().getCut().getHyperplane().getClass(), OrientedPoint.class);
        assertEquals(tree.getPlus().getCut().getSize(), 0);
        assertEquals(tree.getPlus().getAttribute(), Boolean.TRUE);
        assertNull(tree.getPlus().getPlus());
        assertNull(tree.getPlus().getMinus());
        
        OrientedPoint op = (OrientedPoint) tree.getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), lower, 0);
        assertFalse(op.isDirect());
        
        op = (OrientedPoint) tree.getPlus().getCut().getHyperplane();
        assertEquals(op.getLocation().getX(), upper, 0);
        assertTrue(op.isDirect());
        
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getInf(), lower, 0);
        assertEquals(list.get(0).getSup(), upper, 0);
    }
}