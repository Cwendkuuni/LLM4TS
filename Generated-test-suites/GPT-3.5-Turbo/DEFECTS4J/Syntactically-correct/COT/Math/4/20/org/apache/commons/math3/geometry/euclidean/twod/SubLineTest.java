import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);

        // Act
        List<Segment> segments = subLine.getSegments();

        // Assert
        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(start, segments.get(0).getStart());
        Assert.assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Arrange
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(0, 2), new Vector2D(2, 0));
        SubLine subLine1 = new SubLine(line1.getStart(), line1.getEnd());
        SubLine subLine2 = new SubLine(line2.getStart(), line2.getEnd());

        // Act
        Vector2D intersection1 = subLine1.intersection(subLine2, true);
        Vector2D intersection2 = subLine1.intersection(subLine2, false);

        // Assert
        Assert.assertEquals(new Vector2D(1, 1), intersection1);
        Assert.assertEquals(new Vector2D(1, 1), intersection2);
    }

    @Test
    public void testBuildIntervalSet() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);

        // Act
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);

        // Assert
        Assert.assertTrue(intervalSet.contains(0));
        Assert.assertTrue(intervalSet.contains(2));
        Assert.assertFalse(intervalSet.contains(-1));
        Assert.assertFalse(intervalSet.contains(3));
    }

    @Test
    public void testSide() {
        // Arrange
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(0, 2), new Vector2D(2, 0));
        SubLine subLine1 = new SubLine(line1.getStart(), line1.getEnd());
        SubLine subLine2 = new SubLine(line2.getStart(), line2.getEnd());

        // Act
        Side side1 = subLine1.side(subLine2);
        Side side2 = subLine2.side(subLine1);

        // Assert
        Assert.assertEquals(Side.HYPER, side1);
        Assert.assertEquals(Side.HYPER, side2);
    }

    @Test
    public void testSplit() {
        // Arrange
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(0, 2), new Vector2D(2, 0));
        SubLine subLine1 = new SubLine(line1.getStart(), line1.getEnd());
        SubLine subLine2 = new SubLine(line2.getStart(), line2.getEnd());

        // Act
        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(subLine2);
        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(subLine1);

        // Assert
        Assert.assertNull(split1.getPlus());
        Assert.assertNull(split1.getMinus());
        Assert.assertNull(split2.getPlus());
        Assert.assertNull(split2.getMinus());
    }
}