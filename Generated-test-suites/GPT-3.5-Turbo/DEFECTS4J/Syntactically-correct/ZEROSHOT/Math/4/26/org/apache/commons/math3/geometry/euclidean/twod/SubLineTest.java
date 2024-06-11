import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor1() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        SubLine subLine = new SubLine(start, end);
        assertEquals(start.getX(), subLine.getSegments().get(0).getStart().getX(), 0.0);
        assertEquals(start.getY(), subLine.getSegments().get(0).getStart().getY(), 0.0);
        assertEquals(end.getX(), subLine.getSegments().get(0).getEnd().getX(), 0.0);
        assertEquals(end.getY(), subLine.getSegments().get(0).getEnd().getY(), 0.0);
    }

    @Test
    public void testConstructor2() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        Line line = new Line(start, end);
        SubLine subLine = new SubLine(line, new IntervalsSet(0.0, 1.0));
        assertEquals(start.getX(), subLine.getSegments().get(0).getStart().getX(), 0.0);
        assertEquals(start.getY(), subLine.getSegments().get(0).getStart().getY(), 0.0);
        assertEquals(end.getX(), subLine.getSegments().get(0).getEnd().getX(), 0.0);
        assertEquals(end.getY(), subLine.getSegments().get(0).getEnd().getY(), 0.0);
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 1.0);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0.5, 0.0);
        Vector2D end2 = new Vector2D(0.5, 1.0);
        SubLine subLine2 = new SubLine(start2, end2);

        assertEquals(0.5, subLine1.intersection(subLine2, true).getX(), 0.0);
        assertEquals(0.5, subLine1.intersection(subLine2, true).getY(), 0.0);
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 1.0);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0.5, 0.0);
        Vector2D end2 = new Vector2D(0.5, 1.0);
        SubLine subLine2 = new SubLine(start2, end2);

        assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 1.0);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0.5, 0.0);
        Vector2D end2 = new Vector2D(0.5, 1.0);
        SubLine subLine2 = new SubLine(start2, end2);

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());

        assertEquals(start1.getX(), split.getPlus().getSegments().get(0).getStart().getX(), 0.0);
        assertEquals(start1.getY(), split.getPlus().getSegments().get(0).getStart().getY(), 0.0);
        assertEquals(end1.getX(), split.getPlus().getSegments().get(0).getEnd().getX(), 0.0);
        assertEquals(end1.getY(), split.getPlus().getSegments().get(0).getEnd().getY(), 0.0);

        assertNull(split.getMinus());
    }

}