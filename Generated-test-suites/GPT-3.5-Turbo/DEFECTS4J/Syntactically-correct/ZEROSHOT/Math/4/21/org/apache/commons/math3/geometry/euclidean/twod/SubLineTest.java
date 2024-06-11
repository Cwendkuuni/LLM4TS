import org.junit.Test;
import static org.junit.Assert.*;

public class SubLineTest {

    @Test
    public void testConstructor() {

        Line line = new Line(new Vector2D(0,0), new Vector2D(1,1));
        Region<Euclidean1D> region = new IntervalsSet(0,1);
        SubLine subline = new SubLine(line, region);

        assertEquals(subline.getHyperplane(), line);
        assertEquals(subline.getRemainingRegion(), region);
    }

    @Test
    public void testGetSegments() {

        Line line = new Line(new Vector2D(0,0), new Vector2D(1,1));
        Region<Euclidean1D> region = new IntervalsSet(0,1);
        SubLine subline = new SubLine(line, region);

        List<Segment> segments = subline.getSegments();
        assertEquals(segments.size(), 1);

        Segment segment = segments.get(0);
        assertEquals(segment.getStart(), new Vector2D(0,0));
        assertEquals(segment.getEnd(), new Vector2D(1,1));
        assertEquals(segment.getLine(), line);

    }

    @Test
    public void testIntersection() {

        Line line1 = new Line(new Vector2D(0,0), new Vector2D(1,1));
        Line line2 = new Line(new Vector2D(1,0), new Vector2D(0,1));
        Region<Euclidean1D> region1 = new IntervalsSet(0,1);
        Region<Euclidean1D> region2 = new IntervalsSet(0,1);

        SubLine subline1 = new SubLine(line1, region1);
        SubLine subline2 = new SubLine(line2, region2);

        Vector2D intersection = subline1.intersection(subline2, true);
        assertEquals(intersection, new Vector2D(0.5, 0.5));

    }
}