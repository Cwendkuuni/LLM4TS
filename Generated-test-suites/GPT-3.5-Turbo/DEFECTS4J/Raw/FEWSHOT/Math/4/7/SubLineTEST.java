import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.geometry.euclidean.oned.Euclidean1D;
import org.apache.commons.math3.geometry.euclidean.oned.Interval;
import org.apache.commons.math3.geometry.euclidean.oned.IntervalsSet;
import org.apache.commons.math3.geometry.euclidean.oned.OrientedPoint;
import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Euclidean2D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.BSPTree;
import org.apache.commons.math3.geometry.partitioning.Hyperplane;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.apache.commons.math3.geometry.partitioning.Side;
import org.junit.Before;
import org.junit.Test;

public class SubLineTest {
    
    private SubLine subLine;
    private Line line;
    private Vector2D start;
    private Vector2D end;
    
    @Before
    public void setUp() {
        start = new Vector2D(0, 0);
        end = new Vector2D(1, 1);
        line = new Line(start, end);
        subLine = new SubLine(line, new IntervalsSet(0, 1));
    }
    
    @Test
    public void testConstructor() {
        assertEquals(line, subLine.getHyperplane());
        assertEquals(new IntervalsSet(0, 1), subLine.getRemainingRegion());
    }
    
    @Test
    public void testGetSegments() {
        List<Segment> segments = subLine.getSegments();
        assertEquals(1, segments.size());
        assertEquals(start, segments.get(0).getStart());
        assertEquals(end, segments.get(0).getEnd());
        assertEquals(line, segments.get(0).getLine());
    }
    
    @Test
    public void testIntersection_includeEndPoints() {
        SubLine subLine2 = new SubLine(line, new IntervalsSet(0, 2));
        Vector2D intersection = subLine.intersection(subLine2, true);
        assertEquals(new Vector2D(0.5714285714285714, 0.5714285714285714), intersection);
        
        SubLine subLine3 = new SubLine(line, new IntervalsSet(1, 2));
        intersection = subLine.intersection(subLine3, true);
        assertNull(intersection);
        
        SubLine subLine4 = new SubLine(line, new IntervalsSet(0, 0.5));
        intersection = subLine.intersection(subLine4, true);
        assertNull(intersection);
    }
    
    @Test
    public void testIntersection_excludeEndPoints() {
        SubLine subLine2 = new SubLine(line, new IntervalsSet(0, 2));
        Vector2D intersection = subLine.intersection(subLine2, false);
        assertEquals(new Vector2D(0.5714285714285714, 0.5714285714285714), intersection);
        
        SubLine subLine3 = new SubLine(line, new IntervalsSet(1, 2));
        intersection = subLine.intersection(subLine3, false);
        assertNull(intersection);
        
        SubLine subLine4 = new SubLine(line, new IntervalsSet(0, 0.5));
        intersection = subLine.intersection(subLine4, false);
        assertNull(intersection);
    }
    
    @Test
    public void testBuildIntervalSet() {
        IntervalsSet intervalsSet = SubLine.buildIntervalSet(start, end);
        assertEquals(new IntervalsSet(0, 1), intervalsSet);
    }
    
    @Test
    public void testBuildNew() {
        Region<Euclidean1D> remainingRegion = new IntervalsSet(0.5, 1);
        SubLine newSubLine = subLine.buildNew(line, remainingRegion);
        assertEquals(line, newSubLine.getHyperplane());
        assertEquals(remainingRegion, newSubLine.getRemainingRegion());
    }
    
    @Test
    public void testSide() {
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        Side side = subLine.side(line2);
        assertEquals(Side.HYPER, side);
        
        Line line3 = new Line(new Vector2D(1, 0), new Vector2D(2, 0));
        side = subLine.side(line3);
        assertEquals(Side.MINUS, side);
        
        Line line4 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        side = subLine.side(line4);
        assertEquals(Side.PLUS, side);
    }
    
    @Test
    public void testSplit() {
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        SplitSubHyperplane<Euclidean2D> split = subLine.split(line2);
        assertNull(split.getMinus());
        assertNull(split.getPlus());
        
        Line line3 = new Line(new Vector2D(1, 0), new Vector2D(2, 0));
        split = subLine.split(line3);
        assertNull(split.getMinus());
        assertEquals(subLine, split.getPlus());
        
        Line line4 = new Line(new Vector2D(0, 1), new Vector2D(1, 2));
        split = subLine.split(line4);
        assertEquals(subLine, split.getMinus());
        assertNull(split.getPlus());
    }
}