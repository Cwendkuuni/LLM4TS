import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.oned.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region.Location;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(1, 2);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNotNull(intersectionPoint);
        assertEquals(new Vector2D(1, 1), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSide() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);

        Line line = new Line(new Vector2D(1, 0), new Vector2D(1, 2));
        assertEquals(Location.INSIDE, subLine.side(line.toSubSpace(new Vector1D(1))));
    }

    @Test
    public void testSplit() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);

        Line splittingLine = new Line(new Vector2D(1, 0), new Vector2D(1, 2));
        SplitSubHyperplane<Euclidean2D> split = subLine.split(splittingLine);

        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());
    }
}