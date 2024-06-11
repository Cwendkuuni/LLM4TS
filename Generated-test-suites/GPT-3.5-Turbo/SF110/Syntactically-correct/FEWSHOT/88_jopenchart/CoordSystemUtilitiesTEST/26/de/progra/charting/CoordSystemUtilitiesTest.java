package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class CoordSystemUtilitiesTest {
    private CoordSystemUtilities coordSysUtils;
    private CoordSystem c;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;
    
    @Before
    public void setUp() {
        c = new CoordSystem();
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        model = new ChartDataModel();
        coordSysUtils = new CoordSystemUtilities(c, constraints, constraints2, model);
    }
    
    @Test
    public void testComputeLeftMargin() {
        assertEquals(20, coordSysUtils.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        assertEquals(15, coordSysUtils.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        assertEquals(15, coordSysUtils.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        assertEquals(20, coordSysUtils.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        assertEquals(0, coordSysUtils.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        assertEquals(0, coordSysUtils.computeYAxisLabelWidth());
    }
    
    @Test
    public void testDrawNumericalXAxisTicks() {
        // Test case not provided
    }
    
    @Test
    public void testDrawXAxisTicks() {
        // Test case not provided
    }
    
    @Test
    public void testDrawYAxisTicks() {
        // Test case not provided
    }
    
    @Test
    public void testGetXAxisLine2D() {
        // Test case not provided
    }
    
    @Test
    public void testGetYAxisLine2D() {
        // Test case not provided
    }
    
    @Test
    public void testGetSecondYAxisLine2D() {
        // Test case not provided
    }
}