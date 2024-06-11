package jahuwaldt.plot;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }
    
    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxisStub();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }
    
    @Test
    public void testGetScale() {
        PlotAxis plotAxis = new PlotAxisStub();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxisStub();
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new PlotAxisStub();
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
    }
    
    @Test
    public void testGetGridStyle() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setLowerBounds(10.5);
        assertEquals(10.5, plotAxis.getLowerBounds(), 0);
    }
    
    @Test
    public void testGetLowerBounds() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setLowerBounds(10.5);
        assertEquals(10.5, plotAxis.getLowerBounds(), 0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setLowerBounds(10.5);
        assertEquals("10.5", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setUpperBounds(20.5);
        assertEquals(20.5, plotAxis.getUpperBounds(), 0);
    }
    
    @Test
    public void testGetUpperBounds() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setUpperBounds(20.5);
        assertEquals(20.5, plotAxis.getUpperBounds(), 0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setUpperBounds(20.5);
        assertEquals("20.5", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setTickStepSize(0.1);
        assertEquals(0.1, plotAxis.getTickStepSize(), 0);
    }
    
    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxisStub();
        plotAxis.setTickStepSize(0.1);
        assertEquals(0.1, plotAxis.getTickStepSize(), 0);
    }
    
    private class PlotAxisStub extends PlotAxis {
        public void setAxisBounds(PlotRunList p0) {}
        protected void resizeAxis(Rectangle p0) {}
        public void draw(Graphics p0, Component p1, Rectangle p2) {}
    }
}