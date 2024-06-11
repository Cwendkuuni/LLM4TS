import de.progra.charting.*;
import de.progra.charting.model.*;
import java.text.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoordSystemUtilitiesTest {
    
    private CoordSystemUtilities coordSystemUtilities;
    private CoordSystem coord;
    private ChartDataModelConstraints constraints;
    private ChartDataModelConstraints constraints2;
    private ChartDataModel model;
    
    @Before
    public void setup() {
        coord = new CoordSystem();
        constraints = new ChartDataModelConstraints();
        constraints2 = new ChartDataModelConstraints();
        model = new ChartDataModel();
        coordSystemUtilities = new CoordSystemUtilities(coord, constraints, constraints2, model);
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
        int expected = 0; // enter expected value here
        int actual = coordSystemUtilities.computeXAxisLabelHeight();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testComputeYAxisLabelWidth() {
        int expected = 0; // enter expected value here
        int actual = coordSystemUtilities.computeYAxisLabelWidth();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testDrawNumericalXAxisTicks() {
        // write your test for drawNumericalXAxisTicks() method here
    }
    
    @Test
    public void testDrawXAxisTicks() {
        // write your test for drawXAxisTicks() method here
    }
    
    @Test
    public void testDrawYAxisTicks() {
        // write your test for drawYAxisTicks() method here
    }
    
    @Test
    public void testGetXAxisLine2D() {
        Line2D expected = new Line2D.Double();
        Line2D actual = coordSystemUtilities.getXAxisLine2D();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetYAxisLine2D() {
        Line2D expected = new Line2D.Double();
        Line2D actual = coordSystemUtilities.getYAxisLine2D();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSecondYAxisLine2D() {
        Line2D expected = new Line2D.Double();
        Line2D actual = coordSystemUtilities.getSecondYAxisLine2D();
        assertEquals(expected, actual);
    }
}