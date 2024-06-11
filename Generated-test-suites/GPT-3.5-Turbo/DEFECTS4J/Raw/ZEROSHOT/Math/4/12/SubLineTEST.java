import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.*;

public class SubLineTest {

    private SubLine subLine;

    @Before
    public void setUp() {
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        subLine = new SubLine(start, end);
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());
        assertEquals(1, segments.get(0).getStart().getX(), 0);
        assertEquals(2, segments.get(0).getStart().getY(), 0);
        assertEquals(3, segments.get(0).getEnd().getX(), 0);
        assertEquals(4, segments.get(0).getEnd().getY(), 0);
    }

    @Test
    public void testIntersectionIncludeEndPoints() {
        SubLine subLine2 = new SubLine(new Vector2D(2, 3), new Vector2D(4, 5));

        Vector2D intersection = subLine.intersection(subLine2, true);

        assertEquals(3, intersection.getX(), 0);
        assertEquals(4, intersection.getY(), 0);
    }

    @Test
    public void testIntersectionExcludeEndPoints() {
        SubLine subLine2 = new SubLine(new Vector2D(2, 3), new Vector2D(4, 5));

        Vector2D intersection = subLine.intersection(subLine2, false);

        assertEquals(3, intersection.getX(), 0);
        assertEquals(4, intersection.getY(), 0);
    }

    @Test
    public void testSplit() {
        Line line = new Line(new Vector2D(2, 3), new Vector2D(4, 5));
        SubLine subLine2 = new SubLine(line, new IntervalsSet(line.toSubSpace(new Vector2D(2, 3)).getX(), line.toSubSpace(new Vector2D(4, 5)).getX()));

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(subLine2);

        SubLine plus = (SubLine) splitSubHyperplane.getPlus();
        SubLine minus = (SubLine) splitSubHyperplane.getMinus();

        List<Segment> plusSegments = plus.getSegments();
        List<Segment> minusSegments = minus.getSegments();

        assertEquals(1, plusSegments.size());
        assertEquals(2, plusSegments.get(0).getStart().getX(), 0);
        assertEquals(3, plusSegments.get(0).getStart().getY(), 0);
        assertEquals(3, plusSegments.get(0).getEnd().getX(), 0);
        assertEquals(4, plusSegments.get(0).getEnd().getY(), 0);
        
        assertEquals(1, minusSegments.size());
        assertEquals(3, minusSegments.get(0).getStart().getX(), 0);
        assertEquals(4, minusSegments.get(0).getStart().getY(), 0);
        assertEquals(4, minusSegments.get(0).getEnd().getX(), 0);
        assertEquals(5, minusSegments.get(0).getEnd().getY(), 0);
    }


}