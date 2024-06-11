import org.junit.Test;
import org.junit.Assert;

public class SubLineTest {

    @Test
    public void testConstructorWithHyperplaneAndRegion() {
        // Test constructor with hyperplane and region
        // Create a Line hyperplane and a Region
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, region);

        Assert.assertEquals(line, subLine.getHyperplane());
        Assert.assertEquals(region, subLine.getRemainingRegion());
    }

    @Test
    public void testConstructorWithStartAndEnd() {
        // Test constructor with start and end points
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);

        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(start, segments.get(0).getStart());
        Assert.assertEquals(end, segments.get(0).getEnd());
    }

    @Test
    public void testConstructorWithSegment() {
        // Test constructor with a Segment
        Segment segment = new Segment(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine = new SubLine(segment);

        List<Segment> segments = subLine.getSegments();
        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(segment.getStart(), segments.get(0).getStart());
        Assert.assertEquals(segment.getEnd(), segments.get(0).getEnd());
    }

    @Test
    public void testGetSegments() {
        // Test getSegments method
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        List<Segment> segments = subLine.getSegments();

        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        Assert.assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        // Test intersection method
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));

        Vector2D intersectionPoint = subLine1.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersectionPoint);
    }

    @Test
    public void testBuildIntervalSet() {
        // Test buildIntervalSet method
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);

        Assert.assertEquals(0.0, intervalSet.getInf(), 1e-10);
        Assert.assertEquals(1.0, intervalSet.getSup(), 1e-10);
    }

}