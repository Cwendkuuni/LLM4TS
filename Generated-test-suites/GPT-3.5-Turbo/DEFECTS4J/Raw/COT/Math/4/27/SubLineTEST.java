import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    // Create SubLine object for testing
    private SubLine subLine;

    // Initialize SubLine object before each test method
    @Before
    public void setUp() {
        // Create a Line object for the underlying hyperplane
        Line line = new Line(new Vector2D(1, 1), new Vector2D(2, 2));

        // Create an IntervalsSet object for the remaining region
        IntervalsSet intervalsSet = new IntervalsSet(0, 2);

        // Create the SubLine object
        subLine = new SubLine(line, intervalsSet);
    }

    // Test getSegments() method
    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();

        assertEquals(1, segments.size());

        Segment segment = segments.get(0);
        assertEquals(new Vector2D(1, 1), segment.getStart());
        assertEquals(new Vector2D(2, 2), segment.getEnd());
    }

    // Test intersection() method with includeEndPoints set to true
    @Test
    public void testIntersectionWithIncludeEndPoints() {
        // Create another SubLine object for testing
        SubLine otherSubLine = new SubLine(new Line(new Vector2D(1, 0), new Vector2D(3, 0)),
                               new IntervalsSet(0, 2));

        Vector2D intersectionPoint = subLine.intersection(otherSubLine, true);

        assertEquals(new Vector2D(1, 1), intersectionPoint);
    }

    // Test intersection() method with includeEndPoints set to false
    @Test
    public void testIntersectionWithoutIncludeEndPoints() {
        // Create another SubLine object for testing
        SubLine otherSubLine = new SubLine(new Line(new Vector2D(1, 0), new Vector2D(3, 0)),
                               new IntervalsSet(0, 2));

        Vector2D intersectionPoint = subLine.intersection(otherSubLine, false);

        assertNull(intersectionPoint);
    }

    // Test buildIntervalSet() method
    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(1, 1);
        Vector2D end = new Vector2D(2, 2);

        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);

        assertTrue(intervalsSet.contains(1));
        assertTrue(intervalsSet.contains(2));
        assertFalse(intervalsSet.contains(0));
    }

    // Test buildNew() method
    @Test
    public void testBuildNew() {
        Region<Euclidean1D> remainingRegion = new IntervalsSet(0, 1);
        SubLine newSubLine = subLine.buildNew(subLine.getHyperplane(), remainingRegion);

        assertEquals(subLine.getHyperplane(), newSubLine.getHyperplane());
        assertEquals(remainingRegion, newSubLine.getRemainingRegion());
    }

    // Test side() method
    @Test
    public void testSide() {
        Line otherLine = new Line(new Vector2D(0, 1), new Vector2D(2, 3));

        Side side = subLine.side(otherLine);

        assertEquals(Side.HYPER, side);
    }

    // Test split() method
    @Test
    public void testSplit() {
        Line otherLine = new Line(new Vector2D(0, 1), new Vector2D(2, 3));

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(otherLine);

        SubLine plus = (SubLine) splitSubHyperplane.getPlus();
        SubLine minus = (SubLine) splitSubHyperplane.getMinus();

        assertEquals(subLine.getHyperplane(), plus.getHyperplane());
        assertEquals(subLine.getHyperplane(), minus.getHyperplane());
        assertTrue(plus.getRemainingRegion().isEmpty());
        assertTrue(minus.getRemainingRegion().isEmpty());
    }

}