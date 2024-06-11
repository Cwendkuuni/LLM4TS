import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.partitioning.*;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import java.util.List;
import java.util.ArrayList;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // Create a sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        // Check the number of segments
        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());

        // Check the start and end points of the segment
        Assert.assertEquals(start, segments.get(0).getStart());
        Assert.assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check intersection with endpoints included
        Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersection);

        // Check intersection with endpoints excluded
        intersection = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersection);
    }

    @Test
    public void testSide() {
        // Create two lines
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));

        // Create two sub-lines
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(0, 1));

        // Check the side of subLine2 with respect to subLine1
        Assert.assertEquals(Side.PLUS, subLine2.side(subLine1));
    }

    @Test
    public void testSplit() {
        // Create a line
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));

        // Create a sub-line
        SubLine subLine = new SubLine(line, new IntervalsSet(0, 1));

        // Create a hyperplane to split the sub-line
        Line splittingLine = new Line(new Vector2D(0, 1), new Vector2D(1, 0));

        // Split the sub-line
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine.split(splittingLine);

        // Check the plus side sub-line
        SubLine plusSubLine = (SubLine) splitSubLine.getPlus();
        Assert.assertNotNull(plusSubLine);
        Assert.assertEquals(new Vector2D(0.5, 0.5), plusSubLine.getSegments().get(0).getStart());
        Assert.assertEquals(new Vector2D(1, 1), plusSubLine.getSegments().get(0).getEnd());
        
        // Check the minus side sub-line
        SubLine minusSubLine = (SubLine) splitSubLine.getMinus();
        Assert.assertNotNull(minusSubLine);
        Assert.assertEquals(new Vector2D(0, 0), minusSubLine.getSegments().get(0).getStart());
        Assert.assertEquals(new Vector2D(0.5, 0.5), minusSubLine.getSegments().get(0).getEnd());
    }
}