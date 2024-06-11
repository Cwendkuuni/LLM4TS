
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

public class SubLineTest {

    @Test
    public void testConstructor() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Assert.assertNotNull(subLine);
    }
    
    @Test
    public void testGetSegments() {
        Vector2D start = new Vector2D(0, 0);
        Vector2D end = new Vector2D(1, 1);
        SubLine subLine = new SubLine(start, end);
        Assert.assertEquals(1, subLine.getSegments().size());
        Assert.assertEquals(start, subLine.getSegments().get(0).getStart());
        Assert.assertEquals(end, subLine.getSegments().get(0).getEnd());
    }

    @Test
    public void testIntersection() {
        Vector2D start1 = new Vector2D(0, 0);
        Vector2D end1 = new Vector2D(1, 1);

        Vector2D start2 = new Vector2D(0, 1);
        Vector2D end2 = new Vector2D(1, 0);

        SubLine subLine1 = new SubLine(start1, end1);
        SubLine subLine2 = new SubLine(start2, end2);

        Vector2D intersection = subLine1.intersection(subLine2, true);
        Assert.assertEquals(new Vector2D(0.5, 0.5), intersection);
    }
}

