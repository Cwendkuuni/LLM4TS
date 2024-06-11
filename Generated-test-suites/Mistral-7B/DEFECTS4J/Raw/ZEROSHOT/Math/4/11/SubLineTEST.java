import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2DSpace;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testSubLineConstruction() {
        // test simple constructor
        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> remainingRegion = new IntervalsSet(new Interval(0.5, 0.8));
        SubLine subLine = new SubLine(hyperplane, remainingRegion);
        assertArrayEquals(new double[]{0.5, 0.8}, subLine.getSegments().get(0).getStart().toArray(), 0.0);
        assertArrayEquals(new double[]{0.8, 1.0}, subLine.getSegments().get(0).getEnd().toArray(), 0.0);

        // test constructor from two points
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        SubLine subLine2 = new SubLine(start, end);
        assertArrayEquals(new double[]{1.0, 3.0}, subLine2.getSegments().get(0).getStart().toArray(), 0.0);
        assertArrayEquals(new double[]{3.0, 4.0}, subLine2.getSegments().get(0).getEnd().toArray(), 0.0);

        // test constructor from a segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine3 = new SubLine(segment);
        assertArrayEquals(new double[]{0.0, 1.0}, subLine3.getSegments().get(0).getStart().toArray(), 0.0);
        assertArrayEquals(new double[]{1.0, 1.0}, subLine3.getSegments().get(0).getEnd().toArray(), 0.0);
    }

    @Test
    public void testIntersection() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(2, 2), new Vector2D(3, 3));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 0.8)));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));

        Vector2D result = subLine1.intersection(subLine2, true);
        assertNull(result);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 0.8)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));

        result = subLine1.intersection(subLine2, true);
        assertArrayEquals(new double[]{1.0, 1.0}, result.toArray(), 0.0);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 0.8)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(2.5, 3.0)));

        result = subLine1.intersection(subLine2, true);
        assertNull(result);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 0.8)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));

        result = subLine1.intersection(subLine2, false);
        assertNull(result);

        line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));

        subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 0.8)));
        subLine2 = new SubLine(line2, new IntervalsSet(new Interval(1.5, 2.5)));

        result = subLine1.intersection(subLine2, true);
        assertArrayEquals(new double[]{1.0, 1.0}, result.toArray(), 0.0);
    }

    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(line, new IntervalsSet(new Interval(0.5, 0.8), new Interval(1.2, 1.5)));

        List<Segment> segments = subLine.getSegments();

        Segment segment1 = segments.get(0);
        assertArrayEquals(new double[]{0.5, 0.8}, segment1.getStart().toArray(), 0.0);
        assertArrayEquals(new double[]{0.8, 1.0}, segment1.getEnd().toArray(), 0.0);

        Segment segment2 = segments.get(1);
        assertArrayEquals(new double[]{1.2, 1.5}, segment2.getStart().toArray(), 0.0);
        assertArrayEquals(new double[]{1.5, 1.5}, segment2.getEnd().toArray(), 0.0); // end point is considered closed
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 0.8)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(2, 2), new Vector2D(3, 3));

        Side side = subLine1.side(hyperplane);
        assertArrayEquals(Side.MINUS, side.getCode());

        hyperplane = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        side = subLine1.side(hyperplane);
        assertArrayEquals(Side.PLUS, side.getCode());

        hyperplane = new Line(new Vector2D(3, 3), new Vector2D(4, 4));
        side = subLine1.side(hyperplane);
        assertArrayEquals(Side.HYPER, side.getCode());
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(2, 2));

        SubLine subLine1 = new SubLine(line1, new IntervalsSet(new Interval(0.5, 0.8)));

        Hyperplane<Euclidean2D> hyperplane = new Line(new Vector2D(1, 1), new Vector2D(2, 2));

        SplitSubHyperplane<Euclidean2D> result = subLine1.split(hyperplane);

        SubLine subLinePlus = result.getSubHyperplane1();
        assertArrayEquals(Side.MINUS, subLinePlus.getHyperplane().side(hyperplane).getCode());

        SubLine subLineMinus = result.getSubHyperplane2();
        assertArrayEquals(Side.PLUS, subLineMinus.getHyperplane().side(hyperplane).getCode());
    }
}