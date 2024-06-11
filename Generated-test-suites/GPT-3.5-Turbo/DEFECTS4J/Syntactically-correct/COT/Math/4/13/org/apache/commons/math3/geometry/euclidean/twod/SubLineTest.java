import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, region);

        Assert.assertEquals(line, subLine.getHyperplane());
        Assert.assertEquals(region, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, region);

        List<Segment> segments = subLine.getSegments();

        Assert.assertEquals(1, segments.size());

        Segment segment = segments.get(0);
        Assert.assertEquals(new Vector2D(0, 0), segment.getStart());
        Assert.assertEquals(new Vector2D(1, 1), segment.getEnd());
        Assert.assertEquals(line, segment.getLine());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(0, 2), new Vector2D(2, 0));
        Region<Euclidean1D> region1 = new IntervalsSet(0, 1);
        Region<Euclidean1D> region2 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, region1);
        SubLine subLine2 = new SubLine(line2, region2);

        Vector2D intersection = subLine1.intersection(subLine2, true);

        Assert.assertEquals(new Vector2D(1, 1), intersection);
    }

    @Test
    public void testSplit() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, region);
        Line otherLine = new Line(new Vector2D(0, 2), new Vector2D(2, 0));

        SplitSubHyperplane<Euclidean2D> split = subLine.split(otherLine);

        SubLine plus = (SubLine) split.getPlus();
        SubLine minus = (SubLine) split.getMinus();

        Assert.assertEquals(line, plus.getHyperplane());
        Assert.assertEquals(region, plus.getRemainingRegion());

        Assert.assertEquals(line, minus.getHyperplane());
        Assert.assertTrue(minus.getRemainingRegion().isEmpty());
    }
}