package de.progra.charting;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CoordSystemUtilitiesTest {
    private CoordSystemUtilities coordSystemUtilities;

    @Before
    public void setup() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
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

    // Add more test cases for other methods in the CoordSystemUtilities class

    @Test
    public void testDrawNumericalXAxisTicks() {
        // Implement test case
    }

    @Test
    public void testDrawXAxisTicks() {
        // Implement test case
    }

    @Test
    public void testDrawYAxisTicks() {
        // Implement test case
    }

    // Add more test cases for other methods in the CoordSystemUtilities class
}