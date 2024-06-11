import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructorHyperplaneAndRegion() {
        // Test constructor with hyperplane and region
        // Create a hyperplane and a region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(hyperplane, region);

        assertNotNull(subLine);
    }

    @Test
    public void testConstructorStartAndEnd() {
        // Test constructor with start and end points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        assertNotNull(subLine);
    }

    @Test
    public void testConstructorSegment() {
        // Test constructor with a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(segment);

        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        // Test getSegments method
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
    }

    @Test
    public void testIntersection() {
        // Test intersection method
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, false);

        assertNotNull(intersection);
    }

    // Add more test cases for other methods

}