import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.euclidean.oned.*;

public class SubLineTest {

    private SubLine subLine;
    private Line line;
    private Vector2D start;
    private Vector2D end;
    
    @Before
    public void setUp() {
        start = new Vector2D(1, 1);
        end = new Vector2D(3, 3);
        line = new Line(start, end);
        subLine = new SubLine(start, end);
    }
    
    @Test
    public void testConstructor1() {
        assertTrue(subLine.getHyperplane().equals(line));
        assertEquals(subLine.getRemainingRegion().toString(), "[1.0, 3.0]");
    }
    
    @Test
    public void testConstructor2() {
        Segment segment = new Segment(start, end, line);
        SubLine subLine2 = new SubLine(segment);
        assertTrue(subLine2.getHyperplane().equals(line));
        assertEquals(subLine2.getRemainingRegion().toString(), "[1.0, 3.0]");
    }
    
    @Test
    public void testGetSegments() {
        Segment segment = new Segment(start, end, line);
        assertTrue(subLine.getSegments().contains(segment));
    }
    
    @Test
    public void testIntersection() {
        Line line2 = new Line(new Vector2D(2, 1), new Vector2D(2, 3));
        SubLine subLine2 = new SubLine(new Vector2D(2, 1), new Vector2D(2, 3));
        assertNotNull(subLine.intersection(subLine2, true));
        assertNull(subLine.intersection(subLine2, false));
        assertNull(subLine.intersection(line2, true));
        assertNull(subLine.intersection(line2, false));
    }
    
    @Test
    public void testBuildIntervalSet() {
        IntervalsSet set = new IntervalsSet(1, 3);
        assertEquals(SubLine.buildIntervalSet(start, end).toString(), set.toString());
    }
    
    @Test
    public void testSide() {
        Line line2 = new Line(new Vector2D(2, 1), new Vector2D(2, 3));
        assertEquals(subLine.side(line2), SubHyperplane.Side.MINUS);
    }
    
    @Test
    public void testSplit() {
        Line line2 = new Line(new Vector2D(2, 1), new Vector2D(2, 3));
        SplitSubHyperplane<Euclidean2D> split = subLine.split(line2);
        assertEquals(split.getPlus().getRemainingRegion().toString(), "(([-Infinity, 1.0]) ∪ ((3.0, Infinity)))");
        assertEquals(split.getMinus().getRemainingRegion().toString(), "([1.0, 3.0])");
    }
}