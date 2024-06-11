import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start.getX(), segment.getStart().getX(), 0.001);
        assertEquals(start.getY(), segment.getStart().getY(), 0.001);
        assertEquals(end.getX(), segment.getEnd().getX(), 0.001);
        assertEquals(end.getY(), segment.getEnd().getY(), 0.001);
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(1, 2);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(1, intersection.getX(), 0.001);
        assertEquals(1, intersection.getY(), 0.001);
    }

    @Test
    public void testSide() {
        Vector2D startPoint = new Vector2D(0, 0);
        Vector2D endPoint = new Vector2D(2, 2);
        SubLine subLine = new SubLine(startPoint, endPoint);

        Line line1 = new Line(startPoint, endPoint);
        Line line2 = new Line(1, 1, -1);
        
        Vector2D point1 = new Vector2D(2, 0);
        Vector2D point2 = new Vector2D(0, 2);

        Side side1 = subLine.side(line1);
        assertEquals(Side.MINUS, side1);

        Side side2 = subLine.side(line2);
        assertEquals(Side.PLUS, side2);

        Side side3 = subLine.side(point1);
        assertEquals(Side.MINUS, side3);

        Side side4 = subLine.side(point2);
        assertEquals(Side.PLUS, side4);
    }

    @Test
    public void testSplit() {
        Vector2D startPoint = new Vector2D(0, 0);
        Vector2D endPoint = new Vector2D(2, 2);
        SubLine subLine = new SubLine(startPoint, endPoint);

        Line line = new Line(1, 0, -1);
        SplitSubHyperplane<Euclidean2D> split = subLine.split(line);

        SubLine plus = (SubLine) split.getPlus();
        assertEquals(1, plus.getSegments().size());

        SubLine minus = (SubLine) split.getMinus();
        assertEquals(0, minus.getSegments().size());
    }
}