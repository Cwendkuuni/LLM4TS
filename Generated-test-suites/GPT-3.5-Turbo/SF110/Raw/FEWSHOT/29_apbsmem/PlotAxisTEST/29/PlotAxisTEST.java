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
    
    private PlotAxis plotAxis;
    private PlotAxisScale scale;
    private NumberFormat nf;
    
    @Before
    public void setUp() {
        scale = mock(PlotAxisScale.class);
        nf = mock(NumberFormat.class);
        plotAxis = new PlotAxisStub("Label", scale, nf, 2);
    }
    
    @Test
    public void testSetLabel() {
        String newLabel = "New Label";
        plotAxis.setLabel(newLabel);
        assertEquals(newLabel, plotAxis.getLabel());
    }
    
    @Test
    public void testGetLabel() {
        String label = plotAxis.getLabel();
        assertEquals("Label", label);
    }
    
    @Test
    public void testSetScale() {
        PlotAxisScale newScale = mock(PlotAxisScale.class);
        plotAxis.setScale(newScale);
        assertEquals(newScale, plotAxis.getScale());
    }
    
    @Test
    public void testGetScale() {
        PlotAxisScale scale = plotAxis.getScale();
        assertEquals(this.scale, scale);
    }
    
    @Test
    public void testSetNumberFormat() {
        NumberFormat format = mock(NumberFormat.class);
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testGetNumberFormat() {
        NumberFormat nf = plotAxis.getNumberFormat();
        assertEquals(this.nf, nf);
    }
    
    @Test
    public void testSetGridStyle() {
        int style = 0;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }
    
    @Test
    public void testGetGridStyle() {
        int gridStyle = plotAxis.getGridStyle();
        assertEquals(2, gridStyle);
    }
    
    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        // Assert something
    }
    
    @Test
    public void testSetLowerBounds() {
        double value = 10.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }
    
    @Test
    public void testGetLowerBounds() {
        double lowerBounds = plotAxis.getLowerBounds();
        assertEquals(Double.MAX_VALUE, lowerBounds, 0.0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        String lowerBoundsAsString = plotAxis.lowerBoundsAsString();
        assertEquals("1.4E-45", lowerBoundsAsString);
    }
    
    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        // Assert something
    }
    
    @Test
    public void testSetUpperBounds() {
        double value = 20.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }
    
    @Test
    public void testGetUpperBounds() {
        double upperBounds = plotAxis.getUpperBounds();
        assertEquals(-1.7976931348623157E308, upperBounds, 0.0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        String upperBoundsAsString = plotAxis.upperBoundsAsString();
        assertEquals("-1.8E308", upperBoundsAsString);
    }
    
    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        // Assert something
    }
    
    @Test
    public void testSetTickStepSize() {
        double value = 0.1;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }
    
    @Test
    public void testGetTickStepSize() {
        double tickStepSize = plotAxis.getTickStepSize();
        assertEquals(1.0, tickStepSize, 0.0);
    }
    
    @Test
    public void testSetAxisBounds() {
        PlotRunList p0 = mock(PlotRunList.class);
        plotAxis.setAxisBounds(p0);
        // Assert something
    }
    
    @Test
    public void testDraw() {
        Graphics g = mock(Graphics.class);
        Component c = mock(Component.class);
        Rectangle r = mock(Rectangle.class);
        plotAxis.draw(g, c, r);
        // Assert something
    }
    
    @Test
    public void testScaleCoord() {
        int value = plotAxis.scaleCoord(2.0);
        assertEquals(3, value);
    }
    
    @Test
    public void testClone() {
        PlotAxis newObject = (PlotAxis) plotAxis.clone();
        // Assert something
    }
    
    private class PlotAxisStub extends PlotAxis {
        
        public PlotAxisStub(String label, PlotAxisScale scale, NumberFormat nf, int gridStyle) {
            super(label, scale, nf, gridStyle);
        }

        @Override
        public void setAxisBounds(PlotRunList p0) {
            // Stub implementation
        }

        @Override
        protected void resizeAxis(Rectangle p0) {
            // Stub implementation
        }

        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
            // Stub implementation
        }
        
    }

}