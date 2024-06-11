import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0, 1)));
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        Assert.assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }

    @Test
    public void testIntersectionIncludeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersectionExcludeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);
        Assert.assertEquals(new Interval(0, 1), intervalsSet.asList().get(0));
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Side side = subLine1.side(line2);
        Assert.assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(line2);
        SubLine plus = (SubLine) splitSubHyperplane.getPlus();
        SubLine minus = (SubLine) splitSubHyperplane.getMinus();
        Assert.assertEquals(0, plus.getRemainingRegion().getSize());
        Assert.assertEquals(1, minus.getRemainingRegion().getSize());
    }
}