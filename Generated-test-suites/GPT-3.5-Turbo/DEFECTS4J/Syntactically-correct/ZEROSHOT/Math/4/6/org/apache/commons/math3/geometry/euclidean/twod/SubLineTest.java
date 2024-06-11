import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;

public class SubLineTest {

    private SubLine subLine;
    private Vector2D start;
    private Vector2D end;
    private Segment segment;

    @Before
    public void setUp() {
        start = new Vector2D(0, 0);
        end = new Vector2D(1, 1);
        subLine = new SubLine(start, end);
        segment = new Segment(start, end);
    }

    @Test
    public void testConstructor() {
        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        Region<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        assertEquals(subLine.getClass(), SubLine.class);
        assertEquals(hyperplane.getClass(), Line.class);
        assertEquals(remainingRegion.getClass(), IntervalsSet.class);
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(segments.size(), 1);
        Segment segment = segments.get(0);
        assertEquals(segment.getStart(), start);
        assertEquals(segment.getEnd(), end);
    }

    @Test
    public void testIntersectionWithIncludeEndPointsTrue() {
        SubLine subLine2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        Vector2D intersection = subLine.intersection(subLine2, true);
        assertEquals(intersection, new Vector2D(0.5, 0.5));
    }

    @Test
    public void testIntersectionWithIncludeEndPointsFalse() {
        SubLine subLine2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        Vector2D intersection = subLine.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        List<Interval> intervals = intervalSet.asList();
        assertEquals(intervals.size(), 1);
        Interval interval = intervals.get(0);
        assertEquals(interval.getInf(), 0.0, 0.0);
        assertEquals(interval.getSup(), 1.0, 0.0);
    }

    @Test
    public void testBuildNew() {
        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        Region<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        AbstractSubHyperplane<Euclidean2D, Euclidean1D> newSubLine = subLine.buildNew(hyperplane, remainingRegion);
        assertEquals(newSubLine.getClass(), SubLine.class);
        assertEquals(newSubLine.getHyperplane().getClass(), Line.class);
        assertEquals(newSubLine.getRemainingRegion().getClass(), IntervalsSet.class);
    }

    @Test
    public void testSide() {
        Side side = subLine.side(segment);
        assertEquals(side, Side.HYPER);
    }

    @Test
    public void testSplit() {
        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);
        assertEquals(splitSubHyperplane.getPlus().getClass(), SubLine.class);
        assertEquals(splitSubHyperplane.getMinus().getClass(), SubLine.class);
    }

}