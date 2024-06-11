import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, remainingRegion);

        List<Segment> segments = subLine.getSegments();

        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        Assert.assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(0, 1);
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        Vector2D intersection = subLine1.intersection(subLine2, true);

        Assert.assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);

        Assert.assertEquals(0, intervalsSet.getSize(), 1e-10);
        Assert.assertEquals(0, intervalsSet.asList().size());
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));

        Assert.assertEquals(Side.HYPER, subLine1.side(line2));
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SubLine subLine2 = new SubLine(line2, remainingRegion1);

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(subLine2);

        SubLine plusSubLine = (SubLine) splitSubHyperplane.getPlus();
        SubLine minusSubLine = (SubLine) splitSubHyperplane.getMinus();

        Assert.assertEquals(new Vector2D(0, 0), plusSubLine.getSegments().get(0).getStart());
        Assert.assertEquals(new Vector2D(0.5, 0.5), plusSubLine.getSegments().get(0).getEnd());

        Assert.assertEquals(new Vector2D(0.5, 0.5), minusSubLine.getSegments().get(0).getStart());
        Assert.assertEquals(new Vector2D(1, 1), minusSubLine.getSegments().get(0).getEnd());
    }

}