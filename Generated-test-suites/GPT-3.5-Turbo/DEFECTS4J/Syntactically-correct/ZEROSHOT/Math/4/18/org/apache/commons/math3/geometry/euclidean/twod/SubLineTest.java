import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Create a sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Get the segments
        List<Segment> segments = subLine.getSegments();

        // Check that the number of segments is correct
        assertEquals(1, segments.size());

        // Check that the start and end points are correct
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the two sub-lines intersect
        assertTrue(subLine1.intersection(subLine2, true) != null);

        // Check that the two sub-lines do not intersect when endpoints are excluded
        assertTrue(subLine1.intersection(subLine2, false) == null);
    }

    @Test
    public void testBuildIntervalSet() {
        // Create two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);

        // Build the interval set
        IntervalsSet intervalSet = new IntervalsSet(start.getX(), end.getX());

        // Check that the interval set is correct
        assertTrue(intervalSet.contains(start.getX()));
        assertTrue(intervalSet.contains(end.getX()));
        assertFalse(intervalSet.contains(start.getX() - 0.1));
        assertFalse(intervalSet.contains(end.getX() + 0.1));
    }

    @Test
    public void testSide() {
        // Create a sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Create a hyperplane
        Line line = new Line(new Vector2D(0, 1), new Vector2D(1, 0));

        // Check the side of the sub-line with respect to the hyperplane
        assertEquals(Region.Location.INSIDE, subLine.side(line));
    }

    @Test
    public void testSplit() {
        // Create a sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Create a hyperplane
        Line line = new Line(new Vector2D(0, 1), new Vector2D(1, 0));

        // Split the sub-line with the hyperplane
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine.split(line);

        // Check that the split sub-line is not null
        assertTrue(splitSubLine != null);
    }

}