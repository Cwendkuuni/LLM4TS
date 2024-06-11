import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.geometry.euclidean.oned.*;

public class IntervalsSetTest {
    
    @Test
    public void testConstructor() {
        IntervalsSet intervalsSet = new IntervalsSet();
        BSPTree<Euclidean1D> expectedTree = new BSPTree<>(Boolean.TRUE);
        assertEquals(expectedTree, intervalsSet.getTree());
        
        intervalsSet = new IntervalsSet(1.0, 2.0);
        expectedTree = buildTree(1.0, 2.0);
        assertEquals(expectedTree, intervalsSet.getTree());
        
        List<SubHyperplane<Euclidean1D>> boundary = new ArrayList<>();
        IntervalsSet boundaryIntervalsSet = new IntervalsSet(3.0, 4.0);
        boundary.add(boundaryIntervalsSet);
        intervalsSet = new IntervalsSet(boundary);
        expectedTree = new BSPTree<>(boundaryIntervalsSet.getCut(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        assertEquals(expectedTree, intervalsSet.getTree());
    }
    
    @Test
    public void testBuildTree() {
        double lower = Double.NEGATIVE_INFINITY;
        double upper = Double.POSITIVE_INFINITY;
        BSPTree<Euclidean1D> expectedTree = new BSPTree<>(Boolean.TRUE);
        assertEquals(expectedTree, IntervalsSet.buildTree(lower, upper));
        
        lower = 1.0;
        upper = Double.POSITIVE_INFINITY;
        expectedTree = buildTree(lower, upper);
        assertEquals(expectedTree, IntervalsSet.buildTree(lower, upper));
        
        lower = Double.NEGATIVE_INFINITY;
        upper = 2.0;
        expectedTree = new BSPTree<>(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        assertEquals(expectedTree, IntervalsSet.buildTree(lower, upper));
        
        lower = 1.0;
        upper = 2.0;
        expectedTree = buildTree(lower, upper);
        assertEquals(expectedTree, IntervalsSet.buildTree(lower, upper));
    }
    
    @Test
    public void testBuildNew() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        assertEquals(tree, intervalsSet.getTree());
    }
    
    @Test
    public void testComputeGeometricalProperties() {
        IntervalsSet intervalsSet = new IntervalsSet();
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), Precision.SAFE_MIN);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
        
        double lower = 1.0;
        double upper = 2.0;
        intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        double size = upper - lower;
        double barycenter = (upper + lower) / 2.0;
        assertEquals(size, intervalsSet.getSize(), Precision.SAFE_MIN);
        assertEquals(new Vector1D(barycenter), intervalsSet.getBarycenter());
        
        lower = Double.NEGATIVE_INFINITY;
        upper = 2.0;
        intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        assertEquals(size, intervalsSet.getSize(), Precision.SAFE_MIN);
        assertEquals(new Vector1D(barycenter), intervalsSet.getBarycenter());
        
        lower = 1.0;
        upper = Double.POSITIVE_INFINITY;
        intervalsSet = new IntervalsSet(lower, upper);
        intervalsSet.computeGeometricalProperties();
        assertEquals(Double.POSITIVE_INFINITY, intervalsSet.getSize(), Precision.SAFE_MIN);
        assertEquals(Vector1D.NaN, intervalsSet.getBarycenter());
    }
    
    @Test
    public void testGetInf() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        double expectedInf = Double.NEGATIVE_INFINITY;
        assertEquals(expectedInf, intervalsSet.getInf(), Precision.SAFE_MIN);
        
        tree = buildTree(1.0, Double.POSITIVE_INFINITY);
        intervalsSet = new IntervalsSet().buildNew(tree);
        expectedInf = 1.0;
        assertEquals(expectedInf, intervalsSet.getInf(), Precision.SAFE_MIN);
        
        tree = new BSPTree<>(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null);
        intervalsSet = new IntervalsSet().buildNew(tree);
        expectedInf = Double.POSITIVE_INFINITY;
        assertEquals(expectedInf, intervalsSet.getInf(), Precision.SAFE_MIN);
    }
    
    @Test
    public void testGetSup() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        double expectedSup = Double.POSITIVE_INFINITY;
        assertEquals(expectedSup, intervalsSet.getSup(), Precision.SAFE_MIN);
        
        tree = buildTree(Double.NEGATIVE_INFINITY, 2.0);
        intervalsSet = new IntervalsSet().buildNew(tree);
        expectedSup = 2.0;
        assertEquals(expectedSup, intervalsSet.getSup(), Precision.SAFE_MIN);
        
        tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null), null);
        intervalsSet = new IntervalsSet().buildNew(tree);
        expectedSup = Double.POSITIVE_INFINITY;
        assertEquals(expectedSup, intervalsSet.getSup(), Precision.SAFE_MIN);
    }
    
    @Test
    public void testAsList() {
        BSPTree<Euclidean1D> tree = new BSPTree<>(Boolean.TRUE);
        IntervalsSet intervalsSet = new IntervalsSet().buildNew(tree);
        List<Interval> expectedList = new ArrayList<>();
        expectedList.add(new Interval(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(expectedList, intervalsSet.asList());
        
        tree = buildTree(1.0, 2.0);
        intervalsSet = new IntervalsSet().buildNew(tree);
        expectedList = new ArrayList<>();
        expectedList.add(new Interval(1.0, 2.0));
        assertEquals(expectedList, intervalsSet.asList());
        
        tree = buildTree(Double.NEGATIVE_INFINITY, 2.0);
        intervalsSet = new IntervalsSet().buildNew(tree);
        expectedList = new ArrayList<>();
        expectedList.add(new Interval(Double.NEGATIVE_INFINITY, 2.0));
        assertEquals(expectedList, intervalsSet.asList());
        
        tree = new BSPTree<>(new OrientedPoint(new Vector1D(1.0), false).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(new OrientedPoint(new Vector1D(2.0), true).wholeHyperplane(), new BSPTree<>(Boolean.FALSE), new BSPTree<>(Boolean.TRUE), null), null);
        intervalsSet = new IntervalsSet().buildNew(tree);
        expectedList = new ArrayList<>();
        expectedList.add(new Interval(1.0, Double.POSITIVE_INFINITY));
        assertEquals(expectedList, intervalsSet.asList());
    }
    
    private static BSPTree<Euclidean1D> buildTree(final double lower, final double upper) {
        if (Double.isInfinite(lower) && lower < 0.0) {
            if (Double.isInfinite(upper) && upper > 0.0) {
                return new BSPTree<Euclidean1D>(Boolean.TRUE);
            }
            final SubHyperplane<Euclidean1D> upperCut = new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
            return new BSPTree<Euclidean1D>(upperCut, new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null);
        }
        else {
            final SubHyperplane<Euclidean1D> lowerCut = new OrientedPoint(new Vector1D(lower), false).wholeHyperplane();
            if (Double.isInfinite(upper) && upper > 0.0) {
                return new BSPTree<Euclidean1D>(lowerCut, new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null);
            }
            final SubHyperplane<Euclidean1D> upperCut2 = new OrientedPoint(new Vector1D(upper), true).wholeHyperplane();
            return new BSPTree<Euclidean1D>(lowerCut, new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(upperCut2, new BSPTree<Euclidean1D>(Boolean.FALSE), new BSPTree<Euclidean1D>(Boolean.TRUE), null), null);
        }
    }
}