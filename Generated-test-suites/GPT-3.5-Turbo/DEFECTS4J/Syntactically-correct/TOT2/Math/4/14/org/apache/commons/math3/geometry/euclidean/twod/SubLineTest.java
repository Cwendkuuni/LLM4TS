import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRegion() {
        // Test the constructor with a hyperplane and a region
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, region);
        
        assertNotNull(subLine);
    }

    @Test
    public void testConstructorWithEndpoints() {
        // Test the constructor with two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        // Test the getSegments method
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        assertEquals(1, subLine.getSegments().size());
    }

    @Test
    public void testIntersection() {
        // Test the intersection method
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);

        assertNull(intersectionPoint);
    }

    @Test
    public void testSide() {
        // Test the side method
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        Side side = subLine1.side(line2);

        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit() {
        // Test the split method
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(line2);

        assertNotNull(split);
    }

}