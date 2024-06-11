package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testConstructor() {
        PlotAxis axis = new TestPlotAxis();
        
        assertNull(axis.getLabel());
        assertEquals(axis.getScale().getClass(), LinearAxisScale.class);
        assertEquals(axis.getNumberFormat(), NumberFormat.getInstance());
        assertEquals(axis.getGridStyle(), 1);
        assertEquals(axis.getLowerBounds(), Double.MAX_VALUE, 0);
        assertEquals(axis.getUpperBounds(), -1.7976931348623157E308, 0);
    }
    
    @Test
    public void testSetLabel() {
        PlotAxis axis = new TestPlotAxis();
        
        assertNull(axis.getLabel());
        
        axis.setLabel("x-axis");
        
        assertEquals(axis.getLabel(), "x-axis");
    }
    
    @Test
    public void testSetScale() {
        PlotAxis axis = new TestPlotAxis();
        
        assertEquals(axis.getScale().getClass(), LinearAxisScale.class);
        
        PlotAxisScale scale = mock(PlotAxisScale.class);
        axis.setScale(scale);
        
        assertEquals(axis.getScale(), scale);
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new TestPlotAxis();
        
        assertEquals(axis.getNumberFormat(), NumberFormat.getInstance());
        
        NumberFormat format = mock(NumberFormat.class);
        axis.setNumberFormat(format);
        
        assertEquals(axis.getNumberFormat(), format);
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new TestPlotAxis();
        
        assertEquals(axis.getGridStyle(), 1);
        
        axis.setGridStyle(2);
        
        assertEquals(axis.getGridStyle(), 2);
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new TestPlotAxis();
        
        axis.manualLowerBounds(true);
        assertEquals(axis.getLowerBounds(), Double.MAX_VALUE, 0);
        axis.manualLowerBounds(false);
        verify(axis, times(1)).setAxisTickLimits();
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new TestPlotAxis();
        
        axis.setLowerBounds(10);
        
        assertEquals(axis.getLowerBounds(), 10, 0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new TestPlotAxis();
        
        axis.setLowerBounds(10);
        
        assertEquals(axis.lowerBoundsAsString(), "10.0");
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new TestPlotAxis();
        
        axis.manualUpperBounds(true);
        assertEquals(axis.getUpperBounds(), -1.7976931348623157E308, 0);
        axis.manualUpperBounds(false);
        verify(axis, times(1)).setAxisTickLimits();
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new TestPlotAxis();
        
        axis.setUpperBounds(100);
        
        assertEquals(axis.getUpperBounds(), 100, 0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new TestPlotAxis();
        
        axis.setUpperBounds(100);
        
        assertEquals(axis.upperBoundsAsString(), "100.0");
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new TestPlotAxis();
        
        axis.manualTickStepSize(true);
        
        verify(axis, times(0)).setAxisTickLimits();
        
        axis.manualTickStepSize(false);
        
        verify(axis, times(1)).setAxisTickLimits();
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new TestPlotAxis();
        
        axis.setTickStepSize(0.5);
        
        assertEquals(axis.getTickStepSize(), 0.5, 0);
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis axis = new TestPlotAxis();
        
        assertEquals(axis.scaleCoord(10), 11);
    }
    
    @Test
    public void testClone() {
        PlotAxis axis = new TestPlotAxis();
        
        PlotAxis clonedAxis = (PlotAxis) axis.clone();
        
        assertEquals(axis, clonedAxis);
        assertEquals(clonedAxis.getClass(), axis.getClass());
    }

    private class TestPlotAxis extends PlotAxis {
        private static final long serialVersionUID = 1L;

        @Override
        public void setAxisBounds(PlotRunList p0) {
        }

        @Override
        protected void resizeAxis(Rectangle p0) {
        }

        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
        }
    }
}