import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        SubLine subLine = new SubLine(new Vector2D(1, 2), new Vector2D(4, 5));
        assertEquals(1, subLine.getSegments().size());
        assertEquals(new Vector2D(1, 2), subLine.getSegments().get(0).getStart());
        assertEquals(new Vector2D(4, 5), subLine.getSegments().get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        SubLine subLine1 = new SubLine(new Vector2D(1, 1), new Vector2D(3, 3));
        SubLine subLine2 = new SubLine(new Vector2D(2, 1), new Vector2D(2, 3));
        assertEquals(new Vector2D(2, 2), subLine1.intersection(subLine2, true));
    }

    @Test
    public void testSide() {
        SubLine subLine1 = new SubLine(new Vector2D(1, 1), new Vector2D(3, 3));
        Line line = new Line(new Vector2D(2, 1), new Vector2D(2, 3));
        assertEquals(Side.HYPER, subLine1.side(line));
    }

    @Test
    public void testSplit() {
        SubLine original = new SubLine(new Vector2D(-1, -1), new Vector2D(1, 1));
        Line splitter = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SplitSubHyperplane<Euclidean2D> split = original.split(splitter);
        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());
    }
}