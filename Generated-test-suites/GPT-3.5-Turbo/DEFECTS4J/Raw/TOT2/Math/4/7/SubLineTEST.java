import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testSubLineConstructorWithHyperplane() {
        // Test creating a SubLine with a hyperplane
        // Initialize hyperplane and remainingRegion
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(0, 1);

        // Create SubLine
        SubLine subLine = new SubLine(hyperplane, remainingRegion);

        // Assert
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineConstructorWithEndpoints() {
        // Test creating a SubLine with endpoints
        // Initialize start and end points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);

        // Create SubLine
        SubLine subLine = new SubLine(start, end);

        // Assert
        assertEquals(new Line(start, end), subLine.getHyperplane());
    }

    @Test
    public void testSubLineConstructorWithSegment() {
        // Test creating a SubLine with a segment
        // Initialize segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1));

        // Create SubLine
        SubLine subLine = new SubLine(segment);

        // Assert
        assertEquals(segment.getLine(), subLine.getHyperplane());
    }

    // Add more test cases for other methods

}