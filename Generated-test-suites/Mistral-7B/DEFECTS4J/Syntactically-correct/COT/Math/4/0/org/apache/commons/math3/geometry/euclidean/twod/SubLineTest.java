import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
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
import org.apache.commons.math3.geometry.partitioning.SubLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testSimpleConstructor() {
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0, 1));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithEndpoints() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Hyperplane<Euclidean2D> hyperplane = new Line(start, end);
        Region<Euclidean1D> remainingRegion = new IntervalsSet(start, end);
        SubLine subLine = new SubLine(start, end);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithSegment() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Line line = new Line(start, end);
        Segment segment = new Segment(start, end, line);
        Hyperplane<Euclidean2D> hyperplane = segment.getLine();
        Region<Euclidean1D> remainingRegion = new IntervalsSet(start, end);
        SubLine subLine = new SubLine(segment);
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(remainingRegion, subLine.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        IntervalsSet intervalSet = new IntervalsSet(new Interval(0, 0.5), new Interval(0.5, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(intervalSet);
        SubLine subLine = new SubLine(line, remainingRegion);
        List<Segment> segments = subLine.getSegments();
        assertEquals(2, segments.size());
        Segment seg1 = segments.get(0);
        assertEquals(new Vector2D(0, 0), seg1.getStart());
        assertEquals(new Vector2D(0.5, 0.5), seg1.getEnd());
        Segment seg2 = segments.get(1);
        assertEquals(new Vector2D(0.5, 0.5), seg2.getStart());
        assertEquals(new Vector2D(1, 1), seg2.getEnd());
    }

    @Test
    public void testIntersectionWithNullSubLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        assertNull(subLine1.intersection(null, false));
    }

    @Test
    public void testIntersectionWithSameLine() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Line line2 = new Line(line1);
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersectionWithDifferentLinesParallel() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(0, 1)));
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testIntersectionWithDifferentLinesIntersecting() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(1.5, 1.5), intersection);
    }

    @Test
    public void testIntersectionWithDifferentLinesNotIntersecting() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Line line2 = new Line(new Vector2D(1, 2), new Vector2D(2, 3));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1, 2)));
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testIntersectionWithSameLineEndpointsInclusive() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector2D(0), new Vector2D(1)));
        Line line2 = new Line(line1);
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Vector2D(0), new Vector2D(1)));
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersectionWithSameLineEndpointsExclusive() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Vector2D(0), new Vector2D(1)));
        Line line2 = new Line(line1);
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Vector2D(0), new Vector2D(1)));
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Hyperplane<Euclidean2D> hyperplane1 = line1;
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));
        SubLine subLine2 = new SubLine(hyperplane2, new IntervalsSet(new Interval(1, 2)));
        Side side1 = subLine1.side(hyperplane1);
        Side side2 = subLine1.side(hyperplane2);
        Side side3 = subLine2.side(hyperplane1);
        Side side4 = subLine2.side(hyperplane2);
        assertEquals(Side.MINUS, side1);
        assertEquals(Side.PLUS, side2);
        assertEquals(Side.MINUS, side3);
        assertEquals(Side.PLUS, side4);
    }

    @Test
    public void testSplitWithNullHyperplane() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        assertNull(subLine1.split(null));
    }

    @Test
    public void testSplitWithSameHyperplane() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Hyperplane<Euclidean2D> hyperplane = line1;
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        assertNull(split.getLeft());
        assertNull(split.getRight());
    }

    @Test
    public void testSplitWithParallelHyperplanes() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        assertNull(split.getLeft());
        assertNull(split.getRight());
    }

    @Test
    public void testSplitWithIntersectingHyperplanes() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0, 1)));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1, 1));
        Hyperplane<Euclidean2D> hyperplane = line2;
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        SubLine left = (SubLine) split.getLeft();
        SubLine right = (SubLine) split.getRight();
        assertEquals(new Vector2D(0, 0), left.getHyperplane().getSupportingVector().getVector().getComponent(0));
        assertEquals(new Vector2D(0, 0), left.getHyperplane().getSupportingVector().getVector().getComponent(1));
        assertEquals(new Vector2D(0.5, 0.5), right.getHyperplane().getSupportingVector().getVector().getComponent(0));
        assertEquals(new Vector2D(0.5, 0.5), right.getHyperplane().getSupportingVector().getVector().getComponent(1));
    }
}