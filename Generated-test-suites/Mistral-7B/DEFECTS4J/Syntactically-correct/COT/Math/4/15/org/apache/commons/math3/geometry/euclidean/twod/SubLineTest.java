import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRegion() {
        Euclidean2D space = new Euclidean2D();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Euclidean2D space = new Euclidean2D();
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }

    @Test
    public void testConstructorWithSegment() {
        Euclidean2D space = new Euclidean2D();
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        assertEquals(segment.getLine(), subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment testSegment = segments.get(0);
        assertEquals(start, testSegment.getStart());
        assertEquals(end, testSegment.getEnd());
    }

    @Test
    public void testGetSegments() {
        Euclidean2D space = new Euclidean2D();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 0.5), new Interval(1, 2), new Interval(3, 4));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(3, segments.size());
        Segment segment1 = segments.get(0);
        assertEquals(new Vector2D(0, 0.5), segment1.getStart());
        assertEquals(new Vector2D(1, 0.5), segment1.getEnd());
        Segment segment2 = segments.get(1);
        assertEquals(new Vector2D(1, 1), segment2.getStart());
        assertEquals(new Vector2D(1, 2), segment2.getEnd());
        Segment segment3 = segments.get(2);
        assertEquals(new Vector2D(3, 4), segment3.getStart());
        assertEquals(new Vector2D(3, 4), segment3.getEnd());
    }

    @Test
    public void testIntersectionWithNullSubLine() {
        Euclidean2D space = new Euclidean2D();
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = null;
        Vector2D intersectionPoint = new SubLine(line1, new IntervalsSet(new Interval(0, 1))).intersection(subLine, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithSameLine() {
        Euclidean2D space = new Euclidean2D();
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Vector2D intersectionPoint = subLine.intersection(subLine, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithParallelLines() {
        Euclidean2D space = new Euclidean2D();
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithIntersectingLines() {
        Euclidean2D space = new Euclidean2D();
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1, 1), intersectionPoint);
    }

    @Test
    public void testIntersectionWithOverlappingLines() {
        Euclidean2D space = new Euclidean2D();
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 2)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithEndpoints() {
        Euclidean2D space = new Euclidean2D();
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5), new Interval(1, 1)));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1), new Interval(1, 2)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithEndpointsInBothSubLines() {
        Euclidean2D space = new Euclidean2D();
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5), new Interval(1, 1)));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 0.5), new Interval(1, 1)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);
    }

    @Test
    public void testIntersectionWithPointOutsideBothSubLines() {
        Euclidean2D space = new Euclidean2D();
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5), new Interval(1, 1)));
        Line line2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3), new Interval(3, 3)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

}