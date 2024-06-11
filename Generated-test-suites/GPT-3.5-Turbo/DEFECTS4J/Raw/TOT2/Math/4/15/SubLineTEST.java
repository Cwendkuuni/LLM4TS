import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.partitioning.Region.Location;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
        assertEquals(new Line(start, end), segments.get(0).getLine());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        
        assertNull(subLine1.intersection(subLine2, false));
        assertEquals(new Vector2D(0.5, 0.5), subLine1.intersection(subLine2, true));
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assertEquals(0.0, intervalSet.asList().get(0).getInf(), 1e-12);
        assertEquals(1.0, intervalSet.asList().get(0).getSup(), 1e-12);
    }

    @Test
    public void testSide() {
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(0, 1), new Vector2D(1, 0));
        Line hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        assertEquals(Location.OUTSIDE, subLine1.side(hyperplane));
        assertEquals(Location.INSIDE, subLine2.side(hyperplane));
    }

    @Test
    public void testSplit() {
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        Line hyperplane = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SubLine.SplitSubHyperplane<Euclidean2D> splitResult = subLine1.split(hyperplane);
        
        assertEquals(1, splitResult.getPlus().getSegments().size());
        assertEquals(1, splitResult.getMinus().getSegments().size());
    }
}