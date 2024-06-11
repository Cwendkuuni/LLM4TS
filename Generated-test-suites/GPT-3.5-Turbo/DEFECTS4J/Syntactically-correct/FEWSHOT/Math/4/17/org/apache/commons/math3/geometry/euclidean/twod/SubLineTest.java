import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class SubLineTest {
    
    @Test
    public void testConstructor1() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
        assertEquals(start, subLine.getHyperplane().toSpace(new Vector1D(0)));
        assertEquals(end, subLine.getHyperplane().toSpace(new Vector1D(1)));
    }
    
    @Test
    public void testConstructor2() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);
        assertNotNull(subLine);
        assertEquals(start, subLine.getHyperplane().toSpace(new Vector1D(0)));
        assertEquals(end, subLine.getHyperplane().toSpace(new Vector1D(1)));
    }
    
    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertNotNull(segments);
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }
    
    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }
    
    @Test
    public void testIntersection_noIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 2);
        Vector2D end2 = new Vector2D(1, 3);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertNull(intersection);
    }
    
    @Test
    public void testIntersection_includeEndPoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
        
        intersection = subLine1.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Side side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.HYPER, side);
        
        side = subLine2.side(subLine1.getHyperplane());
        assertEquals(Side.HYPER, side);
    }
    
    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        assertNotNull(split);
        assertEquals(subLine1, split.getPlus());
        assertEquals(subLine1, split.getMinus());
    }
}