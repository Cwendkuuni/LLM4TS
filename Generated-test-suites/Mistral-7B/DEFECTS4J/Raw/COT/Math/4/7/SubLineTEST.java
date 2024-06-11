import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.twod.Region;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.SplitSubHyperplane;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRemainingRegion() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0)).toHyperplane();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNotNull(subLine);
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        SubLine subLine = new SubLine(start, end);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);
        assertEquals(start, subLine.getStart());
        assertEquals(end, subLine.getEnd());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        IntervalsSet remainingRegion = new IntervalsSet(new Interval(1.0, 3.0), new Interval(4.0, 5.0));
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        assertEquals(new Segment(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0), line), segments.get(0));
        assertEquals(new Segment(new Vector2D(4.0, 4.0), new Vector2D(5.0, 5.0), line), segments.get(1));
    }

    @Test
    public void testIntersectionWithSameSubLine() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine1 = new SubLine(line, remainingRegion);
        assertEquals(null, subLine1.intersection(subLine1, false));
    }

    @Test
    public void testIntersectionWithDifferentSubLinesOnSameLine() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNotNull(intersectionPoint);
        assertEquals(new Vector2D(1.0, 1.0), intersectionPoint);
    }

    @Test
    public void testIntersectionWithDifferentSubLinesWithoutIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(2.0, 3.0));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testIntersectionWithDifferentSubLinesButOneIsEmpty() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet();
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.0, 2.0));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testIntersectionWithEndPointsIncluded() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine1 = new SubLine(line1, remainingRegion1);

        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.0, 3.0));
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNotNull(intersectionPoint);
        assertEquals(new Vector2D(1.0, 1.0), intersectionPoint);
    }

    @Test
    public void testSideWithSameHyperplane() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        assertEquals(Side.HYPER, subLine.side(line1));
    }

    @Test
    public void testSideWithPerpendicularHyperplane() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        assertEquals(Side.MINUS, subLine.side(line2));
    }

    @Test
    public void testSideWithParallelHyperplane() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Line line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));
        assertEquals(Side.HYPER, subLine.side(line2));
    }

    @Test
    public void testSplitWithSameHyperplane() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(line1, remainingRegion);

        Hyperplane<Euclidean2D> hyperplane = line1.toHyperplane();
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        assertNotNull(splitSubHyperplane);
        assertEquals(SubLine.class, splitSubHyperplane.getSubHyperplane1().getClass());
        assertEquals(SubLine.class, splitSubHyperplane.getSubHyperplane2().getClass());
        assertEquals(null, splitSubHyperplane.getSubHyperplane1().getHyperplane());
        assertEquals(null, splitSubHyperplane.getSubHyperplane2().getHyperplane());
    }

    @Test
    public void testSplitWithPerpendicularHyperplane() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(line1, remainingRegion);

        Line line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(2.0, 1.0));
        Hyperplane<Euclidean2D> hyperplane = line2.toHyperplane();
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        assertNotNull(splitSubHyperplane);
        assertEquals(SubLine.class, splitSubHyperplane.getSubHyperplane1().getClass());
        assertEquals(SubLine.class, splitSubHyperplane.getSubHyperplane2().getClass());
        assertEquals(line2, splitSubHyperplane.getSubHyperplane1().getHyperplane());
        assertEquals(null, splitSubHyperplane.getSubHyperplane2().getHyperplane());
    }

    @Test
    public void testSplitWithParallelHyperplane() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(line1, remainingRegion);

        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0));
        Hyperplane<Euclidean2D> hyperplane = line2.toHyperplane();
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        assertNotNull(splitSubHyperplane);
        assertEquals(SubLine.class, splitSubHyperplane.getSubHyperplane1().getClass());
        assertEquals(SubLine.class, splitSubHyperplane.getSubHyperplane2().getClass());
        assertEquals(null, splitSubHyperplane.getSubHyperplane1().getHyperplane());
        assertEquals(null, splitSubHyperplane.getSubHyperplane2().getHyperplane());
    }

}