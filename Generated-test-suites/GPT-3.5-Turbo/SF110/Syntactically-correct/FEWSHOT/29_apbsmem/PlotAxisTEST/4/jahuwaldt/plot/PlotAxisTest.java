package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {
    private String label;
    private PlotAxisScale scale;
    private NumberFormat nf;
    private int gridStyle;
    private PlotAxis axis;
    private Rectangle plotFrame;
    
    @Before
    public void setUp() {
        label = "test";
        scale = mock(PlotAxisScale.class);
        nf = mock(NumberFormat.class);
        gridStyle = 1;
        axis = new PlotAxisDummy(label, scale, nf, gridStyle);
        plotFrame = new Rectangle(10, 10, 100, 100);
    }
    
    @Test
    public void testSetLabel() {
        String newLabel = "newLabel";
        axis.setLabel(newLabel);
        assertEquals(newLabel, axis.getLabel());
    }
    
    @Test
    public void testGetLabel() {
        assertEquals(label, axis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxisScale newScale = mock(PlotAxisScale.class);
        axis.setScale(newScale);
        assertEquals(newScale, axis.getScale());
    }
    
    @Test
    public void testGetScale() {
        assertEquals(scale, axis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        NumberFormat newFormat = mock(NumberFormat.class);
        axis.setNumberFormat(newFormat);
        assertEquals(newFormat, axis.getNumberFormat());
    }
    
    @Test
    public void testGetNumberFormat() {
        assertEquals(nf, axis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        int newGridStyle = 2;
        axis.setGridStyle(newGridStyle);
        assertEquals(newGridStyle, axis.getGridStyle());
    }
    
    @Test
    public void testGetGridStyle() {
        assertEquals(gridStyle, axis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        axis.manualLowerBounds(true);
        axis.manualLowerBounds(false);
    }
    
    @Test
    public void testSetLowerBounds() {
        double value = 10.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testGetLowerBounds() {
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        axis.setLowerBounds(10.0);
        when(nf.format((float)10.0)).thenReturn("10.0");
        assertEquals("10.0", axis.lowerBoundsAsString());
        verify(nf).format((float)10.0);
    }
    
    @Test
    public void testManualUpperBounds() {
        axis.manualUpperBounds(true);
        axis.manualUpperBounds(false);
    }
    
    @Test
    public void testSetUpperBounds() {
        double value = 20.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testGetUpperBounds() {
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        axis.setUpperBounds(20.0);
        when(nf.format((float)20.0)).thenReturn("20.0");
        assertEquals("20.0", axis.upperBoundsAsString());
        verify(nf).format((float)20.0);
    }
    
    @Test
    public void testManualTickStepSize() {
        axis.manualTickStepSize(true);
        axis.manualTickStepSize(false);
    }
    
    @Test
    public void testSetTickStepSize() {
        double value = 5.0;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize(), 0.001);
    }
    
    @Test
    public void testGetTickStepSize() {
        assertEquals(1.0, axis.getTickStepSize(), 0.001);
    }
    
    @Test
    public void testSetAxisBounds() {
        PlotRunList p0 = mock(PlotRunList.class);
        axis.setAxisBounds(p0);
    }
    
    @Test
    public void testSetSize() {
        axis.setSize(plotFrame);
    }
    
    @Test
    public void testScaleCoord() {
        double x = 10.0;
        assertEquals(12, axis.scaleCoord(x));
    }
    
    @Test
    public void testClone() {
        PlotAxis cloneAxis = (PlotAxis) axis.clone();
        assertEquals(axis, cloneAxis);
    }
    
    private class PlotAxisDummy extends PlotAxis {
        public PlotAxisDummy(String label, PlotAxisScale scale, NumberFormat nf, int gridStyle) {
            super(label, scale, nf, gridStyle);
        }

        @Override
        public void setAxisBounds(PlotRunList p0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        protected void resizeAxis(Rectangle p0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
            // TODO Auto-generated method stub
            
        }
    }
}