import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test simple constructor
        SubLine subLine1 = new SubLine(new Hyperplane<Euclidean2D>(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), new Interval(1, 2)), new IntervalsSet(new Interval(1.5, 3)));
        assertEquals(new Line(new Vector2D(1, 0), new Vector2D(3, 0)), subLine1.getHyperplane());
        List<Segment> segments = subLine1.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(1.5, 0), new Vector2D(3, 0), new Line(new Vector2D(1, 0), new Vector2D(3, 0))), segments.get(0));

        // Test constructor with two endpoints
        SubLine subLine2 = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(2, 2)), subLine2.getHyperplane());
        segments = subLine2.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(2, 2), new Line(new Vector2D(0, 0), new Vector2D(2, 2))), segments.get(0));

        // Test constructor with a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(2, 2), new Line(new Vector2D(0, 0), new Vector2D(2, 2)));
        SubLine subLine3 = new SubLine(segment);
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(2, 2)), subLine3.getHyperplane());
        segments = subLine3.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment, segments.get(0));
    }

    @Test
    public void testSubLineIntersection() {
        // Test intersection with another sub-line on infinite line
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0, 1), new Vector2D(1, 1)), new IntervalsSet(new Interval(2, 3), new Interval(4, 5)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        // Test intersection with another sub-line on finite line
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(2, 2)), new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5)));
        SubLine subLine4 = new SubLine(new Line(new Vector2D(1, 1), new Vector2D(3, 3)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        intersectionPoint = subLine3.intersection(subLine4, true);
        assertEquals(new Vector2D(1.5, 1.5), intersectionPoint);

        // Test intersection with another sub-line not intersecting
        SubLine subLine5 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), new IntervalsSet(new Interval(0, 1), new Interval(2, 3)));
        SubLine subLine6 = new SubLine(new Line(new Vector2D(0, 1), new Vector2D(1, 1)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        intersectionPoint = subLine5.intersection(subLine6, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineGetSegments() {
        // Test getSegments() method with simple constructor
        SubLine subLine1 = new SubLine(new Hyperplane<Euclidean2D>(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4))), new IntervalsSet(new Interval(1.5, 3)));
        List<Segment> segments = subLine1.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(1.5, 0), new Vector2D(3, 0), new Line(new Vector2D(1, 0), new Vector2D(3, 0))), segments.get(0));

        // Test getSegments() method with two endpoints
        SubLine subLine2 = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        segments = subLine2.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(2, 2), new Line(new Vector2D(0, 0), new Vector2D(2, 2))), segments.get(0));

        // Test getSegments() method with a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(2, 2), new Line(new Vector2D(0, 0), new Vector2D(2, 2)));
        SubLine subLine3 = new SubLine(segment);
        segments = subLine3.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment, segments.get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubLineInvalidIntersection() {
        // Test intersection with null sub-line
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        subLine1.intersection(null, true);

        // Test intersection with another sub-line on infinite line with different angles
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        SubLine subLine3 = new SubLine(new Line(new Vector2D(1, 1), new Vector2D(2, 1)), new IntervalsSet(new Interval(2, 3), new Interval(4, 5)));
        subLine1.intersection(subLine3, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubLineInvalidRegion() {
        // Test constructor with an empty or null hyperplane
        new SubLine(null, new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        new SubLine(new Hyperplane<Euclidean2D>(null, new IntervalsSet()), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));

        // Test intersection with an empty or null remaining region
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        subLine1.getRemainingRegion().setEmpty();
        try {
            subLine1.intersection(new SubLine(new Line(new Vector2D(0, 1), new Vector2D(1, 1)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4))), true);
            fail();
        } catch (IllegalArgumentException e) {
        }

        subLine1.getRemainingRegion() = null;
        try {
            subLine1.intersection(new SubLine(new Line(new Vector2D(0, 1), new Vector2D(1, 1)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4))), true);
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubLineInvalidSegment() {
        // Test constructor with an empty or null line
        new SubLine(null, new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));

        // Test constructor with a segment with null start or end points
        new SubLine(new Segment(null, new Vector2D(1, 0)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        new SubLine(new Segment(new Vector2D(1, 0), null), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));

        // Test constructor with a segment with a null line
        new SubLine(new Segment(new Vector2D(1, 0), new Vector2D(2, 0), null), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));

        // Test getSegments() method with a segment with null start or end points
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 0)), new IntervalsSet(new Interval(1, 2), new Interval(3, 4)));
        List<Segment> segments = subLine1.getSegments();
        try {
            segments.add(new Segment(null, new Vector2D(1, 0)));
            fail();
        } catch (IllegalArgumentException e) {
        }

        try {
            segments.set(0, new Segment(new Vector2D(1, 0), null));
            fail();
        } catch (IllegalArgumentException e) {
        }
    }
}