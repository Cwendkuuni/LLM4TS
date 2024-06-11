import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
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

public class SubLineTest {

    @Test
    public void testSimpleConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(0), new Vector1D(1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        Assert.assertEquals(hyperplane, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithEndpoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = buildIntervalSet(start, end);
        SubLine subLine = new SubLine(start, end);
        Assert.assertEquals(hyperplane, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        Hyperplane<Euclidean2D> hyperplane = segment.getLine();
        Region<Euclidean1D> remainingRegion = buildIntervalSet(start, end);
        SubLine subLine = new SubLine(segment);
        Assert.assertEquals(hyperplane, subLine.getHyperplane());
        Assert.assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        Line line = new Line(new Vector2D(0, 0), new Vector2D(3, 3));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(1), new Vector1D(2));
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
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(0), new Vector1D(1));
        SubLine subLine1 = new SubLine(line, remainingRegion);
        Assert.assertNull(subLine1.intersection(null, false));
    }

    @Test
    public void testIntersectionWithSameSubLine() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(0), new Vector1D(1));
        SubLine subLine1 = new SubLine(line, remainingRegion);
        Assert.assertNull(subLine1.intersection(subLine1, false));
    }

    @Test
    public void testIntersectionWithDisjointSubLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Vector1D(0), new Vector1D(1));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Vector1D(2), new Vector1D(3));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);
        SubLine subLine2 = new SubLine(line2, remainingRegion2);
        Assert.assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testIntersectionWithIntersectingSubLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Vector1D(0), new Vector1D(1));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Vector1D(1), new Vector1D(2));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);
        SubLine subLine2 = new SubLine(line2, remainingRegion2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        Assert.assertNotNull(intersectionPoint);
        Euclidean2DSpace.CHECK_EQUALITY(new Vector2D(1, 1), intersectionPoint, 1e-12);
    }

    @Test
    public void testIntersectionWithIntersectingSubLinesIncludeEndPoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(2, 2);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Vector1D(0), new Vector1D(1));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Vector1D(1), new Vector1D(2));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);
        SubLine subLine2 = new SubLine(line2, remainingRegion2);
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertNotNull(intersectionPoint);
        Euclidean2DSpace.CHECK_EQUALITY(new Vector2D(1, 1), intersectionPoint, 1e-12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullIncludeEndPointsFlag() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Vector1D(0), new Vector1D(1));
        SubLine subLine = new SubLine(line, remainingRegion);
        subLine.intersection(null, true);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector1D(0), new Vector1D(1)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Side expectedSide = Side.MINUS;
        Side side = subLine1.side(hyperplane);
        Assert.assertEquals(expectedSide, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector1D(0), new Vector1D(1)));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane);
        Assert.assertNotNull(splitSubHyperplane);
        Assert.assertNotNull(splitSubHyperplane.getSubHyperplane1());
        Assert.assertNotNull(splitSubHyperplane.getSubHyperplane2());
        Hyperplane<Euclidean1D> hyperplane1 = splitSubHyperplane.getSubHyperplane1().getHyperplane();
        Hyperplane<Euclidean1D> hyperplane2 = splitSubHyperplane.getSubHyperplane2().getHyperplane();
        Assert.assertEquals(line1, hyperplane1.getHyperplane());
        Assert.assertEquals(line2, hyperplane2.getHyperplane());
    }

    private Region<Euclidean1D> buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start), line.toSubSpace(end));
    }

}