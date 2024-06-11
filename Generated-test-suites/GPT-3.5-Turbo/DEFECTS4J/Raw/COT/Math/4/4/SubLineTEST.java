import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Create a sub-line with two segments
        SubLine subLine = new SubLine(new Segment(new Vector2D(0, 0), new Vector2D(1, 1)),
                new Segment(new Vector2D(2, 2), new Vector2D(3, 3)));

        // Get the segments
        List<Segment> segments = subLine.getSegments();

        // Check the number of segments
        Assert.assertEquals(2, segments.size());

        // Check the start and end points of the segments
        Assert.assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        Assert.assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
        Assert.assertEquals(new Vector2D(2, 2), segments.get(1).getStart());
        Assert.assertEquals(new Vector2D(3, 3), segments.get(1).getEnd());
    }

    @Test
    public void testIntersection() {
        // Create two sub-lines
        SubLine subLine1 = new SubLine(new Segment(new Vector2D(0, 0), new Vector2D(2, 2)));
        SubLine subLine2 = new SubLine(new Segment(new Vector2D(1, 0), new Vector2D(1, 2)));

        // Compute the intersection with end points included
        Vector2D intersection1 = subLine1.intersection(subLine2, true);
        // The intersection point should be (1, 1)
        Assert.assertEquals(new Vector2D(1, 1), intersection1);

        // Compute the intersection with end points excluded
        Vector2D intersection2 = subLine1.intersection(subLine2, false);
        // The intersection point should be (1, 1)
        Assert.assertEquals(new Vector2D(1, 1), intersection2);

        // Create two parallel sub-lines
        SubLine subLine3 = new SubLine(new Segment(new Vector2D(0, 0), new Vector2D(2, 2)));
        SubLine subLine4 = new SubLine(new Segment(new Vector2D(0, 1), new Vector2D(2, 3)));

        // Compute the intersection with end points included
        Vector2D intersection3 = subLine3.intersection(subLine4, true);
        // The intersection point should be null
        Assert.assertNull(intersection3);

        // Compute the intersection with end points excluded
        Vector2D intersection4 = subLine3.intersection(subLine4, false);
        // The intersection point should be null
        Assert.assertNull(intersection4);
    }

    @Test
    public void testBuildIntervalSet() {
        // Create an interval set from two points
        IntervalsSet intervalSet = SubLine.buildIntervalSet(new Vector2D(0, 0), new Vector2D(2, 2));

        // Get the intervals from the set
        List<Interval> intervals = intervalSet.asList();

        // Check the number of intervals
        Assert.assertEquals(1, intervals.size());

        // Check the start and end points of the interval
        Assert.assertEquals(0.0, intervals.get(0).getInf(), 0.0);
        Assert.assertEquals(2.0, intervals.get(0).getSup(), 0.0);
    }

    @Test
    public void testSide() {
        // Create two sub-lines
        SubLine subLine1 = new SubLine(new Segment(new Vector2D(0, 0), new Vector2D(2, 2)));
        SubLine subLine2 = new SubLine(new Segment(new Vector2D(1, 0), new Vector2D(1, 2)));

        // Check the side of subLine2 with respect to subLine1
        Assert.assertEquals(Region.Location.INSIDE, subLine2.side(subLine1.getHyperplane()));
    }

    @Test
    public void testSplit() {
        // Create two sub-lines
        SubLine subLine1 = new SubLine(new Segment(new Vector2D(0, 0), new Vector2D(2, 2)));
        SubLine subLine2 = new SubLine(new Segment(new Vector2D(1, 0), new Vector2D(1, 2)));

        // Split subLine1 with respect to subLine2
        SubLine.SplitSubHyperplane<Euclidean2D> splitSubLine = subLine1.split(subLine2.getHyperplane());

        // Check the positive side of the split sub-line
        SubLine positive = (SubLine) splitSubLine.getPlus();
        Assert.assertEquals(Region.Location.OUTSIDE, positive.getRemainingRegion().checkPoint(new Vector1D(0.5)));

        // Check the negative side of the split sub-line
        SubLine negative = (SubLine) splitSubLine.getMinus();
        Assert.assertEquals(Region.Location.INSIDE, negative.getRemainingRegion().checkPoint(new Vector1D(1.5)));
    }

}

