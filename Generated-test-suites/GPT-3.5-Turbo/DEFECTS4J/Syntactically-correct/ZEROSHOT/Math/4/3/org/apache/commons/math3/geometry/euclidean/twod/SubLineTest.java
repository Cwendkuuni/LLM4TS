import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructor1() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
    }

    @Test
    public void testConstructor2() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        IntervalsSet remainingRegion = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, remainingRegion);
        assertNotNull(subLine);
    }

    @Test
    public void testConstructor3() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Segment segment = new Segment(start1, end1, new Line(start1, end1));
        SubLine subLine = new SubLine(segment);
        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine = new SubLine(start1, end1);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start1, segments.get(0).getStart());
        assertEquals(end1, segments.get(0).getEnd());
        SubLine subLine2 = new SubLine(start1, end1, start2, end2);
        List<Segment> segments2 = subLine2.getSegments();
        assertEquals(2, segments2.size());
        assertEquals(start1, segments2.get(0).getStart());
        assertEquals(end1, segments2.get(0).getEnd());
        assertEquals(start2, segments2.get(1).getStart());
        assertEquals(end2, segments2.get(1).getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(0.5, intersection.getX(), 1e-10);
        assertEquals(0.5, intersection.getY(), 1e-10);
        Vector2D intersection2 = subLine1.intersection(subLine2, false);
        assertNull(intersection2);
    }

    @Test
    public void testSplit() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Line line = new Line(start, end);
        SubLine subLine2 = new SubLine(line, new IntervalsSet(0, 1));
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine.split(line);
        assertNotNull(splitSubLine);
        assertEquals(subLine2, splitSubLine.getPlus());
        assertNull(splitSubLine.getMinus());
    }

}