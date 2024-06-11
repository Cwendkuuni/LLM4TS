import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {
  
    // Create instance of SubLine for testing
    private SubLine createTestSubLine() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        return new SubLine(start, end);
    }
  
    // Test getSegments method
    @Test
    public void testGetSegments() {
        SubLine subLine = createTestSubLine();
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(new Vector2D(0, 0), segment.getStart());
        assertEquals(new Vector2D(1, 1), segment.getEnd());
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), segment.getLine());
    }
  
    // Test intersection method
    @Test
    public void testIntersection() {
        SubLine subLine1 = createTestSubLine();
        SubLine subLine2 = createTestSubLine();
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0, 0), intersection);
        intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(0, 0), intersection);
    }
  
    // Test buildIntervalSet method
    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assertEquals(0, intervalSet.getSize());
        assertEquals(Double.NEGATIVE_INFINITY, intervalSet.getFirst());
        assertEquals(Double.POSITIVE_INFINITY, intervalSet.getLast());
    }
  
    // Test side method
    @Test
    public void testSide() {
        SubLine subLine = createTestSubLine();
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Side side = subLine.side(line);
        assertEquals(Side.HYPER, side);
    }
  
    // Test split method
    @Test
    public void testSplit() {
        SubLine subLine1 = createTestSubLine();
        SubLine subLine2 = createTestSubLine();
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());
        SubLine plus = (SubLine) split.getPlus();
        SubLine minus = (SubLine) split.getMinus();
        List<Segment> plusSegments = plus.getSegments();
        List<Segment> minusSegments = minus.getSegments();
        assertEquals(1, plusSegments.size());
        assertEquals(0, minusSegments.size());
        Segment plusSegment = plusSegments.get(0);
        assertEquals(new Vector2D(0, 0), plusSegment.getStart());
        assertEquals(new Vector2D(1, 1), plusSegment.getEnd());
    }
  
}