import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

public class PlotAxisTest {
    private PlotAxis plotAxis;

    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) { }
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
    }

    @Test
    public void testSetLabel() {
        // Typical use case
        String label = "x-axis";
        plotAxis.setLabel(label);
        assertEquals(label, plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        // Typical use case
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        // Typical use case
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        // Typical use case
        int style = 1;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        // Typical use case
        boolean flag = true;
        plotAxis.manualLowerBounds(flag);
        assertEquals(flag, plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        // Typical use case
        double value = 0.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
        
        // Edge case: negative value
        plotAxis.setLowerBounds(-10.5);
        assertEquals(-10.5, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        // Typical use case
        double value = 0.0;
        plotAxis.setLowerBounds(value);
        assertEquals(String.valueOf(value), plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        // Typical use case
        boolean flag = true;
        plotAxis.manualUpperBounds(flag);
        assertEquals(flag, plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        // Typical use case
        double value = 100.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
        
        // Edge case: large value
        plotAxis.setUpperBounds(1000.0);
        assertEquals(1000.0, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        // Typical use case
        double value = 100.0;
        plotAxis.setUpperBounds(value);
        assertEquals(String.valueOf(value), plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        // Typical use case
        boolean flag = true;
        plotAxis.manualTickStepSize(flag);
        assertEquals(flag, plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        // Typical use case
        double value = 0.1;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
        
        // Edge case: zero tick step size
        plotAxis.setTickStepSize(0.0);
        assertEquals(0.0, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testScaleCoord() {
        // Typical use case
        double x = 0.5;
        int expected = (int)(plotAxis.xA * plotAxis.scale.func(x) + plotAxis.xB);
        int actual = plotAxis.scaleCoord(x);
        assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        // Typical use case
        PlotAxis cloned = (PlotAxis) plotAxis.clone();
        assertNotSame(plotAxis, cloned);
        assertEquals(plotAxis.getClass(), cloned.getClass());
    }
}