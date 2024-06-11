import static org.junit.Assert.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities coordSystemUtilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        
        assertEquals(20, coordSystemUtilities.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities coordSystemUtilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        
        assertEquals(15, coordSystemUtilities.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities coordSystemUtilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        
        assertEquals(15, coordSystemUtilities.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        CoordSystemUtilities coordSystemUtilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        
        assertEquals(20, coordSystemUtilities.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities coordSystemUtilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        
        assertEquals(0, coordSystemUtilities.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities coordSystemUtilities = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        
        assertEquals(0, coordSystemUtilities.computeYAxisLabelWidth());
    }

    // Additional test cases for other methods should be added here

}