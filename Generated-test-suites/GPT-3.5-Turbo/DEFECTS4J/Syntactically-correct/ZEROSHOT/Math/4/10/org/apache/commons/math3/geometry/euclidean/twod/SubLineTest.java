import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.util.FastMath;

public class SubLineTest {

    private SubLine subLine;
    private Line line;
    private Region<Euclidean1D> remainingRegion;
    private Vector2D start;
    private Vector2D end;
    private Segment segment;

    @Before
    public void setUp() {
        start = new Vector2D(1.0, 2.0);
        end = new Vector2D(3.0, 4.0);
        line = new Line(start, end);
        remainingRegion = new IntervalsSet(line.toSubSpace(start).getX(),
                                           line.toSubSpace(end).getX());
        subLine = new SubLine(line, remainingRegion);
        segment = new Segment(start, end, line);
    }

    @Test
    public void testConstructor() {
        assertEquals(line, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
        assertEquals(line, segment.getLine());
    }

    @Test
    public void testIntersection() {
        // Create another sub-line
        Vector2D start2 = new Vector2D(-1.0, -2.0);
        Vector2D end2 = new Vector2D(1.0, 2.0);
        Line line2 = new Line(start2, end2);
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(line2.toSubSpace(start2).getX(),
                                                               line2.toSubSpace(end2).getX());
        SubLine subLine2 = new SubLine(line2, remainingRegion2);

        // Test intersection with included endpoints
        Vector2D intersection = subLine.intersection(subLine2, true);
        assertNotNull(intersection);
        assertEquals(0.0, intersection.getX(), 1e-10);
        assertEquals(0.0, intersection.getY(), 1e-10);

        // Test intersection without included endpoints
        intersection = subLine.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);
        assertEquals(1, intervalsSet.asList().size());
        Interval interval = intervalsSet.asList().get(0);
        assertEquals(line.toSubSpace(start).getX(), interval.getInf(), 1e-10);
        assertEquals(line.toSubSpace(end).getX(), interval.getSup(), 1e-10);
    }

    @Test
    public void testBuildNew() {
        // Create another hyperplane and region
        Line line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(2.0, 3.0));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(0.0, 1.0);
        // Build new SubLine
        SubLine subLine2 = (SubLine) subLine.buildNew(line2, remainingRegion2);
        // Check the hyperplane and region
        assertEquals(line2, subLine2.getHyperplane());
        assertEquals(remainingRegion2, subLine2.getRemainingRegion());
    }

    @Test
    public void testSide() {
        // Create another hyperplane
        Line line2 = new Line(new Vector2D(1.0, 2.0), new Vector2D(3.0, 4.0));
        // Test side with parallel lines
        Side side = subLine.side(line2);
        double global = line2.getOffset(line);
        assertEquals(Side.HYPER, side);
        // Test side with intersecting lines
        line2 = new Line(new Vector2D(2.0, 3.0), new Vector2D(4.0, 5.0));
        side = subLine.side(line2);
        assertEquals(Side.MINUS, side);
        // Create another sub-line
        Vector2D start3 = new Vector2D(5.0, 6.0);
        Vector2D end3 = new Vector2D(7.0, 8.0);
        Line line3 = new Line(start3, end3);
        Region<Euclidean1D> remainingRegion3 = new IntervalsSet(line3.toSubSpace(start3).getX(),
                                                               line3.toSubSpace(end3).getX());
        SubLine subLine3 = new SubLine(line3, remainingRegion3);
        // Test side with intersecting sub-lines
        side = subLine.side(subLine3);
        assertEquals(Side.HYPER, side);
    }

    @Test
    public void testSplit() {
        // Create another sub-line
        Vector2D start3 = new Vector2D(5.0, 6.0);
        Vector2D end3 = new Vector2D(7.0, 8.0);
        Line line3 = new Line(start3, end3);
        Region<Euclidean1D> remainingRegion3 = new IntervalsSet(line3.toSubSpace(start3).getX(),
                                                               line3.toSubSpace(end3).getX());
        SubLine subLine3 = new SubLine(line3, remainingRegion3);
        // Test split with parallel sub-lines
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(subLine3);
        assertNotNull(splitSubHyperplane);
        assertNull(splitSubHyperplane.getPlus());
        assertNull(splitSubHyperplane.getMinus());
        // Create another sub-line
        Vector2D start4 = new Vector2D(2.0, 3.0);
        Vector2D end4 = new Vector2D(4.0, 5.0);
        Line line4 = new Line(start4, end4);
        Region<Euclidean1D> remainingRegion4 = new IntervalsSet(line4.toSubSpace(start4).getX(),
                                                               line4.toSubSpace(end4).getX());
        SubLine subLine4 = new SubLine(line4, remainingRegion4);
        // Test split with intersecting sub-lines
        splitSubHyperplane = subLine.split(subLine4);
        assertNotNull(splitSubHyperplane);
        assertNotNull(splitSubHyperplane.getPlus());
        assertNotNull(splitSubHyperplane.getMinus());
    }
}