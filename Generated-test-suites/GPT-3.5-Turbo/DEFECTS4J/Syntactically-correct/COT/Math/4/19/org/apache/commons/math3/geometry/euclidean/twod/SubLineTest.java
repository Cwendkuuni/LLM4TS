
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructor1() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);

        // Act
        SubLine subLine = new SubLine(start, end);

        // Assert
        assertNotNull(subLine);
    }

    @Test
    public void testConstructor2() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        IntervalsSet remainingRegion = new IntervalsSet(0, 1);

        // Act
        SubLine subLine = new SubLine(line, remainingRegion);

        // Assert
        assertNotNull(subLine);
    }

    @Test
    public void testConstructor3() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end);

        // Act
        SubLine subLine = new SubLine(segment);

        // Assert
        assertNotNull(subLine);
    }

    @Test
    public void testGetSegments() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Act
        List<Segment> segments = subLine.getSegments();

        // Assert
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        boolean includeEndPoints = true;

        // Act
        Vector2D intersection = subLine1.intersection(subLine2, includeEndPoints);

        // Assert
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersection_IncludeEndPointsFalse() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        boolean includeEndPoints = false;

        // Act
        Vector2D intersection = subLine1.intersection(subLine2, includeEndPoints);

        // Assert
        assertNotNull(intersection);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersection_NoIntersection() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 2);
        Vector2D end2 = new Vector2D(1, 3);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        boolean includeEndPoints = true;

        // Act
        Vector2D intersection = subLine1.intersection(subLine2, includeEndPoints);

        // Assert
        assertNull(intersection);
    }

    @Test
    public void testBuildNew() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(0, 1);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);

        // Act
        SubLine newSubLine = subLine.buildNew(hyperplane, remainingRegion);

        // Assert
        assertNotNull(newSubLine);
    }

    @Test
    public void testSide() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Act
        Side side = subLine1.side(subLine2);

        // Assert
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Act
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2);

        // Assert
        assertNotNull(split);
    }

}
