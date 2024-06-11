import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test construction from hyperplane and remaining region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1)).toImplicitForm();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 0.5), new Interval(1, 2));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test construction from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine1.getHyperplane());
        List<Segment> segments = subLine1.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());

        // Test construction from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine2 = new SubLine(segment);
        assertEquals(segment.getLine(), subLine2.getHyperplane());
        segments = subLine2.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment.getStart(), segments.get(0).getStart());
        assertEquals(segment.getEnd(), segments.get(0).getEnd());
    }

    @Test
    public void testGetSegments() {
        // Test empty sub-line
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(0, 1)).toImplicitForm();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(0, segments.size());

        // Test sub-line covering the whole line
        hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        remainingRegion = new IntervalsSet(new Interval(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY),
                                           new Interval(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        subLine = new SubLine(hyperplane, remainingRegion);
        segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(Double.NEGATIVE_INFINITY, segment.getStart().getX(), 1e-9);
        assertEquals(Double.POSITIVE_INFINITY, segment.getEnd().getX(), 1e-9);

        // Test sub-line with two segments
        start = new Vector2D(0, 0);
        end = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        Line line = new Line(start, end);
        remainingRegion = new IntervalsSet(new Interval(0.5, 1), new Interval(1.5, 2));
        subLine = new SubLine(line, remainingRegion);
        segments = subLine.getSegments();
        assertEquals(2, segments.size());
        segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
        segment = segments.get(1);
        assertEquals(start2, segment.getStart());
        assertEquals(end2, segment.getEnd());
    }

    @Test
    public void testIntersection() {
        // Test intersection with null sub-line
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                         new IntervalsSet(new Interval(0, 0.5), new Interval(1, 2)));
        assertNull(subLine1.intersection(null, false));

        // Test intersection with same sub-line
        SubLine subLine2 = subLine1.copy();
        assertNull(subLine1.intersection(subLine2, false));

        // Test intersection with parallel sub-lines
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                         new IntervalsSet(new Interval(0, 1), new Interval(2, 3)));
        Vector2D intersectionPoint = subLine1.intersection(subLine3, false);
        assertNull(intersectionPoint);

        // Test intersection with intersecting sub-lines
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(2, 1));
        SubLine subLine4 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1), new Interval(1.5, 2)));
        SubLine subLine5 = new SubLine(line2, new IntervalsSet(new Interval(0, 0.5), new Interval(1, 1.5)));
        intersectionPoint = subLine4.intersection(subLine5, false);
        assertNotNull(intersectionPoint);
        assertEquals(0.75, intersectionPoint.getX(), 1e-9);
        assertEquals(0.75 * line1.getAngle() + line1.getIntercept(), intersectionPoint.getY(), 1e-9);

        // Test intersection with endpoints included
        SubLine subLine6 = new SubLine(line1, new IntervalsSet(new Interval(-1, 1), new Interval(2, 3)));
        intersectionPoint = subLine4.intersection(subLine6, true);
        assertNotNull(intersectionPoint);
        assertEquals(0.5, intersectionPoint.getX(), 1e-9);
        assertEquals(0.5 * line1.getAngle() + line1.getIntercept(), intersectionPoint.getY(), 1e-9);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithInvalidArguments() {
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                         new IntervalsSet(new Interval(0, 0.5), new Interval(1, 2)));
        SubLine nullSubLine = null;
        Vector2D intersectionPoint = subLine.intersection(nullSubLine, false);
    }

}