package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PlotAxisTest {
    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxis() {};
        assertNull(plotAxis.getLabel());
    }
    
    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLabel("X-Axis");
        assertEquals("X-Axis", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {};
        assertEquals(LinearAxisScale.class, plotAxis.getScale().getClass());
        
        PlotAxisScale scale = new LogarithmicAxisScale();
        plotAxis.setScale(scale);
        assertEquals(LogarithmicAxisScale.class, plotAxis.getScale().getClass());
    }
    
    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {};
        assertEquals(NumberFormat.getInstance(), plotAxis.getNumberFormat());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getIntegerInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(NumberFormat.getIntegerInstance(), plotAxis.getNumberFormat());
    }
    
    @Test
    public void testGetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {};
        assertEquals(1, plotAxis.getGridStyle());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualLowerBounds(true);
        plotAxis.setLowerBounds(0.0);
        assertEquals(0.0, plotAxis.getLowerBounds(), 0.0001);
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualUpperBounds(true);
        plotAxis.setUpperBounds(10.0);
        assertEquals(10.0, plotAxis.getUpperBounds(), 0.0001);
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualTickStepSize(true);
        plotAxis.setTickStepSize(0.5);
        assertEquals(0.5, plotAxis.getTickStepSize(), 0.0001);
    }
    
    @Test
    public void testSetAxisBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // No implementation
            }
        };
        
        // No assertions since it is an abstract method
        plotAxis.setAxisBounds(null);
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // No implementation
            }
        };
        
        assertEquals(0, plotAxis.scaleCoord(0.0));
        assertEquals(50, plotAxis.scaleCoord(5.0));
        assertEquals(100, plotAxis.scaleCoord(10.0));
    }
    
    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {};
        PlotAxis clone = (PlotAxis)plotAxis.clone();
        assertEquals(plotAxis.getClass(), clone.getClass());
    }
}