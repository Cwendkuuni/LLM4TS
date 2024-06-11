import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Arrange
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        
        // Act
        List<Segment> segments = subLine.getSegments();
        
        // Assert
        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        Assert.assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Arrange
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        
        // Act
        Vector2D intersection1 = subLine1.intersection(subLine2, true);
        Vector2D intersection2 = subLine1.intersection(subLine2, false);
        
        // Assert
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersection1);
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersection2);
    }

    @Test
    public void testBuildIntervalSet() {
        // Arrange
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        
        // Act
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        
        // Assert
        Assert.assertEquals(0.0, intervalSet.asList().get(0).getInf(), 0.0);
        Assert.assertEquals(1.0, intervalSet.asList().get(0).getSup(), 0.0);
    }

    @Test
    public void testBuildNew() {
        // Arrange
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(0, 1);
        
        // Act
        SubLine newSubLine = subLine.buildNew(hyperplane, remainingRegion);
        
        // Assert
        Assert.assertEquals(hyperplane, newSubLine.getHyperplane());
        Assert.assertEquals(remainingRegion, newSubLine.getRemainingRegion());
    }

    @Test
    public void testSide() {
        // Arrange
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        
        // Act
        Side side1 = subLine1.side(subLine2.getHyperplane());
        Side side2 = subLine2.side(subLine1.getHyperplane());
        
        // Assert
        Assert.assertEquals(Side.HYPER, side1);
        Assert.assertEquals(Side.HYPER, side2);
    }

    @Test
    public void testSplit() {
        // Arrange
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 0), new Vector2D(0, 1));
        
        // Act
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        
        // Assert
        Assert.assertNull(splitSubHyperplane.getMinus());
        Assert.assertNull(splitSubHyperplane.getPlus());
    }

}