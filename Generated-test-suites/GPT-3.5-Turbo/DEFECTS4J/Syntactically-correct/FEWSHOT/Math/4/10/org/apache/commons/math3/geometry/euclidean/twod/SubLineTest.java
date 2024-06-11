import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SubLineTest {
    
    @Test
    public void testConstructorWithHyperplaneAndRegion() {
        // Create a hyperplane and a region
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet remainingRegion = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, remainingRegion);
        
        // Check that the hyperplane and remaining region are set correctly
        assertEquals(line, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }
    
    @Test
    public void testConstructorWithStartAndEnd() {
        // Create start and end vectors
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        
        // Create the sub-line using the constructor
        SubLine subLine = new SubLine(start, end);
        
        // Check that the hyperplane is set correctly
        Line expectedHyperplane = new Line(start, end);
        assertEquals(expectedHyperplane, subLine.getHyperplane());
        
        // Check that the remaining region is set correctly
        IntervalsSet expectedRegion = new IntervalsSet(0, 1);
        assertEquals(expectedRegion, subLine.getRemainingRegion());
    }
    
    @Test
    public void testConstructorWithSegment() {
        // Create a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0,0), new Vector2D(1,1)));
        
        // Create the sub-line using the constructor
        SubLine subLine = new SubLine(segment);
        
        // Check that the hyperplane is set correctly
        assertEquals(segment.getLine(), subLine.getHyperplane());
        
        // Check that the remaining region is set correctly
        IntervalsSet expectedRegion = new IntervalsSet(0, 1);
        assertEquals(expectedRegion, subLine.getRemainingRegion());
    }
    
    @Test
    public void testGetSegments() {
        // Create the sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);
        
        // Get the segments
        List<Segment> segments = subLine.getSegments();
        
        // Check that the number of segments is correct
        assertEquals(1, segments.size());
        
        // Check that the segment start and end points are correct
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }
    
    @Test
    public void testIntersectionWithIncludeEndPointsTrue() {
        // Create two sub-lines
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(0.5, 0), new Vector2D(1, 0));
        
        // Perform the intersection with end points included
        Vector2D intersection = subLine1.intersection(subLine2, true);
        
        // Check that the intersection point is correct
        Vector2D expectedIntersection = new Vector2D(0.5, 0.5);
        assertEquals(expectedIntersection, intersection);
    }
    
    @Test
    public void testIntersectionWithIncludeEndPointsFalse() {
        // Create two sub-lines
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine2 = new SubLine(new Vector2D(0.5, 0), new Vector2D(1, 0));
        
        // Perform the intersection with end points excluded
        Vector2D intersection = subLine1.intersection(subLine2, false);
        
        // Check that the intersection point is correct
        assertNull(intersection);
    }
    
    @Test
    public void testBuildIntervalSet() {
        // Create start and end vectors
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        
        // Build the interval set
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        
        // Check that the interval set is correct
        IntervalsSet expectedIntervalSet = new IntervalsSet(0, 1);
        assertEquals(expectedIntervalSet, intervalSet);
    }
    
    @Test
    public void testSide() {
        // Create two sub-lines
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(0.5, 0), new Vector2D(1, 0));
        
        // Get the side of sub-line2 with respect to sub-line1
        Side side = subLine1.side(subLine2.getHyperplane());
        
        // Check that the side is correct
        assertEquals(Side.PLUS, side);
    }
    
    @Test
    public void testSplit() {
        // Create two sub-lines
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(0.5, 0), new Vector2D(1, 0));
        
        // Split subLine1 with subLine2
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        
        // Check that the split sub-hyperplanes are correct
        SubLine plus = (SubLine) split.getPlus();
        SubLine minus = (SubLine) split.getMinus();
        assertEquals(new SubLine(new Vector2D(0.5, 0.5), new Vector2D(1, 1)), plus);
        assertNull(minus);
    }

}