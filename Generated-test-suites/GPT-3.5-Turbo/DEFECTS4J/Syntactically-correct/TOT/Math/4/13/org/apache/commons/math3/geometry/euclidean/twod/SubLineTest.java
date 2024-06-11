import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);

        intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 2);
        Vector2D end2 = new Vector2D(1, 3);
        SubLine subLine2 = new SubLine(start2, end2);

        assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 2);
        Vector2D end2 = new Vector2D(1, 3);
        SubLine subLine2 = new SubLine(start2, end2);

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(subLine2.getHyperplane());
        assertNull(splitSubHyperplane.getPlus());
        assertEquals(subLine1, splitSubHyperplane.getMinus());
    }

}