
package jahuwaldt.plot;

import org.junit.Test;
import java.awt.*;
import java.text.NumberFormat;

import static org.junit.Assert.*;

public class PlotAxisTest {
    private static final double DELTA = 0.00001;
    
    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setScale(new TestAxisScale());
        assertTrue(plotAxis.getScale() instanceof TestAxisScale);
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setNumberFormat(NumberFormat.getCurrencyInstance());
        assertTrue(plotAxis.getNumberFormat() instanceof DecimalFormat);
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setLowerBounds(1.5);
        assertEquals(1.5, plotAxis.getLowerBounds(), DELTA);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setLowerBounds(1.5);
        assertEquals("1.5", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setUpperBounds(10.0);
        assertEquals(10.0, plotAxis.getUpperBounds(), DELTA);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setUpperBounds(10.0);
        assertEquals("10.0", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setTickStepSize(0.5);
        assertEquals(0.5, plotAxis.getTickStepSize(), DELTA);
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setSize(new Rectangle(0, 0, 100, 100));
        int coord = plotAxis.scaleCoord(5.0);
        assertEquals(50, coord);
    }
    
    @Test
    public void testClone() {
        PlotAxis plotAxis = new TestPlotAxis();
        PlotAxis clonedAxis = (PlotAxis) plotAxis.clone();
        assertNotSame(plotAxis, clonedAxis);
    }
    
    private class TestPlotAxis extends PlotAxis {
        @Override
        public void setAxisBounds(PlotRunList p0) {
            // Test method, no functionality
        }
        
        @Override
        protected void resizeAxis(Rectangle p0) {
            // Test method, no functionality
        }
        
        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
            // Test method, no functionality
        }
    }
    
    private class TestAxisScale implements PlotAxisScale {
        @Override
        public double func(double x) {
            return x;
        }
        
        @Override
        public AxisLimitData findGoodLimits(double lb, double ub) {
            return new AxisLimitData(lb, ub, 1.0);
        }
    }
}
