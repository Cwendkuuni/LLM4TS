import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Planes;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link SubLine}.
 */
public class SubLineTest {

    /**
     * Test method for {@link SubLine#SubLine(Vector2D, Vector2D)}.
     */
    @Test
    public void testSubLineConstructor() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Line line = new Line(new Point2D(1.0, 2.0), new Point2D(3.0, 4.0));
        final SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(1.5, 2.5)));
        assertEquals(line, subLine.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(1.5, 2.5)), subLine.getRemainingRegion());
    }

    /**
     * Test method for {@link SubLine#SubLine(Segment)}.
     */
    @Test
    public void testSubLineSegmentConstructor() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Point2D start = new Point2D(1.0, 2.0);
        final Point2D end   = new Point2D(3.0, 4.0);
        final Line line = new Line(start, end);
        final Segment segment = new Segment(start, end, line);
        final SubLine subLine = new SubLine(segment);
        assertEquals(line, subLine.getHyperplane());
        assertEquals(new IntervalsSet(segment.getStart(), segment.getEnd()), subLine.getRemainingRegion());
    }

    /**
     * Test method for {@link SubLine#getSegments()}.
     */
    @Test
    public void testGetSegments() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Line line = new Line(new Point2D(1.0, 2.0), new Point2D(3.0, 4.0));
        final Segment segment1 = new Segment(new Point2D(1.5, 2.5), new Point2D(2.5, 3.5), line);
        final Segment segment2 = new Segment(new Point2D(4.0, 2.5), new Point2D(5.0, 3.5), line);
        final SubLine subLine = new SubLine(line, new IntervalsSet(segment1.getStart(), segment2.getEnd()));
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(segment1, segments.get(0));
        assertEquals(segment2, segments.get(1));
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)}.
     */
    @Test
    public void testIntersection() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(2.0, 2.0));
        final Line line2 = new Line(new Point2D(1.0, 1.0), new Point2D(3.0, 3.0));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Point2D(1.0, 1.0), intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
        intersection = subLine1.intersection(subLine2, true);
        intersection = subLine2.intersection(subLine1, true);
        assertEquals(new Point2D(1.0, 1.0), intersection);
        intersection = subLine2.intersection(subLine1, false);
        assertNull(intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)}.
     * Test the case where both {@code SubLine}s are empty.
     */
    @Test
    public void testIntersectionEmpty() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(2.0, 2.0));
        final Line line2 = new Line(new Point2D(2.0, 2.0), new Point2D(4.0, 4.0));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet());
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
        intersection = subLine2.intersection(subLine1, true);
        assertNull(intersection);
        intersection = subLine2.intersection(subLine1, false);
        assertNull(intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)}.
     * Test the case where {@code SubLine}s intersect but no intersection point lies on both ranges.
     */
    @Test
    public void testIntersectionNoIntersectionPoint() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(2.0, 2.0));
        final Line line2 = new Line(new Point2D(1.0, 1.0), new Point2D(3.0, 3.0));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 3.5)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
        intersection = subLine2.intersection(subLine1, true);
        assertNull(intersection);
        intersection = subLine2.intersection(subLine1, false);
        assertNull(intersection);
    }

    /**
     * Test method for {@link SubLine#intersection(SubLine, boolean)}.
     * Test the case where {@code SubLine}s intersect and intersection point lies on both ranges.
     */
    @Test
    public void testIntersectionIntersectionPoint() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(2.0, 2.0));
        final Line line2 = new Line(new Point2D(1.0, 1.0), new Point2D(3.0, 3.0));
        final SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5)));
        final SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5), new Interval(2.5, 3.5)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Point2D(2.0, 2.0), intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Point2D(2.0, 2.0), intersection);
        intersection = subLine2.intersection(subLine1, true);
        assertEquals(new Point2D(2.0, 2.0), intersection);
        intersection = subLine2.intersection(subLine1, false);
        assertEquals(new Point2D(2.0, 2.0), intersection);
    }

    /**
     * Test method for {@link SubLine#split(Hyperplane)}.
     */
    @Test
    public void testSplit() {
        final Euclidean2DSpace space = new Euclidean2DSpace();
        final Line line = new Line(new Point2D(1.0, 2.0), new Point2D(3.0, 4.0));
        final SubLine subLine1 = new SubLine(line, new IntervalsSet(new Interval(1.5, 2.5)));
        final Hyperplane<Euclidean2D> hyperplane = Planes.xPlane();
        final SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane);
        final SubLine subLinePlus = splitSubHyperplane.getSubHyperplanePlus();
        final SubLine subLineMinus = splitSubHyperplane.getSubHyperplaneMinus();
        final Interval expectedIntervalPlus = new Interval(1.5, 1.75);
        final Interval expectedIntervalMinus = new Interval(2.25, 2.5);
        assertEquals(new SubLine(line, new IntervalsSet(expectedIntervalPlus)), subLinePlus);
        assertEquals(new SubLine(line, new IntervalsSet(expectedIntervalMinus)), subLineMinus);
        final Hyperplane<Euclidean2D> hyperplane2 = new Hyperplane<Euclidean2D>(new Point2D(0.0, 0.0), new Vector2D(1.0, -1.0));
        final SplitSubHyperplane<Euclidean2D> splitSubHyperplane2 = subLine1.split(hyperplane2);
        final SubLine subLineEmpty = splitSubHyperplane2.getSubHyperplanePlus();
        assertEquals(null, subLineEmpty);
        final SubLine subLineResult = splitSubHyperplane2.getSubHyperplaneMinus();
        final Interval expectedIntervalResult = new IntervalsSet(new Interval(1.75, 2.5)).getIntervals().get(0);
        assertEquals(new SubLine(line, new IntervalsSet(expectedIntervalResult)), subLineResult);
    }

}