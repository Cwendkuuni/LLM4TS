import static org.junit.Assert.*;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        // Test the constructor with a hyperplane and a remaining region
        SubLine subLine = new SubLine(new Line(new Vector2D(1, 1), new Vector2D(2, 2)), new IntervalsSet(0.0, 1.0));
        assertNotNull(subLine);
    }

    @Test
    public void testSubLineEndpoints() {
        // Test getting endpoints of a sub-line
        SubLine subLine = new SubLine(new Line(new Vector2D(1, 1), new Vector2D(2, 2)), new IntervalsSet(0.0, 1.0));
        assertEquals(1, subLine.getSegments().size());
    }

    @Test
    public void testSubLineIntersection() {
          // Test intersection of two sub-lines
          SubLine subLine1 = new SubLine(new Line(new Vector2D(1, 1), new Vector2D(2, 2)), new IntervalsSet(0.0, 1.0));
          SubLine subLine2 = new SubLine(new Line(new Vector2D(1, 1), new Vector2D(3, 3)), new IntervalsSet(0.0, 1.0));
          Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
          assertNotNull(intersectionPoint);
    }

    // Add more test cases for other methods in SubLine class

}