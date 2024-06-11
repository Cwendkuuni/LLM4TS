import static org.junit.Assert.*;
import org.junit.*;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities util;

    @Before
    public void setUp() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        util = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        int expected = 20;
        int result = util.computeLeftMargin();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRightMargin() {
        int expected = 15;
        int result = util.computeRightMargin();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        int expected = 15;
        int result = util.computeTopMargin();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        int expected = 20;
        int result = util.computeBottomMargin();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int expected = 0; // change expected value
        int result = util.computeXAxisLabelHeight();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int expected = 0; // change expected value
        int result = util.computeYAxisLabelWidth();
        assertEquals(expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // implementation for test
    }

    @Test
    public void testDrawXAxisTicks() {
        // implementation for test
    }

    @Test
    public void testDrawYAxisTicks() {
        // implementation for test
    }

    @Test
    public void testGetXAxisLine2D() {
        // implementation for test
    }

    @Test
    public void testGetYAxisLine2D() {
        // implementation for test
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // implementation for test
    }

}