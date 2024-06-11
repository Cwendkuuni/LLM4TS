import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;


public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Create a sub-line with start and end points
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        SubLine subLine = new SubLine(start, end);

        // Check if the sub-line returns the correct segments
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection_IncludeEndPoints_True() {
        // Create two sub-lines that intersect
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(2, 5);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check if the intersection point is returned when includeEndPoints is true
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(2, 2), intersection);
    }

    @Test
    public void testIntersection_IncludeEndPoints_False() {
        // Create two sub-lines that intersect
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(2, 5);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check if null is returned when includeEndPoints is false
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testSide_ParallelLines() {
        // Create two parallel sub-lines
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(2, 5);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check if the correct side is returned for parallel sub-lines
        assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSide_IntersectingLines() {
        // Create two intersecting sub-lines
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(2, 5);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check if the correct side is returned for intersecting sub-lines
        assertEquals(Side.PLUS, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit_ParallelLines() {
        // Create two parallel sub-lines
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(2, 5);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check if the correct split sub-hyperplanes are returned for parallel sub-lines
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        assertNull(split.getPlus());
        assertNotNull(split.getMinus());
        assertEquals(subLine1, split.getMinus());
    }

    @Test
    public void testSplit_IntersectingLines() {
        // Create two intersecting sub-lines
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(2, 5);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check if the correct split sub-hyperplanes are returned for intersecting sub-lines
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());
    }

}