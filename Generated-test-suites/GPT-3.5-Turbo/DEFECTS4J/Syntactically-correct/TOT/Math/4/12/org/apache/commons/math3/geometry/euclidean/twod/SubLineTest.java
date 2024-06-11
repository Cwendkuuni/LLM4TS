import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.partitioning.SubHyperplane;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.util.Precision;

import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        // create a sub-line from two endpoints
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);

        // check the number of segments
        assertEquals(1, subLine.getSegments().size());

        // check the start and end points of the segment
        assertEquals(start, subLine.getSegments().get(0).getStart());
        assertEquals(end, subLine.getSegments().get(0).getEnd());
        assertEquals(subLine.getHyperplane(), subLine.getSegments().get(0).getLine());
    }

    @Test
    public void testIntersection_includeEndPoints() {
        // create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(1, 1);
        Vector2D end2 = new Vector2D(3, 3);
        SubLine subLine2 = new SubLine(start2, end2);

        // calculate the intersection with endpoints
        Vector2D intersection = subLine1.intersection(subLine2, true);

        // check the intersection point
        assertNotNull(intersection);
        assertTrue(Precision.equals(intersection.getX(), 1.5, 1.0e-10));
        assertTrue(Precision.equals(intersection.getY(), 1.5, 1.0e-10));
    }

    @Test
    public void testIntersection_excludeEndPoints() {
        // create two sub-lines
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(2, 2);
        SubLine subLine1 = new SubLine(start1, end1);

        Vector2D start2 = new Vector2D(2, 0);
        Vector2D end2 = new Vector2D(3, 1);
        SubLine subLine2 = new SubLine(start2, end2);

        // calculate the intersection without endpoints
        Vector2D intersection = subLine1.intersection(subLine2, false);

        // check the intersection point
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        // create an interval set from two points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);

        // check the interval endpoints
        assertEquals(0.0, intervalSet.asList().get(0).getInf(), 1.0e-10);
        assertEquals(2.0, intervalSet.asList().get(0).getSup(), 1.0e-10);
    }

    @Test
    public void testSide() {
        // create two lines
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(3, 2));

        // create sub-lines from the lines
        SubLine subLine1 = new SubLine(line1, line1.wholeHyperplane());
        SubLine subLine2 = new SubLine(line2, line2.wholeHyperplane());

        // check the side of subLine2 with respect to subLine1
        Side side = subLine1.side(subLine2.getHyperplane());
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        // create two lines
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(2, 2));
        Line line2 = new Line(new Vector2D(1, 0), new Vector2D(3, 2));

        // create sub-lines from the lines
        SubLine subLine1 = new SubLine(line1, line1.wholeHyperplane());
        SubLine subLine2 = new SubLine(line2, line2.wholeHyperplane());

        // split subLine1 by subLine2
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2.getHyperplane());

        // check the splitted sub-lines
        SubLine plus = (SubLine) split.getPlus();
        SubLine minus = (SubLine) split.getMinus();

        // check the plus sub-line
        assertEquals(1, plus.getSegments().size());
        assertEquals(new Vector2D(1, 1), plus.getSegments().get(0).getStart());
        assertEquals(new Vector2D(2, 2), plus.getSegments().get(0).getEnd());
        assertEquals(subLine1.getHyperplane(), plus.getSegments().get(0).getLine());

        // check the minus sub-line
        assertEquals(1, minus.getSegments().size());
        assertEquals(new Vector2D(0, 0), minus.getSegments().get(0).getStart());
        assertEquals(new Vector2D(1, 1), minus.getSegments().get(0).getEnd());
        assertEquals(subLine1.getHyperplane(), minus.getSegments().get(0).getLine());
    }

    @Test
    public void testBuildNew() {
        // create a sub-line
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(2, 2);
        SubLine subLine = new SubLine(start, end);

        // create a new sub-line
        Hyperplane<Euclidean2D> hyperplane = subLine.getHyperplane();
        SubHyperplane<Euclidean1D> remainingRegion = subLine.getRemainingRegion();
        SubLine newSubLine = (SubLine) subLine.buildNew(hyperplane, remainingRegion);

        // check the new sub-line
        assertEquals(subLine.getRemainingRegion(), newSubLine.getRemainingRegion());
        assertEquals(subLine.getHyperplane(), newSubLine.getHyperplane());
    }

}