import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        
        Segment expectedSegment = new Segment(start, end, new Line(start, end));
        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(expectedSegment.getStart(), segments.get(0).getStart());
        Assert.assertEquals(expectedSegment.getEnd(), segments.get(0).getEnd());
        Assert.assertEquals(expectedSegment.getLine(), segments.get(0).getLine());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(1, 1);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertNotNull(intersection);
        Assert.assertEquals(end1, intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        
        Assert.assertEquals(new Interval(0, 1), intervalSet.asList().get(0));
    }

    @Test
    public void testBuildNew() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        IntervalsSet remainingRegion = SubLine.buildIntervalSet(start, end);
        SubLine subLine = new SubLine(line, remainingRegion);
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> newSubLine = subLine.buildNew(line, remainingRegion);
        
        Assert.assertEquals(subLine.getHyperplane(), newSubLine.getHyperplane());
        Assert.assertEquals(subLine.getRemainingRegion(), newSubLine.getRemainingRegion());
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Line line = new Line(start2, end2);
        Side side1 = subLine1.side(line);
        Side side2 = subLine2.side(line);
        
        Assert.assertEquals(Side.HYPER, side1);
        Assert.assertEquals(Side.MINUS, side2);
    }

    @Test
    public void testSplit() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        IntervalsSet remainingRegion = SubLine.buildIntervalSet(start, end);
        SubLine subLine = new SubLine(line, remainingRegion);
        
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine2 = new SubLine(start2, end2);
        
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(subLine2.getHyperplane());
        
        Assert.assertEquals(subLine.getHyperplane(), splitSubHyperplane.getPlus().getHyperplane());
        Assert.assertEquals(subLine.getRemainingRegion(), splitSubHyperplane.getPlus().getRemainingRegion());
        Assert.assertNull(splitSubHyperplane.getMinus());
    }
}