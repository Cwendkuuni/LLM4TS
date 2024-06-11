import static org.junit.Assert.*;
import org.junit.Test;

public class CoordSystemUtilitiesTest {
    private CoordSystemUtilities coordSystemUtilities;
    
    @Before
    public void setUp() {
        // Creating a CoordSystemUtilities object with sample data for testing
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(0, 10);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(-10, 10);
        ChartDataModel model = new ChartDataModel();
        
        coordSystemUtilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }
    
    @Test
    public void testComputeLeftMargin() {
        int expectedResult = 20;
        int result = coordSystemUtilities.computeLeftMargin();
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testComputeRightMargin() {
        int expectedResult = 15;
        int result = coordSystemUtilities.computeRightMargin();
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testComputeTopMargin() {
        int expectedResult = 15;
        int result = coordSystemUtilities.computeTopMargin();
        assertEquals(expectedResult, result);
    }
    
    @Test
    public void testComputeBottomMargin() {
        int expectedResult = 20;
        int result = coordSystemUtilities.computeBottomMargin();
        assertEquals(expectedResult, result);
    }
    
    // More test cases for other methods can be added as needed
    
}