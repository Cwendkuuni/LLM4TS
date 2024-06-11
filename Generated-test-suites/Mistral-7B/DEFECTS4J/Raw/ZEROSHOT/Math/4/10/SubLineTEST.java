import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Lines2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine sl = new SubLine(hyperplane, remainingRegion);
        assertEquals("Incorrect hyperplane", hyperplane, sl.getHyperplane());
        assertEquals("Incorrect remaining region", remainingRegion, sl.getRemainingRegion());

        // test constructor from start and end points
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        Hyperplane<Euclidean2D> line = new Line(start, end).toHyperplane();
        remainingRegion = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
        SubLine sl2 = new SubLine(start, end);
        assertEquals("Incorrect hyperplane", line, sl2.getHyperplane());
        assertEquals("Incorrect remaining region", remainingRegion, sl2.getRemainingRegion());

        // test constructor from a segment
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0), new Line(start, end));
        Hyperplane<Euclidean2D> line2 = new Line(segment.getStart(), segment.getEnd()).toHyperplane();
        remainingRegion = new IntervalsSet(new Interval(0.5, 1.5), new Interval(2.5, 3.5));
        SubLine sl3 = new SubLine(segment);
        assertEquals("Incorrect hyperplane", line2, sl3.getHyperplane());
        assertEquals("Incorrect remaining region", remainingRegion, sl3.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        // test simple case - one interval
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        Line line = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(1.0, 1.5));
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals("Incorrect number of segments", 1, segments.size());
        Segment seg = segments.get(0);
        assertEquals("Incorrect start point", new Vector2D(line.toSpace(seg.getStart()).getX(), line.toSpace(seg.getStart()).getY()), seg.getStart());
        assertEquals("Incorrect end point", new Vector2D(line.toSpace(seg.getEnd()).getX(), line.toSpace(seg.getEnd()).getY()), seg.getEnd());

        // test case with multiple intervals
        start = new Vector2D(0.0, 0.0);
        end = new Vector2D(4.0, 4.0);
        line = new Line(start, end);
        remainingRegion = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        subLine = new SubLine(line, remainingRegion);
        segments = subLine.getSegments();
        assertEquals("Incorrect number of segments", 2, segments.size());
        seg = segments.get(0);
        assertEquals("Incorrect start point", new Vector2D(line.toSpace(seg.getStart()).getX(), line.toSpace(seg.getStart()).getY()), seg.getStart());
        assertEquals("Incorrect end point", new Vector2D(line.toSpace(seg.getEnd()).getX(), line.toSpace(seg.getEnd()).getY()), seg.getEnd());
        seg = segments.get(1);
        assertEquals("Incorrect start point", new Vector2D(line.toSpace(seg.getStart()).getX(), line.toSpace(seg.getStart()).getY()), seg.getStart());
        assertEquals("Incorrect end point", new Vector2D(line.toSpace(seg.getEnd()).getX(), line.toSpace(seg.getEnd()).getY()), seg.getEnd());

        // test case with no intervals
        start = new Vector2D(0.0, 0.0);
        end = new Vector2D(4.0, 4.0);
        line = new Line(start, end);
        remainingRegion = new IntervalsSet();
        subLine = new SubLine(line, remainingRegion);
        segments = subLine.getSegments();
        assertEquals("Incorrect number of segments", 0, segments.size());

        // test case with infinite intervals
        start = new Vector2D(-10.0, -10.0);
        end = new Vector2D(10.0, 10.0);
        line = new Line(start, end);
        remainingRegion = new IntervalsSet(new Interval(-10.0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 10.0));
        subLine = new SubLine(line, remainingRegion);
        segments = subLine.getSegments();
        assertEquals("Incorrect number of segments", 1, segments.size());
        seg = segments.get(0);
        assertEquals("Incorrect start point", new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), seg.getStart());
        assertEquals("Incorrect end point", new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), seg.getEnd());
    }

    @Test
    public void testIntersection() {
        // test intersection of two lines with no intersection point
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(2.0, 2.0);
        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(3.0, 3.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());
        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull("Expected null as intersection point", intersectionPoint);

        // test intersection of two lines with intersection point
        start1 = new Vector2D(0.0, 0.0);
        start2 = new Vector2D(2.0, 2.0);
        end1 = new Vector2D(2.0, 0.0);
        end2 = new Vector2D(4.0, 4.0);
        line1 = new Line(start1, end1);
        line2 = new Line(start2, end2);
        SubLine subLine1_1 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2_1 = new SubLine(line2, new IntervalsSet());
        intersectionPoint = subLine1_1.intersection(subLine2_1, false);
        assertEquals("Expected intersection point (1.0, 1.0)", new Vector2D(1.0, 1.0), intersectionPoint);

        // test intersection of infinite lines with intersection point
        start1 = new Vector2D(-10.0, -10.0);
        end1 = new Vector2D(10.0, 10.0);
        start2 = new Vector2D(-20.0, -20.0);
        end2 = new Vector2D(20.0, 20.0);
        line1 = new Line(start1, end1);
        line2 = new Line(start2, end2);
        SubLine subLine1_2 = new SubLine(line1, new IntervalsSet(new Interval(-10.0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 10.0)));
        SubLine subLine2_2 = new SubLine(line2, new IntervalsSet(new Interval(-20.0, Double.POSITIVE_INFINITY), new Interval(Double.NEGATIVE_INFINITY, 20.0)));
        intersectionPoint = subLine1_2.intersection(subLine2_2, false);
        assertEquals("Expected intersection point (0.0, 0.0)", new Vector2D(0.0, 0.0), intersectionPoint);

        // test intersection of two lines with no common points
        start1 = new Vector2D(0.0, 0.0);
        end1 = new Vector2D(2.0, 2.0);
        start2 = new Vector2D(3.0, 3.0);
        end2 = new Vector2D(4.0, 4.0);
        line1 = new Line(start1, end1);
        line2 = new Line(start2, end2);
        SubLine subLine1_3 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2_3 = new SubLine(line2, new IntervalsSet());
        intersectionPoint = subLine1_3.intersection(subLine2_3, true);
        assertNull("Expected null as intersection point", intersectionPoint);

        // test intersection of two lines with intersection point on endpoints
        start1 = new Vector2D(0.0, 0.0);
        start2 = new Vector2D(2.0, 2.0);
        end1 = new Vector2D(3.0, 3.0);
        end2 = new Vector2D(2.0, 2.0);
        line1 = new Line(start1, end1);
        line2 = new Line(start2, end2);
        SubLine subLine1_4 = new SubLine(line1, new IntervalsSet());
        SubLine subLine2_4 = new SubLine(line2, new IntervalsSet());
        intersectionPoint = subLine1_4.intersection(subLine2_4, true);
        assertEquals("Expected intersection point (2.0, 2.0)", new Vector2D(2.0, 2.0), intersectionPoint);
    }

}