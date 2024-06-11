import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstructorWithHyperplaneAndRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1)).toImplicitForm();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineConstructorWithStartEnd() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        SubLine subLine = new SubLine(start, end);

        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStartVector());
        assertEquals(end, line.getEndVector());
        IntervalsSet iset = (IntervalsSet) subLine.getRemainingRegion();
        assertEquals(1, iset.size());
        assertEquals(new Interval(start.getX(), end.getX()), iset.get(0));
    }

    @Test
    public void testSubLineConstructorWithSegment() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);

        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStartVector());
        assertEquals(end, line.getEndVector());
        IntervalsSet iset = (IntervalsSet) subLine.getRemainingRegion();
        assertEquals(1, iset.size());
        assertEquals(new Interval(start.getX(), end.getX()), iset.get(0));
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet iset = new IntervalsSet(new Interval(0.0, 0.5), new Interval(0.75, 1.0));
        SubLine subLine = new SubLine(line, iset);

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        Segment segment1 = segments.get(0);
        Vector2D start1 = segment1.getStart();
        Vector2D end1 = segment1.getEnd();
        assertEquals(new Vector2D(0.0, 0.5), start1);
        assertEquals(new Vector2D(0.5, 0.5), end1);

        Segment segment2 = segments.get(1);
        Vector2D start2 = segment2.getStart();
        Vector2D end2 = segment2.getEnd();
        assertEquals(new Vector2D(0.75, 1.0), start2);
        assertEquals(new Vector2D(1.0, 1.0), end2);

        // check for empty sub-line
        iset = new IntervalsSet();
        SubLine emptySubLine = new SubLine(line, iset);
        segments = emptySubLine.getSegments();
        assertEquals(0, segments.size());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);

        // check intersection on finite lines
        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 2));
        line2 = new Line(new Vector2D(1, 2), new Vector2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 1.5)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));
        intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1.5, 2.0), intersection);

        // check intersection on infinite lines
        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet());
        subLine2 = new SubLine(line2, new IntervalsSet());
        intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }
}