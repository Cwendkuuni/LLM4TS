import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Test;

public class SubLineTest {
    @Test
    public void testIntersection() {
        // Create sub-lines for intersection
        SubLine subLine1 = new SubLine(new Hyperplane<Euclidean2D>() {}, new Region<Euclidean1D>() {});
        SubLine subLine2 = new SubLine(new Hyperplane<Euclidean2D>() {}, new Region<Euclidean1D>() {});
        
        // Test with includeEndPoints as true
        assertNull(subLine1.intersection(subLine2, true));
        
        // Test with includeEndPoints as false
        assertNull(subLine1.intersection(subLine2, false));
    }
    
    @Test
    public void testSide() {
        // Create sub-line
        SubLine subLine = new SubLine(new Hyperplane<Euclidean2D>() {}, new Region<Euclidean1D>() {});
        
        // Test with parallel lines
        Line thisLine = (Line) subLine.getHyperplane();
        Line otherLine = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Vector2D crossing = thisLine.intersection(otherLine);
        
        double global = otherLine.getOffset(thisLine);
        if (global < -1.0e-10) {
            assertEquals(Side.MINUS, subLine.side(otherLine));
        } else if (global > 1.0e-10) {
            assertEquals(Side.PLUS, subLine.side(otherLine));
        } else {
            assertEquals(Side.HYPER, subLine.side(otherLine));
        }
        
        // Test with intersecting lines
        Line thisLine2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Line otherLine2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        Vector2D crossing2 = thisLine2.intersection(otherLine2);
        
        boolean direct = FastMath.sin(thisLine2.getAngle() - otherLine2.getAngle()) < 0;
        Vector1D x = thisLine2.toSubSpace(crossing2);
        
        if (getRemainingRegion().checkPoint(line1.toSubSpace(v2D)) != Location.OUTSIDE 
            && subLine2.getRemainingRegion().checkPoint(line2.toSubSpace(v2D)) != Location.OUTSIDE) {
            assertEquals(x, subLine.intersection(subLine2, true));
        } else {
            assertNull(subLine.intersection(subLine2, true));
        }
    }
    
    @Test
    public void testSplit() {
        // Create sub-lines for split
        SubLine subLine1 = new SubLine(new Hyperplane<Euclidean2D>() {}, new Region<Euclidean1D>() {});
        SubLine subLine2 = new SubLine(new Hyperplane<Euclidean2D>() {}, new Region<Euclidean1D>() {});
        
        // Test split with parallel lines
        Line thisLine = (Line) subLine1.getHyperplane();
        Line otherLine = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Vector2D crossing = thisLine.intersection(otherLine);
        
        double global = otherLine.getOffset(thisLine);
        if (global < -1.0e-10) {
            assertEquals(subLine2, subLine1.split(otherLine).getMinus());
            assertNull(subLine1.split(otherLine).getPlus());
        } else {
            assertNull(subLine1.split(otherLine).getMinus());
            assertEquals(subLine2, subLine1.split(otherLine).getPlus());
        }
        
        // Test split with intersecting lines
        Line thisLine2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Line otherLine2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        Vector2D crossing2 = thisLine2.intersection(otherLine2);
        
        boolean direct = FastMath.sin(thisLine2.getAngle() - otherLine2.getAngle()) < 0;
        Vector1D x = thisLine2.toSubSpace(crossing2);
        SubHyperplane<Euclidean1D> subPlus  = new OrientedPoint(x, !direct).wholeHyperplane();
        SubHyperplane<Euclidean1D> subMinus = new OrientedPoint(x, direct).wholeHyperplane();
        BSPTree<Euclidean1D> splitTree = getRemainingRegion().getTree(false).split(subMinus);
        BSPTree<Euclidean1D> plusTree  = getRemainingRegion().isEmpty(splitTree.getPlus()) ?
                                              new BSPTree<Euclidean1D>(Boolean.FALSE) :
                                              new BSPTree<Euclidean1D>(subPlus, new BSPTree<Euclidean1D>(Boolean.FALSE),
                                                                       splitTree.getPlus(), null);
        BSPTree<Euclidean1D> minusTree = getRemainingRegion().isEmpty(splitTree.getMinus()) ?
                                              new BSPTree<Euclidean1D>(Boolean.FALSE) :
                                              new BSPTree<Euclidean1D>(subMinus, new BSPTree<Euclidean1D>(Boolean.FALSE),
                                                                       splitTree.getMinus(), null);
        SplitSubHyperplane<Euclidean2D> expectedSplit = new SplitSubHyperplane<Euclidean2D>(new SubLine(thisLine.copySelf(), new IntervalsSet(plusTree)),
                                                                                            new SubLine(thisLine.copySelf(), new IntervalsSet(minusTree)));
        
        assertEquals(expectedSplit, subLine.split(subLine2));
    }
}