import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        List<Segment> segments = subLine.getSegments();
        
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
        assertEquals(subLine.getHyperplane(), segments.get(0).getLine());
    }

    @Test
    public void testIntersectionWithIncludeEndPoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0.5, -1);
        Vector2D end2 = new Vector2D(0.5, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        
        assertNotNull(intersection);
        assertEquals(0.5, intersection.getX(), 0.00001);
        assertEquals(0, intersection.getY(), 0.00001);
    }

    @Test
    public void testIntersectionWithoutIncludeEndPoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0.5, -1);
        Vector2D end2 = new Vector2D(0.5, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, false);
        
        assertNull(intersection);
    }

    @Test
    public void testBuildNew() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(0, 1);
        SubLine newSubLine = subLine.buildNew(hyperplane, remainingRegion);
        
        assertEquals(hyperplane, newSubLine.getHyperplane());
        assertEquals(remainingRegion, newSubLine.getRemainingRegion());
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Side side = subLine1.side(subLine2.getHyperplane());
        
        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0.5, -1);
        Vector2D end2 = new Vector2D(0.5, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(subLine2.getHyperplane());
        
        assertNotNull(splitSubHyperplane.getPlus());
        assertNotNull(splitSubHyperplane.getMinus());
        assertEquals(subLine1.getHyperplane(), splitSubHyperplane.getPlus().getHyperplane());
        assertEquals(subLine1.getHyperplane(), splitSubHyperplane.getMinus().getHyperplane());
        assertEquals(subLine1.getRemainingRegion(), splitSubHyperplane.getPlus().getRemainingRegion());
        assertEquals(subLine1.getRemainingRegion(), splitSubHyperplane.getMinus().getRemainingRegion());
    }
}