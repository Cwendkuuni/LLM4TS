import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SubLineTest {

    private SubLine subLine;
    private Line line;
    private Vector2D start;
    private Vector2D end;

    @Before
    public void setUp() {
        start = new Vector2D(1, 1);
        end = new Vector2D(2, 2);
        line = new Line(start, end);
        subLine = new SubLine(line, new IntervalsSet(0, 1));
    }

    @Test
    public void testGetSegments() {
        // Arrange
        Segment expectedSegment = new Segment(start, end, line);

        // Act
        List<Segment> segments = subLine.getSegments();

        // Assert
        assertEquals(1, segments.size());
        assertEquals(expectedSegment, segments.get(0));
    }

    @Test
    public void testIntersection_WithIntersectingSubLine_ReturnsIntersectionPoint() {
        // Arrange
        Vector2D otherStart = new Vector2D(0, 0);
        Vector2D otherEnd = new Vector2D(2, 2);
        SubLine otherSubLine = new SubLine(new Line(otherStart, otherEnd), new IntervalsSet(0, 1));

        // Act
        Vector2D intersection = subLine.intersection(otherSubLine, true);

        // Assert
        assertNotNull(intersection);
        assertEquals(new Vector2D(1, 1), intersection);
    }

    @Test
    public void testIntersection_WithNonIntersectingSubLine_ReturnsNull() {
        // Arrange
        Vector2D otherStart = new Vector2D(0, 0);
        Vector2D otherEnd = new Vector2D(1, 1);
        SubLine otherSubLine = new SubLine(new Line(otherStart, otherEnd), new IntervalsSet(0, 1));

        // Act
        Vector2D intersection = subLine.intersection(otherSubLine, true);

        // Assert
        assertNull(intersection);
    }

    @Test
    public void testIntersection_WithIntersectingEndPoints_ReturnsIntersectionPoint() {
        // Arrange
        Vector2D otherStart = new Vector2D(2, 2);
        Vector2D otherEnd = new Vector2D(3, 3);
        SubLine otherSubLine = new SubLine(new Line(otherStart, otherEnd), new IntervalsSet(0, 1));

        // Act
        Vector2D intersection = subLine.intersection(otherSubLine, true);

        // Assert
        assertNotNull(intersection);
        assertEquals(2, intersection.getX(), 1e-10);
        assertEquals(2, intersection.getY(), 1e-10);
    }

    @Test
    public void testIntersection_WithNonIntersectingEndPoints_ReturnsNull() {
        // Arrange
        Vector2D otherStart = new Vector2D(2, 2);
        Vector2D otherEnd = new Vector2D(3, 3);
        SubLine otherSubLine = new SubLine(new Line(otherStart, otherEnd), new IntervalsSet(0, 1));

        // Act
        Vector2D intersection = subLine.intersection(otherSubLine, false);

        // Assert
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        // Act
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);

        // Assert
        assertEquals(0, intervalSet.getSize(), 1e-10);
    }

    @Test
    public void testSide_WithParallelLines_ReturnsCorrectSide() {
        // Arrange
        Line otherLine = new Line(new Vector2D(2, 2), new Vector2D(3, 3));

        // Act
        Side side = subLine.side(otherLine);

        // Assert
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSide_WithIntersectingLines_ReturnsCorrectSide() {
        // Arrange
        Line otherLine = new Line(new Vector2D(0, 0), new Vector2D(2, 2));

        // Act
        Side side = subLine.side(otherLine);

        // Assert
        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit_WithParallelLines_ReturnsNull() {
        // Arrange
        Line otherLine = new Line(new Vector2D(2, 2), new Vector2D(3, 3));

        // Act
        SplitSubHyperplane<Euclidean2D> split = subLine.split(otherLine);

        // Assert
        assertNull(split.getPlus());
        assertNull(split.getMinus());
    }

    @Test
    public void testSplit_WithIntersectingLines_ReturnsCorrectSplitSubHyperplane() {
        // Arrange
        Line otherLine = new Line(new Vector2D(0, 0), new Vector2D(2, 2));

        // Act
        SplitSubHyperplane<Euclidean2D> split = subLine.split(otherLine);

        // Assert
        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());
        assertTrue(split.getPlus().getRemainingRegion().isEmpty());
        assertTrue(split.getMinus().getRemainingRegion().isEmpty());
    }

}