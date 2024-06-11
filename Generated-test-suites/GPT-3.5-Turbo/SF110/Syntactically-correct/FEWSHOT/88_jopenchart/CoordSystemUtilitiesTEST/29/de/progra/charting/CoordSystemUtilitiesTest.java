import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.text.*;
import de.progra.charting.model.*;
import de.progra.charting.*;


public class CoordSystemUtilitiesTest {

    private CoordSystemUtilities util;

    @Before
    public void setup() {
        CoordSystem coord = new CoordSystem();
        ChartDataModelConstraints constraints = new ChartDataModelConstraints();
        ChartDataModelConstraints constraints2 = new ChartDataModelConstraints();
        ChartDataModel model = new ChartDataModel();
        util = new CoordSystemUtilities(coord, constraints, constraints2, model);
    }

    @Test
    public void testComputeLeftMargin() {
        assertEquals(20, util.computeLeftMargin());
    }

    @Test
    public void testComputeRightMargin() {
        assertEquals(15, util.computeRightMargin());
    }

    @Test
    public void testComputeTopMargin() {
        assertEquals(15, util.computeTopMargin());
    }

    @Test
    public void testComputeBottomMargin() {
        assertEquals(20, util.computeBottomMargin());
    }

    @Test
    public void testComputeXAxisLabelHeight() {
        assertEquals(0, util.computeXAxisLabelHeight());
    }

    @Test
    public void testComputeYAxisLabelWidth() {
        assertEquals(0, util.computeYAxisLabelWidth());
    }

    @Test
    public void testDrawNumericalXAxisTicks() {
        // TO-DO: Implement test cases for drawNumericalXAxisTicks
    }

    @Test
    public void testDrawXAxisTicks() {
        // TO-DO: Implement test cases for drawXAxisTicks
    }

    @Test
    public void testDrawYAxisTicks() {
        // TO-DO: Implement test cases for drawYAxisTicks
    }

    @Test
    public void testGetXAxisLine2D() {
        // TO-DO: Implement test cases for getXAxisLine2D
    }

    @Test
    public void testGetYAxisLine2D() {
        // TO-DO: Implement test cases for getYAxisLine2D
    }

    @Test
    public void testGetSecondYAxisLine2D() {
        // TO-DO: Implement test cases for getSecondYAxisLine2D
    }
}