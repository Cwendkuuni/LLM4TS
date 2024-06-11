import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Create a sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Get the segments
        List<Segment> segments = subLine.getSegments();

        // Check if the segment start and end points are correct
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersectionWithIncludeEndPoints() {
        // Create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Get the intersection point
        Vector2D intersection = subLine1.intersection(subLine2, true);

        // Check if the intersection point is correct
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersectionWithoutIncludeEndPoints() {
        // Create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Get the intersection point
        Vector2D intersection = subLine1.intersection(subLine2, false);

        // Check if the intersection point is correct
        assertEquals(null, intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        // Create two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);

        // Build an interval set
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);

        // Check if the interval set is correct
        assertEquals(0, intervalSet.asList().get(0).getInf(), 0.0);
        assertEquals(1, intervalSet.asList().get(0).getSup(), 0.0);
    }

    @Test
    public void testSide() {
        // Create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Get the side of the second sub-line with respect to the first sub-line
        Side side = subLine1.side(subLine2.getHyperplane());

        // Check if the side is correct
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        // Create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Split the first sub-line with the second sub-line
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine1.split(subLine2.getHyperplane());

        // Check if the split sub-line plus part is correct
        assertEquals(new Vector2D(0, 0), splitSubLine.getPlus().getRemainingRegion().getBounds().getMin());
        assertEquals(new Vector2D(0.5, 0.5), splitSubLine.getPlus().getRemainingRegion().getBounds().getMax());

        // Check if the split sub-line minus part is correct
        assertEquals(new Vector2D(0.5, 0.5), splitSubLine.getMinus().getRemainingRegion().getBounds().getMin());
        assertEquals(new Vector2D(1, 1), splitSubLine.getMinus().getRemainingRegion().getBounds().getMax());
    }

}