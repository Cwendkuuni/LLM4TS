import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        SubLine subLine = new SubLine(start, end);

        assertEquals(1, subLine.getSegments().size());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(4, 4);
        SubLine subLine2 = new SubLine(start2, end2);

        // They intersect at (2, 2)
        assertEquals(new Vector2D(2, 2), subLine1.intersection(subLine2, true));
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        SubLine subLine = new SubLine(start, end);

        assertTrue(subLine.buildIntervalSet(start, end).checkPoint(start.getX()).equals(start.getY()));
        assertTrue(subLine.buildIntervalSet(start, end).checkPoint(end.getX()).equals(end.getY()));
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(4, 4);
        SubLine subLine2 = new SubLine(start2, end2);

        assertEquals(Side.PLUS, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(4, 4);
        SubLine subLine2 = new SubLine(start2, end2);

        assertNotNull(subLine1.split(subLine2.getHyperplane()));
    }

}