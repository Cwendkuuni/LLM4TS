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
        assertNotNull(subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());
    }

    @Test
    public void testConstructor2() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        IntervalsSet intervalSet = new IntervalsSet(0, 1);

        // Act
        SubLine subLine = new SubLine(line, intervalSet);

        // Assert
        assertNotNull(subLine);
        assertNotNull(subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());
    }

    @Test
    public void testConstructor3() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));

        // Act
        SubLine subLine = new SubLine(segment);

        // Assert
        assertNotNull(subLine);
        assertNotNull(subLine.getHyperplane());
        assertNotNull(subLine.getRemainingRegion());
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
        assertNotNull(segments);
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }

    @Test
    public void testIntersection_includeEndPoints_true() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Act
        Vector2D intersection = subLine1.intersection(subLine2, true);

        // Assert
        assertNotNull(intersection);
        assertEquals(0.5, intersection.getX(), 0.0001);
        assertEquals(0.5, intersection.getY(), 0.0001);
    }

    @Test
    public void testIntersection_includeEndPoints_false() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Act
        Vector2D intersection = subLine1.intersection(subLine2, false);

        // Assert
        assertNotNull(intersection);
        assertEquals(0.5, intersection.getX(), 0.0001);
        assertEquals(0.5, intersection.getY(), 0.0001);
    }

    @Test
    public void testBuildIntervalSet() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);

        // Act
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);

        // Assert
        assertNotNull(intervalsSet);
        assertEquals(0, intervalsSet.asList().get(0).getInf(), 0.0001);
        assertEquals(1, intervalsSet.asList().get(0).getSup(), 0.0001);
    }

    @Test
    public void testBuildNew() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        IntervalsSet intervalSet = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, intervalSet);

        // Act
        SubLine newSubLine = subLine.buildNew(line, intervalSet);

        // Assert
        assertNotNull(newSubLine);
        assertEquals(subLine.getHyperplane(), newSubLine.getHyperplane());
        assertEquals(subLine.getRemainingRegion(), newSubLine.getRemainingRegion());
    }

    @Test
    public void testSide() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 0));

        // Act
        Side side1 = subLine1.side(line);
        Side side2 = subLine2.side(line);

        // Assert
        assertEquals(Side.HYPER, side1);
        assertEquals(Side.HYPER, side2);
    }

    @Test
    public void testSplit() {
        // Arrange
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 0));

        // Act
        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(line);
        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(line);

        // Assert
        assertNotNull(split1);
        assertNotNull(split1.getPlus());
        assertNotNull(split1.getMinus());

        assertNotNull(split2);
        assertNotNull(split2.getPlus());
        assertNotNull(split2.getMinus());
    }

}