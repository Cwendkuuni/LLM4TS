import static org.junit.Assert.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystem c = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, constraints2, model);
        
        // typical use case
        int result1 = util.computeLeftMargin();
        assertEquals(20, result1);
        
        // edge case
        constraints.setMaximumColumnValue(10.0);
        constraints.setMinimumColumnValue(-10.0);
        int result2 = util.computeLeftMargin();
        assertEquals(20, result2);
    }
    
    @Test
    public void testComputeRightMargin() {
        CoordSystem c = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, constraints2, model);
        
        // typical use case
        int result1 = util.computeRightMargin();
        assertEquals(15, result1);
        
        // edge case
        c.setXAxisUnit("x");
        int result2 = util.computeRightMargin();
        assertEquals(18, result2);
    }
    
    @Test
    public void testComputeTopMargin() {
        CoordSystem c = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, constraints2, model);
        
        // typical use case
        int result1 = util.computeTopMargin();
        assertEquals(15, result1);
        
        // edge case
        c.setYAxisUnit("y");
        int result2 = util.computeTopMargin();
        assertEquals(18, result2);
    }
    
    @Test
    public void testComputeBottomMargin() {
        CoordSystem c = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities util = new CoordSystemUtilities(c, constraints, constraints2, model);
        
        // typical use case
        int result1 = util.computeBottomMargin();
        assertEquals(20, result1);
        
        // edge case
        constraints.setMaximumValue(10.0);
        constraints.setMinimumValue(-10.0);
        int result2 = util.computeBottomMargin();
        assertEquals(20, result2);
    }
    
    // More test cases can be added for the remaining methods

}