import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Assert.assertEquals(1, subLine.getSegments().size());
        Assert.assertEquals(start, subLine.getSegments().get(0).getStart());
        Assert.assertEquals(end, subLine.getSegments().get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertEquals(0.5, intersection.getX(), 1e-10);
        Assert.assertEquals(0.5, intersection.getY(), 1e-10);
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(-1, 0.5);
        Vector2D end2 = new Vector2D(2, 0.5);
        SubLine subLine2 = new SubLine(start2, end2);

        Assert.assertEquals(Side.MINUS, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(-1, -1);
        Vector2D end2 = new Vector2D(0, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        Assert.assertEquals(1, split.getPlus().getSegments().size());
        Assert.assertEquals(0, split.getMinus().getSegments().size());
        Assert.assertEquals(start1, split.getPlus().getSegments().get(0).getStart());
        Assert.assertEquals(end1, split.getPlus().getSegments().get(0).getEnd());
    }

}