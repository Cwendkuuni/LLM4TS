package jahuwaldt.plot.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.NumberFormat;

import org.junit.Test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.tests.LinearAxisScale;
import jahuwaldt.plot.tests.util.Rectangle;

public class PlotAxisTest {
    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("x-axis");
        assertEquals("x-axis", axis.getLabel());
    }
    
    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis();
        axis.setLabel("y-axis");
        assertEquals("y-axis", axis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }
    
    @Test
    public void testGetScale() {
        PlotAxis axis = new PlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }
    
    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new PlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
    }
    
    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new PlotAxis();
        axis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.manualLowerBounds(true);
        axis.setLowerBounds(0.0);
        assertEquals("0.0", axis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        axis.setUpperBounds(10.0);
        assertEquals(10.0, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        axis.setUpperBounds(10.0);
        assertEquals(10.0, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        axis.setUpperBounds(10.0);
        assertEquals(10.0, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis();
        axis.manualUpperBounds(true);
        axis.setUpperBounds(10.0);
        assertEquals("10.0", axis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }
    
    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis();
        axis.manualTickStepSize(true);
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }
    
    @Test
    public abstract void setAxisBounds() {
        PlotAxis axis = new PlotAxis();
        PlotRunList p0 = new PlotRunList();
        axis.setAxisBounds(p0);
        // TODO: Add assertions
    }
    
    @Test
    public abstract void resizeAxis() {
        PlotAxis axis = new PlotAxis();
        Rectangle plotFrame = new Rectangle();
        axis.setSize(plotFrame);
        // TODO: Add assertions
    }
    
    @Test
    public abstract void draw() {
        PlotAxis axis = new PlotAxis();
        Graphics g = new Graphics();
        Component c = new Component();
        Rectangle r = new Rectangle();
        axis.draw(g, c, r);
        // TODO: Add assertions
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis();
        axis.setScale(new LinearAxisScale());
        axis.setSize(new Rectangle());
        int result = axis.scaleCoord(3.0);
        // TODO: Add assertions
    }
    
    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis();
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        // TODO: Add assertions
    }
}