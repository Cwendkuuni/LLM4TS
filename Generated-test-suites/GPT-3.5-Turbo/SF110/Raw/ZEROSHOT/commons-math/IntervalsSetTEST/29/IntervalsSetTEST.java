
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import java.util.*;

public class IntervalsSetTest {

    @Test
    public void testBuildTree() {
        // Test buildTree method
        
        // Test case 1: lower = Double.NEGATIVE_INFINITY, upper = Double.POSITIVE_INFINITY
        BSPTree<Euclidean1D> tree1 = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertTrue(tree1.getCut() == null);
        assertTrue(!(boolean) tree1.getAttribute());
        
        // Test case 2: lower = Double.NEGATIVE_INFINITY, upper = 0.0
        BSPTree<Euclidean1D> tree2 = IntervalsSet.buildTree(Double.NEGATIVE_INFINITY, 0.0);
        assertTrue(tree2.getCut() != null);
        assertTrue(tree2.getCut().getHyperplane() instanceof OrientedPoint);
        OrientedPoint op2 = (OrientedPoint) tree2.getCut().getHyperplane();
        assertTrue(op2.isDirect());
        assertTrue(op2.getLocation().getX() == 0.0);
        assertTrue(tree2.getMinus().getCut() == null);
        assertTrue(tree2.getPlus().getCut() == null);
        assertTrue(tree2.getPlus().getAttribute().equals(Boolean.TRUE));
        
        // Test case 3: lower = 0.0, upper = Double.POSITIVE_INFINITY
        BSPTree<Euclidean1D> tree3 = IntervalsSet.buildTree(0.0, Double.POSITIVE_INFINITY);
        assertTrue(tree3.getCut() != null);
        assertTrue(tree3.getCut().getHyperplane() instanceof OrientedPoint);
        OrientedPoint op3 = (OrientedPoint) tree3.getCut().getHyperplane();
        assertTrue(!op3.isDirect());
        assertTrue(op3.getLocation().getX() == 0.0);
        assertTrue(tree3.getMinus().getCut() == null);
        assertTrue(tree3.getPlus().getCut() == null);
        assertTrue(tree3.getMinus().getAttribute().equals(Boolean.TRUE));
    }

    @Test
    public void testConstructorAndGetters() {
        // Test constructor and getters
        
        // Test case 1: no arguments constructor
        IntervalsSet intervalsSet1 = new IntervalsSet();
        assertTrue(intervalsSet1.getTree(false).getCut() == null);
        
        // Test case 2: constructor with lower and upper arguments
        IntervalsSet intervalsSet2 = new IntervalsSet(-1.0, 1.0);
        assertTrue(intervalsSet2.getTree(false).getCut() != null);
        assertTrue(intervalsSet2.getSup() == 1.0);
        assertTrue(intervalsSet2.getInf() == -1.0);
        
        // Test case 3: constructor with tree argument
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet3 = new IntervalsSet(tree);
        assertTrue(intervalsSet3.getTree(true) == tree);
        
        // Test case 4: constructor with boundary argument
        Collection<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet intervalsSet4 = new IntervalsSet(boundary);
        assertTrue(intervalsSet4.getTree(true).getCut() == null);
    }

    @Test
    public void testBuildNew() {
        // Test buildNew method
        
        // Create a BSPTree for testing
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet();
        
        // Test buildNew method with a valid tree
        IntervalsSet newIntervalsSet = intervalsSet.buildNew(tree);
        assertTrue(newIntervalsSet.getTree(true) == tree);
    }

    @Test
    public void testComputeGeometricalProperties() {
        // Test computeGeometricalProperties method
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        intervalsSet.computeGeometricalProperties();
        
        // Check if the properties have been computed correctly
        
        // TODO: Add assertions based on the expected values
    }

    @Test
    public void testGetInf() {
        // Test getInf method
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        double inf = intervalsSet.getInf();

        // Check if the inf value is correct
        assertTrue(inf == -1.0);
    }

    @Test
    public void testGetSup() {
        // Test getSup method
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        double sup = intervalsSet.getSup();

        // Check if the sup value is correct
        assertTrue(sup == 1.0);
    }

    @Test
    public void testAsList() {
        // Test asList method
        IntervalsSet intervalsSet = new IntervalsSet(-1.0, 1.0);
        List<Interval> intervalList = intervalsSet.asList();

        // Check if the list size is correct
        assertTrue(intervalList.size() == 1);

        // Check the values of the interval in the list
        Interval interval = intervalList.get(0);
        assertTrue(interval.getInf() == -1.0);
        assertTrue(interval.getSup() == 1.0);
    }
}
