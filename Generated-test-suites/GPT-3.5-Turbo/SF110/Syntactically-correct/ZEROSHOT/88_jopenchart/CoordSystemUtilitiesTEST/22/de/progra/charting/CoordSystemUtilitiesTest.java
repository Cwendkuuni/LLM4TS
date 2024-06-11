package de.progra.charting;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {
    
    @Test
    public void testComputeLeftMargin() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeLeftMargin();
        assertEquals(20, result);
    }
    
    @Test
    public void testComputeRightMargin() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeRightMargin();
        assertEquals(15, result);
    }
    
    @Test
    public void testComputeTopMargin() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeTopMargin();
        assertEquals(15, result);
    }
    
    @Test
    public void testComputeBottomMargin() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeBottomMargin();
        assertEquals(20, result);
    }
    
    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeXAxisLabelHeight();
        assertEquals(0, result);
    }
    
    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        int result = csu.computeYAxisLabelWidth();
        assertEquals(0, result);
    }
    
    @Test
    public void testDrawNumericalXAxisTicks() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        csu.drawNumericalXAxisTicks(null);
    }
    
    @Test
    public void testDrawXAxisTicks() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        csu.drawXAxisTicks(null);
    }
    
    @Test
    public void testDrawYAxisTicks() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        csu.drawYAxisTicks(null);
    }
    
    @Test
    public void testGetXAxisLine2D() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        Line2D result = csu.getXAxisLine2D();
        assertNull(result);
    }
    
    @Test
    public void testGetYAxisLine2D() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        Line2D result = csu.getYAxisLine2D();
        assertNull(result);
    }
    
    @Test
    public void testGetSecondYAxisLine2D() {
        CoordSystemUtilities csu = new CoordSystemUtilities(null, null, null, null);
        Line2D result = csu.getSecondYAxisLine2D();
        assertNull(result);
    }
}