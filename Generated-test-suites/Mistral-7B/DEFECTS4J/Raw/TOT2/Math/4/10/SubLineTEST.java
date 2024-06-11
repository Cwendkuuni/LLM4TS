import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // Test simple constructor
        SubLine subLine1 = new SubLine(new Hyperplane<Euclidean2D>(new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0)),
                                                                      new IntervalsSet(new Interval(0.0, 0.5))),
                                       new IntervalsSet(new Interval(0.5, 1.0)));
        Assert.assertEquals("Incorrect endpoints of sub-line",
                            new Vector2D[]{new Vector2D(0.5, 0.0), new Vector2D(1.0, 0.0)},
                            subLine1.getSegments().toArray(new Vector2D[0][]));
        
        // Test constructor from two endpoints
        SubLine subLine2 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        Assert.assertEquals("Incorrect endpoints of sub-line",
                            new Vector2D[]{new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.4142135625)},
                            subLine2.getSegments().toArray(new Vector2D[0][]));
        
        // Test constructor from a segment
        Segment segment = new Segment(new Vector2D(1.0, 1.0), new Vector2D(3.0, 1.0));
        SubLine subLine3 = new SubLine(segment);
        Assert.assertEquals("Incorrect endpoints of sub-line",
                            new Vector2D[]{new Vector2D(1.0, 1.0), new Vector2D(3.0, 1.0)},
                            subLine3.getSegments().toArray(new Vector2D[0][]));
    }

    @Test
    public void testIntersection() {
        // Test intersection between two disjoint sub-lines
        SubLine subLine1 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        SubLine subLine2 = new SubLine(new Vector2D(2.0, 0.0), new Vector2D(3.0, 0.0));
        Assert.assertNull("Intersection should be null", subLine1.intersection(subLine2, false));

        // Test intersection between two intersecting sub-lines
        SubLine subLine3 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        SubLine subLine4 = new SubLine(new Vector2D(1.0, 1.0), new Vector2D(2.0, 2.0));
        Vector2D intersectionPoint = subLine3.intersection(subLine4, false);
        Assert.assertNotNull("Intersection should not be null", intersectionPoint);
        Assert.assertEquals("Incorrect intersection point", new Vector2D(1.0, 1.0), intersectionPoint);

        // Test intersection between two infinite sub-lines
        SubLine subLine5 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        SubLine subLine6 = new SubLine(new Vector2D(2.0, 0.0), new Vector2D(3.0, 0.0));
        intersectionPoint = subLine5.intersection(subLine6, true);
        Assert.assertNull("Intersection should be null", intersectionPoint);

        // Test intersection between a bounded and an infinite sub-line
        SubLine subLine7 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        SubLine subLine8 = new SubLine(new Vector2D(2.0, 0.0), new Vector2D(Double.POSITIVE_INFINITY, 0.0));
        intersectionPoint = subLine7.intersection(subLine8, false);
        Assert.assertNull("Intersection should be null", intersectionPoint);

        // Test intersection between an infinite and a bounded sub-line
        intersectionPoint = subLine8.intersection(subLine7, true);
        Assert.assertEquals("Incorrect intersection point", new Vector2D(1.0, 0.0), intersectionPoint);
    }

    @Test
    public void testSplit() {
        // Test splitting a sub-line by a vertical line
        SubLine subLine1 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), Vector2D.UNIT_X);
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane);
        SubLine subLineLeft = split.getLeft();
        SubLine subLineRight = split.getRight();
        Assert.assertEquals("Incorrect number of segments in left sub-line", 1, subLineLeft.getSegments().size());
        Assert.assertEquals("Incorrect number of segments in right sub-line", 1, subLineRight.getSegments().size());
        Assert.assertEquals("Incorrect endpoint of segment in left sub-line", new Vector2D(0.0, 0.0),
                            subLineLeft.getSegments().get(0).getStart());
        Assert.assertEquals("Incorrect endpoint of segment in left sub-line", new Vector2D(1.0, 0.0),
                            subLineLeft.getSegments().get(0).getEnd());
        Assert.assertEquals("Incorrect endpoint of segment in right sub-line", new Vector2D(1.0, 0.0),
                            subLineRight.getSegments().get(0).getStart());
        Assert.assertEquals("Incorrect endpoint of segment in right sub-line", new Vector2D(2.0, 0.0),
                            subLineRight.getSegments().get(0).getEnd());

        // Test splitting a sub-line by a horizontal line
        subLine1 = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(0.0, 2.0));
        hyperplane = new Line(Vector2D.UNIT_Y, new Vector2D(1.0, 0.0));
        split = subLine1.split(hyperplane);
        subLineLeft = split.getLeft();
        subLineRight = split.getRight();
        Assert.assertEquals("Incorrect number of segments in left sub-line", 1, subLineLeft.getSegments().size());
        Assert.assertEquals("Incorrect number of segments in right sub-line", 1, subLineRight.getSegments().size());
        Assert.assertEquals("Incorrect endpoint of segment in left sub-line", new Vector2D(0.0, 0.0),
                            subLineLeft.getSegments().get(0).getStart());
        Assert.assertEquals("Incorrect endpoint of segment in left sub-line", new Vector2D(0.0, 1.0),
                            subLineLeft.getSegments().get(0).getEnd());
        Assert.assertEquals("Incorrect endpoint of segment in right sub-line", new Vector2D(0.0, 1.0),
                            subLineRight.getSegments().get(0).getStart());
        Assert.assertEquals("Incorrect endpoint of segment in right sub-line", new Vector2D(0.0, 2.0),
                            subLineRight.getSegments().get(0).getEnd());
    }

    @Test
    public void testSide() {
        // Test the side of a sub-line with respect to itself
        SubLine subLine = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Side side = subLine.side(subLine.getHyperplane());
        Assert.assertEquals("Incorrect side of sub-line with respect to itself", Side.HYPER, side);

        // Test the side of a sub-line with respect to a parallel line
        Line line = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 0.0));
        side = subLine.side(line);
        Assert.assertEquals("Incorrect side of sub-line with respect to a parallel line", Side.HYPER, side);

        // Test the side of a sub-line with respect to an intersecting line
        line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        side = subLine.side(line);
        Assert.assertEquals("Incorrect side of sub-line with respect to an intersecting line", Side.MINUS, side);

        // Test the side of a sub-line with respect to a line that does not intersect it
        line = new Line(new Vector2D(1.5, 0.0), new Vector2D(2.5, 0.0));
        side = subLine.side(line);
        Assert.assertEquals("Incorrect side of sub-line with respect to a line that does not intersect it", Side.PLUS, side);
    }

    @Test
    public void testAngleBetweenLines() {
        // Test the angle between two parallel lines
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        double angle = line1.angleBetween(line2);
        Assert.assertEquals("Incorrect angle between parallel lines", 0.0, angle, 1e-10);

        // Test the angle between two intersecting lines
        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        line2 = new Line(new Vector2D(0.5, 0.5), new Vector2D(1.5, 1.5));
        angle = line1.angleBetween(line2);
        Assert.assertEquals("Incorrect angle between intersecting lines", Math.PI / 4.0, angle, 1e-10);

        // Test the angle between two perpendicular lines
        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        line2 = new Line(new Vector2D(0.0, 1.0), new Vector2D(1.0, 0.0));
        angle = line1.angleBetween(line2);
        Assert.assertEquals("Incorrect angle between perpendicular lines", Math.PI / 2.0, angle, 1e-10);

        // Test the angle between a line and a vertical line
        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(0.0, 1.0));
        angle = line1.angleBetween(new Line(new Vector2D(0.0, 0.0), Vector2D.UNIT_X));
        Assert.assertEquals("Incorrect angle between a line and a vertical line", Math.PI / 2.0, angle, 1e-10);
    }

    @Test
    public void testGetRemainingRegion() {
        // Test getting the remaining region of a sub-line
        SubLine subLine = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Region<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        Interval expectedInterval = new Interval(0.0, 1.0);
        Assert.assertEquals("Incorrect remaining region of a sub-line", expectedInterval, remainingRegion);
        
        // Test getting the remaining region of an empty sub-line
        subLine = new SubLine(new Vector2D(0.0, 0.0), new Vector2D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        remainingRegion = subLine.getRemainingRegion();
        Assert.assertNull("Expected null for empty sub-line", remainingRegion);
    }
}