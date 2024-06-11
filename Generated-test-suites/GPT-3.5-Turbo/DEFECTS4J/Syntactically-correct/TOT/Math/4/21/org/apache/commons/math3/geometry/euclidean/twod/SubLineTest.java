import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;

public class SubLineTest {

    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(5, 5);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();

        // Check that there is only one segment
        assertEquals(1, segments.size());

        // Check that the start and end points are correct
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testIntersection_IncludeEndPoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(5, 5);
        Vector2D start2 = new Vector2D(0, 5);
        Vector2D end2 = new Vector2D(5, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the intersection point is correct
        assertEquals(new Vector2D(2.5, 2.5), subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersection_NotIncludeEndPoints() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(5, 5);
        Vector2D start2 = new Vector2D(0, 5);
        Vector2D end2 = new Vector2D(5, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the intersection point is null
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(5, 5);

        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);

        // Check that the interval set is correct
        assertEquals(new IntervalsSet(-5, 5), intervalSet);
    }

    @Test
    public void testSide_ParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(5, 5);
        Vector2D start2 = new Vector2D(0, 5);
        Vector2D end2 = new Vector2D(5, 10);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the side is Side.HYPER
        assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSide_IntersectionPointInsideBothSubLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(5, 5);
        Vector2D start2 = new Vector2D(0, 5);
        Vector2D end2 = new Vector2D(5, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the side is Side.PLUS (both sub-lines contain the intersection point)
        assertEquals(Side.PLUS, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSide_IntersectionPointOutsideBothSubLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(5, 5);
        Vector2D start2 = new Vector2D(5, 0);
        Vector2D end2 = new Vector2D(10, 5);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the side is Side.MINUS (neither sub-line contains the intersection point)
        assertEquals(Side.MINUS, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit_ParallelLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(5, 5);
        Vector2D start2 = new Vector2D(0, 5);
        Vector2D end2 = new Vector2D(5, 10);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the split sub-hyperplanes are null
        assertNull(subLine1.split(subLine2.getHyperplane()).getPlus());
        assertNull(subLine1.split(subLine2.getHyperplane()).getMinus());
    }

    @Test
    public void testSplit_IntersectionPointInsideBothSubLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(5, 5);
        Vector2D start2 = new Vector2D(0, 5);
        Vector2D end2 = new Vector2D(5, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the split sub-hyperplanes are equal to the original sub-lines
        assertEquals(subLine1, subLine1.split(subLine2.getHyperplane()).getPlus());
        assertEquals(subLine1, subLine1.split(subLine2.getHyperplane()).getMinus());
    }

    @Test
    public void testSplit_IntersectionPointOutsideBothSubLines() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(5, 5);
        Vector2D start2 = new Vector2D(5, 0);
        Vector2D end2 = new Vector2D(10, 5);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        // Check that the split sub-hyperplanes are null
        assertNull(subLine1.split(subLine2.getHyperplane()).getPlus());
        assertNull(subLine1.split(subLine2.getHyperplane()).getMinus());
    }

}