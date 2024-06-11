import org.junit.Test;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {
    CoordSystemUtilities csu;
    
    @Test
    public void testComputeLeftMargin() {
        csu = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        assertEquals(20, csu.computeLeftMargin());
    }
    
    @Test
    public void testComputeRightMargin() {
        csu = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        assertEquals(15, csu.computeRightMargin());
    }
    
    @Test
    public void testComputeTopMargin() {
        csu = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        assertEquals(15, csu.computeTopMargin());
    }
    
    @Test
    public void testComputeBottomMargin() {
        csu = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        assertEquals(20, csu.computeBottomMargin());
    }

    // Add more test cases here for other methods in CoordSystemUtilities class
    
}