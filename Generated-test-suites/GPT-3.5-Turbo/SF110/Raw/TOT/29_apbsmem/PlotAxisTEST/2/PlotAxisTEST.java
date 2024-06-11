import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.text.NumberFormat;

public class PlotAxisTest {

    private PlotAxis plotAxis;

    @Before
    public void setup() {
        plotAxis = new PlotAxisImpl();
    }

    @Test
    public void testSetLabel() {
        // Typical use case
        String label = "X-Axis";
        plotAxis.setLabel(label);
        assertEquals(label, plotAxis.getLabel());

        // Edge case: empty label
        label = "";
        plotAxis.setLabel(label);
        assertEquals(label, plotAxis.getLabel());

        // Edge case: null label
        label = null;
        plotAxis.setLabel(label);
        assertNull(plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        // Edge case: label not set
        assertNull(plotAxis.getLabel());

        // Typical use case
        String label = "Y-Axis";
        plotAxis.setLabel(label);
        assertEquals(label, plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        // Typical use case
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());

        // Edge case: null scale
        plotAxis.setScale(null);
        assertTrue(plotAxis.getScale() instanceof LinearAxisScale);
    }

    @Test
    public void testGetScale() {
        // Edge case: scale not set
        assertTrue(plotAxis.getScale() instanceof LinearAxisScale);

        // Typical use case
        PlotAxisScale scale = new LogarithmicAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        // Typical use case
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());

        // Edge case: null format
        plotAxis.setNumberFormat(null);
        assertNotNull(plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        // Edge case: format not set
        assertNotNull(plotAxis.getNumberFormat());

        // Typical use case
        NumberFormat format = NumberFormat.getCurrencyInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        // Typical use case
        int style = PlotAxis.kNoGrid;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());

        // Edge case: negative style
        style = -1;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        // Edge case: grid style not set
        assertEquals(1, plotAxis.getGridStyle());

        // Typical use case
        int style = PlotAxis.kMajorGrid;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        // Typical use case
        boolean flag = true;
        plotAxis.manualLowerBounds(flag);
        assertTrue(plotAxis.manualLB);

        // Edge case: false flag
        flag = false;
        plotAxis.manualLowerBounds(flag);
        assertFalse(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        // Typical use case
        double value = 10.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0001);

        // Edge case: negative value
        value = -10.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0001);
    }

    @Test
    public void testGetLowerBounds() {
        // Edge case: lower bounds not set
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.0001);

        // Typical use case
        double value = 5.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0001);
    }

    @Test
    public void testLowerBoundsAsString() {
        // Edge case: lower bounds not set
        assertEquals(plotAxis.getNumberFormat().format((float) Double.MAX_VALUE), plotAxis.lowerBoundsAsString());

        // Typical use case
        double value = 7.5;
        plotAxis.setLowerBounds(value);
        assertEquals(plotAxis.getNumberFormat().format((float) value), plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        // Typical use case
        boolean flag = true;
        plotAxis.manualUpperBounds(flag);
        assertTrue(plotAxis.manualUB);

        // Edge case: false flag
        flag = false;
        plotAxis.manualUpperBounds(flag);
        assertFalse(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        // Typical use case
        double value = 100.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0001);

        // Edge case: negative value
        value = -100.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0001);
    }

    @Test
    public void testGetUpperBounds() {
        // Edge case: upper bounds not set
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.0001);

        // Typical use case
        double value = 50.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0001);
    }

    @Test
    public void testUpperBoundsAsString() {
        // Edge case: upper bounds not set
        assertEquals(plotAxis.getNumberFormat().format((float) -1.7976931348623157E308), plotAxis.upperBoundsAsString());

        // Typical use case
        double value = 90.5;
        plotAxis.setUpperBounds(value);
        assertEquals(plotAxis.getNumberFormat().format((float) value), plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        // Typical use case
        boolean flag = true;
        plotAxis.manualTickStepSize(flag);
        assertTrue(plotAxis.manualQuant);

        // Edge case: false flag
        flag = false;
        plotAxis.manualTickStepSize(flag);
        assertFalse(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        // Typical use case
        double value = 1.0;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0001);

        // Edge case: negative value
        value = -1.0;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0001);
    }

    @Test
    public void testGetTickStepSize() {
        // Edge case: tick step size not set
        assertEquals(0.0, plotAxis.getTickStepSize(), 0.0001);

        // Typical use case
        double value = 0.5;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0001);
    }

    @Test
    public void testSetAxisBounds() {
        // Method is abstract, cannot be tested directly
    }

    @Test
    public void testSetSize() {
        // Method is abstract, cannot be tested directly
    }

    @Test
    public void testScaleCoord() {
        // Typical use case
        double x = 10.0;
        int expected = (int)(plotAxis.xA * plotAxis.getScale().func(x) + plotAxis.xB);
        assertEquals(expected, plotAxis.scaleCoord(x));

        // Edge case: negative x value
        x = -10.0;
        expected = (int)(plotAxis.xA * plotAxis.getScale().func(x) + plotAxis.xB);
        assertEquals(expected, plotAxis.scaleCoord(x));
    }

    @Test
    public void testClone() {
        // Typical use case
        PlotAxis clone = (PlotAxis) plotAxis.clone();
        assertNotSame(plotAxis, clone);
        assertEquals(plotAxis.getLabel(), clone.getLabel());
        assertEquals(plotAxis.getScale(), clone.getScale());
        assertEquals(plotAxis.getNumberFormat(), clone.getNumberFormat());
        assertEquals(plotAxis.getGridStyle(), clone.getGridStyle());
        assertEquals(plotAxis.getLowerBounds(), clone.getLowerBounds(), 0.0001);
        assertEquals(plotAxis.getUpperBounds(), clone.getUpperBounds(), 0.0001);
        assertEquals(plotAxis.getTickStepSize(), clone.getTickStepSize(), 0.0001);
        assertEquals(plotAxis.manualLB, clone.manualLB);
        assertEquals(plotAxis.manualUB, clone.manualUB);
        assertEquals(plotAxis.manualQuant, clone.manualQuant);
    }

    private static class PlotAxisImpl extends PlotAxis {
        @Override
        public void setAxisBounds(PlotRunList p0) {
            // Do nothing for testing purposes
        }

        @Override
        protected void resizeAxis(Rectangle p0) {
            // Do nothing for testing purposes
        }

        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
            // Do nothing for testing purposes
        }
    }
}