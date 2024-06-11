import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 0)).toImmutable();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        Assert.assertEquals(hyperplane, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
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
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);

        Line line = (Line) subLine.getHyperplane();
        Assert.assertEquals(start, line.getStartVector());
        Assert.assertEquals(end, line.getEndVector());

        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(segment, segments.get(0));
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        Line line = new Line(new Vector2D(0, 0), new Vector2D(4, 0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1, 2), new Interval(3, 4));
        SubLine subLine = new SubLine(line, remainingRegion);

        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Assert.assertEquals(start1, segment1.getStart());
        Assert.assertEquals(end1, segment1.getEnd());
        Segment segment2 = segments.get(1);
        Assert.assertEquals(start2, segment2.getStart());
        Assert.assertEquals(end2, segment2.getEnd());
    }

    @Test
    public void testIntersectionWithNullSubLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = null;
        Vector2D result = subLine.intersection(line1, false);
        Assert.assertNull(result);
    }

    @Test
    public void testIntersectionWithSameSubLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Vector2D result = subLine1.intersection(subLine1, false);
        Assert.assertNull(result);
    }

    @Test
    public void testIntersectionWithIntersectingSubLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Vector2D result = subLine1.intersection(subLine2, false);
        Assert.assertNotNull(result);
        Line expectedLine = new Line(new Vector2D(1, 1), new Vector2D(1, 1));
        Assert.assertEquals(expectedLine.getMidPoint(), result);
    }

    @Test
    public void testIntersectionWithNonIntersectingSubLines() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));
        Vector2D result = subLine1.intersection(subLine2, false);
        Assert.assertNull(result);
    }

    @Test
    public void testIntersectionWithIncludeEndPointsTrue() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));
        Vector2D result = subLine1.intersection(subLine2, true);
        Assert.assertNotNull(result);
        Line expectedLine = new Line(new Vector2D(0.5, 0.5), new Vector2D(0.5, 0.5));
        Assert.assertEquals(expectedLine.getMidPoint(), result);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet result = SubLine.buildIntervalSet(start, end);
        Interval expectedInterval = new Interval(start.getX(), end.getX());
        Assert.assertEquals(expectedInterval, result.getIntervals().get(0));
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(2, 0));
        Side result = subLine.side(hyperplane);
        Side expectedSide = Side.MINUS;
        Assert.assertEquals(expectedSide, result);

        hyperplane = new Line(new Vector2D(1, 1), new Vector2D(3, 1));
        result = subLine.side(hyperplane);
        expectedSide = Side.PLUS;
        Assert.assertEquals(expectedSide, result);

        hyperplane = new Line(new Vector2D(1, 0.5), new Vector2D(2, 0.5));
        result = subLine.side(hyperplane);
        expectedSide = Side.HYPER;
        Assert.assertEquals(expectedSide, result);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SplitSubHyperplane<Euclidean2D> result = subLine.split(hyperplane);
        SubLine subLinePlus = result.getSubHyperplane1();
        SubLine subLineMinus = result.getSubHyperplane2();

        Line expectedLine1 = new Line(new Vector2D(0, 0), new Vector2D(0.5, 0.5));
        Line expectedLine2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1, 1));
        Assert.assertEquals(expectedLine1, subLinePlus.getHyperplane());
        Assert.assertEquals(expectedLine2, subLineMinus.getHyperplane());

        Hyperplane<Euclidean2D> hyperplanePlus = new Line(new Vector2D(0, 0), new Vector2D(0.5, 0.5));
        Hyperplane<Euclidean2D> hyperplaneMinus = new Line(new Vector2D(0.5, 0.5), new Vector2D(1, 1));
        Assert.assertEquals(hyperplanePlus, subLinePlus.getHyperplane().getImmutable());
        Assert.assertEquals(hyperplaneMinus, subLineMinus.getHyperplane().getImmutable());

        SubHyperplane<Euclidean1D> subHyperplanePlus = new OrientedPoint(new Vector1D(0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> subHyperplaneMinus = new OrientedPoint(new Vector1D(0.5), true).wholeHyperplane();
        BSPTree<Euclidean1D> bspTreePlus = new BSPTree<Euclidean1D>(Boolean.FALSE, new BSPTree<Euclidean1D>(Boolean.FALSE), subHyperplanePlus, null);
        BSPTree<Euclidean1D> bspTreeMinus = new BSPTree<Euclidean1D>(Boolean.FALSE, new BSPTree<Euclidean1D>(Boolean.FALSE), subHyperplaneMinus, null);
        Assert.assertEquals(bspTreePlus, subLinePlus.getBSPTree());
        Assert.assertEquals(bspTreeMinus, subLineMinus.getBSPTree());
    }

}