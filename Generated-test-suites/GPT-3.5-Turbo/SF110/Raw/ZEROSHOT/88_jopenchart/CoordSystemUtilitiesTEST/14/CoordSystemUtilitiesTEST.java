import org.junit.Test;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {

    CoordSystemUtilities csu;

    @Test
    public void testComputeLeftMargin() {
        csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeLeftMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeRightMargin() {
        csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeRightMargin();
        assertEquals(15, result);
    }

     @Test
    public void testComputeTopMargin() {
        csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeTopMargin();
        assertEquals(15, result);
    }

    @Test
    public void testComputeBottomMargin() {
        csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeBottomMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeXAxisLabelHeight();
        assertEquals(0, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeYAxisLabelWidth();
        assertEquals(0, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        csu = new CoordSystemUtilities(null, null, null, null);
        csu.drawNumericalXAxisTicks(null);
        // TODO: Add Assertion
    }

    @Test
    public void testDrawXAxisTicks() {
        csu = new CoordSystemUtilities(null, null, null, null);
        csu.drawXAxisTicks(null);
        // TODO: Add Assertion
    }

    @Test
    public void testDrawYAxisTicks() {
        csu = new CoordSystemUtilities(null, null, null, null);
        csu.drawYAxisTicks(null);
        // TODO: Add Assertion
    }

    @Test
    public void testGetXAxisLine2D() {
        csu = new CoordSystemUtilities(null, null, null, null);
        Line2D result = csu.getXAxisLine2D();
        // TODO: Add Assertion
    }

    @Test
    public void testGetYAxisLine2D() {
        csu = new CoordSystemUtilities(null, null, null, null);
        Line2D result = csu.getYAxisLine2D();
        // TODO: Add Assertion
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        csu = new CoordSystemUtilities(null, null, null, null);
        Line2D result = csu.getSecondYAxisLine2D();
        // TODO: Add Assertion
    }
}