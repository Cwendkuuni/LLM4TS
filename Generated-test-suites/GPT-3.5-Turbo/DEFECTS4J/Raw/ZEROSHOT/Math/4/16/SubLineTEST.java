import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;

public class SubLineTest {
    
    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        Line line = new Line(start, end);
        IntervalsSet intervalSet = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, intervalSet);
        
        List<Segment> segments = subLine.getSegments();
        
        assertEquals(1, segments.size());
        assertEquals(start, segment.get(0).getStart());
        assertEquals(end, segment.get(0).getEnd());
        assertEquals(line, segment.get(0).getLine());
    }
    
    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(1, 2);
        Vector2D end1 = new Vector2D(3, 4);
        Line line1 = new Line(start1, end1);
        IntervalsSet intervalSet1 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, intervalSet1);
        
        Vector2D start2 = new Vector2D(2, 3);
        Vector2D end2 = new Vector2D(4, 5);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalSet2 = new IntervalsSet(0, 1);
        SubLine subLine2 = new SubLine(line2, intervalSet2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        
        assertNotNull(intersection);
        assertEquals(2, intersection.getX(), 1e-10);
        assertEquals(3, intersection.getY(), 1e-10);
    }
    
    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        
        assertEquals(1, intervalSet.asList().size());
        assertEquals(0, intervalSet.asList().get(0).getInf(), 1e-10);
        assertEquals(1, intervalSet.asList().get(0).getSup(), 1e-10);
    }
    
    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 0);
        Line line1 = new Line(start1, end1);
        IntervalsSet intervalSet1 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, intervalSet1);
        
        Vector2D point1 = new Vector2D(1, 1);
        Vector2D point2 = new Vector2D(3, 1);
        
        Side side1 = subLine1.side(point1);
        Side side2 = subLine1.side(point2);
        
        assertEquals(Side.PLUS, side1);
        assertEquals(Side.MINUS, side2);
    }
    
    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 0);
        Line line1 = new Line(start1, end1);
        IntervalsSet intervalSet1 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, intervalSet1);
        
        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(3, 0);
        Line line2 = new Line(start2, end2);
        IntervalsSet intervalSet2 = new IntervalsSet(0, 1);
        SubLine subLine2 = new SubLine(line2, intervalSet2);
        
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(subLine2);
        
        SubLine plus = (SubLine) splitSubHyperplane.getPlus();
        SubLine minus = (SubLine) splitSubHyperplane.getMinus();
        
        assertEquals(start1, plus.getSegments().get(0).getStart());
        assertEquals(start2, minus.getSegments().get(0).getStart());
    }
}