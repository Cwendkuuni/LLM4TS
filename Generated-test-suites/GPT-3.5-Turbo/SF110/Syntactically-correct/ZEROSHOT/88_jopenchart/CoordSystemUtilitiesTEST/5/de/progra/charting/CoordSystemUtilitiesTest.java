import static org.junit.Assert.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, null);
        int result = util.computeLeftMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, null);
        int result = util.computeRightMargin();
        assertEquals(15, result);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, null);
        int result = util.computeTopMargin();
        assertEquals(15, result);
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, null);
        int result = util.computeBottomMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, null);
        int result = util.computeXAxisLabelHeight();
        assertEquals(0, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities util = new CoordSystemUtilities(null, null, null, null);
        int result = util.computeYAxisLabelWidth();
        assertEquals(0, result);
    }

    //Add more test methods for the remaining methods in the class

}