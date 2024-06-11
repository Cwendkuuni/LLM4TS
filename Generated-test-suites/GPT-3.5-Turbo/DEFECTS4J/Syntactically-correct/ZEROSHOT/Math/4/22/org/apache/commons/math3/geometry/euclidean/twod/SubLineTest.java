import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        Assert.assertEquals(start, subLine.getStartPoint());
        Assert.assertEquals(end, subLine.getEndPoint());
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();

        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(start, segments.get(0).getStart());
        Assert.assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        Assert.assertNotNull(intersectionPoint);
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);
    }

    @Test
    public void testSplit() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        Line splittingLine = new Line(new Vector2D(0.5, 0), new Vector2D(0.5, 1));
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine.split(splittingLine);

        SubLine plusSubLine = (SubLine) splitSubLine.getPlus();
        SubLine minusSubLine = (SubLine) splitSubLine.getMinus();

        Assert.assertEquals(new Vector2D(0, 0), plusSubLine.getStartPoint());
        Assert.assertEquals(new Vector2D(0.5, 0.5), plusSubLine.getEndPoint());

        Assert.assertEquals(new Vector2D(0.5, 0.5), minusSubLine.getStartPoint());
        Assert.assertEquals(new Vector2D(1, 1), minusSubLine.getEndPoint());
    }

}