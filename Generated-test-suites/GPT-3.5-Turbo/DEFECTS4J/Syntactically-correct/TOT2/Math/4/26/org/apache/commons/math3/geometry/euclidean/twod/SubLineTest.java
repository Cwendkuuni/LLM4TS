import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    private SubLine subLine;

    @Before
    public void setup() {
        subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        SubLine otherSubLine = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        Vector2D intersectionPoint = subLine.intersection(otherSubLine, true);
        assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);
    }

    @Test
    public void testBuildNew() {
        AbstractSubHyperplane subHyperplane = subLine.buildNew(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        assertNotNull(subHyperplane);
    }

    @Test
    public void testSide() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Side side = subLine.side(line);
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        Line line = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        SplitSubHyperplane splitSubHyperplane = subLine.split(line);
        assertNotNull(splitSubHyperplane);
    }

}