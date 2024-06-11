import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        assertEquals(subLine.getHyperplane().getOffset(start), subLine.getHyperplane().getOffset(end), 0.0);
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        List<Segment> segments = subLine.getSegments();
        
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(1, 1);
        Vector2D p3 = new Vector2D(0, 1);
        Vector2D p4 = new Vector2D(1, 0);
        
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(p3, p4);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        
        assertEquals(0.0, intervalSet.getInf(), 0.0);
        assertEquals(1.0, intervalSet.getSup(), 0.0);
    }

    @Test
    public void testSide() {
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(1, 1);
        Vector2D p3 = new Vector2D(0, 1);
        Vector2D p4 = new Vector2D(1, 0);
        
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(p3, p4);
        
        assertEquals(Side.PLUS, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit() {
        Vector2D p1 = new Vector2D(0, 0);
        Vector2D p2 = new Vector2D(1, 1);
        Vector2D p3 = new Vector2D(0, 1);
        Vector2D p4 = new Vector2D(1, 0);
        
        SubLine subLine1 = new SubLine(p1, p2);
        SubLine subLine2 = new SubLine(p3, p4);
        
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        
        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());
    }
}