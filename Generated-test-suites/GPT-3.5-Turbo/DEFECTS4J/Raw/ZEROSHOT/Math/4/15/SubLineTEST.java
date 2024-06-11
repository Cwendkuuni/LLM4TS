import org.junit.Test;

public class SubLineTest {

    @Test
    public void testBuildIntervalSet() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        IntervalsSet intervalSet = SubLine.buildIntervalSet(start, end);
        assert intervalSet.getInf() == 0.0;
        assert intervalSet.getSup() == 1.0;
    }
    
    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        assert segments.size() == 1;
        assert segments.get(0).getStart().equals(start);
        assert segments.get(0).getEnd().equals(end);
    }
    
    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assert intersection.getX() == 0.5;
        assert intersection.getY() == 0.5;
    }

    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Hyperplane<Euclidean2D> hyperplane2 = (Hyperplane<Euclidean2D>) subLine2.getHyperplane();
        assert subLine1.side(hyperplane2) == Side.HYPER;
    }
    
    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);
        Hyperplane<Euclidean2D> hyperplane2 = (Hyperplane<Euclidean2D>) subLine2.getHyperplane();
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(hyperplane2);
        SubLine plus = (SubLine) split.getPlus();
        SubLine minus = (SubLine) split.getMinus();
        assert plus.getSegments().size() == 0;
        assert minus.getSegments().size() == 1;
        assert minus.getSegments().get(0).getStart().equals(start1);
        assert minus.getSegments().get(0).getEnd().equals(end1);
    }
}