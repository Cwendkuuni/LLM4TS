import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.partitioning.Region.Location;

public class SubLineTest {

    @Test
    public void testCreateSubLineFromTwoPoints() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(2.0, 3.0);
        SubLine subLine = new SubLine(start, end);
        
        assertNotNull(subLine);
        // Additional assertions can be added here to check the correctness of the SubLine creation
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(3.0, 3.0);
        SubLine subLine = new SubLine(start, end);
        
        assertEquals(1, subLine.getSegments().size());
        // Additional assertions can be added here to check the correctness of the segments retrieved
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(1.0, 1.0);
        Vector2D end1 = new Vector2D(3.0, 3.0);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2.0, 1.0);
        Vector2D end2 = new Vector2D(4.0, 3.0);
        SubLine subLine2 = new SubLine(start2, end2);

        assertNotNull(subLine1.intersection(subLine2, true));
        assertNull(subLine1.intersection(subLine2, false));
        // Additional assertions can be added here to test intersection scenarios
    }

    // Add more test cases as needed

}