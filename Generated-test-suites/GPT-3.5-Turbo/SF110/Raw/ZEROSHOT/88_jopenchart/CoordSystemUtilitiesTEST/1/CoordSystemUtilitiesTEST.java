import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoordSystemUtilitiesTest {
  
    private CoordSystemUtilities coordSystemUtilities;
    
    @Before
    public void setUp() {
        CoordSystem c = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        
        coordSystemUtilities = new CoordSystemUtilities(c, constraints, constraints2, model);
    }
    
    @Test
    public void testComputeLeftMargin() {
        int expected = 20;
        int actual = coordSystemUtilities.computeLeftMargin();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeRightMargin() {
        int expected = 15;
        int actual = coordSystemUtilities.computeRightMargin();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeTopMargin() {
        int expected = 15;
        int actual = coordSystemUtilities.computeTopMargin();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeBottomMargin() {
        int expected = 20;
        int actual = coordSystemUtilities.computeBottomMargin();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeXAxisLabelHeight() {
        int expected = 0;
        int actual = coordSystemUtilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeYAxisLabelWidth() {
        int expected = 0;
        int actual = coordSystemUtilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDrawNumericalXAxisTicks() {
        // TODO: Write test for drawNumericalXAxisTicks()
    }
    
    @Test
    public void testDrawXAxisTicks() {
        // TODO: Write test for drawXAxisTicks()
    }
    
    @Test
    public void testDrawYAxisTicks() {
        // TODO: Write test for drawYAxisTicks()
    }
    
    @Test
    public void testGetXAxisLine2D() {
        // TODO: Write test for getXAxisLine2D()
    }
    
    @Test
    public void testGetYAxisLine2D() {
        // TODO: Write test for getYAxisLine2D()
    }
    
    @Test
    public void testGetSecondYAxisLine2D() {
        // TODO: Write test for getSecondYAxisLine2D()
    }
    
}