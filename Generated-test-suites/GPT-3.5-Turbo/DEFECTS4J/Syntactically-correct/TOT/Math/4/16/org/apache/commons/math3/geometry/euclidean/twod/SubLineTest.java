import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    // Create a sub-line from two endpoints
    @Test
    public void testCreateSubLineFromEndpoints() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);

        SubLine subLine = new SubLine(start, end);

        Assert.assertEquals(start, subLine.getSegments().get(0).getStart());
        Assert.assertEquals(end, subLine.getSegments().get(0).getEnd());
    }

    // Create a sub-line from a segment
    @Test
    public void testCreateSubLineFromSegment() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);

        Segment segment = new Segment(start, end);
        SubLine subLine = new SubLine(segment);

        Assert.assertEquals(start, subLine.getSegments().get(0).getStart());
        Assert.assertEquals(end, subLine.getSegments().get(0).getEnd());
    }

    // Test getSegments() method for an empty sub-line
    @Test
    public void testGetSegmentsEmptySubLine() {
        Vector2D start = new Vector2D(1.0, 1.0);
        Vector2D end = new Vector2D(0.0, 0.0);

        SubLine subLine = new SubLine(start, end);

        Assert.assertEquals(0, subLine.getSegments().size());
    }

    // Test getIntersection method with includeEndPoints set to true
    @Test
    public void testIntersectionWithIncludeEndPoints() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 1.0);

        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2 = new Vector2D(3.0, 3.0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        Assert.assertNull(intersectionPoint);
    }

    // Test getIntersection method with includeEndPoints set to false
    @Test
    public void testIntersectionWithoutIncludeEndPoints() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 1.0);

        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2 = new Vector2D(3.0, 3.0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, false);

        Assert.assertNull(intersectionPoint);
    }

    // Test intersection with one sub-line being fully inside the other sub-line
    @Test
    public void testIntersectionWithOneSubLineInsideAnother() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(3.0, 3.0);

        Vector2D start2 = new Vector2D(1.0, 1.0);
        Vector2D end2 = new Vector2D(2.0, 2.0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);

        Assert.assertNull(intersectionPoint);
    }

    // Test side method with sub-lines that are parallel
    @Test
    public void testSideWithParallelSubLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 1.0);

        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2 = new Vector2D(3.0, 3.0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Region.Location side = subLine1.side(subLine2);

        Assert.assertEquals(Region.Location.HYPER, side);
    }

    // Test side method with sub-lines that intersect at a point
    @Test
    public void testSideWithIntersectingSubLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 1.0);

        Vector2D start2 = new Vector2D(1.0, 0.0);
        Vector2D end2 = new Vector2D(0.0, 1.0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Region.Location side = subLine1.side(subLine2);

        Assert.assertEquals(Region.Location.OUTSIDE, side);
    }

    // Test split method with parallel sub-lines
    @Test
    public void testSplitWithParallelSubLines() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);

        SubLine subLine = new SubLine(start, end);

        Vector2D start2 = new Vector2D(2.0, 2.0);
        Vector2D end2 = new Vector2D(3.0, 3.0);

        SubLine subLine2 = new SubLine(start2, end2);

        SubLine.SplitSubHyperplane split = subLine.split(subLine2);

        Assert.assertNull(split.getPlus());
        Assert.assertEquals(subLine, split.getMinus());
    }

    // Test split method with intersecting sub-lines
    @Test
    public void testSplitWithIntersectingSubLines() {
        Vector2D start1 = new Vector2D(0.0, 0.0);
        Vector2D end1 = new Vector2D(1.0, 1.0);

        Vector2D start2 = new Vector2D(1.0, 0.0);
        Vector2D end2 = new Vector2D(0.0, 1.0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        SubLine.SplitSubHyperplane split = subLine1.split(subLine2);

        Assert.assertNull(split.getPlus());
        Assert.assertEquals(subLine1, split.getMinus());
    }

}