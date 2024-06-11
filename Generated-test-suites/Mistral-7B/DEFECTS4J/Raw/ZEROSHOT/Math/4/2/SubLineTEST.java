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
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineFromTwoEndpoints() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.5));
        Vector2D start = new Vector2D(0.2, 0.3);
        Vector2D end = new Vector2D(0.6, 0.7);
        List<Segment> expectedSegments = new ArrayList<>();
        Segment segment = new Segment(start, end, (Line) hyperplane);
        expectedSegments.add(segment);
        SubLine subLine = new SubLine(start, end);
        List<Segment> actualSegments = subLine.getSegments();
        assertEquals(expectedSegments, actualSegments);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testSubLineFromSegment() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.5));
        Vector2D start = new Vector2D(0.2, 0.3);
        Vector2D end = new Vector2D(0.6, 0.7);
        Segment segment = new Segment(start, end, (Line) hyperplane);
        SubLine subLine = new SubLine(segment);
        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(segment);
        List<Segment> actualSegments = subLine.getSegments();
        assertEquals(expectedSegments, actualSegments);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.5), new Interval(0.8, 1.0));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        List<Segment> expectedSegments = new ArrayList<>();
        Vector2D start1 = new Vector2D(0.15, 0.2);
        Vector2D end1 = new Vector2D(0.25, 0.3);
        Segment segment1 = new Segment(start1, end1, (Line) hyperplane);
        Vector2D start2 = new Vector2D(0.9, 0.95);
        Vector2D end2 = new Vector2D(1.0, 1.0);
        Segment segment2 = new Segment(start2, end2, (Line) hyperplane);
        expectedSegments.add(segment1);
        expectedSegments.add(segment2);
        List<Segment> actualSegments = subLine.getSegments();
        assertEquals(expectedSegments, actualSegments);
    }

    @Test
    public void testIntersection() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.1, 0.5));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.2, 0.8));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        Vector2D expectedIntersection = new Vector2D(0.25, 0.6);
        Vector2D actualIntersection = subLine1.intersection(subLine2, true);
        assertEquals(expectedIntersection, actualIntersection);
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullSubLine() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNull(subLine.intersection(null, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithNullIncludeEndPoints() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        Region<Euclidean1D> remainingRegion1 = new IntervalsSet(new Interval(0.1, 0.5));
        Region<Euclidean1D> remainingRegion2 = new IntervalsSet(new Interval(0.2, 0.8));
        SubLine subLine1 = new SubLine(hyperplane1, remainingRegion1);
        SubLine subLine2 = new SubLine(hyperplane2, remainingRegion2);
        assertNull(subLine1.intersection(subLine2, null));
    }

    @Test
    public void testSplit() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        Hyperplane<Euclidean2D> hyperplaneToSplit = new Line(new Vector2D(0.3, 0.3), new Vector2D(0.7, 0.7));
        SubHyperplane<Euclidean1D> expectedPlus = new SubLine(new Line(new Vector2D(0.1, 0.1), new Vector2D(0.3, 0.3)), new IntervalsSet(new Interval(0.1, 0.3)));
        SubHyperplane<Euclidean1D> expectedMinus = new SubLine(new Line(new Vector2D(0.7, 0.7), new Vector2D(1.0, 1.0)), new IntervalsSet(new Interval(0.5, 0.7)));
        SplitSubHyperplane<Euclidean2D> actualSplit = subLine.split(hyperplaneToSplit);
        SubHyperplane<Euclidean1D> actualPlus = actualSplit.getSubHyperplane1();
        SubHyperplane<Euclidean1D> actualMinus = actualSplit.getSubHyperplane2();
        assertEquals(expectedPlus.getHyperplane(), actualPlus.getHyperplane());
        assertEquals(expectedMinus.getHyperplane(), actualMinus.getHyperplane());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitWithNullSubLineToSplit() {
        Euclidean2DSpace euclideanSpace = new Euclidean2DSpace();
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.1, 0.5));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertNull(subLine.split(null));
    }

}