import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test construction from hyperplane and remaining region
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNotNull(subLine);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test construction from start and end points
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(3.0, 3.0);
        SubLine line = new SubLine(start, end);
        assertNotNull(line);
        assertEquals(new Line(start, end), line.getHyperplane());
        assertEquals(buildIntervalSet(start, end), line.getRemainingRegion());

        // Test construction from segment
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Line(new Vector2D(0.0, 0.0),
                                                                                              new Vector2D(2.0, 2.0)));
        SubLine subSegment = new SubLine(segment);
        assertNotNull(subSegment);
        assertEquals(segment.getLine(), subSegment.getHyperplane());
        assertEquals(segment.getIntervalsSet(), subSegment.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        // Test construction from hyperplane and remaining region
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0), new Line(new Vector2D(0.0, 0.0),
                                                                                         new Vector2D(1.0, 1.0))), segments.get(0));
        assertEquals(new Segment(new Vector2D(2.0, 2.0), new Vector2D(3.0, 3.0), new Line(new Vector2D(1.0, 1.0),
                                                                                         new Vector2D(2.0, 2.0))), segments.get(1));

        // Test construction from start and end points
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(3.0, 3.0);
        SubLine line = new SubLine(start, end);
        List<Segment> lineSegments = line.getSegments();
        assertEquals(1, lineSegments.size());
        assertEquals(new Segment(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0), new Line(new Vector2D(0.0, 0.0),
                                                                                      new Vector2D(3.0, 3.0))), lineSegments.get(0));
    }

    @Test
    public void testIntersectionWithOtherSubLine() {
        // Test intersection with a parallel line
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(3.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        // Test intersection with an intersecting line
        Line intersectingLine = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0));
        SubLine intersectingSubLine = new SubLine(intersectingLine, new IntervalsSet(new Interval(0.0, 1.0),
                                                                                         new Interval(2.0, 3.0)));
        intersectionPoint = subLine1.intersection(intersectingSubLine, false);
        assertNotNull(intersectionPoint);
        assertEquals(new Vector2D(1.0, 1.0), intersectionPoint);

        // Test intersection with an empty line
        Line emptyLine = new Line(new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
                                new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        SubLine emptySubLine = new SubLine(emptyLine, new IntervalsSet());
        intersectionPoint = subLine1.intersection(emptySubLine, false);
        assertNull(intersectionPoint);

        // Test intersection with a vertical line
        Line verticalLine = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        SubLine verticalSubLine = new SubLine(verticalLine, new IntervalsSet(new Interval(0.0, 1.0)));
        intersectionPoint = subLine1.intersection(verticalSubLine, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithSameSubLine() {
        // Test intersection with the same subline
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(line, remainingRegion);
        Vector2D intersectionPoint = subLine.intersection(subLine, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersectionWithSelf() {
        // Test intersection with the same subline
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(line, remainingRegion);
        Vector2D intersectionPoint = subLine.intersection(subLine, true);
        assertNull(intersectionPoint);
    }

    // Add more tests for other methods like split, side, etc., if needed

    private static IntervalsSet buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(), line.toSubSpace(end).getX());
    }

}