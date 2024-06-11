import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SubLineTest {

    private SubLine subLine;

    @Before
    public void setUp() {
        // create a sub-line for testing
        subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
    }

    @Test
    public void testGetSegments() {
        // test if the sub-line contains the correct segments
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // create another sub-line for testing
        SubLine otherSubLine = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));

        // test if the intersection point is correctly computed
        Vector2D intersection = subLine.intersection(otherSubLine, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testBuildNew() {
        // create a new sub-line
        SubLine newSubLine = subLine.buildNew(new Line(new Vector2D(0, 0), new Vector2D(2, 2)), new IntervalsSet(0, 2));

        // test if the new sub-line has the correct hyperplane and remaining region
        assertEquals(new Line(new Vector2D(0, 0), new Vector2D(2, 2)), newSubLine.getHyperplane());
        assertEquals(new IntervalsSet(0, 2), newSubLine.getRemainingRegion());
    }

    @Test
    public void testSide() {
        // create a line for testing
        Line line = new Line(new Vector2D(0, 1), new Vector2D(1, 0));

        // test if the side is correctly computed
        assertEquals(Side.MINUS, subLine.side(line));
        assertEquals(Side.PLUS, subLine.side(new Line(new Vector2D(1, 0), new Vector2D(2, 1))));
        assertEquals(Side.HYPER, subLine.side(new Line(new Vector2D(0, 2), new Vector2D(1, 2))));
    }

    @Test
    public void testSplit() {
        // create a line for testing
        Line line = new Line(new Vector2D(1, 0), new Vector2D(0, 1));

        // test if the sub-line is correctly split
        SplitSubHyperplane<Euclidean2D> split = subLine.split(line);
        assertNull(split.getPlus());
        assertEquals(new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)),
                new IntervalsSet(0)), split.getMinus());
    }

}