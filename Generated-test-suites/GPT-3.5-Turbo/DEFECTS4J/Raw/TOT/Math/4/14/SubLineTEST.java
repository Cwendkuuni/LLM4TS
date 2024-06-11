import static org.junit.Assert.*;

import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.junit.Before;
import org.junit.Test;

public class SubLineTest {

    private SubLine subLine;
    private Vector2D start;
    private Vector2D end;

    @Before
    public void setUp() {
        start = new Vector2D(0, 0);
        end = new Vector2D(2, 0);
        subLine = new SubLine(start, end);
    }

    @Test
    public void testGetSegments() {
        // Typical Case
        assertEquals(1, subLine.getSegments().size());
        assertEquals(start, subLine.getSegments().get(0).getStart());
        assertEquals(end, subLine.getSegments().get(0).getEnd());

        // Edge Case
        subLine = new SubLine(new Vector2D(0, 0), new Vector2D(0, 0));
        assertEquals(1, subLine.getSegments().size());
        assertEquals(new Vector2D(Double.NEGATIVE_INFINITY, 0), subLine.getSegments().get(0).getStart());
        assertEquals(new Vector2D(Double.POSITIVE_INFINITY, 0), subLine.getSegments().get(0).getEnd());

        // Error Case
        subLine = new SubLine(new Vector2D(1, 0), new Vector2D(0, 0));
        assertEquals(0, subLine.getSegments().size());
    }

    @Test
    public void testIntersection() {
        // Typical Case
        SubLine other = new SubLine(new Vector2D(1, 1), new Vector2D(1, -1));
        assertEquals(new Vector2D(1, 0), subLine.intersection(other, true));
        assertNull(subLine.intersection(other, false));

        // Edge Case
        // subLine covers the whole line, other intersects with subLine in the middle
        other = new SubLine(new Vector2D(1, 0), new Vector2D(1, 2));
        assertEquals(new Vector2D(1, 0), subLine.intersection(other, true));
        assertNull(subLine.intersection(other, false));

        // Error Case
        // subLine and other are parallel and don't intersect
        other = new SubLine(new Vector2D(0, 1), new Vector2D(2, 1));
        assertNull(subLine.intersection(other, true));
        assertNull(subLine.intersection(other, false));
    }

    @Test
    public void testSide() {
        // Typical Case
        // other line is above subLine
        SubLine other = new SubLine(new Vector2D(1, 1), new Vector2D(2, 1));
        assertEquals(Location.OUTSIDE, subLine.side(other));

        // other line is below subLine
        other = new SubLine(new Vector2D(1, -1), new Vector2D(2, -1));
        assertEquals(Location.OUTSIDE, subLine.side(other));

        // other line intersects with subLine
        other = new SubLine(new Vector2D(1, 1), new Vector2D(1, -1));
        assertEquals(Location.INSIDE, subLine.side(other));

        // Edge Case
        // other line is exactly above subLine
        other = new SubLine(new Vector2D(0, 1), new Vector2D(2, 1));
        assertEquals(Location.BOUNDARY, subLine.side(other));

        // other line is exactly below subLine
        other = new SubLine(new Vector2D(0, -1), new Vector2D(2, -1));
        assertEquals(Location.BOUNDARY, subLine.side(other));

        // other line intersects with subLine at start point
        other = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        assertEquals(Location.BOUNDARY, subLine.side(other));

        // other line intersects with subLine at end point
        other = new SubLine(new Vector2D(0, 1), new Vector2D(1, 0));
        assertEquals(Location.BOUNDARY, subLine.side(other));

        // Error Case
        // other line is parallel to subLine and doesn't intersect
        other = new SubLine(new Vector2D(0, 1), new Vector2D(2, 1));
        assertEquals(Location.OUTSIDE, subLine.side(other));
    }

    @Test
    public void testSplit() {
        // Typical Case
        // other line intersects with subLine, splitting it into two sub-lines
        SubLine other = new SubLine(new Vector2D(1, 1), new Vector2D(1, -1));
        SubLine.SplitSubHyperplane<Euclidean2D> split = subLine.split(other);
        assertEquals(subLine, split.getPlus());
        assertNull(split.getMinus().getSegments().get(0).getStart());
        assertEquals(new Vector2D(0, 0), split.getMinus().getSegments().get(0).getEnd());
        assertNull(split.getMinus().getSegments().get(1).getStart());
        assertEquals(new Vector2D(2, 0), split.getMinus().getSegments().get(1).getEnd());

        // Edge Case
        // other line intersects with subLine at start point
        other = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        split = subLine.split(other);
        assertEquals(subLine, split.getPlus());
        assertNull(split.getMinus().getSegments().get(0).getStart());
        assertEquals(new Vector2D(0, 0), split.getMinus().getSegments().get(0).getEnd());
        assertEquals(new Vector2D(0, 0), split.getMinus().getSegments().get(1).getStart());
        assertEquals(new Vector2D(2, 0), split.getMinus().getSegments().get(1).getEnd());

        // other line intersects with subLine at end point
        other = new SubLine(new Vector2D(0, 1), new Vector2D(1, 0));
        split = subLine.split(other);
        assertEquals(subLine, split.getPlus());
        assertEquals(new Vector2D(0, 0), split.getMinus().getSegments().get(0).getStart());
        assertEquals(new Vector2D(2, 0), split.getMinus().getSegments().get(0).getEnd());
        assertNull(split.getMinus().getSegments().get(1).getStart());
        assertEquals(new Vector2D(2, 0), split.getMinus().getSegments().get(1).getEnd());

        // Error Case
        // other line is parallel to subLine and doesn't intersect
        other = new SubLine(new Vector2D(0, 1), new Vector2D(2, 1));
        split = subLine.split(other);
        assertNull(split.getPlus());
        assertEquals(subLine, split.getMinus());
    }

}