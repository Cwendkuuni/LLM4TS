import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubHyperplane;
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
    public void testSubLineConstruction() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertSame(hyperplane, subLine.getHyperplane());
        assertSame(remainingRegion, subLine.getRemainingRegion());

        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(2.0, 2.0);
        SubLine subLineFromPoints = new SubLine(start, end);
        assertEquals(start, subLineFromPoints.getStartEndpoint().getPoint());
        assertEquals(end, subLineFromPoints.getEndpoints().get(0).getEndPoint().getPoint());

        Segment segment = new Segment(new Vector2D(1.5, 1.5), new Vector2D(2.5, 2.5), new Line(start, end));
        SubLine subLineFromSegment = new SubLine(segment);
        assertEquals(start, subLineFromSegment.getStartEndpoint().getPoint());
        assertEquals(end, subLineFromSegment.getEndpoints().get(0).getEndPoint().getPoint());
    }

    @Test
    public void testGetSegments() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());

        Segment firstSegment = segments.get(0);
        assertEquals(new Vector2D(1.0, 1.0), firstSegment.getStart().getPoint());
        assertEquals(new Vector2D(1.0, 2.0), firstSegment.getEnd().getPoint());

        Segment secondSegment = segments.get(1);
        assertEquals(new Vector2D(2.0, 2.0), secondSegment.getStart().getPoint());
        assertEquals(new Vector2D(2.0, 3.0), secondSegment.getEnd().getPoint());
    }

    @Test
    public void testIntersection() {
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(3.0, 3.0), new Vector2D(4.0, 4.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0));
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(1.0, 1.0), new Vector2D(3.0, 3.0));
        Region<Euclidean1D> remainingRegion3 = new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0), new Interval(4.0, 5.0));
        SubLine subLine3 = new SubLine(hyperplane3, remainingRegion3);

        intersectionPoint = subLine1.intersection(subLine3, true);
        assertEquals(new Vector2D(1.5, 1.5), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine3, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subHyperplane1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));

        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(1.0, 1.0));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subHyperplane2 = new SubLine(line2, new IntervalsSet(new Interval(0.0, 1.0)));

        Side side = subHyperplane1.side(subHyperplane2.getHyperplane());
        assertEquals(Side.MINUS, side);

        side = subHyperplane2.side(subHyperplane1.getHyperplane());
        assertEquals(Side.PLUS, side);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        subHyperplane1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));

        line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        subHyperplane2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        side = subHyperplane1.side(subHyperplane2.getHyperplane());
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subHyperplane1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0)));

        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(1.0, 1.0));
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subHyperplane2 = new SubLine(line2, new IntervalsSet(new Interval(0.0, 1.0)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 0.5));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subHyperplane1.split(hyperplane);

        assertEquals(subHyperplane1, splitSubHyperplane.getSubHyperplane1());
        assertEquals(subHyperplane2, splitSubHyperplane.getSubHyperplane2());

        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(0.5, 1.5));
        splitSubHyperplane = subHyperplane1.split(hyperplane2);

        assertNull(splitSubHyperplane);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        subHyperplane1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 1.0), new Interval(2.0, 3.0)));

        line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(1.0, 1.0));
        subHyperplane2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 2.0)));

        hyperplane = new Line(new Vector2D(1.0, 0.5), new Vector2D(2.0, 0.5));
        splitSubHyperplane = subHyperplane1.split(hyperplane);

        SubHyperplane<Euclidean1D> subHyperplanePlus = splitSubHyperplane.getSubHyperplane1();
        SubHyperplane<Euclidean1D> subHyperplaneMinus = splitSubHyperplane.getSubHyperplane2();

        assertEquals(subHyperplane1, subHyperplanePlus.getHyperplane());
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subHyperplanePlusSub = ((SubLine) subHyperplanePlus.getHyperplane()).getSubHyperplanes().get(0);
        assertEquals(new IntervalsSet(new Interval(0.0, 1.0)), subHyperplanePlusSub.getRemainingRegion());

        assertEquals(subHyperplane2, subHyperplaneMinus.getHyperplane());
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subHyperplaneMinusSub = ((SubLine) subHyperplaneMinus.getHyperplane()).getSubHyperplanes().get(0);
        assertEquals(new IntervalsSet(new Interval(2.0, 3.0)), subHyperplaneMinusSub.getRemainingRegion());
    }

}