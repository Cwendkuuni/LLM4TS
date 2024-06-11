import org.junit.Test;

public class SubLineTest {

    @Test
    public void testConstructor() {
        // Create a SubLine using a Line and a Region
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, region);
        
        // Check if the hyperplane and remainingRegion are set correctly
        Assert.assertEquals(line, subLine.getHyperplane());
        Assert.assertEquals(region, subLine.getRemainingRegion());
    }
    
    @Test
    public void testGetSegments() {
        // Create a SubLine with two segments
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(2, 2);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine = new SubLine(new Segment(start1, end1), new Segment(start2, end2));
        
        // Get the segments
        List<Segment> segments = subLine.getSegments();
        
        // Check if the segments are correct
        Assert.assertEquals(2, segments.size());
        Assert.assertEquals(new Segment(start1, end1), segments.get(0));
        Assert.assertEquals(new Segment(start2, end2), segments.get(1));
    }
    
    @Test
    public void testIntersection() {
        // Create two SubLines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        // Test intersection with includeEndPoints set to true
        Assert.assertEquals(new Vector2D(0.5, 0.5), subLine1.intersection(subLine2, true));
        
        // Test intersection with includeEndPoints set to false
        Assert.assertNull(subLine1.intersection(subLine2, false));
    }
    
    @Test
    public void testBuildIntervalSet() {
        // Create an interval set using two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        
        // Check if the interval set is correct
        Assert.assertEquals(new IntervalsSet(0, 1), intervalSet);
    }
    
    @Test
    public void testBuildNew() {
        // Create a SubLine using a Line and a Region
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line, region);
        
        // Create a new SubLine using the buildNew method
        SubLine subLine2 = subLine1.buildNew(line, region);
        
        // Check if the new SubLine is equal to the original SubLine
        Assert.assertEquals(subLine1, subLine2);
    }
    
    @Test
    public void testSide() {
        // Create two Lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        
        // Create a SubLine using the first Line
        SubLine subLine = new SubLine(line1, new IntervalsSet(0, 1));
        
        // Test side() method with the second Line
        Assert.assertEquals(Side.HYPER, subLine.side(line2));
    }
    
    @Test
    public void testSplit() {
        // Create two Lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        
        // Create a SubLine using the first Line
        SubLine subLine = new SubLine(line1, new IntervalsSet(0, 1));
        
        // Split the SubLine using the second Line
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine.split(line2);
        
        // Check if the split SubLine is correct
        Assert.assertEquals(new SubLine(line1.copySelf(), new IntervalsSet(0, 1)), splitSubLine.getPlus());
        Assert.assertNull(splitSubLine.getMinus());
    }
}