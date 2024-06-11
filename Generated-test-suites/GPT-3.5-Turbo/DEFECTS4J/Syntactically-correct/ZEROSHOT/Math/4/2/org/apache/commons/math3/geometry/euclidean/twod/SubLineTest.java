import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    private SubLine subLine;

    @Before
    public void setUp() {
        // Create a sub-line from (1, 2) to (3, 4)
        Vector2D start = new Vector2D(1, 2);
        Vector2D end = new Vector2D(3, 4);
        subLine = new SubLine(start, end);
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());

        Segment segment = segments.get(0);
        assertEquals(new Vector2D(1, 2), segment.getStart());
        assertEquals(new Vector2D(3, 4), segment.getEnd());
    }

    @Test
    public void testIntersection() {
        // Create another sub-line that intersects with the original sub-line
        Vector2D start = new Vector2D(2, 3);
        Vector2D end = new Vector2D(4, 5);
        SubLine otherSubLine = new SubLine(start, end);

        // Test intersection with all endpoints included
        Vector2D intersection = subLine.intersection(otherSubLine, true);
        assertNotNull(intersection);
        assertEquals(new Vector2D(2, 3), intersection);

        // Test intersection with endpoints excluded
        intersection = subLine.intersection(otherSubLine, false);
        assertNotNull(intersection);
        assertEquals(new Vector2D(2, 3), intersection);

        // Create another sub-line that doesn't intersect with the original sub-line
        start = new Vector2D(5, 6);
        end = new Vector2D(7, 8);
        otherSubLine = new SubLine(start, end);

        // Test intersection with all endpoints included
        intersection = subLine.intersection(otherSubLine, true);
        assertNull(intersection);

        // Test intersection with endpoints excluded
        intersection = subLine.intersection(otherSubLine, false);
        assertNull(intersection);
    }

    @Test
    public void testSide() {
        // Create a hyperplane that intersects with the original sub-line
        Vector2D start = new Vector2D(2, 3);
        Vector2D end = new Vector2D(4, 5);
        Line otherLine = new Line(start, end);

        // Test side when the hyperplane intersects with the sub-line
        assertEquals(Side.HYPER, subLine.side(otherLine));

        // Create a hyperplane that is parallel to the original sub-line
        start = new Vector2D(1, 2);
        end = new Vector2D(3, 4);
        otherLine = new Line(start, end);

        // Test side when the hyperplane is parallel to the sub-line
        assertEquals(Side.HYPER, subLine.side(otherLine));

        // Create a hyperplane that is on the negative side of the original sub-line
        start = new Vector2D(-1, 0);
        end = new Vector2D(0, 1);
        otherLine = new Line(start, end);

        // Test side when the hyperplane is on the negative side of the sub-line
        assertEquals(Side.MINUS, subLine.side(otherLine));

        // Create a hyperplane that is on the positive side of the original sub-line
        start = new Vector2D(4, 5);
        end = new Vector2D(5, 6);
        otherLine = new Line(start, end);

        // Test side when the hyperplane is on the positive side of the sub-line
        assertEquals(Side.PLUS, subLine.side(otherLine));
    }

    @Test
    public void testSplit() {
        // Create a hyperplane that intersects with the original sub-line
        Vector2D start = new Vector2D(2, 3);
        Vector2D end = new Vector2D(4, 5);
        Line otherLine = new Line(start, end);

        // Test split when the hyperplane intersects with the sub-line
        SplitSubHyperplane<Euclidean2D> split = subLine.split(otherLine);
        assertNotNull(split.getPlus());
        assertNotNull(split.getMinus());

        // Create a hyperplane that is parallel to the original sub-line
        start = new Vector2D(1, 2);
        end = new Vector2D(3, 4);
        otherLine = new Line(start, end);

        // Test split when the hyperplane is parallel to the sub-line
        split = subLine.split(otherLine);
        assertNotNull(split.getPlus());
        assertNull(split.getMinus());

        // Create a hyperplane that is on the negative side of the original sub-line
        start = new Vector2D(-1, 0);
        end = new Vector2D(0, 1);
        otherLine = new Line(start, end);

        // Test split when the hyperplane is on the negative side of the sub-line
        split = subLine.split(otherLine);
        assertNull(split.getPlus());
        assertNotNull(split.getMinus());

        // Create a hyperplane that is on the positive side of the original sub-line
        start = new Vector2D(4, 5);
        end = new Vector2D(5, 6);
        otherLine = new Line(start, end);

        // Test split when the hyperplane is on the positive side of the sub-line
        split = subLine.split(otherLine);
        assertNotNull(split.getPlus());
        assertNull(split.getMinus());
    }

}