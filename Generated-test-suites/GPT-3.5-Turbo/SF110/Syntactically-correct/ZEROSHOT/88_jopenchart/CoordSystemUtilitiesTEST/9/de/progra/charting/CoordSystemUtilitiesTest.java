package de.progra.charting;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {
    
    @Test
    public void testComputeLeftMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);
        
        int result = csu.computeLeftMargin();
        
        //Test the expected output for computeLeftMargin
        assertEquals(20, result);
    }
    
    @Test
    public void testComputeRightMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);
        
        int result = csu.computeRightMargin();
        
        //Test the expected output for computeRightMargin
        assertEquals(15, result);
    }
    
    @Test
    public void testComputeTopMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);
        
        int result = csu.computeTopMargin();
        
        //Test the expected output for computeTopMargin
        assertEquals(15, result);
    }
    
    @Test
    public void testComputeBottomMargin() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);
        
        int result = csu.computeBottomMargin();
        
        //Test the expected output for computeBottomMargin
        assertEquals(20, result);
    }
    
    @Test
    public void testComputeXAxisLabelHeight() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);
        
        int result = csu.computeXAxisLabelHeight();
        
        //Test the expected output for computeXAxisLabelHeight
        assertEquals(0, result);
    }
    
    @Test
    public void testComputeYAxisLabelWidth() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        CoordSystemUtilities csu = new CoordSystemUtilities(coord, constraints, constraints2, model);
        
        int result = csu.computeYAxisLabelWidth();
        
        //Test the expected output for computeYAxisLabelWidth
        assertEquals(0, result);
    }
    
    @Test
    public void testDrawNumericalXAxisTicks() {
        // TODO: Write test case for drawNumericalXAxisTicks
    }
    
    @Test
    public void testDrawXAxisTicks() {
        // TODO: Write test case for drawXAxisTicks
    }
    
    @Test
    public void testDrawYAxisTicks() {
        // TODO: Write test case for drawYAxisTicks
    }
    
    @Test
    public void testGetXAxisLine2D() {
        // TODO: Write test case for getXAxisLine2D
    }
    
    @Test
    public void testGetYAxisLine2D() {
        // TODO: Write test case for getYAxisLine2D
    }
    
    @Test
    public void testGetSecondYAxisLine2D() {
        // TODO: Write test case for getSecondYAxisLine2D
    }
    
}