import org.junit.*;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int result = csu.computeLeftMargin();

        assertEquals(20, result);
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int result = csu.computeRightMargin();

        assertEquals(15, result);
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int result = csu.computeTopMargin();

        assertEquals(15, result);
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int result = csu.computeBottomMargin();

        assertEquals(20, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int result = csu.computeXAxisLabelHeight();

        assertEquals(0, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);

        int result = csu.computeYAxisLabelWidth();

        assertEquals(0, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // TODO: Implement test
    }

    @Test
    public void testDrawXAxisTicks() {
        // TODO: Implement test
    }

    @Test
    public void testDrawYAxisTicks() {
        // TODO: Implement test
    }

    @Test
    public void testGetXAxisLine2D() {
        // TODO: Implement test
    }

    @Test
    public void testGetYAxisLine2D() {
        // TODO: Implement test
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // TODO: Implement test
    }
}