import static org.junit.Assert.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    CoordSystemUtilities utils;

    @Before
    public void setUp() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();

        utils = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        assertEquals(20, utils.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        assertEquals(15, utils.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        assertEquals(15, utils.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        assertEquals(20, utils.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        assertEquals(0, utils.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        assertEquals(0, utils.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Testing not implemented for this method
    }

    @Test
    public void testDrawXAxisTicks() {
        // Testing not implemented for this method
    }

    @Test
    public void testDrawYAxisTicks() {
        // Testing not implemented for this method
    }

    @Test
    public void testGetXAxisLine2D() {
        // Testing not implemented for this method
    }

    @Test
    public void testGetYAxisLine2D() {
        // Testing not implemented for this method
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // Testing not implemented for this method
    }
}