package jahuwaldt.plot;

import org.junit.Test;
import org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {};
        String label = "Test label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis() {};
        String label = "Test label";
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
        assertEquals(flag, axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        double value = 5.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0.00001);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        double value = 5.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0.00001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {};
        double value = 5.0;
        axis.setLowerBounds(value);
        assertEquals(String.valueOf((float)value), axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        boolean flag = true;
        axis.manualUpperBounds(flag);
        assertEquals(flag, axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        double value = 10.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0.00001);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        double value = 10.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0.00001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {};
        double value = 10.0;
        axis.setUpperBounds(value);
        assertEquals(String.valueOf((float)value), axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        boolean flag = true;
        axis.manualTickStepSize(flag);
        assertEquals(flag, axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        double value = 0.5;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize(), 0.00001);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        double value = 0.5;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize(), 0.00001);
    }
    
    @Test
    public void testSetAxisBounds() {
        // test logic for this method
    }

    @Test
    public void testSetSize() {
        // test logic for this method
    }

    @Test
    public void testDraw() {
        // test logic for this method
    }

    @Test
    public void testScaleCoord() {
        // test logic for this method
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis() {};
        PlotAxis clone = (PlotAxis)axis.clone();
        assertNotSame(axis, clone);
        assertEquals(axis.getLabel(), clone.getLabel());
        assertEquals(axis.getScale(), clone.getScale());
        assertEquals(axis.getNumberFormat(), clone.getNumberFormat());
        assertEquals(axis.getGridStyle(), clone.getGridStyle());
    }

}