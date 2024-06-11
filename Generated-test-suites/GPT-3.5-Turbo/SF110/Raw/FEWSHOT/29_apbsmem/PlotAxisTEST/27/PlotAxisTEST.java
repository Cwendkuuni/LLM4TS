package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Rectangle;
import java.io.Serializable;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {
    
    private class PlotAxisMock extends PlotAxis {
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
    
    private PlotAxisMock plotAxis;
    
    @Before
    public void setUp() {
        plotAxis = new PlotAxisMock();
    }
    
    @Test
    public void testSetLabel() {
        plotAxis.setLabel("test");
        assertEquals("test", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        plotAxis.setGridStyle(PlotAxis.kTickMarksOnly);
        assertEquals(PlotAxis.kTickMarksOnly, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        plotAxis.manualLowerBounds(false);
    }
    
    @Test
    public void testSetLowerBounds() {
        plotAxis.setLowerBounds(1.0);
        assertEquals(1.0, plotAxis.getLowerBounds(), 0.0);
    }
    
    @Test
    public void testGetLowerBounds() {
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        assertEquals("1.7976931348623157E308", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        plotAxis.manualUpperBounds(false);
    }
    
    @Test
    public void testSetUpperBounds() {
        plotAxis.setUpperBounds(1.0);
        assertEquals(1.0, plotAxis.getUpperBounds(), 0.0);
    }
    
    @Test
    public void testGetUpperBounds() {
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        assertEquals("-1.7976931348623157E308", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        plotAxis.manualTickStepSize(false);
    }
    
    @Test
    public void testSetTickStepSize() {
        plotAxis.setTickStepSize(1.0);
        assertEquals(1.0, plotAxis.getTickStepSize(), 0.0);
    }
    
    @Test
    public void testGetTickStepSize() {
        assertEquals(0.0, plotAxis.getTickStepSize(), 0.0);
    }
}