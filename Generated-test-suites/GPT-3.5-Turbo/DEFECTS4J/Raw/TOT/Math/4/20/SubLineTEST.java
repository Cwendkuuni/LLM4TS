import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Test case for the getSegments method

        // Create a sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Retrieve the segments
        List<Segment> segments = subLine.getSegments();

        // Verify that the number of segments is correct
        Assert.assertEquals(1, segments.size());

        // Verify that the start and end points of the segment are correct
        Assert.assertEquals(start, segments.get(0).getStart());
        Assert.assertEquals(end, segments.get(0).getEnd());

    }

    @Test
    public void testIntersection() {
        // Test case for the intersection method

        // Create two sub-lines
        Vector2D start1 = new Vector2D(-1, -1);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 0);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

         // Calculate the intersection point
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        // Verify that the intersection point is correct
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);

        // Calculate the intersection point without including the endpoints
        Vector2D intersectionPointNoEndpoints = subLine1.intersection(subLine2, false);

        // Verify that the intersection point without endpoints is correct
        Assert.assertNull(intersectionPointNoEndpoints);
    }

    // Add more test methods for the other public methods in the SubLine class

    // ...

}