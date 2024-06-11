import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }
    
    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        
        List<Segment> segments = subLine.getSegments();
        Segment segment = segments.get(0);
        
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
    }
    
    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }
    
    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        
        assertEquals(new Interval(0, 1), intervalSet.getFirst());
    }
    
    @Test
    public void testSideParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Side side = subLine1.side(subLine2);
        
        assertEquals(Side.MINUS, side);
    }
    
    @Test
    public void testSideIntersectionLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Side side = subLine1.side(subLine2);
        
        assertEquals(Side.HYPER, side);
    }
    
    @Test
    public void testSplitParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        SplitSubHyperplane split = subLine1.split(subLine2);
        SubLine plus = split.getPlus();
        SubLine minus = split.getMinus();
        
        // Check plus side
        List<Segment> plusSegments = plus.getSegments();
        Segment plusSegment = plusSegments.get(0);
        assertEquals(new Vector2D(0, 1), plusSegment.getStart());
        assertEquals(new Vector2D(1, 1), plusSegment.getEnd());
        
        // Check minus side
        List<Segment> minusSegments = minus.getSegments();
        Segment minusSegment = minusSegments.get(0);
        assertEquals(new Vector2D(0, 0), minusSegment.getStart());
        assertEquals(new Vector2D(1, 0), minusSegment.getEnd());
    }
    
    @Test
    public void testSplitIntersectionLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(0, 1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        SplitSubHyperplane split = subLine1.split(subLine2);
        SubLine plus = split.getPlus();
        SubLine minus = split.getMinus();
        
        // Check plus side
        List<Segment> plusSegments = plus.getSegments();
        Segment plusSegment = plusSegments.get(0);
        assertEquals(new Vector2D(0, 0), plusSegment.getStart());
        assertEquals(new Vector2D(1, 1), plusSegment.getEnd());
        
        // Check minus side
        List<Segment> minusSegments = minus.getSegments();
        Segment minusSegment = minusSegments.get(0);
        assertEquals(new Vector2D(1, 0), minusSegment.getStart());
        assertEquals(new Vector2D(0, 1), minusSegment.getEnd());
    }
}