package jahuwaldt.plot;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {
    
    @Test
    public void testSetLabel() {
        PlotAxis axis = new LinearPlotAxis();
        axis.setLabel("x-axis");
        assertEquals("x-axis", axis.getLabel());
        
        axis.setLabel("");
        assertEquals("", axis.getLabel());
    }
    
    @Test
    public void testGetScale() {
        PlotAxis axis = new LinearPlotAxis();
        PlotAxisScale scale = new LogarithmicAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
        
        axis.setScale(null);
        assertTrue(axis.getScale() instanceof LinearAxisScale);
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new LinearPlotAxis();
        NumberFormat format = NumberFormat.getIntegerInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
        
        axis.setNumberFormat(null);
        assertNotNull(axis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new LinearPlotAxis();
        axis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
        
        axis.setGridStyle(PlotAxis.kNoGrid);
        assertEquals(PlotAxis.kNoGrid, axis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new LinearPlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.getManualLB());
        
        axis.manualLowerBounds(false);
        assertFalse(axis.getManualLB());
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new LinearPlotAxis();
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getLowerBounds(), 0.0001);
        
        axis.setLowerBounds(-1.0);
        assertEquals(-1.0, axis.getLowerBounds(), 0.0001);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new LinearPlotAxis();
        axis.setLowerBounds(0.0);
        assertEquals("0", axis.lowerBoundsAsString());
        
        axis.setLowerBounds(-1.0);
        assertEquals("-1", axis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new LinearPlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.getManualUB());
        
        axis.manualUpperBounds(false);
        assertFalse(axis.getManualUB());
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new LinearPlotAxis();
        axis.setUpperBounds(10.0);
        assertEquals(10.0, axis.getUpperBounds(), 0.0001);
        
        axis.setUpperBounds(20.0);
        assertEquals(20.0, axis.getUpperBounds(), 0.0001);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new LinearPlotAxis();
        axis.setUpperBounds(10.0);
        assertEquals("10", axis.upperBoundsAsString());
        
        axis.setUpperBounds(20.0);
        assertEquals("20", axis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new LinearPlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.getManualQuant());
        
        axis.manualTickStepSize(false);
        assertFalse(axis.getManualQuant());
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new LinearPlotAxis();
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.0001);
        
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getTickStepSize(), 0.0001);
    }
    
    // Other test cases for remaining methods and functionality...
}