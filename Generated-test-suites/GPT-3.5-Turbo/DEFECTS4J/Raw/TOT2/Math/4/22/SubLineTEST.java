import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        // Test SubLine constructor with valid input
        SubLine subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        // Test the getSegments method with different scenarios
        // Add test cases for empty sub-line, single segment sub-line, and unbounded sub-line
    }

    @Test
    public void testIntersection() {
        // Test the intersection method with various cases including intersecting sub-lines, non-intersecting sub-lines, and endpoints inclusion
    }

    @Test
    public void testBuildIntervalSet() {
        // Test the buildIntervalSet method with different input points
    }

    @Test
    public void testSide() {
        // Test the side method with different scenarios including parallel lines, intersecting lines, and different sides of the sub-line
    }

    @Test
    public void testSplit() {
        // Test the split method with different splitting hyperplanes and scenarios
    }
}