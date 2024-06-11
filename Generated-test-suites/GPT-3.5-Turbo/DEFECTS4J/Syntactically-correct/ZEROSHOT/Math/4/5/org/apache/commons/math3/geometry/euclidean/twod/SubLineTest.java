import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        SubLine line = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        List<Segment> segments = line.getSegments();
        
        assertEquals(1, segments.size());
        assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }
    
    @Test
    public void testIntersectionIncludeEndPoints() {
        SubLine line1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine line2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        
        Vector2D intersection = line1.intersection(line2, true);
        
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }
    
    @Test
    public void testIntersectionExcludeEndPoints() {
        SubLine line1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine line2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        
        Vector2D intersection = line1.intersection(line2, false);
        
        assertNull(intersection);
    }
    
    @Test
    public void testBuildIntervalSet() {
        IntervalsSet intervalSet = SubLine.buildIntervalSet(new Vector2D(0, 0), new Vector2D(1, 1));
        
        assertEquals(0.0, intervalSet.getInf(), 0.01);
        assertEquals(1.0, intervalSet.getSup(), 0.01);
    }
    
    @Test
    public void testSide() {
        SubLine line1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine line2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        
        assertEquals(Location.INSIDE, line1.side(line2));
    }
    
    @Test
    public void testSplit() {
        SubLine line1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine line2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        
        SubLine[] splitLines = line1.split(line2).getSubHyperplanes();
        
        assertEquals(new Vector2D(0.5, 0.5), splitLines[0].getSegments().get(0).getStart());
        assertEquals(new Vector2D(1, 1), splitLines[0].getSegments().get(0).getEnd());
        
        assertEquals(new Vector2D(0, 0), splitLines[1].getSegments().get(0).getStart());
        assertEquals(new Vector2D(0.5, 0.5), splitLines[1].getSegments().get(0).getEnd());
    }

}