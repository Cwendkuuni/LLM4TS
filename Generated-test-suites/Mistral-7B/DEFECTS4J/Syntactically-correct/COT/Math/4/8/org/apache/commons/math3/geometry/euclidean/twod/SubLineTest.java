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
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStartVector());
        assertEquals(end, line.getEndVector());
        IntervalsSet expectedIntervalSet = new IntervalsSet(new Interval(start.getX(), end.getX()));
        assertEquals(expectedIntervalSet, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStartVector());
        assertEquals(end, line.getEndVector());
        IntervalsSet expectedIntervalSet = new IntervalsSet(new Interval(start.getX(), end.getX()));
        assertEquals(expectedIntervalSet, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end1);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment expectedSegment = new Segment(start, new Vector2D(1, 1), new Line(start, end1));
        assertEquals(expectedSegment, segments.get(0));

        IntervalSet remainingRegion = new IntervalsSet(new Interval(end1.getX(), end2.getX()));
        SubLine subLine2 = new SubLine(start, end2, remainingRegion);
        segments = subLine2.getSegments();
        assertEquals(2, segments.size());
        expectedSegment = new Segment(new Vector2D(end1.getX(), end1.getY()), end2, new Line(start, end2));
        assertEquals(expectedSegment, segments.get(1));
    }

    @Test
    public void testIntersectionNull() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testIntersectionOnLine() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D expectedVector = new Vector2D(1, 1);
        Vector2D actualVector = subLine1.intersection(subLine2, false);
        assertEquals(expectedVector, actualVector);
    }

    @Test
    public void testIntersectionOutsideOfBothSubLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(


3, 3);
SubLine subLine1 = new SubLine(start1, end1);
SubLine subLine2 = new SubLine(start2, end2);
assertNull(subLine1.intersection(subLine2, false));

}

@Test
public void testIntersectionOnEndPointOfFirstSubLine() {
Vector2D start1 = new Vector2D(0, 0);
Vector2D end1 = new Vector2D(1, 1);
Vector2D start2 = new Vector2D(1, 1);
Vector2D end2 = new Vector2D(2, 2);
SubLine subLine1 = new SubLine(start1, end1);
SubLine subLine2 = new SubLine(start2, end2);
assertTrue(subLine1.isPointInRegion(new Vector2D(1, 1)));
Vector2D expectedVector = new Vector2D(1, 1);
Vector2D actualVector = subLine1.intersection(subLine2, true);
assertEquals(expectedVector, actualVector);
}

@Test
public void testIntersectionOnEndPointOfSecondSubLine() {
Vector2D start1 = new Vector2D(0, 0);
Vector2D end1 = new Vector2D(1, 1);
Vector2D start2 = new Vector2D(1, 1);
Vector2D end2 = new Vector2D(2, 2);
SubLine subLine1 = new SubLine(start1, end1);
SubLine subLine2 = new SubLine(start2, end2);
assertFalse(subLine2.isPointInRegion(new Vector2D(1, 1)));
assertNull(subLine1.intersection(subLine2, true));
}

@Test
public void testIntersectionOnBothEndPoints() {
Vector2D start1 = new Vector2D(0, 0);
Vector2D end1 = new Vector2D(1, 1);
Vector2D start2 = new Vector2D(1, 1);
Vector2D end2 = new Vector2D(1, 1);
SubLine subLine1 = new SubLine(start1, end1);
SubLine subLine2 = new SubLine(start2, end2);
assertTrue(subLine1.isPointInRegion(new Vector2D(1, 1)));
assertTrue(subLine2.isPointInRegion(new Vector2D(1, 1)));
Vector2D expectedVector = new Vector2D(1, 1);
Vector2D actualVector = subLine1.intersection(subLine2, true);
assertEquals(expectedVector, actualVector);
}

@Test
public void testBuildIntervalSet() {
Vector2D start = new Vector2D(0, 0);
Vector2D end = new Vector2D(1, 1);
IntervalsSet expectedIntervalSet = new IntervalsSet(new Interval(start.getX(), end.getX()));
IntervalsSet actualIntervalSet = SubLine.buildIntervalSet(start, end);
assertEquals(expectedIntervalSet, actualIntervalSet);
}

}