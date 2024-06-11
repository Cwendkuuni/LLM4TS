import org.junit.Test;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {

    // Test computeLeftMargin method
    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int result = utils.computeLeftMargin();
        assertEquals(20, result);
    }

    // Test computeRightMargin method
    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int result = utils.computeRightMargin();
        assertEquals(15, result);
    }

    // Test computeTopMargin method
    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int result = utils.computeTopMargin();
        assertEquals(15, result);
    }

    // Test computeBottomMargin method
    @Test
    public void testComputeBottomMargin() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int result = utils.computeBottomMargin();
        assertEquals(20, result);
    }

    // Test computeXAxisLabelHeight method
    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int result = utils.computeXAxisLabelHeight();
        assertEquals(0, result);
    }

    // Test computeYAxisLabelWidth method
    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        int result = utils.computeYAxisLabelWidth();
        assertEquals(0, result);
    }

    // Test drawNumericalXAxisTicks method
    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        utils.drawNumericalXAxisTicks(g);
    }

    // Test drawXAxisTicks method
    @Test
    public void testDrawXAxisTicks() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        utils.drawXAxisTicks(g);
    }

    // Test drawYAxisTicks method
    @Test
    public void testDrawYAxisTicks() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        utils.drawYAxisTicks(g);
    }
    
    // Test getXAxisLine2D method
    @Test
    public void testGetXAxisLine2D() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        Line2D result = utils.getXAxisLine2D();
        assertNotNull(result);
    }

    // Test getYAxisLine2D method
    @Test
    public void testGetYAxisLine2D() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        Line2D result = utils.getYAxisLine2D();
        assertNotNull(result);
    }

    // Test getSecondYAxisLine2D method
    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystemUtilities utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
        Line2D result = utils.getSecondYAxisLine2D();
        assertNotNull(result);
    }
}