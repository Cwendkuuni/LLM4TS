import static org.junit.Assert.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        // Test typical use case
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = util.computeLeftMargin();
        assertEquals(20, result);
    }

    @Test
    public void testComputeRightMargin() {
        // Test typical use case
        CoordSystemUtilities util = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = util.computeRightMargin();
        assertEquals(15, result);
    }

    // Add more test cases for other methods

}