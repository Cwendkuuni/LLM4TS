import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {
    private static final double EPSILON = 1e-10;

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine = new SubLine(line, null);
        
        Segment segment = subLine.getSegments().get(0);
        assertEquals(1.0, segment.getStart().getX(), EPSILON);
        assertEquals(1.0, segment.getStart().getY(), EPSILON);
        assertEquals(3.0, segment.getEnd().getX(), EPSILON);
        assertEquals(3.0, segment.getEnd().getY(), EPSILON);
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        Line line2 = new Line(new Vector2D(2, 4), new Vector2D(4, 2));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(3.0, intersection.getX(), EPSILON);
        assertEquals(3.0, intersection.getY(), EPSILON);
        
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assertFalse(intervalSet.isEmpty());
        assertEquals(1.0, intervalSet.asList().get(0).getInf(), EPSILON);
        assertEquals(3.0, intervalSet.asList().get(0).getSup(), EPSILON);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        Line line2 = new Line(new Vector2D(2, 4), new Vector2D(4, 2));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);
        
        Side side = subLine1.side(subLine2);
        assertEquals(Side.HYPER, side);
        
        side = subLine2.side(subLine1);
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        Line line2 = new Line(new Vector2D(2, 4), new Vector2D(4, 2));
        SubLine subLine1 = new SubLine(line1, null);
        SubLine subLine2 = new SubLine(line2, null);
        
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(subLine2);
        SubLine plusSplit = (SubLine) splitSubHyperplane.getPlus();
        SubLine minusSplit = (SubLine) splitSubHyperplane.getMinus();
        
        Segment plusSegment = plusSplit.getSegments().get(0);
        assertEquals(1.0, plusSegment.getStart().getX(), EPSILON);
        assertEquals(1.0, plusSegment.getStart().getY(), EPSILON);
        assertEquals(3.0, plusSegment.getEnd().getX(), EPSILON);
        assertEquals(3.0, plusSegment.getEnd().getY(), EPSILON);
        
        Segment minusSegment = minusSplit.getSegments().get(0);
        assertEquals(3.0, minusSegment.getStart().getX(), EPSILON);
        assertEquals(3.0, minusSegment.getStart().getY(), EPSILON);
        assertEquals(1.0, minusSegment.getEnd().getX(), EPSILON);
        assertEquals(1.0, minusSegment.getEnd().getY(), EPSILON);
    }
}