// import necessary packages
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.*;

public class SubLineTest {

    // instance variables
    private SubLine subLine;
    private Line line;
    private Vector2D start;
    private Vector2D end;
    private Region<Euclidean1D> remainingRegion;

    @Before
    public void setUp() {
        start = new Vector2D(0, 0);
        end = new Vector2D(3, 4);
        line = new Line(start, end);
        remainingRegion = new IntervalsSet(0, 1);
        subLine = new SubLine(line, remainingRegion);
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());

        Segment segment = segments.get(0);
        assertEquals(start, segment.getStart());
        assertEquals(end, segment.getEnd());
        assertEquals(line, segment.getLine());
    }

    @Test
    public void testIntersection() {
        SubLine otherSubLine = new SubLine(line, new IntervalsSet(1, 2));

        Vector2D intersectionPoint = subLine.intersection(otherSubLine, true);
        assertEquals(new Vector2D(1, 1.333), intersectionPoint);
    }

    @Test
    public void testBuildIntervalSet() {
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assertEquals(new IntervalsSet(0, 1), intervalSet);
    }

    @Test
    public void testSide() {
        Line otherLine = new Line(new Vector2D(1, 0), new Vector2D(1, 5));

        Side side = subLine.side(otherLine);
        assertEquals(Side.PLUS, side);
    }

    @Test
    public void testSplit() {
        Line otherLine = new Line(new Vector2D(2, 0), new Vector2D(2, 5));

        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(otherLine);
        SubLine plus = (SubLine) splitSubHyperplane.getPlus();
        SubLine minus = (SubLine) splitSubHyperplane.getMinus();

        assertEquals(new IntervalsSet(0, 1), plus.getRemainingRegion());
        assertEquals(new IntervalsSet(1, 2), minus.getRemainingRegion());
    }

}