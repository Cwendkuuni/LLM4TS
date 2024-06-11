import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.junit.Test;
import java.util.List;

public class SubLineTest {

    @Test
    public void testConstructor1() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        // Check if sub-line is correctly created
        assert(subLine.getRemainingRegion().getSize() == end.distance(start));
    }

    @Test
    public void testConstructor2() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        Segment segment = new Segment(start, end, line);
        SubLine subLine = new SubLine(segment);
        // Check if sub-line is correctly created
        assert(subLine.getRemainingRegion().getSize() == end.distance(start));
    }

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        // Check if segments list has correct size and endpoints
        assert(segments.size() == 1);
        assert(segments.get(0).getStart().equals(start));
        assert(segments.get(0).getEnd().equals(end));
    }

    @Test
    public void testIntersection1() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        // Check if intersection point is correct
        assert(intersection.equals(new Vector2D(0.5, 0.5)));
    }

    @Test
    public void testIntersection2() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 2);
        Vector2D end2 = new Vector2D(1, 3);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        // Check if intersection point is null (lines don't intersect)
        assert(intersection == null);
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        Side side1 = subLine1.side(subLine2);
        Side side2 = subLine2.side(subLine1);
        // Check if sides are correct
        assert(side1 == Side.HYPER);
        assert(side2 == Side.HYPER);
    }

    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(subLine2);
        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(subLine1);
        // Check if split sub-hyperplanes are correct
        assert(split1.getPlus() == null && split1.getMinus() != null);
        assert(split2.getPlus() != null && split2.getMinus() == null);
    }

}