import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.test.EqualsTester;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link SubLine}.
 */
public class SubLineTest {

    private static final double EPSILON = 1.0e-14;
    private Euclidean2DSpace space;

    @Before
    public void setUp() {
        space = new Euclidean2DSpace();
    }

    /**
     * Test constructor from hyperplane and remaining region.
     */
    @Test
    public void testConstructorFromHyperplaneAndRegion() {
        final Vector2D start = new Vector2D(0.0, 0.0);
        final Vector2D end   = new Vector2D(1.0, 1.0);
        final Line line     = new Line(start, end);
        final Interval interval = new Interval(0.5, 0.6);
        final Region region  = new IntervalsSet(interval);
        final SubLine subLine = new SubLine(line, region);

        final Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        final Region remainingRegion = subLine.getRemainingRegion();

        assertEquals(line, hyperplane);
        assertEquals(remainingRegion, region);
    }

    /**
     * Test constructor from start and end points.
     */
    @Test
    public void testConstructorFromPoints() {
        final Vector2D start = new Vector2D(0.0, 0.0);
        final Vector2D end   = new Vector2D(1.0, 1.0);
        final SubLine subLine = new SubLine(start, end);
        final List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    /**
     * Test constructor from segment.
     */
    @Test
    public void testConstructorFromSegment() {
        final Vector2D start = new Vector2D(0.0, 0.0);
        final Vector2D end   = new Vector2D(1.0, 1.0);
        final Segment segment = new Segment(start, end);
        final SubLine subLine = new SubLine(segment);
        final List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegments() {
        final Vector2D start1 = new Vector2D(0.0, 0.0);
        final Vector2D end1   = new Vector2D(1.0, 1.0);
        final Vector2D start2 = new Vector2D(1.0, 1.0);
        final Vector2D end2   = new Vector2D(2.0, 2.0);
        final Line line = new Line(start1, end1);
        final Interval interval1 = new Interval(0.0, 0.5);
        final Interval interval2 = new Interval(0.5, 1.0);
        final IntervalsSet remainingRegion = new IntervalsSet(interval1, interval2);
        final SubLine subLine = new SubLine(line, remainingRegion);
        final List<Segment> segments = subLine.getSegments();

        assertEquals(2, segments.size());
        assertEquals(start1, segments.get(0).getStart());
        assertEquals(new Vector2D(0.5, 0.5), segments.get(0).getEnd());
        assertEquals(new Vector2D(1.0, 1.0), segments.get(1).getStart());
        assertEquals(end2, segments.get(1).getEnd());
    }

    /**
     * Test intersection method.
     */
    @Test
    public void testIntersection() {
        final Vector2D start1 = new Vector2D(0.0, 0.0);
        final Vector2D end1   = new Vector2D(1.0, 0.0);
        final Vector2D start2 = new Vector2D(1.0, 1.0);
        final Vector2D end2   = new Vector2D(2.0, 1.0);
        final Line line1 = new Line(start1, end1);
        final Line line2 = new Line(start2, end2);
        final SubLine subLine1 = new SubLine(line1);
        final SubLine subLine2 = new SubLine(line2);

        // intersection point is null when lines are parallel and not intersecting
        assertNull(subLine1.intersection(subLine2, true));

        // intersection point is null when lines intersect outside of both sublines
        assertNull(subLine1.intersection(subLine2, false));

        // intersection point is null when lines intersect only on endpoints and endpoints are not considered
        final Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        // intersection point is not null when lines intersect and endpoints are considered
        final SubLine subLine3 = new SubLine(new Segment(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.75)));
        final Vector2D intersection2 = subLine1.intersection(subLine3, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection2, EPSILON);
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals() {
        final Vector2D start1 = new Vector2D(0.0, 0.0);
        final Vector2D end1   = new Vector2D(1.0, 1.0);
        final Line line1 = new Line(start1, end1);
        final Interval interval1 = new Interval(0.0, 0.5);
        final Interval interval2 = new Interval(0.5, 1.0);
        final IntervalsSet remainingRegion1 = new IntervalsSet(interval1, interval2);
        final SubLine subLine1 = new SubLine(line1, remainingRegion1);

        final Vector2D start2 = new Vector2D(1.0, 0.0);
        final Vector2D end2   = new Vector2D(2.0, 1.0);
        final Line line2 = new Line(start2, end2);
        final IntervalsSet remainingRegion2 = new IntervalsSet(interval1);
        final SubLine subLine2 = new SubLine(line2, remainingRegion2);

        new EqualsTester()
                .addEqualityGroup(subLine1, subLine1)
                .addEqualityGroup(subLine1, new SubLine(line1, remainingRegion1))
                .addEqualityGroup(subLine1, new SubLine(new Line(line1), remainingRegion1))
                .addEqualityGroup(subLine1, new SubLine(line1, new IntervalsSet(interval1, interval2)))
                .addEqualityGroup(subLine1, new SubLine(new Line(line1), new IntervalsSet(interval1, interval2)))
                .addEqualityGroup(subLine1, new SubLine(new Line(line1.copySelf()), new IntervalsSet(interval1, interval2)))
                .addEqualityGroup(subLine1, new SubLine(line1, new IntervalsSet(interval1)))
                .addEqualityGroup(subLine1, new SubLine(line1, new IntervalsSet(interval2)))
                .addEqualityGroup(subLine1, new SubLine(new Line(line1.copySelf()), new IntervalsSet(interval1)))
                .addEqualityGroup(subLine1, new SubLine(new Line(line1.copySelf()), new IntervalsSet(interval2)))
                .addEqualityGroup(subLine2, subLine2)
                .addEqualityGroup(subLine2, new SubLine(line2, remainingRegion2))
                .addEqualityGroup(subLine2, new SubLine(new Line(line2), remainingRegion2))
                .addEqualityGroup(subLine2, new SubLine(line2, new IntervalsSet(interval1)))
                .addEqualityGroup(subLine2, new SubLine(new Line(line2), new IntervalsSet(interval1)))
                .addEqualityGroup(subLine2, new SubLine(line2, new IntervalsSet(interval2)))
                .addEqualityGroup(subLine2, new SubLine(new Line(line2), new IntervalsSet(interval2)))
                .testEquals();

        new EqualsTester()
                .addEqualityGroup(subLine1, line1)
                .addEqualityGroup(subLine1, remainingRegion1)
                .addEqualityGroup(subLine1, subLine1.getHyperplane())
                .addEqualityGroup(subLine1, subLine1.getRemainingRegion())
                .testNotEquals();

        new EqualsTester()
                .addEqualityGroup(subLine1, new Object())
                .testNotEquals();
    }

}