import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor() {
        // Create a sub-line from two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        assertEquals(subLine.getHyperplane().getClass(), Line.class);
        assertEquals(((Line)subLine.getHyperplane()).getStart().getX(), start.getX(), 0.0);
        assertEquals(((Line)subLine.getHyperplane()).getStart().getY(), start.getY(), 0.0);
        assertEquals(((Line)subLine.getHyperplane()).getEnd().getX(), end.getX(), 0.0);
        assertEquals(((Line)subLine.getHyperplane()).getEnd().getY(), end.getY(), 0.0);

        assertEquals(subLine.getRemainingRegion().getClass(), IntervalsSet.class);
        assertEquals(subLine.getSegments().size(), 1);
        assertEquals(subLine.getSegments().get(0).getStart().getX(), start.getX(), 0.0);
        assertEquals(subLine.getSegments().get(0).getStart().getY(), start.getY(), 0.0);
        assertEquals(subLine.getSegments().get(0).getEnd().getX(), end.getX(), 0.0);
        assertEquals(subLine.getSegments().get(0).getEnd().getY(), end.getY(), 0.0);
    }

    @Test
    public void testIntersection() {
        // Create first sub-line
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(10, 10);
        SubLine subLine1 = new SubLine(start1, end1);

        // Create second sub-line
        Vector2D start2 = new Vector2D(0, 10);
        Vector2D end2 = new Vector2D(10, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);

        assertNotNull(intersection);
        assertEquals(intersection.getX(), 5.0, 0.0);
        assertEquals(intersection.getY(), 5.0, 0.0);

        intersection = subLine1.intersection(subLine2, false);

        assertNull(intersection);
    }

    @Test
    public void testSide() {
        // Create first sub-line
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(10, 10);
        SubLine subLine1 = new SubLine(start1, end1);

        // Create second sub-line
        Vector2D start2 = new Vector2D(0, 10);
        Vector2D end2 = new Vector2D(10, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        assertEquals(subLine1.side(subLine2), Side.HYPER);
        assertEquals(subLine2.side(subLine1), Side.HYPER);

        // Create third sub-line
        Vector2D start3 = new Vector2D(5, 0);
        Vector2D end3 = new Vector2D(5, 10);
        SubLine subLine3 = new SubLine(start3, end3);

        assertEquals(subLine1.side(subLine3), Side.HYPER);
        assertEquals(subLine3.side(subLine1), Side.HYPER);
    }

    @Test
    public void testSplit() {
        // Create first sub-line
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(10, 10);
        SubLine subLine1 = new SubLine(start1, end1);

        // Create second sub-line
        Vector2D start2 = new Vector2D(0, 10);
        Vector2D end2 = new Vector2D(10, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(subLine2);
        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(subLine1);

        assertNull(split1.getPlus());
        assertNull(split1.getMinus());
        assertNull(split2.getPlus());
        assertNull(split2.getMinus());
    }

}