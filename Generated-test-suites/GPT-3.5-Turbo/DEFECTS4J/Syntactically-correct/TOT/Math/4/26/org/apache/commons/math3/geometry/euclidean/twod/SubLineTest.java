import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Create a SubLine with two disjoint segments
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(5, 5));
        
        // Expecting two segments in the result
        assertEquals(2, subLine.getSegments().size());
        
        // Verify the starting and ending points of each segment
        assertEquals(new Vector2D(0, 0), subLine.getSegments().get(0).getStart());
        assertEquals(new Vector2D(2.5, 2.5), subLine.getSegments().get(0).getEnd());
        assertEquals(new Vector2D(2.5, 2.5), subLine.getSegments().get(1).getStart());
        assertEquals(new Vector2D(5, 5), subLine.getSegments().get(1).getEnd());
    }
    
    @Test
    public void testIntersection() {
        // Create two SubLines that intersect each other
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(5, 5));
        SubLine subLine2 = new SubLine(new Vector2D(0, 5), new Vector2D(5, 0));
        
        // Expecting the intersection point to be (2.5, 2.5)
        assertEquals(new Vector2D(2.5, 2.5), subLine1.intersection(subLine2, true));
        
        // Expecting null when endpoints are not considered
        assertNull(subLine1.intersection(subLine2, false));
    }
    
    @Test
    public void testIntersectionParallelLines() {
        // Create two SubLines that are parallel to each other
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(0, 5));
        SubLine subLine2 = new SubLine(new Vector2D(1, 0), new Vector2D(1, 5));
        
        // Expecting null as parallel lines do not intersect
        assertNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));
    }
    
    @Test
    public void testSide() {
        // Create two SubLines that intersect each other
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(5, 5));
        SubLine subLine2 = new SubLine(new Vector2D(0, 5), new Vector2D(5, 0));
        
        // Expecting both SubLines to be on the positive side of each other
        assertEquals(Side.PLUS, subLine1.side(subLine2.getHyperplane()));
        assertEquals(Side.PLUS, subLine2.side(subLine1.getHyperplane()));
    }
    
    @Test
    public void testSplit() {
        // Create a SubLine
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(5, 5));
        
        // Create a Hyperplane that will split the SubLine
        Hyperplane<Euclidean2D> splittingHyperplane = new Line(new Vector2D(0, 5), new Vector2D(5, 0));
        
        // Expecting the SubLine to be split into two parts
        SplitSubHyperplane<Euclidean2D> split = subLine.split(splittingHyperplane);
        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());
    }
}