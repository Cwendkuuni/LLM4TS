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
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1.5, 2.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        Assert.assertEquals(hyperplane, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        SubLine subLine = new SubLine(start, end);

        Line expectedLine = new Line(start, end);
        Region expectedRegion = new IntervalsSet(expectedLine.toSubSpace(start).getX(),
                expectedLine.toSubSpace(end).getX());

        Assert.assertEquals(expectedLine, subLine.getHyperplane());
        Assert.assertEquals(expectedRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);

        Line expectedLine = new Line(start, end);
        Region expectedRegion = new IntervalsSet(expectedLine.toSubSpace(start).getX(),
                expectedLine.toSubSpace(end).getX());

        Assert.assertEquals(expectedLine, subLine.getHyperplane());
        Assert.assertEquals(expectedRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(2.0, 0.0);
        Vector2D end2 = new Vector2D(3.0, 0.0);
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 0.0));

        Region region = new IntervalsSet(line.toSubSpace(start1).getX(),
                line.toSubSpace(end1).getX(),
                line.toSubSpace(start2).getX(),
                line.toSubSpace(end2).getX());

        SubLine subLine = new SubLine(line, region);

        List<Segment> segments = subLine.getSegments();

        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(new Segment(start1, end1, line));
        expectedSegments.add(new Segment(start2, end2, line));

        Assert.assertEquals(expectedSegments, segments);
    }

    @Test
    public void testIntersectionWithNullSubLine() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        Line line = new Line(start, end);

        SubLine subLine = null;

        Vector2D result = subLine.intersection(line, true);

        Assert.assertNull(result);
    }

    @Test
    public void testIntersectionWithSameLine() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        Line line = new Line(start, end);

        SubLine subLine = new SubLine(line, new IntervalsSet(start.toSpace(line), end.toSpace(line)));

        Vector2D result = subLine.intersection(subLine, true);

        Assert.assertEquals(start, result);
    }

    @Test
    public void testIntersectionWithParallelLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 1.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(start1.toSpace(line1), end1.toSpace(line1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(start2.toSpace(line2), end2.toSpace(line2)));

        Vector2D result = subLine1.intersection(subLine2, true);

        Assert.assertEquals(result, null);
    }

    @Test
    public void testIntersectionWithIntersectingLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.5, 1.0);
        Vector2D end2 = new Vector2D(2.5, 1.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(start1.toSpace(line1), end1.toSpace(line1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(start2.toSpace(line2), end2.toSpace(line2)));

        Vector2D result = subLine1.intersection(subLine2, true);

        Vector2D expectedResult = new Vector2D(1.0, 1.0);

        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIntersectionWithEndPointsOnSubLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(0.5, 0.5);
        Vector2D end2 = new Vector2D(1.5, 0.5);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(start1.toSpace(line1), end1.toSpace(line1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(start2.toSpace(line2), end2.toSpace(line2)));

        Vector2D result = subLine1.intersection(subLine2, true);

        Assert.assertEquals(start2, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithInvalidIncludeEndPoints() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(0.5, 0.5);
        Vector2D end2 = new Vector2D(1.5, 0.5);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(start1.toSpace(line1), end1.toSpace(line1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(start2.toSpace(line2), end2.toSpace(line2)));

        subLine1.intersection(subLine2, false);
    }

    @Test
    public void testSideMethodWithParallelLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 1.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(start1.toSpace(line1), end1.toSpace(line1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(start2.toSpace(line2), end2.toSpace(line2)));

        Side result = subLine1.side(line2.toHyperplane());

        Assert.assertEquals(Side.PARALLEL, result);
    }

    @Test
    public void testSideMethodWithIntersectingLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.5, 1.0);
        Vector2D end2 = new Vector2D(2.5, 1.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(start1.toSpace(line1), end1.toSpace(line1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(start2.toSpace(line2), end2.toSpace(line2)));

        Side result = subLine1.side(line2.toHyperplane());

        Assert.assertEquals(Side.INTERSECTS, result);
    }

    @Test
    public void testSplitMethodWithNullHyperplane() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        Line line = new Line(start, end);

        SubLine subLine = new SubLine(line, new IntervalsSet(start.toSpace(line), end.toSpace(line)));

        Hyperplane<Euclidean2D> hyperplane = null;

        SplitSubHyperplane<Euclidean2D> result = subLine.split(hyperplane);

        Assert.assertNull(result);
    }

    @Test
    public void testSplitMethodWithSameHyperplane() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 0.0);
        Line line = new Line(start, end);

        SubLine subLine = new SubLine(line, new IntervalsSet(start.toSpace(line), end.toSpace(line)));

        Hyperplane<Euclidean2D> hyperplane = new Line(start, end).toHyperplane();

        SplitSubHyperplane<Euclidean2D> result = subLine.split(hyperplane);

        Assert.assertEquals(subLine, result.getSubHyperplane1());
        Assert.assertEquals(subLine, result.getSubHyperplane2());
    }

    @Test
    public void testSplitMethodWithIntersectingHyperplanes() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 0.0);
        Vector2D start2 = new Vector2D(1.5, 1.0);
        Vector2D end2 = new Vector2D(2.5, 1.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);

        SubLine subLine = new SubLine(line1, new IntervalsSet(start1.toSpace(line1), end1.toSpace(line1)));

        Hyperplane<Euclidean2D> hyperplane = line2.toHyperplane();

        SplitSubHyperplane<Euclidean2D> result = subLine.split(hyperplane);

        SubLine subLine1 = new SubLine(line1.copySelf(), new IntervalsSet(new Interval(start1.getX(), start2.getX()),
                new Interval(end2.getX(), end1.getX())));
        SubLine subLine2 = new SubLine(line1.copySelf(), new IntervalsSet(new Interval(start2.getX(), end2.getX()),
                new Interval(start1.getX(), start2.getX()),
                new Interval(end1.getX(), end2.getX())));

        Assert.assertEquals(subLine1, result.getSubHyperplane1());
        Assert.assertEquals(subLine2, result.getSubHyperplane2());
    }

}