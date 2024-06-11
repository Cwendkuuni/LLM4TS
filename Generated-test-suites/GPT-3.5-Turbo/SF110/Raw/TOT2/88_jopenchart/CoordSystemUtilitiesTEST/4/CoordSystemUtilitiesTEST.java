import de.progra.charting.model.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = util.computeLeftMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = util.computeRightMargin();
        assertEquals(15, result);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = util.computeTopMargin();
        assertEquals(15, result);
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = util.computeBottomMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = util.computeXAxisLabelHeight();
        assertEquals(0, result); // edge case where heights are not calculated properly
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = util.computeYAxisLabelWidth();
        assertEquals(0, result); // edge case where widths are not calculated properly
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        // test case where Graphics2D object is null
        util.drawNumericalXAxisTicks(null);
    }

    @Test
    public void testDrawXAxisTicks() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        // test case where Graphics2D object is null
        util.drawXAxisTicks(null);
    }

    @Test
    public void testDrawYAxisTicks() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        // test case where Graphics2D object is null
        util.drawYAxisTicks(null);
    }

    @Test
    public void testGetXAxisLine2D() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        // test case where constraints are null
        util.getXAxisLine2D();
    }

    @Test
    public void testGetYAxisLine2D() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        // test case where constraints are null
        util.getYAxisLine2D();
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        // test case where constraints2 are null
        util.getSecondYAxisLine2D();
    }

}