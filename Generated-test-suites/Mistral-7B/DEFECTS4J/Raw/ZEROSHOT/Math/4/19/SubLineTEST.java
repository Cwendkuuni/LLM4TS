import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(3.0, 0.0));
        SubLine subLine = new SubLine(hyperplane, new IntervalsSet(new Interval(1.5, 2.5)));
        assertEquals(hyperplane, subLine.getHyperplane());
        assertEquals(new IntervalsSet(new Interval(1.5, 2.5)), subLine.getRemainingRegion());

        // test constructor from two points
        Vector2D start = new Vector2D(0.0, 0.0);
        Vector2D end = new Vector2D(4.0, 0.0);
        SubLine subLine2 = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        assertEquals(start, line.getStartVector());
        assertEquals(end, line.getEndVector());
        assertEquals(new IntervalsSet(new Interval(0.0, 4.0), new Interval(4.0, Double.POSITIVE_INFINITY)),
                     subLine.getRemainingRegion());

        // test constructor from a segment
        Segment segment = new Segment(new Vector2D(0.0, 0.0), new Vector2D(3.0, 0.0));
        SubLine subLine3 = new SubLine(segment);
        assertEquals(segment.getLine(), subLine3.getHyperplane());
        assertEquals(segment.getEndPoint(), subLine3.getEndpoints().get(0).getEnd());

        // test getSegments method
        List<Segment> segments = subLine3.getSegments();
        assertEquals(1, segments.size());
        assertEquals(segment.getStartPoint(), segments.get(0).getStart());
        assertEquals(segment.getEndPoint(), segments.get(0).getEnd());

        // test intersection method with other sub-line
        SubLine subLine4 = new SubLine(new Line(new Vector2D(0.0, 0.0), new Vector2D(2.0, 2.0)),
                                        new IntervalsSet(new Interval(1.0, 3.0)));
        Vector2D intersectionPoint = subLine3.intersection(subLine4, false);
        assertNull(intersectionPoint);

        // test intersection method with the same sub-line
        SubLine subLine5 = new SubLine(subLine3);
        intersectionPoint = subLine3.intersection(subLine5, false);
        assertNotNull(intersectionPoint);
        assertEquals(new Vector2D(1.5, 0.0), intersectionPoint);

        // test intersection method with another sub-line overlapping with this one
        Line line2 = new Line(new Vector2D(1.0, 0.0), new Vector2D(5.0, 0.0));
        SubLine subLine6 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 4.5)));
        intersectionPoint = subLine3.intersection(subLine6, false);
        assertNotNull(intersectionPoint);
        assertEquals(new Vector2D(3.0, 0.0), intersectionPoint);

        // test intersection method with another sub-line disjoint from this one
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(6.0, 0.0));
        SubLine subLine7 = new SubLine(line3, new IntervalsSet(new Interval(-1.0, 1.0)));
        intersectionPoint = subLine3.intersection(subLine7, false);
        assertNull(intersectionPoint);

        // test intersection method with another sub-line containing this one
        Line line4 = new Line(new Vector2D(0.0, 0.0), new Vector2D(8.0, 0.0));
        SubLine subLine8 = new SubLine(line4, new IntervalsSet(new Interval(0.0, Double.POSITIVE_INFINITY),
                                                              new Interval(Double.NEGATIVE_INFINITY, 8.0)));
        intersectionPoint = subLine3.intersection(subLine8, false);
        assertEquals(new Vector2D(1.5, 0.0), intersectionPoint);

        // test intersection method with another sub-line containing this one but not including its endpoints
        Line line5 = new Line(new Vector2D(0.0, 0.0), new Vector2D(10.0, 0.0));
        SubLine subLine9 = new SubLine(line5, new IntervalsSet(new Interval(1.0, 9.0)));
        intersectionPoint = subLine3.intersection(subLine9, false);
        assertNull(intersectionPoint);

        // test intersection method with another sub-line including only this sub-line's endpoints
        Line line6 = new Line(new Vector2D(0.0, 0.0), new Vector2D(3.0, 0.0));
        SubLine subLine10 = new SubLine(line6, new IntervalsSet(new Interval(1.5, 1.5), new Interval(2.5, 2.5)));
        intersectionPoint = subLine3.intersection(subLine10, false);
        assertNotNull(intersectionPoint);
        assertEquals(new Vector2D(1.5, 0.0), intersectionPoint);

        // test intersection method with another sub-line including only this sub-line's start point
        Line line7 = new Line(new Vector2D(2.0, 0.0), new Vector2D(4.0, 0.0));
        SubLine subLine11 = new SubLine(line7, new IntervalsSet(new Interval(0.0, 1.5)));
        intersectionPoint = subLine3.intersection(subLine11, false);
        assertNull(intersectionPoint);

    }

    @Test
    public void testSubLineSplit() {
        // test splitting a sub-line with a hyperplane parallel to it
        Line line1 = new Line(new Vector2D(0.0, 0.0), new Vector2D(4.0, 0.0));
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1.0, 0.0), new Vector2D(3.0, 0.0));
        SubLine subLine = new SubLine(line1, new IntervalsSet(new Interval(1.5, 3.0)));
        SplitSubHyperplane<Euclidean2D> split = subLine.split(hyperplane);
        assertEquals(subLine, split.getLeft());
        assertEquals(null, split.getRight());

        // test splitting a sub-line with a hyperplane intersecting it
        Line line2 = new Line(new Vector2D(0.0, 0.0), new Vector2D(4.0, 4.0));
        Hyperplane<Euclidean2D> hyperplane2 = new Line(new Vector2D(2.0, 0.0), new Vector2D(2.0, 4.0));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.0, 4.0)));
        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(hyperplane2);
        assertEquals(new SubLine(line2.copySelf(), new IntervalsSet(new Interval(1.0, 2.0), new Interval(3.0, 4.0))),
                     split2.getLeft());
        assertEquals(new SubLine(line2.copySelf(), new IntervalsSet(new Interval(2.0, 3.0))), split2.getRight());

        // test splitting a sub-line with a hyperplane intersecting it only at its endpoints
        Line line3 = new Line(new Vector2D(0.0, 0.0), new Vector2D(8.0, 0.0));
        Hyperplane<Euclidean2D> hyperplane3 = new Line(new Vector2D(4.0, 0.0), new Vector2D(6.0, 0.0));
        SubLine subLine3 = new SubLine(line3, new IntervalsSet(new Interval(1.0, 7.0)));
        SplitSubHyperplane<Euclidean2D> split3 = subLine3.split(hyperplane3);
        assertEquals(new SubLine(line3.copySelf(), new IntervalsSet(new Interval(1.0, 4.0), new Interval(6.0, 7.0))),
                     split3.getLeft());
        assertEquals(new SubLine(line3.copySelf(), new IntervalsSet(new Interval(4.0, 6.0))), split3.getRight());

        // test splitting a sub-line with a hyperplane intersecting it only at one point
        Line line4 = new Line(new Vector2D(0.0, 0.0), new Vector2D(8.0, 0.0));
        Hyperplane<Euclidean2D> hyperplane4 = new Line(new Vector2D(3.0, 0.0), new Vector2D(5.0, 0.0));
        SubLine subLine4 = new SubLine(line4, new IntervalsSet(new Interval(1.0, 7.0)));
        SplitSubHyperplane<Euclidean2D> split4 = subLine4.split(hyperplane4);
        assertEquals(new SubLine(line4.copySelf(), new IntervalsSet(new Interval(1.0, 3.0), new Interval(5.0, 7.0))),
                     split4.getLeft());
        assertEquals(new SubLine(line4.copySelf(), new IntervalsSet(new Interval(3.0, 5.0))), split4.getRight());

        // test splitting a sub-line with a hyperplane not intersecting it
        Line line5 = new Line(new Vector2D(0.0, 0.0), new Vector2D(8.0, 0.0));
        Hyperplane<Euclidean2D> hyperplane5 = new Line(new Vector2D(6.0, 0.0), new Vector2D(10.0, 0.0));
        SubLine subLine5 = new SubLine(line5, new IntervalsSet(new Interval(1.0, 7.0)));
        SplitSubHyperplane<Euclidean2D> split5 = subLine5.split(hyperplane5);
        assertEquals(subLine5, split5.getLeft());
        assertEquals(null, split5.getRight());
    }

}