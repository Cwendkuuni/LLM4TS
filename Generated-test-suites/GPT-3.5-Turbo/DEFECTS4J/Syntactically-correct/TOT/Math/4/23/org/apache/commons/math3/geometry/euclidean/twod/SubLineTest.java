
import org.junit.Test;

import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region.Location;

import java.util.List;

import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Create a sub-line from (0,0) to (1,1)
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        
        // Get the segments of the sub-line
        List<Segment> segments = subLine.getSegments();
        
        // Check that only one segment is returned
        assertEquals(1, segments.size());
        
        // Check that the start and end points of the segment are correct
        assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Create two sub-lines that intersect at point (1,1)
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine2 = new SubLine(new Vector2D(1, 0), new Vector2D(1, 2));
        
        // Get the intersection point of the sub-lines
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        
        // Check that the intersection point is correct
        assertEquals(new Vector2D(1, 1), intersectionPoint);
    }

    @Test
    public void testIntersection_noIntersection() {
        // Create two sub-lines that do not intersect
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(2, 0), new Vector2D(2, 1));
        
        // Get the intersection point of the sub-lines
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        
        // Check that the intersection point is null
        assertNull(intersectionPoint);
    }

    @Test
    public void testIntersection_includeEndPoints() {
        // Create two sub-lines that intersect at endpoint (1,1)
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(1, 1), new Vector2D(2, 2));
        
        // Get the intersection point of the sub-lines including endpoints
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        
        // Check that the intersection point is correct
        assertEquals(new Vector2D(1, 1), intersectionPoint);
    }

    @Test
    public void testIntersection_excludeEndPoints() {
        // Create two sub-lines that intersect at endpoint (1,1)
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(1, 1), new Vector2D(2, 2));
        
        // Get the intersection point of the sub-lines excluding endpoints
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        
        // Check that the intersection point is null
        assertNull(intersectionPoint);
    }

    @Test
    public void testGetRemainingRegion() {
        // Create a sub-line from (0,0) to (1,1)
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        
        // Get the remaining region of the sub-line
        Location location = subLine.getRemainingRegion().checkPoint(subLine.toSpace(new Vector2D(0.5, 0.5)));
        
        // Check that the point lies inside the remaining region
        assertEquals(Location.INSIDE, location);
    }
}

