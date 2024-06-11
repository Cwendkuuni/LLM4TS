package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Component;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setLabel("Label");
        assertEquals("Label", plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertNull(plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxisImpl();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertTrue(plotAxis.getScale() instanceof LinearAxisScale);
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxisImpl();
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertTrue(plotAxis.getNumberFormat() instanceof NumberFormat);
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(1, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setLowerBounds(1.0);
        assertEquals(1.0, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals("3.4E38", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setUpperBounds(100.0);
        assertEquals(100.0, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals("1.7976931348623157E308", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setTickStepSize(2.0);
        assertEquals(2.0, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(1.0, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        PlotRunList plotRunList = new PlotRunList();
        plotAxis.setAxisBounds(plotRunList);
        // Add assertion here
    }

    @Test
    public void testSetSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        Rectangle plotFrame = new Rectangle();
        plotAxis.setSize(plotFrame);
        // Add assertion here
    }

    @Test
    public void testDraw() {
        PlotAxis plotAxis = new PlotAxisImpl();
        Graphics graphics = null;
        Component component = null;
        Rectangle rectangle = new Rectangle();
        plotAxis.draw(graphics, component, rectangle);
        // Add assertion here
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(0, plotAxis.scaleCoord(1.0));
        assertEquals(0, plotAxis.scaleCoord(0.0));
        assertEquals(0, plotAxis.scaleCoord(-1.0));
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxisImpl();
        PlotAxis clonedAxis = (PlotAxis)plotAxis.clone();
        assertTrue(plotAxis.getClass().getName() == clonedAxis.getClass().getName());
    }
}