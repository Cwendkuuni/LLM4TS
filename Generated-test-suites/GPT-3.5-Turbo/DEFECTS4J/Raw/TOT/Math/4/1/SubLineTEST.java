import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    private SubLine subLine;

    @Before
    public void setUp() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        subLine = new SubLine(start, end);
    }

    @Test
    public void testSubLineConstructor() {
        assertEquals(subLine.getHyperplane().getX(FastMath.PI / 4), 0, 1e-6);
        assertEquals(subLine.getHyperplane().getY(FastMath.PI / 4), 0, 1e-6);
        assertEquals(subLine.getHyperplane().getX(-FastMath.PI / 4), 1, 1e-6);
        assertEquals(subLine.getHyperplane().getY(-FastMath.PI / 4), 1, 1e-6);
    }

    @Test
    public void testSubLineGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(segments.size(), 1);
        
        Segment segment = segments.get(0);
        assertEquals(segment.getStart().getX(), 0, 1e-6);
        assertEquals(segment.getStart().getY(), 0, 1e-6);
        assertEquals(segment.getEnd().getX(), 1, 1e-6);
        assertEquals(segment.getEnd().getY(), 1, 1e-6);
    }

    @Test
    public void testSubLineIntersection() {
        Vector2D start = new Vector2D(0, 1);
        Vector2D end = new Vector2D(1, 0);
        SubLine otherSubLine = new SubLine(start, end);

        Vector2D intersection = subLine.intersection(otherSubLine, true);
        assertEquals(intersection.getX(), 0.5, 1e-6);
        assertEquals(intersection.getY(), 0.5, 1e-6);

        Vector2D noIntersection = subLine.intersection(otherSubLine, false);
        assertNull(noIntersection);
    }

    @Test
    public void testSubLineSide() {
        Line line1 = (Line) subLine.getHyperplane();
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(0, 1));

        Side side = subLine.side(line2);
        assertEquals(side, Side.HYPER);

        side = subLine.side(line1);
        assertEquals(side, Side.PLUS);
    }

    @Test
    public void testSubLineSplit() {
        Line line = (Line) subLine.getHyperplane();
        Line splitter = new Line(new Vector2D(0.5, 0), new Vector2D(0.5, 1));

        SplitSubHyperplane<Euclidean2D> split = subLine.split(splitter);

        SubLine plus = (SubLine) split.getPlus();
        assertEquals(plus.getHyperplane().getX(FastMath.PI / 4), 0, 1e-6);
        assertEquals(plus.getHyperplane().getY(FastMath.PI / 4), 0, 1e-6);

        SubLine minus = (SubLine) split.getMinus();
        assertEquals(minus.getHyperplane().getX(FastMath.PI / 4), 1, 1e-6);
        assertEquals(minus.getHyperplane().getY(FastMath.PI / 4), 1, 1e-6);
    }

}