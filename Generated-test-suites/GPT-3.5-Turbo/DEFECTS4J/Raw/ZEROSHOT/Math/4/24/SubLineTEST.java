import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {
    
    @Test
    public void testGetSegments() {
        // Create a sub-line with two segments
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        // Check that the sub-line has two segments
        assertEquals(2, subLine1.getSegments().size());
        
        // Create a sub-line with one segment
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine2 = new SubLine(start2, end2);
        
        // Check that the sub-line has one segment
        assertEquals(1, subLine2.getSegments().size());
        
        // Create a sub-line with no segments
        Vector2D start3 = new Vector2D(4, 4);
        Vector2D end3 = new Vector2D(4, 4);
        SubLine subLine3 = new SubLine(start3, end3);
        
        // Check that the sub-line has no segments
        assertEquals(0, subLine3.getSegments().size());
    }
    
    @Test
    public void testIntersection() {
        // Create two sub-lines that intersect
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        // Check that the sub-lines intersect
        Vector2D intersection1 = subLine1.intersection(subLine2, true);
        assertNotNull(intersection1);
        
        // Create two sub-lines that do not intersect
        Vector2D start3 = new Vector2D(0, 0);
        Vector2D end3 = new Vector2D(0, 1);
        SubLine subLine3 = new SubLine(start3, end3);
        
        Vector2D start4 = new Vector2D(1, 1);
        Vector2D end4 = new Vector2D(1, 2);
        SubLine subLine4 = new SubLine(start4, end4);
        
        // Check that the sub-lines do not intersect
        Vector2D intersection2 = subLine3.intersection(subLine4, true);
        assertNull(intersection2);
    }
    
    @Test
    public void testSplit() {
        // Create a sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        // Create a hyperplane that intersects the sub-line
        Vector2D point1 = new Vector2D(0, 1);
        Vector2D point2 = new Vector2D(1, 0);
        Line hyperplane = new Line(point1, point2);
        
        // Split the sub-line with the hyperplane
        SplitSubHyperplane<Euclidean2D> split = subLine.split(hyperplane);
        
        // Check that the split sub-lines cover the original sub-line
        SubLine plusSplit = (SubLine) split.getPlus();
        SubLine minusSplit = (SubLine) split.getMinus();
        
        assertEquals(Location.INSIDE, plusSplit.getRemainingRegion().checkPoint(start));
        assertEquals(Location.INSIDE, plusSplit.getRemainingRegion().checkPoint(end));
        
        assertEquals(Location.INSIDE, minusSplit.getRemainingRegion().checkPoint(start));
        assertEquals(Location.INSIDE, minusSplit.getRemainingRegion().checkPoint(end));
    }
}