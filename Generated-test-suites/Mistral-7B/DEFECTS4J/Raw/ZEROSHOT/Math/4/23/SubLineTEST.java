import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test construction from hyperplane and remaining region
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1)).toRegion();
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test construction from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine2 = new SubLine(start, end);
        assertEquals(start, subLine2.getSegments().get(0).getStart());
        assertEquals(end, subLine2.getSegments().get(0).getEnd());

        // Test construction from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1)));
        SubLine subLine3 = new SubLine(segment);
        assertEquals(segment.getStart(), subLine3.getSegments().get(0).getStart());
        assertEquals(segment.getEnd(), subLine3.getSegments().get(0).getEnd());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1), new Interval(2, 3)).toRegion();
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();

        assertEquals(2, segments.size());
        assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
        assertEquals(new Vector2D(2, 2), segments.get(1).getStart());
        assertEquals(new Vector2D(3, 3), segments.get(1).getEnd());
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)).toRegion());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)).toRegion());

        // Test intersection on a common point
        Vector2D intersectionPoint = new Vector2D(1, 1);
        Vector2D result = subLine1.intersection(subLine2, true);
        assertEquals(intersectionPoint, result);

        // Test intersection on different sublines
        intersectionPoint = new Vector2D(0.5, 0.5);
        Line line3 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(0, 1)).toRegion());
        result = subLine1.intersection(subLine3, true);
        assertNull(result);

        // Test intersection on endpoints
        intersectionPoint = new Vector2D(1, 1);
        result = subLine1.intersection(subLine2, false);
        assertNull(result);
    }

    @Test
    public void testSplit() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1), new Interval(2, 3)).toRegion();
        SubLine subLine = new SubLine(line, remainingRegion);
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5)).toHyperplane();

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        SubLine expectedSubLine1 = new SubLine(line, new IntervalsSet(new Interval(0, 0.5)).toRegion());
        SubLine expectedSubLine2 = new SubLine(line, new IntervalsSet(new Interval(0.5, 1)).union(new Interval(2, 3)).toRegion());

        assertEquals(expectedSubLine1.getHyperplane(), splitSubHyperplane.getHyperplane1().getHyperplane());
        assertEquals(expectedSubLine2.getHyperplane(), splitSubHyperplane.getHyperplane2().getHyperplane());
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)).toRegion());
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)).toRegion());

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5)).toHyperplane();
        Side expectedSide = Side.MINUS;
        Side side = subLine1.side(hyperplane);
        assertEquals(expectedSide, side);

        expectedSide = Side.PLUS;
        side = subLine2.side(hyperplane);
        assertEquals(expectedSide, side);

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0, 0), new Vector2D(2, 2)).toHyperplane();
        expectedSide = Side.HYPER;
        side = subLine1.side(hyperplane2);
        assertEquals(expectedSide, side);
    }

}