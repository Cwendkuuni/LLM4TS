import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        List<Segment> segments1 = subLine1.getSegments();
        assertEquals(1, segments1.size());
        assertEquals(start1, segments1.get(0).getStart());
        assertEquals(end1, segments1.get(0).getEnd());
        assertEquals(new Line(start1, end1), segments1.get(0).getLine());
        
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine2 = new SubLine(start2, end2);
        
        List<Segment> segments2 = subLine2.getSegments();
        assertEquals(1, segments2.size());
        assertEquals(start2, segments2.get(0).getStart());
        assertEquals(end2, segments2.get(0).getEnd());
        assertEquals(new Line(start2, end2), segments2.get(0).getLine());
        
        Vector2D start3 = new Vector2D(4, 4);
        Vector2D end3 = new Vector2D(5, 5);
        SubLine subLine3 = new SubLine(start3, end3);
        
        List<Segment> segments3 = subLine3.getSegments();
        assertEquals(1, segments3.size());
        assertEquals(start3, segments3.get(0).getStart());
        assertEquals(end3, segments3.get(0).getEnd());
        assertEquals(new Line(start3, end3), segments3.get(0).getLine());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection1 = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1, 1), intersection1);
        
        Vector2D intersection2 = subLine1.intersection(subLine2, false);
        assertNull(intersection2);
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Side side1 = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.PLUS, side1);
        
        Side side2 = subLine1.side(subLine1.getHyperplane());
        assertEquals(Side.HYPER, side2);
    }

    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        SplitSubHyperplane split = subLine1.split(subLine2.getHyperplane());
        
        SubLine plusSubLine = (SubLine) split.getPlus();
        assertTrue(plusSubLine.getRemainingRegion().checkPoint(new OrientedPoint(1, true)) == Region.Location.INSIDE);
        
        SubLine minusSubLine = (SubLine) split.getMinus();
        assertTrue(minusSubLine.getRemainingRegion().checkPoint(new OrientedPoint(1, true)) == Region.Location.OUTSIDE);
    }

}