import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.AbstractSubHyperplane;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    @Test
    public void testSubLineCreation() {
        // Test creating a SubLine with a Line and IntervalsSet
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.5, 2.0));
        SubLine subLine = new SubLine(line, intervalsSet);
        Assert.assertEquals(line, subLine.getHyperplane());
        Assert.assertEquals(intervalsSet, subLine.getRemainingRegion());

        // Test creating a SubLine with two endpoints
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        SubLine subLineFromPoints = new SubLine(start, end);
        Assert.assertEquals(new Line(start, end), subLineFromPoints.getHyperplane());
        Assert.assertEquals(new IntervalsSet(new Interval(0.0, 1.0)), subLineFromPoints.getRemainingRegion());

        // Test creating a SubLine with a Segment
        Vector2D startSegment = new Vector2D(0.5, 0.5);
        Vector2D endSegment = new Vector2D(1.5, 1.5);
        Segment segment = new Segment(startSegment, endSegment, new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0)));
        SubLine subLineFromSegment = new SubLine(segment);
        Assert.assertEquals(new Line(startSegment, endSegment), subLineFromSegment.getHyperplane());
        Assert.assertEquals(new IntervalsSet(new Interval(0.5, 1.5)), subLineFromSegment.getRemainingRegion());
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 0.0));
        IntervalsSet intervalsSet = new IntervalsSet(new Interval(0.0, 0.5), new Interval(1.0, 1.5), new Interval(2.0, 2.5));
        SubLine subLine = new SubLine(line, intervalsSet);

        List<Segment> segments = subLine.getSegments();
        Assert.assertNotNull(segments);
        Assert.assertEquals(3, segments.size());

        Segment segment0 = segments.get(0);
        Assert.assertNotNull(segment0);
        Vector2D start0 = segment0.getStart();
        Vector2D end0 = segment0.getEnd();
        Assert.assertEquals(new Vector2D(0.0, 0.5), start0);
        Assert.assertEquals(new Vector2D(0.5, 0.5), end0);

        Segment segment1 = segments.get(1);
        Assert.assertNotNull(segment1);
        Vector2D start1 = segment1.getStart();
        Vector2D end1 = segment1.getEnd();
        Assert.assertEquals(new Vector2D(1.0, 1.5), start1);
        Assert.assertEquals(new Vector2D(1.5, 1.5), end1);

        Segment segment2 = segments.get(2);
        Assert.assertNotNull(segment2);
        Vector2D start2 = segment2.getStart();
        Vector2D end2 = segment2.getEnd();
        Assert.assertEquals(new Vector2D(2.0, 2.5), start2);
        Assert.assertEquals(new Vector2D(2.5, 2.5), end2);
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 1.5)));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertNotNull(intersectionPoint);
        Assert.assertEquals(new Vector2D(1.0, 1.0), intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 0.0));
        line2 = new Line(new Vector2D(1.0, 1.0), new Vector2D(2.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 1.5)));

        intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2, false);
        Assert.assertNull(intersectionPoint);

        line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(1.0, 1.0));
        line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(2.0, 1.0));
        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.0, 0.5)));
        SubLine subLine2Parallel = new SubLine(line2, new IntervalsSet(new Interval(0.0, 2.0)));

        intersectionPoint = subLine1.intersection(subLine2Parallel, true);
        Assert.assertNull(intersectionPoint);

        intersectionPoint = subLine1.intersection(subLine2Parallel, false);
        Assert.assertNull(intersectionPoint);
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(1.0, 1.0);
        Line line = new Line(start, end);
        IntervalsSet expectedIntervalSet = new IntervalsSet(new Interval(0.0, 1.0));
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        Assert.assertEquals(expectedIntervalSet, intervalSet);
    }
}