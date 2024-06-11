import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.commons.math3.geometry.partitioning.Region;
import org.apache.commons.math3.geometry.partitioning.Side;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    private SubLine subLine;

    @Before
    public void setup(){
        subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
    }

    @Test
    public void testGetSegments(){
        // Test if both segments are correctly returned
        assertEquals(2, subLine.getSegments().size());

        // Test if the start and end points of both segments are correct
        assertEquals(0, subLine.getSegments().get(0).getStart().getX(), 0.0001);
        assertEquals(0, subLine.getSegments().get(0).getStart().getY(), 0.0001);
        assertEquals(1, subLine.getSegments().get(0).getEnd().getX(), 0.0001);
        assertEquals(1, subLine.getSegments().get(0).getEnd().getY(), 0.0001);
        assertEquals(1, subLine.getSegments().get(1).getStart().getX(), 0.0001);
        assertEquals(1, subLine.getSegments().get(1).getStart().getY(), 0.0001);
        assertEquals(0, subLine.getSegments().get(1).getEnd().getX(), 0.0001);
        assertEquals(0, subLine.getSegments().get(1).getEnd().getY(), 0.0001);
    }

    @Test
    public void testIntersection(){
        // Test intersection when includeEndPoints is true
        Vector2D intersection1 = subLine.intersection(new SubLine(new Vector2D(0.5, 0.5), new Vector2D(2, 2)), true);
        assertEquals(0.5, intersection1.getX(), 0.0001);
        assertEquals(0.5, intersection1.getY(), 0.0001);
        Vector2D intersection2 = subLine.intersection(new SubLine(new Vector2D(0.5, 0.5), new Vector2D(1, 1)), true);
        assertEquals(0.5, intersection2.getX(), 0.0001);
        assertEquals(0.5, intersection2.getY(), 0.0001);
        assertNull(subLine.intersection(new SubLine(new Vector2D(1.5, 1.5), new Vector2D(2, 2)), true));

        // Test intersection when includeEndPoints is false
        assertNull(subLine.intersection(new SubLine(new Vector2D(0, 0), new Vector2D(0.5, 0.5)), false));
        assertNull(subLine.intersection(new SubLine(new Vector2D(0.5, 0.5), new Vector2D(1, 1)), false));
        assertNull(subLine.intersection(new SubLine(new Vector2D(1, 1), new Vector2D(1.5, 1.5)), false));
    }

    @Test
    public void testBuildIntervalSet(){
        // Test if the interval set is correctly built
        SubLine subLine = new SubLine(new Vector2D(0, 0), new Vector2D(1, 1));
        Region<Euclidean1D> intervalSet = subLine.buildIntervalSet(new Vector2D(0, 0), new Vector2D(1, 1));
        assertEquals(0, intervalSet.getSize(), 0.00001);
        assertEquals(0, intervalSet.asList().get(0).getInf(), 0.00001);
        assertEquals(1, intervalSet.asList().get(0).getSup(), 0.00001);
    }

    @Test
    public void testSplit(){
        // Test the split of the sub-line
        Hyperplane<Euclidean2D,Object> hyperplane = new Line(new Vector2D(0.5, 0), new Vector2D(0.5, 1));
        SplitSubHyperplane<Euclidean2D> splitSubHyperplane = subLine.split(hyperplane);

        // Test if the plus region of the split sub-hyperplane is correct
        assertEquals(0, splitSubHyperplane.getPlus().getRemainingRegion().getSize(), 0.00001);
        assertEquals(0.5, splitSubHyperplane.getPlus().getRemainingRegion().asList().get(0).getInf(), 0.00001);
        assertEquals(1, splitSubHyperplane.getPlus().getRemainingRegion().asList().get(0).getSup(), 0.00001);

        // Test if the minus region of the split sub-hyperplane is correct
        assertEquals(0.5, splitSubHyperplane.getMinus().getRemainingRegion().getSize(), 0.00001);
        assertEquals(0, splitSubHyperplane.getMinus().getRemainingRegion().asList().get(0).getInf(), 0.00001);
        assertEquals(0.5, splitSubHyperplane.getMinus().getRemainingRegion().asList().get(0).getSup(), 0.00001);
    }

    @Test
    public void testSide(){
        // Test side of hyperplane when sub-line is on the minus side of hyperplane
        Line hyperplane1 = new Line(new Vector2D(0, 0), new Vector2D(1, 0));
        assertEquals(Side.MINUS, subLine.side(hyperplane1));

        // Test side of hyperplane when sub-line is on the plus side of hyperplane
        Line hyperplane2 = new Line(new Vector2D(0, 0), new Vector2D(0, 1));
        assertEquals(Side.PLUS, subLine.side(hyperplane2));

        // Test side of hyperplane when sub-line intersects with hyperplane
        Line hyperplane3 = new Line(new Vector2D(0, 0), new Vector2D(1, 1));
        assertEquals(Side.HYPER, subLine.side(hyperplane3));
    }

    @Test
    public void testBuildNewSubLine(){
        // Test creation of a new sub-line using buildNew method
        Hyperplane<Euclidean2D,Object> hyperplane = new Line(new Vector2D(0.5, 0), new Vector2D(0.5, 1));
        Region<Euclidean1D> region = subLine.getRemainingRegion().split(new OrientedPoint(new Vector1D(0.5), false));

        SubLine newSubLine = (SubLine) subLine.buildNew(hyperplane, region);
        assertEquals(0, newSubLine.getSegments().size());

    }

}