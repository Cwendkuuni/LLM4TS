import org.apache.commons.math3.geometry.euclidean.twod.*;
import org.junit.Assert;
import org.junit.Test;

public class SubLineTest {

    private static final double EPSILON = 1e-10;

    @Test
    public void testGetSegments() {
        // Test case 1: Sub-line with one segment
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Assert.assertEquals(1, subLine1.getSegments().size());
        
        Segment segment1 = subLine1.getSegments().get(0);
        Assert.assertEquals(start1, segment1.getStart());
        Assert.assertEquals(end1, segment1.getEnd());
        
        // Test case 2: Sub-line with multiple segments
        Vector2D start2 = new Vector2D(0, 0);
        Vector2D end2 = new Vector2D(2, 2);
        SubLine subLine2 = new SubLine(start2, end2);
        
        Assert.assertEquals(2, subLine2.getSegments().size());
        
        Segment segment21 = subLine2.getSegments().get(0);
        Assert.assertEquals(start2, segment21.getStart());
        Assert.assertEquals(new Vector2D(1, 1), segment21.getEnd());
        
        Segment segment22 = subLine2.getSegments().get(1);
        Assert.assertEquals(new Vector2D(1, 1), segment22.getStart());
        Assert.assertEquals(end2, segment22.getEnd());

        // Test case 3: Empty sub-line
        SubLine subLine3 = new SubLine(new Vector2D(1, 1), new Vector2D(1, 1));

        Assert.assertEquals(0, subLine3.getSegments().size());
    }

    @Test
    public void testIntersection() {
        // Test case 1: Sub-line intersecting with itself
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);
        SubLine subLine1 = new SubLine(start1, end1);
        
        Vector2D intersection1 = subLine1.intersection(subLine1, true);
        Assert.assertEquals(0.5, intersection1.getX(), EPSILON);
        Assert.assertEquals(0.5, intersection1.getY(), EPSILON);

        // Test case 2: Sub-lines intersecting with each other
        SubLine subLine21 = new SubLine(new Vector2D(1, 0), new Vector2D(0, 1));
        SubLine subLine22 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        
        Vector2D intersection2 = subLine21.intersection(subLine22, true);
        Assert.assertEquals(0.5, intersection2.getX(), EPSILON);
        Assert.assertEquals(0.5, intersection2.getY(), EPSILON);
        
        // Test case 3: Sub-lines not intersecting with each other
        SubLine subLine3 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 0));
        SubLine subLine4 = new SubLine(new Vector2D(0, 1), new Vector2D(1, 1));
        
        Vector2D intersection3 = subLine3.intersection(subLine4, true);
        Assert.assertNull(intersection3);
    }

    @Test
    public void testSide() {
        // Test case 1: Sub-lines are on same line
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(0, 0), new Vector2D(2, 2));
        
        Assert.assertEquals(Side.HYPER, subLine1.side(subLine2.getHyperplane()));
        
        // Test case 2: Sub-line is inside another sub-line
        SubLine subLine3 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine4 = new SubLine(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.75));
        
        Assert.assertEquals(Side.INSIDE, subLine3.side(subLine4.getHyperplane()));
        
        // Test case 3: Sub-line is outside another sub-line
        SubLine subLine5 = new SubLine(new Vector2D(0.25, 0.75), new Vector2D(0.75, 0.25));
        SubLine subLine6 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        
        Assert.assertEquals(Side.OUTSIDE, subLine5.side(subLine6.getHyperplane()));
    }

    @Test
    public void testSplit() {
        // Test case 1: Splitting with parallel sub-line
        SubLine subLine1 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine2 = new SubLine(new Vector2D(0, 1), new Vector2D(1, 2));
        
        SplitSubHyperplane<Euclidean2D> split1 = subLine1.split(subLine2.getHyperplane());
        Assert.assertNull(split1.getPlus());
        Assert.assertEquals(subLine1, split1.getMinus());
        
        // Test case 2: Splitting with intersecting sub-line
        SubLine subLine3 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine4 = new SubLine(new Vector2D(0, 1), new Vector2D(1, 0));
        
        SplitSubHyperplane<Euclidean2D> split2 = subLine3.split(subLine4.getHyperplane());
        Assert.assertEquals(subLine4, split2.getPlus());
        Assert.assertEquals(subLine3, split2.getMinus());
        
        // Test case 3: Splitting with sub-line inside another sub-line
        SubLine subLine5 = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        SubLine subLine6 = new SubLine(new Vector2D(0.25, 0.25), new Vector2D(0.75, 0.75));
        
        SplitSubHyperplane<Euclidean2D> split3 = subLine5.split(subLine6.getHyperplane());
        Assert.assertEquals(subLine6, split3.getPlus());
        Assert.assertEquals(subLine5, split3.getMinus());
    }

}