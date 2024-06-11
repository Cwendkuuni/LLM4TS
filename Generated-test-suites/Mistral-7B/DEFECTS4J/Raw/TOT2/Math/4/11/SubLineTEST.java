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
        // Test simple constructor
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                        new IntervalsSet(new Interval(0, 0.5), new Interval(1.5, 2)));
        SubLine expectedSubLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                                               new IntervalsSet(new Interval(0, 0.5), new Interval(1.5, 2)));
        assertEquals(expectedSubLine.getHyperplane(), subLine1.getHyperplane());
        assertEquals(expectedSubLine.getRemainingRegion(), subLine1.getRemainingRegion());

        // Test constructor from two endpoints
        Vector2D startPoint = new Vector2D(0, 0);
        Vector2D endPoint   = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(startPoint, endPoint);
        SubLine expectedSubLine2 = new SubLine(new Line(startPoint, endPoint),
                                               new IntervalsSet(new Interval(startPoint.getX(), endPoint.getX())));
        assertEquals(expectedSubLine2.getHyperplane(), subLine2.getHyperplane());
        assertEquals(expectedSubLine2.getRemainingRegion(), subLine2.getRemainingRegion());

        // Test constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine3 = new SubLine(segment);
        SubLine expectedSubLine3 = new SubLine(new Line(segment.getStart(), segment.getEnd()),
                                               new IntervalsSet(new Interval(segment.getStart().getX(), segment.getEnd().getX())));
        assertEquals(expectedSubLine3.getHyperplane(), subLine3.getHyperplane());
        assertEquals(expectedSubLine3.getRemainingRegion(), subLine3.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Vector2D startPoint1 = new Vector2D(0, 0);
        Vector2D endPoint1   = new Vector2D(1, 1);
        Vector2D startPoint2 = new Vector2D(1, 1);
        Vector2D endPoint2   = new Vector2D(2, 2);
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(startPoint1.getX(), endPoint1.getX()),
                                                             new Interval(startPoint2.getX(), endPoint2.getX())));

        List<Segment> segments = subLine.getSegments();
        List<Segment> expectedSegments = new ArrayList<Segment>() {{
            add(new Segment(startPoint1, endPoint1, line));
            add(new Segment(startPoint2, endPoint2, line));
        }};
        assertEquals(expectedSegments.size(), segments.size());
        for (int i = 0; i < segments.size(); i++) {
            assertEquals(expectedSegments.get(i).getStart(), segments.get(i).getStart());
            assertEquals(expectedSegments.get(i).getEnd(), segments.get(i).getEnd());
        }
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 0.5), new Interval(1.5, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1), new Interval(1.5, 2)));

        // Test intersection on finite lines
        Vector2D expectedIntersection = new Vector2D(0.5, 0.5);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(expectedIntersection.getX(), intersection.getX(), 1e-6);
        assertEquals(expectedIntersection.getY(), intersection.getY(), 1e-6);

        // Test intersection on infinite lines
        line1 = new Line(new Vector2D(0, 0), new Vector2D(Double.POSITIVE_INFINITY, 0));
        line2 = new Line(new Vector2D(Double.NEGATIVE_INFINITY, 0), new Vector2D(0, Double.POSITIVE_INFINITY));
        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet());
        intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(Double.NEGATIVE_INFINITY, intersection.getX(), 1e-6);
        assertEquals(0, intersection.getY(), 1e-6);

        // Test intersection on infinite lines with different slopes
        line1 = new Line(new Vector2D(0, 0), new Vector2D(Double.POSITIVE_INFINITY, 1));
        line2 = new Line(new Vector2D(Double.NEGATIVE_INFINITY, 0), new Vector2D(0, Double.POSITIVE_INFINITY));
        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet());
        intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(Double.NEGATIVE_INFINITY, intersection.getX(), 1e-6);
        assertEquals(Double.POSITIVE_INFINITY, intersection.getY(), 1e-6);

        // Test intersection on infinite and finite lines
        line1 = new Line(new Vector2D(0, 0), new Vector2D(Double.POSITIVE_INFINITY, 0));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));
        intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(1.5, intersection.getX(), 1e-6);
        assertEquals(1.0, intersection.getY(), 1e-6);

        // Test intersection on infinite and finite lines with different slopes
        line1 = new Line(new Vector2D(0, 0), new Vector2D(Double.POSITIVE_INFINITY, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));
        intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D startPoint = new Vector2D(0, 0);
        Vector2D endPoint   = new Vector2D(1, 1);
        IntervalsSet expectedIntervalSet = new IntervalsSet(new Interval(startPoint.getX(), endPoint.getX()));
        IntervalsSet intervalSet = SubLine.buildIntervalSet(startPoint, endPoint);
        assertEquals(expectedIntervalSet, intervalSet);
    }
}