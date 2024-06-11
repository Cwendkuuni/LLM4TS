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
    public void testConstructorWithHyperplaneAndRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertSame(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        SubLine subLine = new SubLine(start, end);
        Line expectedLine = new Line(start, end);
        assertEquals(expectedLine, subLine.getHyperplane());
        IntervalSet expectedIntervalSet = new IntervalsSet(new Interval(start.getX(), end.getX()));
        assertEquals(expectedIntervalSet, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        Line expectedLine = segment.getLine();
        assertEquals(expectedLine, subLine.getHyperplane());
        assertEquals(segment.getIntervalsSet(), subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.0, 0.0);
        Vector2D end2 = new Vector2D(2.0, 0.0);
        Line line = new Line(start1, end1);
        IntervalsSet intervalSet = new IntervalsSet(new Interval(start1.getX(), end1.getX()),
                                                      new Interval(start2.getX(), end2.getX()));
        SubLine subLine = new SubLine(line, intervalSet);
        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(new Segment(start1, end1, line));
        expectedSegments.add(new Segment(start2, end2, line));
        List<Segment> segments = subLine.getSegments();
        assertEquals(expectedSegments, segments);
    }

    @Test
    public void testIntersectionWithAnotherSubLineWhenBothAreBounded() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 1.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(start1.getX(), end1.getX()));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(start2.getX(), end2.getX()));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        Vector2D expectedIntersection = new Vector2D(1.0, 0.5);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(expectedIntersection, intersection);
    }

    @Test
    public void testIntersectionWithAnotherSubLineWhenBothAreUnbounded() {
        Vector2D start1 = new Vector2D(-100.0, 0.0);
        Vector2D end1 = new Vector2D(100.0, 0.0);
        Vector2D start2 = new Vector2D(-100.0, 0.0);
        Vector2D end2 = new Vector2D(100.0, 0.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(start1.getX(), end1.getX()));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(start2.getX(), end2.getX()));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        Vector2D expectedIntersection = null;
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithAnotherSubLineWhenOneIsUnbounded() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(-100.0, 0.0);
        Vector2D end2 = new Vector2D(100.0, 0.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalsSet1 = new IntervalsSet(new Interval(start1.getX(), end1.getX()));
        IntervalsSet intervalsSet2 = new IntervalsSet(new Interval(start2.getX(), end2.getX()));
        SubLine subLine1 = new SubLine(line1, intervalsSet1);
        SubLine subLine2 = new SubLine(line2, intervalsSet2);
        Vector2D expectedIntersection = null;
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithAnotherSubLineWhenBothAreNull() {
        SubLine subLine1 = null;
        SubLine subLine2 = null;
        Vector2D intersection = subLine1.intersection(subLine2, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithAnotherSubLineWhenOneIsNull() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)),
                                        new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = null;
        Vector2D intersection = subLine1.intersection(subLine2, true);
    }

}