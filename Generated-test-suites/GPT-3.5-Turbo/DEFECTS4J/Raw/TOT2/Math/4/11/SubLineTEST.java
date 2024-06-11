import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        // Test SubLine constructor with valid inputs
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        // Test getSegments method
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        List<Segment> segments = subLine.getSegments();
        
        assertEquals(1, segments.size());
    }

    @Test
    public void testIntersection() {
        // Test intersection method with valid inputs
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start, end);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);

        assertNotNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        // Test buildIntervalSet method
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);
        
        assertNotNull(intervalsSet);
    }

    @Test
    public void testSide() {
        // Test side method with valid inputs
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start, end);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Side side = subLine1.side(subLine2.getHyperplane());

        assertNotNull(side);
    }

    @Test
    public void testSplit() {
        // Test split method with valid inputs
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start, end);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(subLine2.getHyperplane());

        assertNotNull(splitSubHyperplane);
    }
}