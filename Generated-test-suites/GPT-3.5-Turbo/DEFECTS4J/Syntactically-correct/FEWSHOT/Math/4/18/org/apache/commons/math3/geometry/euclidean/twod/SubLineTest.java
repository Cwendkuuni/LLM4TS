import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testConstructor1() {
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        assertEquals(new Vector2D(0, 0), subLine.getHyperplane().toSubSpace(new Vector2D(0, 0)));
        assertEquals(new Vector2D(1, 1), subLine.getHyperplane().toSubSpace(new Vector2D(1, 1)));
        assertEquals(Location.INSIDE, subLine.getRemainingRegion().checkPoint(new Vector1D(0.5)));
    }

    @Test
    public void testConstructor2() {
        SubLine subLine = new SubLine(new Segment(new Vector2D(0, 0), new Vector2D(1, 1)));
        assertEquals(new Vector2D(0, 0), subLine.getHyperplane().toSubSpace(new Vector2D(0, 0)));
        assertEquals(new Vector2D(1, 1), subLine.getHyperplane().toSubSpace(new Vector2D(1, 1)));
        assertEquals(Location.INSIDE, subLine.getRemainingRegion().checkPoint(new Vector1D(0.5)));
    }

    @Test
    public void testGetSegments() {
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        assertEquals(1, subLine.getSegments().size());
        assertEquals(new Vector2D(0, 0), subLine.getSegments().get(0).getStart());
        assertEquals(new Vector2D(1, 1), subLine.getSegments().get(0).getEnd());
        assertEquals(subLine.getHyperplane(), subLine.getSegments().get(0).getLine());
    }

    @Test
    public void testIntersectionIncludeEndPoints() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0, 1), new Vector2D(1, 0)), new IntervalsSet(0, 1));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersectionNotIncludeEndPoints() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(1, 0), new Vector2D(2, 1)), new IntervalsSet(0, 1));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testSide() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        assertEquals(Side.HYPER, subLine1.side(line2));
    }

    @Test
    public void testSplit() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(line2);
        assertEquals(new Vector2D(0.5, 0.5), split.getPlus().getHyperplane().toSpace(new Vector1D(0.5)));
        assertEquals(new Vector2D(0, 0), split.getMinus().getHyperplane().toSpace(new Vector1D(0)));
        assertEquals(new Vector2D(1, 1), split.getMinus().getHyperplane().toSpace(new Vector1D(1)));
    }
}