import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoordSystemUtilitiesTest {
    private CoordSystemUtilities coordSystemUtil;

    @Before
    public void setUp() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        coordSystemUtil = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        int result = coordSystemUtil.computeLeftMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeRightMargin() {
        int result = coordSystemUtil.computeRightMargin();
        assertEquals(15, result);
    }

    @Test
    public void testComputeTopMargin() {
        int result = coordSystemUtil.computeTopMargin();
        assertEquals(15, result);
    }

    @Test
    public void testComputeBottomMargin() {
        int result = coordSystemUtil.computeBottomMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int result = coordSystemUtil.computeXAxisLabelHeight();
        assertEquals(0, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int result = coordSystemUtil.computeYAxisLabelWidth();
        assertEquals(0, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // implement test case
    }

    @Test
    public void testDrawXAxisTicks() {
        // implement test case
    }

    @Test
    public void testDrawYAxisTicks() {
        // implement test case
    }

    @Test
    public void testGetXAxisLine2D() {
        // implement test case
    }

    @Test
    public void testGetYAxisLine2D() {
        // implement test case
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // implement test case
    }
}