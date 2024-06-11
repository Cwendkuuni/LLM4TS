import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.junit.Test;

public class SubLineTest {

    private static final double EPSILON = 1e-10;

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
    public void testIntersection_NoIntersection() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start, end);
        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(3, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Act
        Vector2D intersection = subLine1.intersection(subLine2, true);

        // Assert
        assertNull(intersection);
    }

    @Test
    public void testIntersection_IntersectionWithEndPoints() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start, end);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Act
        Vector2D intersection = subLine1.intersection(subLine2, true);

        // Assert
        assertNotNull(intersection);
        assertEquals(1, intersection.getX(), EPSILON);
        assertEquals(1, intersection.getY(), EPSILON);
    }

    @Test
    public void testIntersection_IntersectionWithoutEndPoints() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start, end);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Act
        Vector2D intersection = subLine1.intersection(subLine2, false);

        // Assert
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);

        // Act
        SubLine subLine = new SubLine(start, end);

        // Assert
        assertEquals(start.getX(), subLine.getRemainingRegion().getBounds().getLower(), EPSILON);
        assertEquals(end.getX(), subLine.getRemainingRegion().getBounds().getUpper(), EPSILON);
    }

    @Test
    public void testSide() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Act
        Location location = subLine.side(start);

        // Assert
        assertEquals(Location.BOUNDARY, location);
    }

    @Test
    public void testSplit() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Vector2D point = new Vector2D(0.5, 0.5);
        SubLine subLine2 = new SubLine(point, end);

        // Act
        SubLine.SplitSubHyperplane<Euclidean2D> split = subLine.split(subLine2);

        // Assert
        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());
    }

    // Add more test cases for other methods if needed

}

