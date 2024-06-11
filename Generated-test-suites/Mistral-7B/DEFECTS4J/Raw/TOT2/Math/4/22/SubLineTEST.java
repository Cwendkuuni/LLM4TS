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
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        // Test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());

        // Test constructor from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> line = new Line(start, end);
        List<Interval> intervals = new IntervalsSet(start.getX(), end.getX()).asList();
        Region<Euclidean1D> intervalsRegion = new IntervalsSet(intervals);
        SubLine subLine1 = new SubLine(start, end);
        assertEquals(line, subLine1.getHyperplane());
        assertEquals(intervalsRegion, subLine1.getRemainingRegion());

        // Test constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(start, end));
        SubLine subLine2 = new SubLine(segment);
        assertEquals(line, subLine2.getHyperplane());
        assertEquals(intervalsRegion, subLine2.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        // Test getSegments method
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(new Interval(0, 1), new Interval(1, 2), new Interval(3, 4));
        SubLine subLine = new SubLine(line, region);
        List<Segment> segments = subLine.getSegments();
        assertEquals(3, segments.size());
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(1, 1), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(1, 1), new Vector2D(2, 2), line), segments.get(1));
        assertEquals(new Segment(new Vector2D(3, 3), new Vector2D(4, 4), line), segments.get(2));
    }

    @Test
    public void testIntersection() {
        // Test intersection method
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(1, 1), intersectionPoint);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2, 3)));
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(-1, 0)));
        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);
    }

    @Test
    public void testBuildIntervalSet() {
        // Test buildIntervalSet static method
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);
        List<Interval> intervals = intervalsSet.asList();
        assertEquals(2, intervals.size());
        assertEquals(new Interval(0, 1), intervals.get(0));
        assertEquals(new Interval(1, 1), intervals.get(1));
    }

    @Test
    public void testSide() {
        // Test side method
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(1, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Side side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.MINUS, side);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(1, 2));
        SubLine subLine3 = new SubLine(line1, new IntervalsSet(new Interval(1, 2)));
        side = subLine1.side(subLine3.getHyperplane());
        assertEquals(Side.PLUS, side);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line3 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine4 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        SubLine subLine5 = new SubLine(line3, new IntervalsSet(new Interval(2, 3)));
        side = subLine4.side(subLine5.getHyperplane());
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        // Test split method
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(1, 2));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane);
        SubLine subLine2 = (SubLine) splitSubHyperplane.getSubHyperplaneA();
        SubLine subLine3 = (SubLine) splitSubHyperplane.getSubHyperplaneB();
        assertEquals(line1, subLine1.getHyperplane());
        assertEquals(line2, subLine2.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(0, 1)), subLine1.getRemainingRegion());
        assertEquals(new IntervalsSet(new Interval(1, 2)), subLine2.getRemainingRegion());
        assertEquals(new IntervalsSet(new Interval(1, 2)), subLine3.getRemainingRegion());
    }

}