import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.util.FastMath;

import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSimpleConstructor() {
        // Create a simple line
        Vector2D p1 = new Vector2D(0, 1);
        Vector2D p2 = new Vector2D(2, 3);
        SubLine subLine = new SubLine(new Line(p1, p2),
                new IntervalsSet(p1.getX(), p2.getX()));

        // Verify the hyperplane and remaining region
        assertEquals(subLine.getHyperplane(), new Line(p1, p2));
        assertEquals(subLine.getRemainingRegion(), new IntervalsSet(p1.getX(), p2.getX()));
    }

    @Test
    public void testConstructorFromEndpoints() {
        // Create a line from two endpoints
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(3, 3);
        SubLine subLine = new SubLine(start, end);

        // Verify the hyperplane and remaining region
        assertEquals(subLine.getHyperplane(), new Line(start, end));
        assertEquals(subLine.getRemainingRegion(), new IntervalsSet(start.getX(), end.getX()));
    }

    @Test
    public void testConstructorFromSegment() {
        // Create a line from a segment
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(2, 2);
        Segment segment = new Segment(p1, p2, new Line(p1, p2));
        SubLine subLine = new SubLine(segment);

        // Verify the hyperplane and remaining region
        assertEquals(subLine.getHyperplane(), new Line(p1, p2));
        assertEquals(subLine.getRemainingRegion(), new IntervalsSet(p1.getX(), p2.getX()));
    }

    @Test
    public void testGetSegments() {
        // Create a sub-line with two segments
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(2, 2);
        SubLine subLine = new SubLine(p1, p2);

        // Get the segments
        List<Segment> segments = subLine.getSegments();

        assertEquals(segments.size(), 1);
        assertEquals(segments.get(0), new Segment(p1, p2, new Line(p1, p2)));
    }

    @Test
    public void testIntersection() {
        // Create two sub-lines
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(2, 2);
        Vector2D p3 = new Vector2D(1, 1);
        Vector2D p4 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(p3, p4);

        // Calculate the intersection point
        Vector2D intersection = subLine1.intersection(subLine2, true);

        // Verify the intersection point
        assertEquals(intersection, new Vector2D(1, 1));
    }

    @Test
    public void testIntersectionNoEndpoints() {
        // Create two sub-lines
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(2, 2);
        Vector2D p3 = new Vector2D(1, 1);
        Vector2D p4 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(p3, p4);

        // Calculate the intersection point without endpoints
        Vector2D intersection = subLine1.intersection(subLine2, false);

        // Verify the intersection point
        assertNull(intersection);
    }

    @Test
    public void testIntersectionNoIntersection() {
        // Create two sub-lines
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(1, 1);
        Vector2D p3 = new Vector2D(2, 2);
        Vector2D p4 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(p3, p4);

        // Calculate the intersection point
        Vector2D intersection = subLine1.intersection(subLine2, true);

        // Verify the intersection point
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        // Create an interval set from two points
        Vector2D start = new Vector2D(1, 1);
        Vector2D p2 = new Vector2D(3, 3);
        Line line = new Line(start, p2);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, p2);

        // Verify the interval set
        assertEquals(intervalSet, new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(p2).getX()));
    }

    @Test
    public void testSide() {
        // Create two sub-lines
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(2, 2);
        Vector2D p3 = new Vector2D(1, 1);
        Vector2D p4 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(p3, p4);

        // Calculate the side of subLine2 with respect to subLine1
        SubHyperplane.Side side = subLine1.side(subLine2.getHyperplane());

        // Verify the side
        assertEquals(side, SubHyperplane.Side.PLUS);
    }

    @Test
    public void testSplit() {
        // Create two sub-lines
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(2, 2);
        Vector2D p3 = new Vector2D(1, 1);
        Vector2D p4 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(p3, p4);

        // Split subLine1 with respect to subLine2
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());

        // Verify the split
        assertEquals(split.getPlus(), new SubLine(p1, p2));
        assertEquals(split.getMinus(), null);
    }

}