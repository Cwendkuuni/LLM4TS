import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SubLine;
import org.apache.commons.math3.util.FastMath;

public class SubLineTest {

    @Test
    public void testConstruction() {
        Line line = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine1 = new SubLine(line, new IntervalsSet(new Interval(1.5, 2.5)));
        SubLine subLine2 = new SubLine(line, new Interval(0.5, 3.5));

        assertEquals("Check hyperplane", line, subLine1.getHyperplane());
        assertEquals("Check remaining region", new IntervalsSet(new Interval(1.5, 2.5)), subLine1.getRemainingRegion());
        assertEquals("Check hyperplane", line, subLine2.getHyperplane());
        assertEquals("Check remaining region", new IntervalsSet(new Interval(0.5, 3.5)), subLine2.getRemainingRegion());

        SubLine subLine3 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 0.0)), null);
        SubLine subLine4 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(0.0, 3.0)), null);

        assertEquals("Check hyperplane", new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 0.0)), subLine3.getHyperplane());
        assertEquals("Check remaining region", new IntervalsSet(), subLine3.getRemainingRegion());
        assertEquals("Check hyperplane", new Line(new Vector2D(0.0, 0.0), new Vector2D(0.0, 3.0)), subLine4.getHyperplane());
        assertEquals("Check remaining region", new IntervalsSet(), subLine4.getRemainingRegion());

        Segment segment = new Segment(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0), line);
        SubLine subLine5 = new SubLine(segment);

        assertEquals("Check hyperplane", line, subLine5.getHyperplane());
        assertEquals("Check remaining region", new IntervalsSet(new Interval(1.0, 2.0)), subLine5.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(1.5, 2.5)));

        List<Segment> segments = subLine.getSegments();

        assertEquals("Check number of segments", 1, segments.size());
        Segment segment = segments.get(0);
        assertEquals("Check start point", new Vector2D(1.5, 1.5), segment.getStart());
        assertEquals("Check end point", new Vector2D(2.0, 2.0), segment.getEnd());
        assertEquals("Check line", line, segment.getLine());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 0.0));
        Line line2 = new Line(new Vector2D(0.0, 3.0), new Vector2D(3.0, 0.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(1.0, 2.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        assertEquals("Check intersection point", new Vector2D(1.5, 1.5), intersectionPoint);

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 3.0)));

        intersectionPoint = subLine1.intersection(subLine2, true);

        assertNull("Check no intersection", intersectionPoint);

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0), new Interval(3.0, 4.0)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.0, 3.0)));

        intersectionPoint = subLine1.intersection(subLine2, false);

        assertNull("Check no intersection", intersectionPoint);
    }
}