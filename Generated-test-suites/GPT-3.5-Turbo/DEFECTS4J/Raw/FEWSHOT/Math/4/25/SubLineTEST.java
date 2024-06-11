import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubLineTest {

    private static final double EPSILON = 1e-10;

    private static SubLine subLine;
    private static SubLine subLineInside;
    private static SubLine subLineOutside;

    @BeforeClass
    public static void setUp() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        subLine = new SubLine(hyperplane, new IntervalsSet(-10, 10));

        Hyperplane<Euclidean2D> hyperplaneInside = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        subLineInside = new SubLine(hyperplaneInside, new IntervalsSet(-5, 5));

        Hyperplane<Euclidean2D> hyperplaneOutside = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        subLineOutside = new SubLine(hyperplaneOutside, new IntervalsSet(11, 20));
    }

    @Test
    public void testGetHyperplane() {
        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        assertTrue(hyperplane instanceof Line);
        Line line = (Line) hyperplane;
        assertEquals(0, line.getY(0), EPSILON);
        assertEquals(1, line.getY(1), EPSILON);
    }

    @Test
    public void testGetRemainingRegion() {
        Region<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        assertTrue(remainingRegion instanceof IntervalsSet);
        IntervalsSet intervalsSet = (IntervalsSet) remainingRegion;
        assertEquals(-10, intervalsSet.asList().get(0).getInf(), EPSILON);
        assertEquals(10, intervalsSet.asList().get(0).getSup(), EPSILON);
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }

    @Test
    public void testIntersectionWithIncludeEndPoints() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(-10, 10));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(1, 0), new Vector2D(2, 1)), new IntervalsSet(-10, 10));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);

        subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(-10, 1));
        subLine2 = new SubLine(new Line(new Vector2D(1, 0), new Vector2D(2, 1)), new IntervalsSet(-10, 10));

        intersectionPoint = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);
    }

    @Test
    public void testIntersectionWithoutIncludeEndPoints() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(-10, 10));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(1, 0), new Vector2D(2, 1)), new IntervalsSet(-10, 10));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);

        subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(-10, 1));
        subLine2 = new SubLine(new Line(new Vector2D(1, 0), new Vector2D(2, 1)), new IntervalsSet(-10, 10));

        intersectionPoint = subLine1.intersection(subLine2, false);
        assertNull(intersectionPoint);
    }

    @Test
    public void testBuildNew() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(-10, 10);
        SubLine newSubLine = subLine.buildNew(hyperplane, remainingRegion);
        assertEquals(hyperplane, newSubLine.getHyperplane());
        assertEquals(remainingRegion, newSubLine.getRemainingRegion());
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, -1), new Vector2D(2, 0));

        Side side = subLine.side(line1);
        assertEquals(Side.HYPER, side);

        side = subLine.side(line2);
        assertEquals(Side.MINUS, side);
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, -1), new Vector2D(2, 0));

        SplitSubHyperplane<Euclidean2D> split = subLine.split(line1);
        assertNull(split.getPlus());
        assertEquals(subLine, split.getMinus());

        split = subLine.split(line2);
        assertNull(split.getMinus());
        assertEquals(subLine, split.getPlus());
    }
}