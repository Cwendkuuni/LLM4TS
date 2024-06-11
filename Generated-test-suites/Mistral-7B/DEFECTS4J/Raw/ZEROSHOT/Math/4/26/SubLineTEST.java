import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        SubLine line = new SubLine(start, end);
        assertEquals(space.makeLine(start, end), line.getHyperplane());
        IntervalsSet expected = new IntervalsSet(new Interval(start.getX(), end.getX()));
        assertEquals(expected, line.getRemainingRegion());
    }

    @Test
    public void testIntersection() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));
        Vector2D expected = new Vector2D(1.0, 1.0);
        assertEquals(expected, subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testGetSegments() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        IntervalsSet intervals = new IntervalsSet(new Interval(start.getX(), end.getX()),
                                                   new Interval(start.getX() + 1.0, end.getX() + 1.0));
        SubLine subLine = new SubLine(line, intervals);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);
        assertEquals(start, segment1.getStart());
        assertEquals(new Vector2D(1.0, 1.0), segment1.getEnd());
        assertEquals(new Vector2D(2.0, 2.0), segment2.getStart());
        assertEquals(end, segment2.getEnd());
    }
}