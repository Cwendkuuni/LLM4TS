import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        SubLine subLine = new SubLine(new Vector2D(-1, 0), new Vector2D(1, 0));
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Vector2D(-1, 0), segments.get(0).getStart());
        assertEquals(new Vector2D(1, 0), segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        SubLine subLine1 = new SubLine(new Vector2D(-1, 0), new Vector2D(1, 0));
        SubLine subLine2 = new SubLine(new Vector2D(0, -1), new Vector2D(0, 1));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0, 0), intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(-1, 0);
        Vector2D end = new Vector2D(1, 0);
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);
        assertEquals(-1.0, intervalsSet.asList().get(0).getInf(), 1e-10);
        assertEquals(1.0, intervalsSet.asList().get(0).getSup(), 1e-10);
    }

    @Test
    public void testSubLineConstructor1() {
        Vector2D start = new Vector2D(-1, 0);
        Vector2D end = new Vector2D(1, 0);
        SubLine subLine = new SubLine(start, end);
        assertEquals(1, subLine.getSegments().size());
        assertEquals(start, subLine.getSegments().get(0).getStart());
        assertEquals(end, subLine.getSegments().get(0).getEnd());
    }

    @Test
    public void testSubLineConstructor2() {
        Segment segment = new Segment(new Vector2D(-1, 0), new Vector2D(1, 0), new Line(new Vector2D(2, 0), new Vector2D(0, 2)));
        SubLine subLine = new SubLine(segment);
        assertEquals(1, subLine.getSegments().size());
        assertEquals(new Vector2D(-1, 0), subLine.getSegments().get(0).getStart());
        assertEquals(new Vector2D(1, 0), subLine.getSegments().get(0).getEnd());
    }

    @Test
    public void testSide() {
        SubLine subLine1 = new SubLine(new Vector2D(-1, 0), new Vector2D(1, 0));
        SubLine subLine2 = new SubLine(new Vector2D(0, -1), new Vector2D(0, 1));
        assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit() {
        SubLine subLine1 = new SubLine(new Vector2D(-1, 0), new Vector2D(1, 0));
        SubLine subLine2 = new SubLine(new Vector2D(0, -1), new Vector2D(0, 1));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        SubLine plus = (SubLine) split.getPlus();
        SubLine minus = (SubLine) split.getMinus();
        assertEquals(1, plus.getSegments().size());
        assertEquals(0, minus.getSegments().size());
        assertEquals(new Vector2D(-1, 0), plus.getSegments().get(0).getStart());
        assertEquals(new Vector2D(1, 0), plus.getSegments().get(0).getEnd());
    }

}