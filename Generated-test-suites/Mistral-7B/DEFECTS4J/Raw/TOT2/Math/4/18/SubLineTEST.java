import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test cases for {@link SubLine}.
 */
public class SubLineTest {

    /**
     * Test constructor with hyperplane and remaining region.
     */
    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    /**
     * Test constructor with two endpoints.
     */
    @Test
    public void testConstructorWithTwoEndpoints() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end   = new Vector2D(1.0, 0.0);
        SubLine subLine = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }

    /**
     * Test constructor with a segment.
     */
    @Test
    public void testConstructorWithSegment() {
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0), new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)));
        SubLine subLine = new SubLine(segment);
        assertEquals(segment.getLine(), subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segmentTest = segments.get(0);
        assertEquals(segment.getStart(), segmentTest.getStart());
        assertEquals(segment.getEnd(), segmentTest.getEnd());
    }

    /**
     * Test getSegments method.
     */
    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.25), new Interval(0.5, 1.0));
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0.0, 0.0), segment1.getStart());
        assertEquals(new Vector2D(0.25, 0.0), segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(0.5, 0.0), segment2.getStart());
        assertEquals(new Vector2D(1.0, 0.0), segment2.getEnd());
    }

    /**
     * Test intersection method with null subLine.
     */
    @Test
    public void testIntersectionWithNullSubLine() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        SubLine subLine = null;
        Vector2D result = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.25), new Interval(0.5, 1.0))).intersection(subLine, false);
        assertNull(result);
    }

    /**
     * Test intersection method with no intersection.
     */
    @Test
    public void testIntersectionWithNoIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(2.0, 0.0), new Vector2D(3.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.25), new Interval(0.5, 1.0)));
        Vector2D result = subLine1.intersection(new SubLine(line2, new IntervalsSet(new Interval(0.3, 0.6))), false);
        assertNull(result);
    }

    /**
     * Test intersection method with intersection.
     */
    @Test
    public void testIntersectionWithIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.0, 1.5)));
        Vector2D result = subLine1.intersection(new SubLine(line2, new IntervalsSet(new Interval(0.25, 0.75))), false);
        assertEquals(new Vector2D(0.5, 0.5), result);
    }

    /**
     * Test intersection method with intersection on endpoints.
     */
    @Test
    public void testIntersectionWithIntersectionOnEndpoints() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        Vector2D result = subLine1.intersection(new SubLine(line2, new IntervalsSet(new Interval(0.0, 1.0))), true);
        assertEquals(new Vector2D(0.5, 0.5), result);
    }

    /**
     * Test side method with parallel lines.
     */
    @Test
    public void testSideWithParallelLines() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        Side side = subLine1.side(new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0))));
        assertEquals(Side.HYPER, side);
    }

    /**
     * Test side method with intersecting lines.
     */
    @Test
    public void testSideWithIntersectingLines() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        Side side = subLine1.side(new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0))));
        assertEquals(Side.MINUS, side);
    }

    /**
     * Test split method with empty remaining region.
     */
    @Test
    public void testSplitWithEmptyRemainingRegion() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 0.0));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0.0, 1.0)));
        SubHyperplane<Euclidean1D> result = subLine.split(hyperplane);
        assertNull(result);
    }

    /**
     * Test split method with no intersection.
     */
    @Test
    public void testSplitWithNoIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(2.0, 0.0), new Vector2D(3.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.5), new Vector2D(2.0, 0.5));
        SubHyperplane<Euclidean1D> result = subLine1.split(hyperplane);
        assertNull(result);
    }

    /**
     * Test split method with intersection.
     */
    @Test
    public void testSplitWithIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(1.0, 0.5), new Vector2D(2.0, 0.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.5), new Vector2D(1.5, 0.5));
        SubHyperplane<Euclidean1D> result = subLine1.split(hyperplane);
        SubHyperplane<Euclidean1D> expectedResult = new SubHyperplane<Euclidean1D>(new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)), new IntervalsSet(new Interval(0.0, 0.5))), null);
        assertEquals(expectedResult, result);
    }

}