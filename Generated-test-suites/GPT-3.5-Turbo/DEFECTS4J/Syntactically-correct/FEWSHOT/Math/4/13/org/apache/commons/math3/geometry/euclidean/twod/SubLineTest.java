import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SubLineTest {
    private SubLine subLine;

    @Before
    public void setUp() {
        subLine = new SubLine(new Vector2D(1, 2), new Vector2D(3, 4));
    }

    @Test
    public void testGetSegments() {
        assertEquals(1, subLine.getSegments().size());
        assertEquals(new Vector2D(1, 2), subLine.getSegments().get(0).getStart());
        assertEquals(new Vector2D(3, 4), subLine.getSegments().get(0).getEnd());
        assertEquals(subLine, subLine.getSegments().get(0).getLine());
    }

    @Test
    public void testIntersection_includeEndPoints() {
        SubLine subLine2 = new SubLine(new Vector2D(2, 3), new Vector2D(4, 5));
        Vector2D intersection = subLine.intersection(subLine2, true);
        assertEquals(new Vector2D(3, 4), intersection);
    }

    @Test
    public void testIntersection_excludeEndPoints() {
        SubLine subLine2 = new SubLine(new Vector2D(2, 3), new Vector2D(4, 5));
        Vector2D intersection = subLine.intersection(subLine2, false);
        assertNull(intersection);
    }

    @Test
    public void testBuildIntervalSet() {
        IntervalsSet intervalSet = SubLine.buildIntervalSet(new Vector2D(1, 2), new Vector2D(3, 4));
        assertEquals(1, intervalSet.asList().size());
        assertEquals(1, intervalSet.asList().get(0).getInf(), 0.0);
        assertEquals(3, intervalSet.asList().get(0).getSup(), 0.0);
    }

    @Test
    public void testBuildNew() {
        SubLine newSubLine = subLine.buildNew(subLine.getHyperplane(), subLine.getRemainingRegion());
        assertEquals(subLine, newSubLine);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(1, 2), new Vector2D(2, 4));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        assertEquals(Side.PLUS, subLine.side(line1));
        assertEquals(Side.MINUS, subLine.side(line2));
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(1, 2), new Vector2D(2, 4));
        Line line2 = new Line(new Vector2D(1, 1), new Vector2D(3, 3));
        SplitSubHyperplane<Euclidean2D> split = subLine.split(line1);
        assertNull(split.getPlus());
        assertEquals(subLine, split.getMinus());
        split = subLine.split(line2);
        assertEquals(subLine, split.getPlus());
        assertNull(split.getMinus());
    }
}