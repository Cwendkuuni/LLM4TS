import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import de.progra.charting.ChartDataModelConstraints;
import de.progra.charting.CoordSystem;
import de.progra.charting.model.ChartDataModel;

public class CoordSystemUtilitiesTest {

    @Mock
    private CoordSystem coordSystem;

    private ChartDataModelConstraints constraints;

    private ChartDataModelConstraints constraints2;

    private ChartDataModel model;

    private CoordSystemUtilities coordSystemUtilities;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        constraints = new ChartDataModelConstraints(0, 10, 0, 5);
        constraints2 = new ChartDataModelConstraints(0, 10, 0, 5);
        model = new ChartDataModel();

        coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin_withPositiveXMinAndPositiveXMax() {
        int leftMargin = coordSystemUtilities.computeLeftMargin();
        assertEquals(20, leftMargin);
    }

    @Test
    public void testComputeLeftMargin_withNegativeXMinAndPositiveXMax() {
        constraints = new ChartDataModelConstraints(-10, 10, 0, 5);
        coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
        
        int leftMargin = coordSystemUtilities.computeLeftMargin();
        assertTrue(leftMargin >= 20);
    }

    @Test
    public void testComputeRightMargin() {
        int rightMargin = coordSystemUtilities.computeRightMargin();
        assertTrue(rightMargin >= 15);
    }

    @Test
    public void testComputeTopMargin() {
        int topMargin = coordSystemUtilities.computeTopMargin();
        assertTrue(topMargin >= 15);
    }

    @Test
    public void testComputeBottomMargin_withNegativeYMinAndPositiveYMax() {
        constraints = new ChartDataModelConstraints(0, 10, -5, 5);
        coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);

        int bottomMargin = coordSystemUtilities.computeBottomMargin();
        assertTrue(bottomMargin >= 20);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int xAxisLabelHeight = coordSystemUtilities.computeXAxisLabelHeight();
        assertTrue(xAxisLabelHeight >= 0);
    }

    @Test
    public void testComputeYAxisLabelWidth_withNegativeXMin() {
        constraints = new ChartDataModelConstraints(-10, 10, 0, 5);
        coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);

        int yAxisLabelWidth = coordSystemUtilities.computeYAxisLabelWidth();
        assertTrue(yAxisLabelWidth >= 0);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // To be implemented
        fail("Not yet implemented");
    }
    
    @Test
    public void testDrawXAxisTicks() {
        // To be implemented
        fail("Not yet implemented");
    }

    @Test
    public void testDrawYAxisTicks() {
        // To be implemented
        fail("Not yet implemented");
    }

    @Test
    public void testGetXAxisLine2D() {
        // To be implemented
        fail("Not yet implemented");
    }

    @Test
    public void testGetYAxisLine2D() {
        // To be implemented
        fail("Not yet implemented");
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // To be implemented
        fail("Not yet implemented");
    }
}