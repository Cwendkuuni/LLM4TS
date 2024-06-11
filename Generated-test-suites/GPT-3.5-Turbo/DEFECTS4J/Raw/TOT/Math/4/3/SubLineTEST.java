import org.junit.Before;
import org.junit.Test;

public class SubLineTest {

    private SubLine subLine;

    @Before
    public void setUp() {
        subLine = new SubLine(new Vector2D(1, 1), new Vector2D(3, 3));
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());

        Segment segment = segments.get(0);
        Assert.assertEquals(new Vector2D(1, 1), segment.getStart());
        Assert.assertEquals(new Vector2D(3, 3), segment.getEnd());
    }

    @Test
    public void testIntersection() {
        SubLine subLine2 = new SubLine(new Vector2D(2, 2), new Vector2D(4, 4));

        Vector2D intersectionPoint = subLine.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(2, 2), intersectionPoint);
    }

    @Test
    public void testIntersectionWithEndpointsExcluded() {
        SubLine subLine2 = new SubLine(new Vector2D(2, 2), new Vector2D(4, 4));

        Vector2D intersectionPoint = subLine.intersection(subLine2, false);
        Assert.assertEquals(new Vector2D(2, 2), intersectionPoint);
    }

    @Test
    public void testIntersectionWithEndpointsExcluded_NoIntersection() {
        SubLine subLine2 = new SubLine(new Vector2D(4, 4), new Vector2D(6, 6));

        Vector2D intersectionPoint = subLine.intersection(subLine2, false);
        Assert.assertNull(intersectionPoint);
    }

    @Test
    public void testSide() {
        Line line2 = new Line(new Vector2D(2, 0), new Vector2D(2, 4));
        SubLine subLine2 = new SubLine(line2, null);

        Side side = subLine.side(line2);
        Assert.assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        Line line2 = new Line(new Vector2D(2, 0), new Vector2D(2, 4));
        SubLine subLine2 = new SubLine(line2, null);

        SplitSubHyperplane<Euclidean2D> splitResult = subLine.split(line2);

        SubLine plus = (SubLine) splitResult.getPlus();
        Assert.assertEquals(new Vector2D(2, 1), plus.getSegments().get(0).getStart());
        Assert.assertEquals(new Vector2D(2, 3), plus.getSegments().get(0).getEnd());

        SubLine minus = (SubLine) splitResult.getMinus();
        Assert.assertEquals(new Vector2D(1, 1), minus.getSegments().get(0).getStart());
        Assert.assertEquals(new Vector2D(2, 1), minus.getSegments().get(0).getEnd());
    }

}