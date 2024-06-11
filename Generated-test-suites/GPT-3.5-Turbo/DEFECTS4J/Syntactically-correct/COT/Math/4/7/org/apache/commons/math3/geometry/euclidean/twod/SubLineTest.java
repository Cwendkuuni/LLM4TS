import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructor1() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        assertEquals(start, subLine.getSegments().get(0).getStart());
        assertEquals(end, subLine.getSegments().get(0).getEnd());
    }
    
    @Test
    public void testConstructor2() {
        Vector2D start = new Vector2D(5.0, 6.0);
        Vector2D end = new Vector2D(7.0, 8.0);
        SubLine subLine = new SubLine(start, end);
        assertEquals(1, subLine.getSegments().size());
        assertEquals(start, subLine.getSegments().get(0).getStart());
        assertEquals(end, subLine.getSegments().get(0).getEnd());
    }
    
    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(2.0, 3.0);
        Vector2D end2 = new Vector2D(4.0, 5.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(2.5, intersection.getX(), 0.0);
        assertEquals(3.5, intersection.getY(), 0.0);
    }
    
    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(2.0, 3.0);
        Vector2D end2 = new Vector2D(4.0, 5.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
    }
    
    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1 = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(2.0, 3.0);
        Vector2D end2 = new Vector2D(4.0, 5.0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        assertNull(split.getMinus());
        assertNotNull(split.getPlus());
        assertEquals(start1, split.getPlus().getSegments().get(0).getStart());
        assertEquals(end1, split.getPlus().getSegments().get(0).getEnd());
    }
}