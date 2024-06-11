
package de.progra.charting;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities coordSystemUtilities;

    @Before
    public void setup() {
        CoordSystem coordSystem = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints(0, 10, 0, 100);
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints(0, 10, 0, 100);
        ChartDataModel model = new ChartDataModel();
        coordSystemUtilities = new CoordSystemUtilities(coordSystem, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        int expected = 24;
        int result = coordSystemUtilities.computeLeftMargin();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeRightMargin() {
        int expected = 29;
        int result = coordSystemUtilities.computeRightMargin();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeTopMargin() {
        int expected = 33;
        int result = coordSystemUtilities.computeTopMargin();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeBottomMargin() {
        int expected = 24;
        int result = coordSystemUtilities.computeBottomMargin();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        int expected = 10;
        int result = coordSystemUtilities.computeXAxisLabelHeight();
        assertEquals(expected, result);
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        int expected = 12;
        int result = coordSystemUtilities.computeYAxisLabelWidth();
        assertEquals(expected, result);
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // TODO: Implement test case
    }

    @Test
    public void testDrawXAxisTicks() {
        // TODO: Implement test case
    }

    @Test
    public void testDrawYAxisTicks() {
        // TODO: Implement test case
    }
    
    @Test
    public void testGetXAxisLine2D() {
        // TODO: Implement test case
    }
    
    @Test
    public void testGetYAxisLine2D() {
        // TODO: Implement test case
    }
    
    @Test
    public void testGetSecondYAxisLine2D() {
        // TODO: Implement test case
    }
}

