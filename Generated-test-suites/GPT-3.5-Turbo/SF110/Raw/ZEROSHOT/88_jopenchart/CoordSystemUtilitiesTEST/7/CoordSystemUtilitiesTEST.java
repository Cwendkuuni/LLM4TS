import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoordSystemUtilitiesTest {
    
    CoordSystemUtilities coordSystemUtilities;
    CoordSystem coord;
    ChartDataModelConstraints constraints;
    ChartDataModelConstraints constraints2;
    ChartDataModel model;
    
    @Before
    public void setUp() {
        coord = new CoordSystem();
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        model = new ChartDataModel();
        
        coordSystemUtilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }
    
    @Test
    public void testComputeLeftMargin() {
        int result = coordSystemUtilities.computeLeftMargin();
        assertEquals(20, result);
    }
    
    @Test
    public void testComputeRightMargin() {
        int result = coordSystemUtilities.computeRightMargin();
        assertEquals(15, result);
    }
    
    @Test
    public void testComputeTopMargin() {
        int result = coordSystemUtilities.computeTopMargin();
        assertEquals(15, result);
    }
    
    @Test
    public void testComputeBottomMargin() {
        int result = coordSystemUtilities.computeBottomMargin();
        assertEquals(20, result);
    }
    
    @Test
    public void testComputeXAxisLabelHeight() {
        int result = coordSystemUtilities.computeXAxisLabelHeight();
        assertEquals(0, result);
    }
    
    @Test
    public void testComputeYAxisLabelWidth() {
        int result = coordSystemUtilities.computeYAxisLabelWidth();
        assertEquals(0, result);
    }
    
    @Test
    public void testDrawNumericalXAxisTicks() {
        // Unable to test graphics methods
    }
    
    @Test
    public void testDrawXAxisTicks() {
        // Unable to test graphics methods
    }
    
    @Test
    public void testDrawYAxisTicks() {
        // Unable to test graphics methods
    }
    
    @Test
    public void testGetXAxisLine2D() {
        Line2D result = coordSystemUtilities.getXAxisLine2D();
        assertNotNull(result);
    }
    
    @Test
    public void testGetYAxisLine2D() {
        Line2D result = coordSystemUtilities.getYAxisLine2D();
        assertNotNull(result);
    }
    
    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D result = coordSystemUtilities.getSecondYAxisLine2D();
        assertNotNull(result);
    }
}