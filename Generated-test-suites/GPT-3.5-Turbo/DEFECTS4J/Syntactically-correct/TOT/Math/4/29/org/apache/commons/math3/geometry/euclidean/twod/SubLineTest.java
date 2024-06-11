import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);

        SubLine subLine = new SubLine(start, end);

        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervalSet, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);

        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersectionIncludeEndPoints() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start, end);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(4, 4);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);

        assertEquals(new Vector2D(2, 2), intersection);
    }

    @Test
    public void testIntersectionNotIncludeEndPoints() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start, end);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(4, 4);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, false);

        assertNull(intersection);
    }

    @Test
    public void testSide() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start, end);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(4, 4);
        SubLine subLine2 = new SubLine(start2, end2);

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0,0), new Vector2D(1,1));

        Side side = subLine1.side(hyperplane);

        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        SubLine subLine = new SubLine(start, end);

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0,0), new Vector2D(1,1));

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);

        assertNull(splitSubHyperplane.getMinus());
        assertNull(splitSubHyperplane.getPlus());
    }

}