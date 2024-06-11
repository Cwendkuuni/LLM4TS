import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link SubLine} class
 */
public class SubLineTest {

    /**
     * Test constructor with valid arguments
     */
    @Test
    public void testConstructionWithValidArguments() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        SubLine subLine = new SubLine(start, end);
        Line line = subLine.getHyperplane();
        List<Segment> segments = subLine.getSegments();
        Line expectedLine = new Line(start, end);
        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(new Segment(start, end, expectedLine));
        Assert.assertEquals(expectedLine, line);
        Assert.assertEquals(expectedSegments, segments);
    }

    /**
     * Test constructor with invalid arguments (null vectors)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithInvalidArguments() {
        Vector2D start = null;
        Vector2D end   = new Vector2D(1.0, 2.0);
        new SubLine(start, end);
    }

    /**
     * Test constructor with invalid arguments (empty interval set)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithEmptyIntervalSet() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervalSet = new IntervalsSet();
        new SubLine(line, intervalSet);
    }

    /**
     * Test getSegments method with valid arguments
     */
    @Test
    public void testGetSegmentsWithValidArguments() {
        Vector2D start = new Vector2D(1.0, 2.0);
        Vector2D end   = new Vector2D(3.0, 4.0);
        Line line = new Line(start, end);
        IntervalsSet intervalSet = buildIntervalSet(start, end);
        SubLine subLine = new SubLine(line, intervalSet);
        List<Segment> expectedSegments = new ArrayList<>();
        expectedSegments.add(new Segment(start, end, line));
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(expectedSegments, segments);
    }

    /**
     * Test getSegments method with empty interval set
     */
    @Test
    public void testGetSegmentsWithEmptyIntervalSet() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervalSet = new IntervalsSet();
        SubLine subLine = new SubLine(line, intervalSet);
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(0, segments.size());
    }

    /**
     * Test intersection method with valid arguments
     */
    @Test
    public void testIntersectionWithValidArguments() {
        Vector2D start1 = new Vector2D(1.0, 2.0);
        Vector2D end1   = new Vector2D(3.0, 4.0);
        Vector2D start2 = new Vector2D(5.0, 6.0);
        Vector2D end2   = new Vector2D(7.0, 8.0);
        Line line1 = new Line(start1, end1);
        Line line2 = new Line(start2, end2);
        SubLine subLine1 = new SubLine(line1, buildIntervalSet(start1, end1));
        SubLine subLine2 = new SubLine(line2, buildIntervalSet(start2, end2));
        Vector2D expectedIntersection = line1.intersection(line2);
        boolean includeEndPoints = true;
        Vector2D intersection = subLine1.intersection(subLine2, includeEndPoints);
        Assert.assertEquals(expectedIntersection, intersection);
    }

    /**
     * Test intersection method with invalid arguments (null sub-lines)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithInvalidArgumentsNullSubLine1() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine2 = new SubLine(line1, buildIntervalSet(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0)));
        SubLine subLine1 = null;
        Vector2D intersection = subLine1.intersection(subLine2, false);
    }

    /**
     * Test intersection method with invalid arguments (null sub-lines 2)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIntersectionWithInvalidArgumentsNullSubLine2() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, buildIntervalSet(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0)));
        SubLine subLine2 = null;
        Vector2D intersection = subLine1.intersection(subLine2, false);
    }

    /**
     * Test intersection method with empty sub-lines
     */
    @Test
    public void testIntersectionWithEmptySubLines() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet());
        Vector2D intersection = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersection);
    }

    /**
     * Test side method with valid arguments
     */
    @Test
    public void testSideWithValidArguments() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end   = new Vector2D(1.0, 1.0);
        Line line1 = new Line(start, end);
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, buildIntervalSet(start, end));
        SubLine subLine2 = new SubLine(line2, buildIntervalSet(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5)));
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(line2);
        Side expectedSide = subLine1.side(hyperplane);
        Assert.assertEquals(Side.MINUS, expectedSide);
    }

    /**
     * Test side method with parallel lines
     */
    @Test
    public void testSideWithParallelLines() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end   = new Vector2D(1.0, 1.0);
        Line line1 = new Line(start, end);
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1, buildIntervalSet(start, end));
        SubLine subLine2 = new SubLine(line2, buildIntervalSet(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0)));
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(line2);
        Side expectedSide = Side.HYPER;
        Assert.assertEquals(expectedSide, subLine1.side(hyperplane));
    }

    /**
     * Test split method with valid arguments
     */
    @Test
    public void testSplitWithValidArguments() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end   = new Vector2D(1.0, 1.0);
        Line line1 = new Line(start, end);
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, buildIntervalSet(start, end));
        SubLine subLine2 = new SubLine(line2, buildIntervalSet(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5)));
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(line2);
        SplitSubHyperplane<Euclidean2D> expectedSplitSubHyperplane = new SplitSubHyperplane<Euclidean2D>(null, subLine1);
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane);
        Assert.assertEquals(expectedSplitSubHyperplane, splitSubHyperplane);
    }

    /**
     * Test split method with invalid arguments (null hyperplane)
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSplitWithInvalidArgumentsNullHyperplane() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine1 = new SubLine(line1, buildIntervalSet(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0)));
        Hyperplane<Euclidean2D> hyperplane = null;
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane);
    }

    /**
     * Test split method with empty interval set
     */
    @Test
    public void testSplitWithEmptyIntervalSet() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet());
        Hyperplane<Euclidean2D> hyperplane = new Hyperplane<Euclidean2D>(line2);
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine1.split(hyperplane);
        Assert.assertNull(splitSubHyperplane);
    }

    /**
     * Helper method to build interval set from two points
     */
    private IntervalsSet buildIntervalSet(Vector2D start, Vector2D end) {
        Line line = new Line(start, end);
        return new IntervalsSet(line.toSubSpace(start).getX(),
                                line.toSubSpace(end).getX());
    }

}