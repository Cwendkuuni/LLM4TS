import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();

        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(start, segments.get(0).getStart());
        Assert.assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersectionIncludeEndPoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);

        Assert.assertEquals(new Vector2D(1, 1), intersection);
    }

    @Test
    public void testIntersectionExcludeEndPoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, false);

        Assert.assertEquals(null, intersection);
    }

    @Test
    public void testSideParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(3, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        SubHyperplane.SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2);

        Assert.assertEquals(null, split.getPlus());
        Assert.assertEquals(subLine1, split.getMinus());
    }

    @Test
    public void testSideIntersectingLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        SubHyperplane.SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2);

        Assert.assertEquals(subLine2, split.getPlus());
        Assert.assertEquals(subLine1, split.getMinus());
    }

    @Test
    public void testBuildNew() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        Region<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        SubLine newSubLine = subLine.buildNew(hyperplane, remainingRegion);

        Assert.assertEquals(subLine.getHyperplane(), newSubLine.getHyperplane());
        Assert.assertEquals(subLine.getRemainingRegion(), newSubLine.getRemainingRegion());
    }

    @Test
    public void testSplitParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(3, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        SubHyperplane.SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2);

        Assert.assertEquals(null, split.getPlus());
        Assert.assertEquals(subLine1, split.getMinus());
    }

    @Test
    public void testSplitIntersectingLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 0);
        Vector2D end2 = new Vector2D(2, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        SubHyperplane.SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2);

        Assert.assertEquals(subLine2, split.getPlus());
        Assert.assertEquals(subLine1, split.getMinus());
    }
}

