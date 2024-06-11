import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities coordSystemUtils;

    @Before
    public void setUp() {
        coordSystemUtils = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        int expectedMargin = 20;
        int actualMargin = coordSystemUtils.computeLeftMargin();
        assertEquals(expectedMargin, actualMargin);
    }

    @Test
    public void testComputeRightMargin() {
        int expectedMargin = 15;
        int actualMargin = coordSystemUtils.computeRightMargin();
        assertEquals(expectedMargin, actualMargin);
    }

    @Test
    public void testComputeTopMargin() {
        int expectedMargin = 15;
        int actualMargin = coordSystemUtils.computeTopMargin();
        assertEquals(expectedMargin, actualMargin);
    }

    @Test
    public void testComputeBottomMargin() {
        int expectedMargin = 20;
        int actualMargin = coordSystemUtils.computeBottomMargin();
        assertEquals(expectedMargin, actualMargin);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int expectedHeight = 0;
        int actualHeight = coordSystemUtils.computeXAxisLabelHeight();
        assertEquals(expectedHeight, actualHeight);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int expectedWidth = 0;
        int actualWidth = coordSystemUtils.computeYAxisLabelWidth();
        assertEquals(expectedWidth, actualWidth);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Add test case
    }

    @Test
    public void testDrawXAxisTicks() {
        // Add test case
    }

    @Test
    public void testDrawYAxisTicks() {
        // Add test case
    }

    @Test
    public void testGetXAxisLine2D() {
        // Add test case
    }

    @Test
    public void testGetYAxisLine2D() {
        // Add test case
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // Add test case
    }
}