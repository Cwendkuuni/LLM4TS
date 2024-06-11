import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Point2D;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineIntersection() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        Line line2 = new Line(new Point2D(0.0, 1.0), new Point2D(1.0, 2.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0.5, 1.5)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Point2D(0.5, 1.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSubLineSplit() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Line line1 = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        Line line2 = new Line(new Point2D(0.5, 0.5), new Point2D(1.5, 1.5));
        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0), new Interval(1.5, 2.0)));

        Hyperplane hyperplane = new Line(new Point2D(0.25, 0.25), new Vector2D(1.0, -1.0));
        SubHyperplane subHyperplane = subLine.split(hyperplane);

        assertTrue(subHyperplane instanceof SplitSubHyperplane);
        SplitSubHyperplane splitSubHyperplane = (SplitSubHyperplane) subHyperplane;
        assertEquals(subLine.getHyperplane().getAngle(), splitSubHyperplane.getThis().getHyperplane().getAngle(), 0.001);
        assertEquals(subLine.getRemainingRegion().getMinX(), splitSubHyperplane.getThis().getRemainingRegion().getMinX(), 0.001);
        assertEquals(subLine.getRemainingRegion().getMaxX(), splitSubHyperplane.getOther().getRemainingRegion().getMinX(), 0.001);
    }

    @Test
    public void testSubLineGetSegments() {
        Euclidean2DSpace space = new Euclidean2DSpace();
        Line line = new Line(new Point2D(0.0, 0.0), new Point2D(1.0, 1.0));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0.25, 0.75), new Interval(0.9, 1.3)));

        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment segment1 = segments.get(0);
        Segment segment2 = segments.get(1);

        assertEquals(new Point2D(0.25, 0.25), segment1.getStart());
        assertEquals(new Point2D(0.75, 0.75), segment1.getEnd());
        assertEquals(new Point2D(0.9, 0.9), segment2.getStart());
        assertEquals(new Point2D(1.3, 1.3), segment2.getEnd());
    }

}