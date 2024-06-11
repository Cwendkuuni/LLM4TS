import static org.junit.Assert.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {
    
    CoordSystem c = new CoordSystem();
    ChartDataModelConstraints constraints = new ChartDataModelConstraints();
    ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
    ChartDataModel model = new ChartDataModel();
    CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, constraints2, model);
    
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
    
    // Add more test cases for the remaining methods
    
}