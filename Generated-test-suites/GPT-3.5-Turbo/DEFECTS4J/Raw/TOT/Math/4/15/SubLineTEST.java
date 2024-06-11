import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

public class SubLineTest {

    private SubLine subLine;
    private Vector2D start;
    private Vector2D end;

    @Before
    public void setUp() {
        start = new Vector2D(0, 0);
        end = new Vector2D(1, 1);
        subLine = new SubLine(start, end);
    }

    @Test
    public void testConstructor() {
        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersectionWithSameSubLine() {
        Vector2D intersection = subLine.intersection(subLine, true);
        assertEquals(start, intersection);
    }

    @Test
    public void testIntersectionWithEmptySubLine() {
        SubLine emptySubLine = new SubLine(new Vector2D(2, 2), new Vector2D(3, 3));
        Vector2D intersection = subLine.intersection(emptySubLine, true);
        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithSubLine() {
        SubLine otherSubLine = new SubLine(new Vector2D(0, 1), new Vector2D(1, 0));
        Vector2D intersection = subLine.intersection(otherSubLine, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersectionWithSubLineIncludeEndPoints() {
        SubLine otherSubLine = new SubLine(new Vector2D(0, 1), new Vector2D(1, 0));
        Vector2D intersection = subLine.intersection(otherSubLine, false);
        assertNull(intersection);
    }

    @Test
    public void testSideWithParallelSubLine() {
        SubLine parallelSubLine = new SubLine(new Vector2D(2, 2), new Vector2D(3, 3));
        assertEquals(Side.HYPER, subLine.side(parallelSubLine.getHyperplane()));
    }

    @Test
    public void testSideWithIntersectingSubLine() {
        SubLine intersectingSubLine = new SubLine(new Vector2D(0, 1), new Vector2D(1, 0));
        assertEquals(Side.PLUS, subLine.side(intersectingSubLine.getHyperplane()));
    }

    @Test
    public void testSideWithOutsideSubLine() {
        SubLine outsideSubLine = new SubLine(new Vector2D(2, 3), new Vector2D(3, 2));
        assertEquals(Side.MINUS, subLine.side(outsideSubLine.getHyperplane()));
    }

    @Test
    public void testSplitWithInsideSubLine() {
        SubLine insideSubLine = new SubLine(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.75));
        SplitSubHyperplane<Euclidean2D> split = subLine.split(insideSubLine.getHyperplane());
        assertEquals(insideSubLine, split.getPlus());
        assertNull(split.getMinus());
    }

    @Test
    public void testSplitWithOutsideSubLine() {
        SubLine outsideSubLine = new SubLine(new Vector2D(2, 3), new Vector2D(3, 2));
        SplitSubHyperplane<Euclidean2D> split = subLine.split(outsideSubLine.getHyperplane());
        assertEquals(outsideSubLine, split.getMinus());
        assertNull(split.getPlus());
    }

    @Test
    public void testSplitWithIntersectingSubLine() {
        SubLine intersectingSubLine = new SubLine(new Vector2D(0, 1), new Vector2D(1, 0));
        SplitSubHyperplane<Euclidean2D> split = subLine.split(intersectingSubLine.getHyperplane());
        assertEquals(new Vector2D(0.5, 0.5), split.getPlus().getHyperplane().toSpace(split.getPlus().getHyperplane().toSubSpace(new Vector2D(0.5, 0.5))));
        assertEquals(new Vector2D(0.5, 0.5), split.getMinus().getHyperplane().toSpace(split.getMinus().getHyperplane().toSubSpace(new Vector2D(0.5, 0.5))));
    }

}