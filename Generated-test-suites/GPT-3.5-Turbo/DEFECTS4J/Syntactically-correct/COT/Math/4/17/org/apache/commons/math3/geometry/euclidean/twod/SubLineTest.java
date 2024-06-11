import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region.Location;

public class SubLineTest {

    // Test constructor

    @Test
    public void testConstructor() {
        // Create a sub-line from two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Check that the sub-line is correctly constructed
        assertEquals(start, subLine.getFirstEndpoint());
        assertEquals(end, subLine.getLastEndpoint());
    }

    // Test getSegments

    @Test
    public void testGetSegments() {
        // Create a sub-line from two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Get the endpoints of the sub-line
        List<Segment> segments = subLine.getSegments();

        // Check that the sub-line contains one segment
        assertEquals(1, segments.size());

        // Check that the segment has the correct start and end points
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    // Test intersection

    @Test
    public void testIntersection() {
        // Create a sub-line from two points
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        // Create a sub-line from two intersecting points
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the sub-lines intersect at (0.5, 0.5)
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);

        // Check that the sub-lines do not intersect at (0.5, 0.5) if endpoints are not included
        intersection = subLine1.intersection(subLine2, false);
        assertEquals(null, intersection);
    }

    // Test side

    @Test
    public void testSide() {
        // Create a sub-line from two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Create two test points
        Vector2D point1 = new Vector2D(0.5, 0.5);
        Vector2D point2 = new Vector2D(0, 1);

        // Check that point1 is inside the sub-line
        assertEquals(Location.INSIDE, subLine.side(point1));

        // Check that point2 is outside the sub-line
        assertEquals(Location.OUTSIDE, subLine.side(point2));
    }

    // Test split

    @Test
    public void testSplit() {
        // Create a sub-line from two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Create a test hyperplane
        Line hyperplane = new Line(new Vector2D(0, 1), new Vector2D(1, 0));

        // Split the sub-line by the hyperplane
        SplitSubHyperplane<Euclidean2D> split = subLine.split(hyperplane);

        // Check that the sub-line splits into two halves
        assertTrue(split.hasInside());
        assertTrue(split.hasOutside());

        // Check that the inside sub-line contains the point (0.5, 0.5)
        SubLine inside = (SubLine) split.getInside();
        assertEquals(Location.INSIDE, inside.side(new Vector2D(0.5, 0.5)));

        // Check that the outside sub-line contains the point (0, 1)
        SubLine outside = (SubLine) split.getOutside();
        assertEquals(Location.OUTSIDE, outside.side(new Vector2D(0, 1)));
    }

}