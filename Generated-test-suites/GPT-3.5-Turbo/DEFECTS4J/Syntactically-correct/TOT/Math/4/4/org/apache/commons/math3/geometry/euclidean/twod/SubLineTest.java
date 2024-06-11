import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.RegionFactory;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {
    
    // Helper method to check if two double values are equal within a given epsilon
    private boolean equals(double x, double y, double epsilon) {
        return FastMath.abs(x - y) < epsilon;
    }
    
    @Test
    public void testConstructor() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervals = new IntervalsSet(0, 1);
        
        SubLine subLine = new SubLine(line, intervals);
        
        assertEquals(line, subLine.getHyperplane());
        assertEquals(intervals, subLine.getRemainingRegion());
    }
    
    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervals = new IntervalsSet(0, 1);
        
        SubLine subLine = new SubLine(line, intervals);
        
        Segment segment = subLine.getSegments().get(0);
        
        assertEquals(line.toSpace(new Vector1D(0)), segment.getStart());
        assertEquals(line.toSpace(new Vector1D(1)), segment.getEnd());
        assertEquals(line, segment.getLine());
    }
    
    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 0), new Vector2D(-1, 1));
        
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(0, 1));
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertTrue(equals(0, intersection.getX(), 1e-10));
        assertTrue(equals(0, intersection.getY(), 1e-10));

        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);

        subLine1 = new SubLine(line1, new IntervalsSet(0, 2));
        subLine2 = new SubLine(line2, new IntervalsSet(0, 2));
        
        intersection = subLine1.intersection(subLine2, true);
        assertTrue(equals(0, intersection.getX(), 1e-10));
        assertTrue(equals(0, intersection.getY(), 1e-10));
        
        intersection = subLine1.intersection(subLine2, false);
        assertNull(intersection);
    }
    
    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        
        IntervalsSet intervals = SubLine.buildIntervalSet(start, end);
        
        assertEquals(0, intervals.asList().get(0).getInf(), 1e-10);
        assertEquals(1, intervals.asList().get(0).getSup(), 1e-10);
    }
    
    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, -1), new Vector2D(1, 0));
        
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(0, 1));

        assertEquals(Location.INSIDE, subLine1.side(line2));
        assertEquals(Location.OUTSIDE, subLine2.side(line1));
        
        subLine1 = new SubLine(line1, new IntervalsSet(0, 2));
        subLine2 = new SubLine(line2, new IntervalsSet(0, 2));
        
        assertEquals(Location.INSIDE, subLine1.side(line2));
        assertEquals(Location.OUTSIDE, subLine2.side(line1));
    }
    
    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, -1), new Vector2D(1, 0));
        
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(0, 1));

        SplitSubHyperplane<Euclidean2D> split = subLine1.split(line2);
        SubLine plus = (SubLine) split.getPlus();
        SubLine minus = (SubLine) split.getMinus();
        
        assertEquals(line1, plus.getHyperplane());
        assertEquals(new IntervalsSet(0, 0.5), plus.getRemainingRegion());
        assertEquals(line1, minus.getHyperplane());
        assertEquals(new IntervalsSet(0.5, 1), minus.getRemainingRegion());
        
        subLine1 = new SubLine(line1, new IntervalsSet(0, 2));
        subLine2 = new SubLine(line2, new IntervalsSet(0, 2));
        
        split = subLine1.split(line2);
        plus = (SubLine) split.getPlus();
        minus = (SubLine) split.getMinus();
        
        assertEquals(line1, plus.getHyperplane());
        assertEquals(new IntervalsSet(0, 0.5), plus.getRemainingRegion());
        assertEquals(line1, minus.getHyperplane());
        assertEquals(new IntervalsSet(0.5, 1), minus.getRemainingRegion());
    }
    
}