package jahuwaldt.plot;

import static org.junit.Assert.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {};
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }
        
    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis() {};
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }
        
    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {};
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }
        
    @Test
    public void testGetScale() {
        PlotAxis axis = new PlotAxis() {};
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }
        
    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }
        
    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }
        
    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {};
        int style = 2;
        axis.setGridStyle(style);
        assertEquals(style, axis.getGridStyle());
    }
        
    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new PlotAxis() {};
        int style = 2;
        axis.setGridStyle(style);
        assertEquals(style, axis.getGridStyle());
    }
        
    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        boolean flag = true;
        axis.manualLowerBounds(flag);
        assertTrue(axis.getManualLB());
    }
        
    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        double value = 10.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0.001);
    }
        
    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        double value = 10.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0.001);
    }
        
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {};
        double value = 10.0;
        axis.setLowerBounds(value);
        assertEquals("10.0", axis.lowerBoundsAsString());
    }
        
    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        boolean flag = true;
        axis.manualUpperBounds(flag);
        assertTrue(axis.getManualUB());
    }
        
    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        double value = 20.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0.001);
    }
        
    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        double value = 20.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0.001);
    }
        
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {};
        double value = 20.0;
        axis.setUpperBounds(value);
        assertEquals("20.0", axis.upperBoundsAsString());
    }
        
    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        boolean flag = true;
        axis.manualTickStepSize(flag);
        assertTrue(axis.getManualQuant());
    }
        
    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        double value = 5.0;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize(), 0.001);
    }
        
    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        double value = 5.0;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize(), 0.001);
    }
    
    // TODO: Continue testing the remaining methods in the class
    
}