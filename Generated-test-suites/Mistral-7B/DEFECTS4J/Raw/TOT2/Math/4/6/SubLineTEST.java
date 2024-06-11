import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        // Test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        assertEquals("Unexpected hyperplane", hyperplane, subLine.getHyperplane());
        assertEquals("Unexpected remaining region", remainingRegion, subLine.getRemainingRegion());

        // Test constructor from two endpoints
        Hyperplane<Euclidean2D> hyperplaneLine = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegionLine = new IntervalsSet(new Interval(0, 1));
        SubLine subLineLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        assertEquals("Unexpected hyperplane", hyperplaneLine, subLineLine.getHyperplane());
        assertEquals("Unexpected remaining region", remainingRegionLine, subLineLine.getRemainingRegion());

        // Test constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), hyperplaneLine);
        SubLine subLineSegment = new SubLine(segment);
        assertEquals("Unexpected hyperplane", hyperplaneLine, subLineSegment.getHyperplane());
        assertEquals("Unexpected remaining region", remainingRegionLine, subLineSegment.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0, 0.5), new Interval(0.5, 1)));
        List<Segment> segments = subLine.getSegments();

        assertEquals("Unexpected number of segments", 2, segments.size());
        assertEquals("Unexpected first segment start", new Vector2D(0, 0), segments.get(0).getStart());
        assertEquals("Unexpected first segment end", new Vector2D(0.5, 0.5), segments.get(0).getEnd());
        assertEquals("Unexpected second segment start", new Vector2D(0.5, 0.5), segments.get(1).getStart());
        assertEquals("Unexpected second segment end", new Vector2D(1, 1), segments.get(1).getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));

        // Test intersection on same line
        Vector2D intersection = subLine1.intersection(subLine1, true);
        assertNotNull("Unexpected null intersection", intersection);
        assertEquals("Unexpected intersection coordinates", new Vector2D(0.5, 0.5), intersection);

        // Test intersection on different lines, with intersection
        intersection = subLine1.intersection(subLine2, true);
        assertNotNull("Unexpected null intersection", intersection);
        assertEquals("Unexpected intersection coordinates", new Vector2D(1, 1), intersection);

        // Test intersection on different lines, without intersection
        intersection = subLine1.intersection(subLine2, false);
        assertNull("Unexpected non-null intersection", intersection);
    }

    @Test
    public void testIntersectionEmpty() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));

        // Test intersection on empty sub-line
        Vector2D intersection = subLine1.intersection(subLine1, true);
        assertNull("Unexpected non-null intersection", intersection);

        // Test intersection on non-empty sub-line
        intersection = subLine1.intersection(subLine2, true);
        assertNull("Unexpected non-null intersection", intersection);
    }
}