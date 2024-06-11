import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        SubLine subLine = new SubLine(start, end);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        assertEquals(new Line(start, end), subLine.getHyperplane());
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet remainingRegion = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0));
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(0.0, 0.5), new Vector2D(1.0, 0.5), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(1.5, 1.0), new Vector2D(2.0, 1.0), line), segments.get(1));
    }

    @Test
    public void testIntersectionWithOtherSubLine() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.0)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 1.0), intersection);
    }

    @Test
    public void testIntersectionWithOtherSubLineNoIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullSubLine() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0.0, 0.5)));
        subLine.intersection(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullIncludeEndPoints() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5)));
        Vector2D intersection = subLine1.intersection(new SubLine(line2, null), true);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        List<Interval> intervals = intervalSet.asList();
        assertEquals(1, intervals.size());
        assertEquals(start.getX(), intervals.get(0).getInf(), 1e-14);
        assertEquals(end.getX(), intervals.get(0).getSup(), 1e-14);
    }

    @Test
    public void testBuildNew() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5));
        SubLine subLine1 = new SubLine(line, remainingRegion);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine2 = (SubLine) SubLine.buildNew(hyperplane, remainingRegion2);
        assertEquals(hyperplane, subLine2.getHyperplane());
        assertEquals(remainingRegion2, subLine2.getRemainingRegion());
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5)));
        Hyperplane<Euclidean2D> hyperplane = line2;
        Side side = subLine1.side(hyperplane);
        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0));
        SubLine subLine = new SubLine(line, remainingRegion);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.0, 1.0));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        assertNotNull(splitSubHyperplane);
        SubLine subLinePlus = splitSubHyperplane.getSubHyperplanePlus();
        SubLine subLineMinus = splitSubHyperplane.getSubHyperplaneMinus();
        assertEquals(new Line(new Vector2D(0.0, 0.0), new Vector2D(0.5)), subLinePlus.getHyperplane());
        assertEquals(new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0)), subLineMinus.getHyperplane());
        List<Segment> segmentsPlus = subLinePlus.getSegments();
        List<Segment> segmentsMinus = subLineMinus.getSegments();
        assertEquals(1, segmentsPlus.size());
        assertEquals(new Segment(new Vector2D(0.0, 0.5), new Vector2D(0.5, 0.5), line), segmentsPlus.get(0));
        assertEquals(1, segmentsMinus.size());
        assertEquals(new Segment(new Vector2D(1.5, 1.0), new Vector2D(2.0, 2.0), line), segmentsMinus.get(0));
    }

    @Test
    public void testSplitParallelLines() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0));
        SubLine subLine = new SubLine(line, remainingRegion);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.0, 0.5));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        assertNotNull(splitSubHyperplane);
        SubLine subLinePlus = splitSubHyperplane.getSubHyperplanePlus();
        SubLine subLineMinus = splitSubHyperplane.getSubHyperplaneMinus();
        assertEquals(new Line(new Vector2D(0.0, 0.0), new Vector2D(0.5)), subLinePlus.getHyperplane());
        assertEquals(new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0)), subLineMinus.getHyperplane());
        List<Segment> segmentsPlus = subLinePlus.getSegments();
        List<Segment> segmentsMinus = subLineMinus.getSegments();
        assertEquals(1, segmentsPlus.size());
        assertEquals(new Segment(new Vector2D(0.0, 0.5), new Vector2D(0.5, 0.5), line), segmentsPlus.get(0));
        assertEquals(1, segmentsMinus.size());
        assertEquals(new Segment(new Vector2D(1.5, 1.0), new Vector2D(2.0, 2.0), line), segmentsMinus.get(0));
    }

    @Test
    public void testSplitLinesWithIntersection() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0));
        SubLine subLine = new SubLine(line, remainingRegion);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.75));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        assertNotNull(splitSubHyperplane);
        SubLine subLinePlus = splitSubHyperplane.getSubHyperplanePlus();
        SubLine subLineMinus = splitSubHyperplane.getSubHyperplaneMinus();
        assertEquals(new Line(new Vector2D(0.0, 0.0), new Vector2D(0.25)), subLinePlus.getHyperplane());
        assertEquals(new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0)), subLineMinus.getHyperplane());
        List<Segment> segmentsPlus = subLinePlus.getSegments();
        List<Segment> segmentsMinus = subLineMinus.getSegments();
        assertEquals(1, segmentsPlus.size());
        assertEquals(new Segment(new Vector2D(0.0, 0.25), new Vector2D(0.25, 0.25), line), segmentsPlus.get(0));
        assertEquals(1, segmentsMinus.size());
        assertEquals(new Segment(new Vector2D(1.5, 1.0), new Vector2D(2.0, 2.0), line), segmentsMinus.get(0));
    }

}