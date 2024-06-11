package de.progra.charting;

import de.progra.charting.model.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {

    @Test
    public void testComputeLeftMargin() {
        // Test typical case
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = utils.computeLeftMargin();
        assertEquals(20, result);

        // Test edge case
        // Add edge case test here
    }

    @Test
    public void testComputeRightMargin() {
        // Test typical case
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = utils.computeRightMargin();
        assertEquals(15, result);

        // Test edge case
        // Add edge case test here
    }

    @Test
    public void testComputeTopMargin() {
        // Test typical case
        CoordSystemUtilities utils = new CoordSystemUtilities(new CoordSystem(), new ChartDataModelConstraints(), new ChartDataModelConstraints(), new ChartDataModel());
        int result = utils.computeTopMargin();
        assertEquals(15, result);

        // Test edge case
        // Add edge case test here
    }

    // Add more test cases for the remaining methods

}