import org.apache.commons.math3.geometry.euclidean.oned.*;
import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.apache.commons.math3.geometry.partitioning.*;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    // Test constructor
    @Test
    public void testConstructor() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, region);
        Assert.assertEquals(line, subLine.getHyperplane());
        Assert.assertEquals(region, subLine.getRemainingRegion());
    }

    // Test getSegments method
    @Test
    public void testGetSegments() {
        Line line = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine = new SubLine(line, region);
        
        List<Segment> segments = subLine.getSegments();
        
        Assert.assertEquals(1, segments.size());
        Assert.assertEquals(new Vector2D(0, 0), segments.get(0).getStart());
        Assert.assertEquals(new Vector2D(1, 1), segments.get(0).getEnd());
        Assert.assertEquals(line, segments.get(0).getLine());
    }

    // Test intersection method
    @Test
    public void testIntersection_IncludeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        Region<Euclidean1D> region1 = new IntervalsSet(0, 1);
        Region<Euclidean1D> region2 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, region1);
        SubLine subLine2 = new SubLine(line2, region2);
        
        Vector2D intersection = subLine1.intersection(subLine2, true);
        
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    @Test
    public void testIntersection_ExcludeEndPoints() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        Region<Euclidean1D> region1 = new IntervalsSet(0, 1);
        Region<Euclidean1D> region2 = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, region1);
        SubLine subLine2 = new SubLine(line2, region2);
        
        Vector2D intersection = subLine1.intersection(subLine2, false);
        
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersection);
    }

    // Test side method
    @Test
    public void testSide() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, region);
        SubLine subLine2 = new SubLine(line2, region);
        
        Side side1 = subLine1.side(subLine2);
        Side side2 = subLine2.side(subLine1);
        
        Assert.assertEquals(Side.PLUS, side1);
        Assert.assertEquals(Side.PLUS, side2);
    }

    // Test split method
    @Test
    public void testSplit() {
        Line line1 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        Line line2 = new Line(new Vector2D(0, 1), new Vector2D(1, 0));
        Region<Euclidean1D> region = new IntervalsSet(0, 1);
        SubLine subLine1 = new SubLine(line1, region);
        SubLine subLine2 = new SubLine(line2, region);
        
        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(subLine2);
        SplitSubHyperplane<Euclidean2D> split2 = subLine2.split(subLine1);
        
        Assert.assertEquals(subLine1, split1.getPlus());
        Assert.assertEquals(subLine2, split1.getMinus());
        Assert.assertEquals(subLine1, split2.getMinus());
        Assert.assertEquals(subLine2, split2.getPlus());
    }

}