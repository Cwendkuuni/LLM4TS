import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertNotNull(segments);
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersectionNoEndpoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNotNull(intersection);
        assertEquals(0.5, intersection.getX(), 1e-10);
        assertEquals(0.5, intersection.getY(), 1e-10);
    }

    @Test
    public void testIntersectionWithEndpoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(0.5, intersection.getX(), 1e-10);
        assertEquals(0.5, intersection.getY(), 1e-10);
    }

    @Test
    public void testIntersectionNoIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 2);
        Vector2D end2 = new Vector2D(1, 3);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testSideParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        Line line2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        Side side = subLine1.side(line2);
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSideDifferentLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        Line line2 = new Line(new Vector2D(0, 2), new Vector2D(1, 3));
        Side side = subLine1.side(line2);
        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplitParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        Line line2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(line2);
        assertNull(split.getPlus());
        assertNull(split.getMinus());
    }

    @Test
    public void testSplitDifferentLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        Line line2 = new Line(new Vector2D(0, 2), new Vector2D(1, 3));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(line2);
        assertNull(split.getPlus());
        assertNotNull(split.getMinus());
        assertEquals(subLine1, split.getMinus());
    }

}