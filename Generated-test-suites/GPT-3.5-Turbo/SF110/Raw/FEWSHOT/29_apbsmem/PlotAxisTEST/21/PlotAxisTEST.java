package jahuwaldt.plot;

import org.junit.Test;

import java.awt.*;
import java.text.NumberFormat;

import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new LinearAxis();
        axis.setLabel("X Axis");

        assertEquals("X Axis", axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new LinearAxis();
        axis.setLabel("Y Axis");

        assertEquals("Y Axis", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new LinearAxis();
        PlotAxisScale scale = new LogAxisScale();
        axis.setScale(scale);

        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis axis = new LinearAxis();
        PlotAxisScale scale = new LogAxisScale();
        axis.setScale(scale);

        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new LinearAxis();
        NumberFormat format = NumberFormat.getPercentInstance();
        axis.setNumberFormat(format);

        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new LinearAxis();
        NumberFormat format = NumberFormat.getCurrencyInstance();
        axis.setNumberFormat(format);

        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new LinearAxis();
        axis.setGridStyle(PlotAxis.kMajorGrid);

        assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new LinearAxis();
        axis.setGridStyle(PlotAxis.kTickMarksOnly);

        assertEquals(PlotAxis.kTickMarksOnly, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new LinearAxis();
        axis.manualLowerBounds(true);

        assertTrue(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new LinearAxis();
        axis.setLowerBounds(0.0);

        assertEquals(0.0, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new LinearAxis();
        axis.setLowerBounds(0.0);

        assertEquals(0.0, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new LinearAxis();
        axis.setLowerBounds(10.5);

        assertEquals("10.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new LinearAxis();
        axis.manualUpperBounds(true);

        assertTrue(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new LinearAxis();
        axis.setUpperBounds(100.0);

        assertEquals(100.0, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new LinearAxis();
        axis.setUpperBounds(100.0);

        assertEquals(100.0, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new LinearAxis();
        axis.setUpperBounds(99.9);

        assertEquals("99.9", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new LinearAxis();
        axis.manualTickStepSize(true);

        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new LinearAxis();
        axis.setTickStepSize(0.1);

        assertEquals(0.1, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new LinearAxis();
        axis.setTickStepSize(0.1);

        assertEquals(0.1, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new LinearAxis();
        axis.xA = 2.0;
        axis.xB = 3.0;
        axis.scale = new LogAxisScale();

        assertEquals(8, axis.scaleCoord(5.0));
    }

    @Test
    public void testClone() {
        PlotAxis axis = new LinearAxis();
        axis.setLabel("Z Axis");
        PlotAxis clonedAxis = (PlotAxis) axis.clone();

        assertNotSame(axis, clonedAxis);
        assertEquals(axis.getLabel(), clonedAxis.getLabel());
        assertSame(axis.getScale(), clonedAxis.getScale());
        assertNotSame(axis.getNumberFormat(), clonedAxis.getNumberFormat());
        assertEquals(axis.getGridStyle(), clonedAxis.getGridStyle());
        assertEquals(axis.getLowerBounds(), clonedAxis.getLowerBounds(), 0.0);
        assertEquals(axis.getUpperBounds(), clonedAxis.getUpperBounds(), 0.0);
        assertEquals(axis.getTickStepSize(), clonedAxis.getTickStepSize(), 0.0);
    }
}