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
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);

        Line line = subLine.getHyperplane();
        assertArrayEquals(new double[] {-2.0, -1.0, 1.0, 2.0}, line.getParameters(), 0.001);

        IntervalsSet expectedIntervalSet = new IntervalsSet(new Interval(0.0, 1.5), new Interval(1.8, Double.POSITIVE_INFINITY));
        assertArrayEquals(expectedIntervalSet.toArray(), ((IntervalsSet) subLine.getRemainingRegion()).asList().toArray(), 0.001);

        List<Segment> segments = subLine.getSegments();
        assertArrayEquals(new Segment[] {new Segment(new Vector2D(1.0, 2.0), new Vector2D(1.5, 2.5), line),
                                          new Segment(new Vector2D(1.8, 2.8), new Vector2D(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), line)},
                segments.toArray(), 0.001);
    }

    @Test
    public void testSubLineIntersection() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(4.0, 5.0);
        Vector2D end2   = new Vector2D(6.0, 7.0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        start2 = new Vector2D(2.5, 3.5);
        end2   = new Vector2D(5.5, 6.5);

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertArrayEquals(new double[] {2.0, 2.5}, intersectionPoint.toArray(), 0.001);

        start1 = new Vector2D(-1.0, -2.0);
        end1   = new Vector2D(1.0, 2.0);

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertNull(intersectionPoint);

        start2 = new Vector2D(4.0, 5.0);
        end2   = new Vector2D(6.0, 7.0);

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        start1 = new Vector2D(1.0, 2.0);
        end1   = new Vector2D(3.0, 4.0);

        start2 = new Vector2D(2.0, 2.5);
        end2   = new Vector2D(4.0, 3.5);

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertArrayEquals(new double[] {2.0, 2.5}, intersectionPoint.toArray(), 0.001);
    }

    @Test
    public void testSubLineSide() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);

        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(start, end);

        Side expectedSide = Side.MINUS;
        Side side = line1.side(line2);
        assertArrayEquals(expectedSide.ordinal(), side.ordinal());

        Line line3 = new Line(new Vector2D(3.0, 4.0), new Vector2D(4.0, 5.0));
        side = line2.side(line3);
        assertArrayEquals(Side.PLUS.ordinal(), side.ordinal());

        Line line4 = new Line(new Vector2D(2.0, 2.5), new Vector2D(4.0, 3.5));
        side = line2.side(line4);
        assertArrayEquals(Side.HYPER.ordinal(), side.ordinal());

        Line line5 = new Line(new Vector2D(3.0, 4.0), new Vector2D(5.0, 6.0));
        side = line2.side(line5);
        assertArrayEquals(Side.MINUS.ordinal(), side.ordinal());

        Line line6 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        side = line2.side(line6);
        assertArrayEquals(Side.HYPER.ordinal(), side.ordinal());
    }

    @Test
    public void testSubLineSplit() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);

        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(start, end);

        Hyperplane<Euclidean2D> splittingPlane = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));

        SubHyperplane<Euclidean1D> expectedSubPlus = new OrientedPoint(new Vector1D(-1.0), false).wholeHyperplane();
        SubHyperplane<Euclidean1D> expectedSubMinus = new OrientedPoint(new Vector1D(1.5), true).wholeHyperplane();

        AbstractSubHyperplane<Euclidean2D, Euclidean1D> subLine = new SubLine(line1, line2.getRemainingRegion());
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(splittingPlane);

        assertArrayEquals(expectedSubPlus.getHyperplane().getClass(), splitSubHyperplane.getSubHyperplane1().getHyperplane().getClass());
        assertArrayEquals(expectedSubMinus.getHyperplane().getClass(), splitSubHyperplane.getSubHyperplane2().getHyperplane().getClass());
    }

}