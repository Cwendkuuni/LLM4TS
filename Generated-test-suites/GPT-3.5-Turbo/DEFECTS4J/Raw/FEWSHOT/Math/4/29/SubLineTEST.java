import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SubLineTest {
    @Test
    public void testConstructor1() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Line line = (Line) subLine.getHyperplane();
        IntervalsSet remainingRegion = (IntervalsSet) subLine.getRemainingRegion();
        
        assertEquals(start, line.toSpace(new Vector1D(remainingRegion.asList().get(0).getInf())));
        assertEquals(end, line.toSpace(new Vector1D(remainingRegion.asList().get(0).getSup())));
    }
    
    @Test
    public void testConstructor2() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        Segment segment = new Segment(start, end, new Line(start, end));
        SubLine subLine = new SubLine(segment);
        Line line = (Line) subLine.getHyperplane();
        IntervalsSet remainingRegion = (IntervalsSet) subLine.getRemainingRegion();
        
        assertEquals(start, line.toSpace(new Vector1D(remainingRegion.asList().get(0).getInf())));
        assertEquals(end, line.toSpace(new Vector1D(remainingRegion.asList().get(0).getSup())));
    }
    
    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        List<Segment> segments = subLine.getSegments();
        
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
        assertEquals(new Line(start, end), segments.get(0).getLine());
    }
    
    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
        
        intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
    }
    
    @Test
    public void testSide() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        assertEquals(Side.HYPER, subLine1.side(subLine1));
        assertEquals(Side.HYPER, subLine1.side(subLine2));
    }
    
    @Test
    public void testSplit() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);
        SubLine subLine2 = new SubLine(start2, end2);
        
        SplitSubHyperplane<Euclidean2D> split = subLine1.split(subLine2);
        SubLine subLinePlus = (SubLine) split.getPlus();
        SubLine subLineMinus = (SubLine) split.getMinus();
        
        assertEquals(start1, ((Line) subLinePlus.getHyperplane()).toSpace(new Vector1D(((IntervalsSet) subLinePlus.getRemainingRegion()).asList().get(0).getInf())));
        assertEquals(new Vector2D(0.5, 0.5), ((Line) subLinePlus.getHyperplane()).toSpace(new Vector1D(((IntervalsSet) subLinePlus.getRemainingRegion()).asList().get(0).getSup())));
        assertEquals(new Vector2D(0.5, 0.5), ((Line) subLineMinus.getHyperplane()).toSpace(new Vector1D(((IntervalsSet) subLineMinus.getRemainingRegion()).asList().get(0).getInf())));
        assertEquals(end1, ((Line) subLineMinus.getHyperplane()).toSpace(new Vector1D(((IntervalsSet) subLineMinus.getRemainingRegion()).asList().get(0).getSup())));
    }
}