import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class SubLineTest {
    @Test
    public void testConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 0);
        SubLine subLine = new SubLine(start, end);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 0);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        Vector2D start2 = new Vector2D(0.5, -0.5);
        Vector2D end2 = new Vector2D(0.5, 0.5);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0), intersection);
    }

    @Test
    public void testIntersectionWithEndpoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        Vector2D start2 = new Vector2D(0.5, -0.5);
        Vector2D end2 = new Vector2D(0.5, 0.5);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        Vector2D start2 = new Vector2D(0.5, -0.5);
        Vector2D end2 = new Vector2D(0.5, 0.5);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        assertEquals(Side.PLUS, subLine1.side(subLine2));
        assertEquals(Side.MINUS, subLine2.side(subLine1));
    }

    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        Vector2D start2 = new Vector2D(0.5, -0.5);
        Vector2D end2 = new Vector2D(0.5, 0.5);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(subLine2);
        assertTrue(split1.getPlus() instanceof SubLine);
        assertTrue(split1.getMinus() instanceof SubLine);
        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(subLine1);
        assertTrue(split2.getPlus() instanceof SubLine);
        assertTrue(split2.getMinus() instanceof SubLine);
    }
}