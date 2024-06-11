import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    private static SubLine subLine;

    @BeforeClass
    public static void setup() {
        subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
    }

    @Test
    public void testConstructor() {
        assertEquals(Line.class, subLine.getHyperplane().getClass());
        assertEquals(Region.class, subLine.getRemainingRegion().getClass());
    }

    @Test
    public void testGetSegments() {
        assertEquals(1, subLine.getSegments().size());
        assertEquals(new Vector2D(0, 0), subLine.getSegments().get(0).getStart());
        assertEquals(new Vector2D(1, 1), subLine.getSegments().get(0).getEnd());
    }

    @Test
    public void testIntersection_withIntersectionPointIncluded() {
        SubLine other = new SubLine(new Vector2D(-1, 0), new Vector2D(1, 0));
        Vector2D intersectionPoint = subLine.intersection(other, true);
        assertEquals(new Vector2D(0, 0), intersectionPoint);
    }

    @Test
    public void testIntersection_withIntersectionPointExcluded() {
        SubLine other = new SubLine(new Vector2D(-1, 0), new Vector2D(1, 0));
        Vector2D intersectionPoint = subLine.intersection(other, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersection_withNoIntersection() {
        SubLine other = new SubLine(new Vector2D(-1, 0), new Vector2D(0, 1));
        Vector2D intersectionPoint = subLine.intersection(other, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testBuildIntervalSet() {
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(new Vector2D(0, 0), new Vector2D(1, 1));
        assertEquals(0.0, intervalsSet.getInf(), 0.0);
        assertEquals(1.4142135623730951, intervalsSet.getSup(), 0.0);
    }

    @Test
    public void testBuildNew() {
        SubLine newSubLine = (SubLine) subLine.buildNew(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new Region());
        assertEquals(Line.class, newSubLine.getHyperplane().getClass());
        assertEquals(Region.class, newSubLine.getRemainingRegion().getClass());
    }

    @Test
    public void testSide_withParallelLinesAndNegativeOffset() {
        Line otherLine = new Line(new Vector2D(-1, 0), new Vector2D(-1, 1));
        assertEquals(Side.MINUS, subLine.side(otherLine));
    }

    @Test
    public void testSide_withParallelLinesAndPositiveOffset() {
        Line otherLine = new Line(new Vector2D(1, 0), new Vector2D(1, 1));
        assertEquals(Side.PLUS, subLine.side(otherLine));
    }

    @Test
    public void testSide_withParallelLinesAndZeroOffset() {
        Line otherLine = new Line(new Vector2D(0, 0), new Vector2D(0, 1));
        assertEquals(Side.HYPER, subLine.side(otherLine));
    }

    @Test
    public void testSide_withIntersectingLines() {
        Line otherLine = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        assertEquals(Side.MINUS, subLine.side(otherLine));
    }

    @Test
    public void testSplit_withParallelLinesAndNegativeOffset() {
        Line otherLine = new Line(new Vector2D(-1, 0), new Vector2D(-1, 1));
        SplitSubHyperplane splitSubHyperplane = subLine.split(otherLine);
        assertNull(splitSubHyperplane.getPlus());
        assertEquals(subLine, splitSubHyperplane.getMinus());
    }

    @Test
    public void testSplit_withParallelLinesAndPositiveOffset() {
        Line otherLine = new Line(new Vector2D(1, 0), new Vector2D(1, 1));
        SplitSubHyperplane splitSubHyperplane = subLine.split(otherLine);
        assertEquals(subLine, splitSubHyperplane.getPlus());
        assertNull(splitSubHyperplane.getMinus());
    }

    @Test
    public void testSplit_withIntersectingLines() {
        Line otherLine = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SplitSubHyperplane splitSubHyperplane = subLine.split(otherLine);
        assertEquals(subLine, splitSubHyperplane.getPlus().getHyperplane());
        assertEquals(subLine, splitSubHyperplane.getMinus().getHyperplane());
    }

}