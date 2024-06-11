import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        
        Assert.assertEquals(hyperplane, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 0);
        SubLine subLine = new SubLine(start, end);
        
        Line line = (Line) subLine.getHyperplane();
        Assert.assertEquals(start, line.getStartVector());
        Assert.assertEquals(end, line.getEndVector());

        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        Assert.assertEquals(start, segment.getStart());
        Assert.assertEquals(end, segment.getEnd());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 0);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        
        Line line = (Line) subLine.getHyperplane();
        Assert.assertEquals(start, line.getStartVector());
        Assert.assertEquals(end, line.getEndVector());

        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Segment testSegment = segments.get(0);
        Assert.assertEquals(start, testSegment.getStart());
        Assert.assertEquals(end, testSegment.getEnd());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Region<Euclidean1D> region = new IntervalsSet(new Interval(1, 2), new Interval(3, 4));
        SubLine subLine = new SubLine(line, region);
        
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(2, segments.size());

        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);

        Assert.assertEquals(new Vector2D(1, 1), segment1.getStart());
        Assert.assertEquals(new Vector2D(1, 2), segment1.getEnd());
        Assert.assertEquals(new Vector2D(3, 3), segment2.getStart());
        Assert.assertEquals(new Vector2D(3, 4), segment2.getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(2, 2), intersectionPoint);
    }

    @Test
    public void testIntersectionWithNull() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));

        Assert.assertNull(subLine1.intersection(subLine2, true));
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end   = new Vector2D(1, 1);
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);

        List<Interval> intervals = intervalsSet.asList();
        Assert.assertEquals(1, intervals.size());

        Interval interval = intervals.get(0);
        Assert.assertEquals(0.0, interval.getInf().getX(), 1e-14);
        Assert.assertEquals(1.0, interval.getSup().getX(), 1e-14);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        Side side = subLine1.side(line2.getHyperplane());
        Assert.assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 0), new Vector2D(2, 2));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        SubLine subLinePlus = split.getSubHyperplane1();
        SubLine subLineMinus = split.getSubHyperplane2();

        Assert.assertEquals(subLine1, subLinePlus);
        Assert.assertEquals(subLine2, subLineMinus);
    }

}