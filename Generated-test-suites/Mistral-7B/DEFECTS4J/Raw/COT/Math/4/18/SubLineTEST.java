import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNotNull(subLine);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }

    @Test
    public void testSubLineConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        assertNotNull(subLine);
        assertEquals(segment.getLine(), subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segmentTest = segments.get(0);
        assertEquals(start, segmentTest.getStart());
        assertEquals(end, segmentTest.getEnd());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubLineConstructorWithNullHyperplane() {
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        Hyperplane<Euclidean2D> hyperplane = null;
        new SubLine(hyperplane, remainingRegion);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubLineConstructorWithNullRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = null;
        new SubLine(hyperplane, remainingRegion);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubLineConstructorWithStartAndNullEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = null;
        new SubLine(start, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubLineConstructorWithNullStartAndEnd() {
        Vector2D start = null;
        Vector2D end = new Vector2D(0, 0);
        new SubLine(start, end);
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());

        start = new Vector2D(0, 0);
        end = new Vector2D(1, 1);
        start.setX(Double.POSITIVE_INFINITY);
        end.setX(Double.POSITIVE_INFINITY);
        hyperplane = new Line(start, end).toHyperplane();
        remainingRegion = new IntervalsSet(new Interval(0, 1));
        subLine = new SubLine(hyperplane, remainingRegion);
        segments = subLine.getSegments();
        assertEquals(1, segments.size());
        segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());

        start = new Vector2D(0, 0);
        end = new Vector2D(1, 1);
        hyperplane = new Line(start, end).toHyperplane();
        remainingRegion = new IntervalsSet(new Interval(1, 2));
        subLine = new SubLine(hyperplane, remainingRegion);
        segments = subLine.getSegments();
        assertEquals(0, segments.size());

        start = new Vector2D(0, 0);
        end = new Vector2D(1, 1);
        start.setY(Double.POSITIVE_INFINITY);
        end.setY(Double.POSITIVE_INFINITY);
        hyperplane = new Line(start, end).toHyperplane();
        remainingRegion = new IntervalsSet(new Interval(0, 1));
        subLine = new SubLine(hyperplane, remainingRegion);
        segments = subLine.getSegments();
        assertEquals(0, segments.size());

        start = new Vector2D(0, 0);
        end = new Vector2D(1, 1);
        hyperplane = new Line(start, end).toHyperplane();
        remainingRegion = new IntervalsSet(new Interval(0, 0));
        subLine = new SubLine(hyperplane, remainingRegion);
        segments = subLine.getSegments();
        assertEquals(0, segments.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullSubLine() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine thisSubLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine otherSubLine = null;
        thisSubLine.intersection(otherSubLine, true);
    }

    @Test
    public void testIntersectionWithSubLineOnSameLine() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        SubLine thisSubLine = new SubLine(start1, end1);
        SubLine otherSubLine = new SubLine(start2, end2);
        Vector2D intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithSubLineIntersectingEndPoint() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        SubLine thisSubLine = new SubLine(start1, end1);
        SubLine otherSubLine = new SubLine(start2, end2);
        Vector2D intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNotNull(intersectionPoint);
        assertEquals(start2, intersectionPoint);

        start2 = new Vector2D(1, 1);
        start2.setX(Double.POSITIVE_INFINITY);
        end2 = new Vector2D(2, 2);
        otherSubLine = new SubLine(start2, end2);
        intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNotNull(intersectionPoint);
        assertEquals(end1, intersectionPoint);
    }

    @Test
    public void testIntersectionWithSubLineIntersectingMiddlePoint() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0.5, 0.5);
        Vector2D end2 = new Vector2D(1.5, 1.5);
        SubLine thisSubLine = new SubLine(start1, end1);
        SubLine otherSubLine = new SubLine(start2, end2);
        Vector2D intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNotNull(intersectionPoint);
        // Checking intersectionPoint coordinates are between start1 and end1
        assertTrue(start1.getX() <= intersectionPoint.getX() && intersectionPoint.getX() <= end1.getX());
        assertTrue(start1.getY() <= intersectionPoint.getY() && intersectionPoint.getY() <= end1.getY());

        start2 = new Vector2D(0.5, 0.5);
        start2.setX(Double.NEGATIVE_INFINITY);
        end2 = new Vector2D(1.5, 1.5);
        otherSubLine = new SubLine(start2, end2);
        intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNotNull(intersectionPoint);
        // Checking intersectionPoint coordinates are between start2 and end2
        assertTrue(start2.getX() <= intersectionPoint.getX() && intersectionPoint.getX() <= end2.getX());
        assertTrue(start2.getY() <= intersectionPoint.getY() && intersectionPoint.getY() <= end2.getY());
    }

    @Test
    public void testIntersectionWithSubLineNotIntersecting() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        SubLine thisSubLine = new SubLine(start1, end1);
        SubLine otherSubLine = new SubLine(start2, end2);
        Vector2D intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNull(intersectionPoint);

        start2 = new Vector2D(1, 1);
        start2.setX(Double.POSITIVE_INFINITY);
        end2 = new Vector2D(2, 2);
        otherSubLine = new SubLine(start2, end2);
        intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNull(intersectionPoint);

        start2 = new Vector2D(1, 1);
        start2.setY(Double.POSITIVE_INFINITY);
        end2 = new Vector2D(2, 2);
        otherSubLine = new SubLine(start2, end2);
        intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNull(intersectionPoint);

        start1 = new Vector2D(0, 0);
        end1 = new Vector2D(1, 1);
        start2 = new Vector2D(2, 2);
        end2 = new Vector2D(3, 3);
        thisSubLine = new SubLine(start1, end1);
        otherSubLine = new SubLine(start2, end2);
        intersectionPoint = thisSubLine.intersection(otherSubLine, true);
        assertNull(intersectionPoint);
    }

}