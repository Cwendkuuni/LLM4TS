import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        // Create Line hyperplane and IntervalsSet remainingRegion instances
        Line line = new Line(new Vector2D(1, 2), new Vector2D(3, 4));
        IntervalsSet remainingRegion = new IntervalsSet(0, 1);

        // Create SubLine instance using the constructor being tested
        SubLine subLine = new SubLine(line, remainingRegion);

        // Assert that the hyperplane and remainingRegion are set correctly
        assertEquals(line, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEndPoints() {
        // Create Vector2D start and end instances
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);

        // Create SubLine instance using the constructor being tested
        SubLine subLine = new SubLine(start, end);

        // Assert that the hyperplane and remainingRegion are set correctly
        Line expectedLine = new Line(start, end);
        assertEquals(expectedLine, subLine.getHyperplane());

        IntervalsSet expectedRemainingRegion = new IntervalsSet(0, 1);
        assertEquals(expectedRemainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        // Create Line instance
        Line line = new Line(new Vector2D(1, 2), new Vector2D(3, 4));

        // Create Segment instance
        Segment segment = new Segment(new Vector2D(1, 2), new Vector2D(3, 4), line);

        // Create SubLine instance using the constructor being tested
        SubLine subLine = new SubLine(segment);

        // Assert that the hyperplane and remainingRegion are set correctly
        assertEquals(line, subLine.getHyperplane());

        IntervalsSet expectedRemainingRegion = new IntervalsSet(0, 1);
        assertEquals(expectedRemainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        // Create SubLine instance
        SubLine subLine = new SubLine(new Vector2D(1, 2), new Vector2D(3, 4));

        // Get the segments of the subLine
        List<Segment> segments = subLine.getSegments();

        // Assert that the list of segments is correct
        assertEquals(1, segments.size());

        Segment expectedSegment = new Segment(new Vector2D(1, 2), new Vector2D(3, 4), subLine.getHyperplane());
        assertEquals(expectedSegment, segments.get(0));
    }

    @Test
    public void testIntersectionWithIncludeEndPointsTrue() {
        // Create SubLine instances
        SubLine subLine1 = new SubLine(new Vector2D(1, 2), new Vector2D(3, 4));
        SubLine subLine2 = new SubLine(new Vector2D(5, 6), new Vector2D(7, 8));

        // Test intersection with includeEndPoints = true
        assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionWithIncludeEndPointsFalse() {
        // Create SubLine instances
        SubLine subLine1 = new SubLine(new Vector2D(1, 2), new Vector2D(3, 4));
        SubLine subLine2 = new SubLine(new Vector2D(5, 6), new Vector2D(7, 8));

        // Test intersection with includeEndPoints = false
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testSide() {
        // Create SubLine instances
        SubLine subLine1 = new SubLine(new Vector2D(1, 2), new Vector2D(3, 4));
        SubLine subLine2 = new SubLine(new Vector2D(5, 6), new Vector2D(7, 8));

        // Test side
        assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit() {
        // Create SubLine instances
        SubLine subLine1 = new SubLine(new Vector2D(1, 2), new Vector2D(3, 4));
        SubLine subLine2 = new SubLine(new Vector2D(5, 6), new Vector2D(7, 8));

        // Test split
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(subLine2.getHyperplane());
        assertNull(splitSubHyperplane.getMinus());
        assertNull(splitSubHyperplane.getPlus());
    }
}