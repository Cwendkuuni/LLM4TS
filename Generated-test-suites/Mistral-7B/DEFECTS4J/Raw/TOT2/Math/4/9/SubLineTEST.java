import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.subspace.SubHyperplane;
import org.apache.commons.math3.geometry.euclidean.twod.subspace.SubLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test construction with Hyperplane and IntervalsSet
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        IntervalsSet intervals = new IntervalsSet(start.getX(), end.getX());
        SubLine subLine = new SubLine(hyperplane, intervals);
        assertEquals("Check sub-line endpoints", new Segment(start, end).getEnd(), subLine.getSegments().get(0).getEnd());

        // Test construction with Line and IntervalsSet
        Line line = new Line(start, end);
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(start.getX(), end.getX()));
        SubLine subLineTest = new SubLine(line, intervalsSet);
        assertEquals("Check sub-line hyperplane", line, subLineTest.getHyperplane());
        assertEquals("Check sub-line endpoints", new Segment(start, end).getEnd(), subLineTest.getSegments().get(0).getEnd());

        // Test construction with Segment
        Segment segment = new Segment(start, end);
        SubLine subLineSegment = new SubLine(segment);
        assertEquals("Check sub-line hyperplane", line, subLineSegment.getHyperplane());
        assertEquals("Check sub-line endpoints", new Segment(start, end).getEnd(), subLineSegment.getSegments().get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1   = new Vector2D(2.0, 2.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2   = new Vector2D(3.0, 3.0);

        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(line1.toSubSpace(start1).getX(), line1.toSubSpace(end1).getX()));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(line2.toSubSpace(start2).getX(), line2.toSubSpace(end2).getX()));

        // Test intersection of two sub-lines that do not intersect
        assertNull("Check intersection of non-intersecting sub-lines", subLine1.intersection(subLine2, false));

        // Test intersection of two sub-lines that intersect at a point
        Vector2D intersectionPoint = new Vector2D(1.5, 1.5);
        SubLine subLine1Intersect = new SubLine(line1, new IntervalsSet(line1.toSubSpace(intersectionPoint).getX(), line1.toSubSpace(intersectionPoint).getX()));
        SubLine subLine2Intersect = new SubLine(line2, new IntervalsSet(line2.toSubSpace(intersectionPoint).getX(), line2.toSubSpace(intersectionPoint).getX()));
        assertEquals("Check intersection of sub-lines that intersect at a point", intersectionPoint, subLine1Intersect.intersection(subLine2Intersect, false));

        // Test intersection of two sub-lines that intersect at an endpoint
        start1 = new Vector2D(0.0, 0.0);
        end1 = new Vector2D(1.0, 1.0);
        start2 = new Vector2D(1.0, 1.0);
        end2 = new Vector2D(1.0, 2.0);
        SubLine subLine1Endpoint = new SubLine(line1, new IntervalsSet(line1.toSubSpace(start1).getX(), line1.toSubSpace(start1).getX()));
        SubLine subLine2Endpoint = new SubLine(line2, new IntervalsSet(line2.toSubSpace(end2).getX(), line2.toSubSpace(end2).getX()));
        assertNull("Check intersection of sub-lines that intersect at an endpoint (inclusive endpoints)", subLine1Endpoint.intersection(subLine2Endpoint, false));

        // Test intersection of two sub-lines that intersect at an endpoint (exclusive endpoints)
        start1 = new Vector2D(0.0, 0.0);
        end1 = new Vector2D(1.0, 1.0);
        start2 = new Vector2D(1.0, 1.0);
        end2 = new Vector2D(1.0, 2.0);
        SubLine subLine1EndpointExclusive = new SubLine(line1, new IntervalsSet(line1.toSubSpace(start1).getX(), line1.toSubSpace(start1).getX(), true));
        SubLine subLine2EndpointExclusive = new SubLine(line2, new IntervalsSet(line2.toSubSpace(end2).getX(), line2.toSubSpace(end2).getX(), true));
        assertNull("Check intersection of sub-lines that intersect at an endpoint (exclusive endpoints)", subLine1EndpointExclusive.intersection(subLine2EndpointExclusive, false));
    }

    @Test
    public void testSplit() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end   = new Vector2D(4.0, 4.0);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Interval interval1 = new Interval(0.0, 1.0);
        Interval interval2 = new Interval(2.0, 3.0);
        IntervalsSet intervals = new IntervalsSet(interval1, interval2);
        SubLine subLine = new SubLine(hyperplane, intervals);

        // Test splitting a sub-line by a hyperplane that is parallel to it
        Hyperplane<Euclidean2D> hyperplaneParallel = new Line(new Vector2D(1.0, 0.0), new Vector2D(5.0, 0.0));
        SplitSubHyperplane<Euclidean2D> splitResult = subLine.split(hyperplaneParallel);
        assertEquals("Check split result is composed of two sub-lines", 2, splitResult.getNumberOfSubHyperplanes());
        assertEquals("Check first sub-line is the same as original sub-line", subLine, splitResult.getSubHyperplane(0));
        assertEquals("Check second sub-line is empty", null, splitResult.getSubHyperplane(1));

        // Test splitting a sub-line by a hyperplane that is not parallel to it
        Vector2D point = new Vector2D(2.0, 2.0);
        Hyperplane<Euclidean2D> hyperplaneNotParallel = new Line(start, point);
        SplitSubHyperplane<Euclidean2D> splitResultNotParallel = subLine.split(hyperplaneNotParallel);
        assertEquals("Check split result is composed of two sub-lines", 2, splitResultNotParallel.getNumberOfSubHyperplanes());
        assertFalse("Check first sub-line is not the same as original sub-line", subLine.equals(splitResultNotParallel.getSubHyperplane(0)));
        assertFalse("Check second sub-line is not the same as original sub-line", subLine.equals(splitResultNotParallel.getSubHyperplane(1)));
    }
}