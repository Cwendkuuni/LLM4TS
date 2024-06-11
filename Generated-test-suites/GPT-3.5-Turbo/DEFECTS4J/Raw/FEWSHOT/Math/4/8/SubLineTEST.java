import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.junit.BeforeClass;
import org.junit.Test;

public class SubLineTest {
    private static final double EPSILON = 1e-10;
    private static SubLine subLine;
    
    @BeforeClass
    public static void setUp() {
        subLine = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
    }

    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(new Segment(new Vector2D(0, 0), new Vector2D(1, 1), new Line(new Vector2D(0, 0), new Vector2D(1, 1))), segments.get(0));
    }

    @Test
    public void testIntersection_IncludeEndPoints() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0, 1), new Vector2D(1, 0)), new IntervalsSet(0, 1));
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
        
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 0.5));
        
        Vector2D noIntersection = subLine1.intersection(subLine3, true);
        assertNull(noIntersection);
    }

    @Test
    public void testIntersection_NotIncludeEndPoints() {
        SubLine subLine1 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(new Line(new Vector2D(0, 1), new Vector2D(1, 0)), new IntervalsSet(0, 1));
        
        Vector2D intersection = subLine1.intersection(subLine2, false);
        assertEquals(new Vector2D(0.5, 0.5), intersection);
        
        SubLine subLine3 = new SubLine(new Line(new Vector2D(0, 0), new Vector2D(1, 1)), new IntervalsSet(0, 0.5));
        
        Vector2D noIntersection = subLine1.intersection(subLine3, false);
        assertNull(noIntersection);
    }

    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 1));
        
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(0, 1));
        
        assertEquals(Side.HYPER, subLine1.side(line2));
        assertEquals(Side.MINUS, subLine2.side(line1));
    }

    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 1));
        
        SubLine subLine1 = new SubLine(line1, new IntervalsSet(0, 1));
        SubLine subLine2 = new SubLine(line2, new IntervalsSet(0, 1));
        
        SplitSubHyperplane<Euclidean2D> splitSubLine = subLine1.split(line2);
        SubLine plus = (SubLine)splitSubLine.getPlus();
        SubLine minus = (SubLine)splitSubLine.getMinus();
        
        assertEquals(new SubLine(line1.copySelf(), new IntervalsSet(0, 0.5)), plus);
        assertTrue(plus.getRemainingRegion().isEmpty());
        assertEquals(new SubLine(line1.copySelf(), new IntervalsSet(0.5, 1)), minus);
        assertTrue(minus.getRemainingRegion().isEmpty());
    }

}