import org.apache.commons.math3.geometry.euclidean.oned.Vector1D;
import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.Segment;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.partitioning.Region.Location;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SubLineTest {

    @Test
    public void testSubLineConstructor() {
        Line line = new Line(new Vector1D(0.0), new Vector1D(1.0));
        SubLine subLine = new SubLine(line, line.wholeHyperplane());
        assertEquals(line, subLine.getHyperplane());
    }

    @Test
    public void testIntersectionWithIncludedEndPoints() {
        SubLine subLine1 = new SubLine(new Vector1D(1.0), new Vector1D(3.0));
        SubLine subLine2 = new SubLine(new Vector1D(2.0), new Vector1D(4.0));
        assertEquals(new Vector1D(2.0), subLine1.intersection(subLine2, true));
    }

    @Test
    public void testIntersectionWithExcludedEndPoints() {
        SubLine subLine1 = new SubLine(new Vector1D(1.0), new Vector1D(3.0));
        SubLine subLine2 = new SubLine(new Vector1D(3.0), new Vector1D(5.0));
        assertNull(subLine1.intersection(subLine2, false));
    }

    @Test
    public void testGetSegmentsEmpty() {
        SubLine subLine = new SubLine(new Vector1D(1.0), new Vector1D(1.0));
        assertEquals(0, subLine.getSegments().size());
    }

    @Test
    public void testGetSegmentsUnbounded() {
        SubLine subLine = new SubLine(new Vector1D(Double.NEGATIVE_INFINITY), new Vector1D(Double.POSITIVE_INFINITY));
        assertEquals(1, subLine.getSegments().size());
    }

    @Test
    public void testSide() {
        SubLine subLine1 = new SubLine(new Vector1D(1.0), new Vector1D(3.0));
        SubLine subLine2 = new SubLine(new Vector1D(2.0), new Vector1D(4.0));
        assertEquals(Location.INSIDE, subLine1.side(subLine2.getHyperplane()));
    }

    @Test
    public void testSplit() {
        SubLine subLine1 = new SubLine(new Vector1D(1.0), new Vector1D(3.0));
        SubLine subLine2 = new SubLine(new Vector1D(2.0), new Vector1D(4.0));
        assertEquals(2, subLine1.split(subLine2.getHyperplane()).getPlus().getSegments().size());
        assertEquals(1, subLine1.split(subLine2.getHyperplane()).getMinus().getSegments().size());
    }
}