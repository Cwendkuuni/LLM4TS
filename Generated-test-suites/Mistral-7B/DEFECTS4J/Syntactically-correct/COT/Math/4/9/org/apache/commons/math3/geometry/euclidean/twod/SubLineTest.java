import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSimpleConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructFromTwoEndpoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = buildIntervalSet(start, end);
        SubLine subLine = new SubLine(start, end);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructFromSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end);
        Hyperplane<Euclidean2D> hyperplane = segment.getLine();
        Region<Euclidean1D> remainingRegion = buildIntervalSet(start, end);
        SubLine subLine = new SubLine(segment);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Interval interval1 = new Interval(0.2, 0.4);
        Interval interval2 = new Interval(0.6, 0.8);
        IntervalsSet remainingRegion = new IntervalsSet(interval1, interval2);
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0.2, 0.2), segment1.getStart());
        assertEquals(new Vector2D(0.4, 0.4), segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(0.6, 0.6), segment2.getStart());
        assertEquals(new Vector2D(0.8, 0.8), segment2.getEnd());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullSubLine() {
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet());
        subLine.intersection(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithDifferentDimensions() {
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet());
        subLine.intersection(new SubLine(new Line(new Vector1D(0), new Vector1D(1)), new IntervalsSet()), true);
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());
        Vector2D intersectionPoint = new Vector2D(0.75, 0.75);
        assertTrue(subLine1.intersection(subLine2, true).equals(intersectionPoint));
        assertTrue(subLine2.intersection(subLine1, true).equals(intersectionPoint));
    }

    @Test
    public void testIntersectionWithDifferentEndpoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0.5, 1), new Vector2D(2, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine2.intersection(subLine1, true));
    }

    @Test
    public void testIntersectionWithDifferentRemainingRegions() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        Region<Euclidean1D> region1 = new IntervalsSet(new Interval(0.2, 0.4), new Interval(0.6, 0.8));
        Region<Euclidean1D> region2 = new IntervalsSet(new Interval(0.3, 0.5), new Interval(0.7, 0.9));
        SubLine subLine1 = new SubLine(line1, region1);
        SubLine subLine2 = new SubLine(line2, region2);
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine2.intersection(subLine1, true));
    }

    @Test
    public void testIntersectionWithEmptyRegions() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        Region<Euclidean1D> region1 = new IntervalsSet();
        Region<Euclidean1D> region2 = new IntervalsSet(new Interval(0.3, 0.5), new Interval(0.7, 0.9));
        SubLine subLine1 = new SubLine(line1, region1);
        SubLine subLine2 = new SubLine(line2, region2);
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine2.intersection(subLine1, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSideWithNullHyperplane() {
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet());
        subLine.side(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitWithNullHyperplane() {
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet());
        subLine.split(null);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(0.5, 0.5));
        Side side = subLine1.side(hyperplane);
        assertEquals(Side.MINUS, side);
        side = subLine2.side(hyperplane);
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(0.5, 0.5));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        SubLine subLinePlus = split.getPlus();
        SubLine subLineMinus = split.getMinus();
        assertEquals(subLine1, subLinePlus);
        assertEquals(subLine2, subLineMinus);

        hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(0.5, 0.5));
        hyperplane.setOffset(-1.0);
        split = subLine1.split(hyperplane);
        subLinePlus = split.getPlus();
        subLineMinus = split.getMinus();
        assertEquals(subLine2, subLinePlus);
        assertEquals(subLine1, subLineMinus);
    }

    private Region<Euclidean1D> buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
    }

}