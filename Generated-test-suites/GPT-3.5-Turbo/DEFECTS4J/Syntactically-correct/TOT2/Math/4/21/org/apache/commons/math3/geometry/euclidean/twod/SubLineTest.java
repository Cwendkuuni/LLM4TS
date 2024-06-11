import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        // Given
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);

        // When
        SubLine subLine = new SubLine(line, region);

        // Then
        assertNotNull(subLine);
    }

    @Test
    public void testSubLineEndpoints() {
        // Given
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);

        // When
        SubLine subLine = new SubLine(start, end);

        // Then
        assertEquals(1, subLine.getSegments().size());
    }

    @Test
    public void testSubLineIntersection() {
        // Given
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(1, 2);
        SubLine subLine2 = new SubLine(start2, end2);

        // When
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        // Then
        assertNotNull(intersectionPoint);
    }

    @Test
    public void testSubLineSplit() {
        // Given
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);
        Line splitLine = new Line(new Vector2D(1, 0), new Vector2D(1, 2));

        // When
        SplitSubHyperplane<Euclidean2D> split = subLine.split(splitLine);

        // Then
        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());
    }
}