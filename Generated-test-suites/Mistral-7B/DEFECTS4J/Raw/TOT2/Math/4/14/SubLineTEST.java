import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructor() {
        // Test simple constructor
        SubLine subLine1 = new SubLine(new Hyperplane2D.Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                        new IntervalsSet(new Interval(0, 1), new Interval(2, 3)));
        assertEquals(new Hyperplane2D.Line(new Vector2D(0, 0), new Vector2D(1, 1)), subLine1.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(0, 1), new Interval(2, 3)), subLine1.getRemainingRegion());

        // Test constructor from two endpoints
        SubLine subLine2 = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(2, 2)), subLine2.getHyperplane());
        List<Segment> segments = subLine2.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(2, 2), new Line(new Vector2D(0, 0), new Vector2D(2, 2))), segments.get(0));

        // Test constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(2, 2)));
        SubLine subLine3 = new SubLine(segment);
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(2, 2)), subLine3.getHyperplane());
        segments = subLine3.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment, segments.get(0));

        // Test getSegments method
        SubLine subLine4 = new SubLine(new Vector2D(0, 0), new Vector2D(3, 3));
        segments = subLine4.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(3, 3))), segments.get(0));
        assertEquals(new Segment(new Vector2D(2, 2), new Vector2D(3, 3), new Line(new Vector2D(0, 0), new Vector2D(3, 3))), segments.get(1));
    }

    @Test
    public void testIntersection() {
        // Test intersection with same line
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine2 = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        assertEquals(null, subLine1.intersection(subLine2, true));

        // Test intersection with parallel lines
        SubLine subLine3 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine4 = new SubLine(new Vector2D(1, 0), new Vector2D(2, 0));
        assertEquals(null, subLine3.intersection(subLine4, true));

        // Test intersection with intersecting lines
        SubLine subLine5 = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine6 = new SubLine(new Vector2D(1, 1), new Vector2D(3, 3));
        Vector2D intersectionPoint = subLine5.intersection(subLine6, true);
        assertEquals(new Vector2D(1, 1), intersectionPoint);

        // Test intersection with empty sub-line
        SubLine subLine7 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine8 = new SubLine(new Vector2D(3, 3), new Vector2D(4, 4));
        assertEquals(null, subLine7.intersection(subLine8, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionNullSubLine() {
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine nullSubLine = null;
        subLine.intersection(nullSubLine, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionNullIncludeEndPoints() {
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        subLine.intersection(new SubLine(new Vector2D(1, 1), new Vector2D(3, 3)), false);
    }

}