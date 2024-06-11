package jahuwaldt.plot.test;

import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import org.junit.Before;
import org.junit.Test;
import java.text.NumberFormat;
import static org.junit.Assert.*;

public class PlotAxisTest {

    private PlotAxis plotAxis;

    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // Method not implemented for testing
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // Method not implemented for testing
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Method not implemented for testing
            }
        };
    }

    @Test
    public void testSetLabel() {
        String label = "Test Label";
        plotAxis.setLabel(label);
        assertEquals(label, plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        int style = 2;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        boolean flag = true;
        plotAxis.manualLowerBounds(flag);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        double value = 1.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testManualUpperBounds() {
        boolean flag = true;
        plotAxis.manualUpperBounds(flag);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        double value = 1.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testManualTickStepSize() {
        boolean flag = true;
        plotAxis.manualTickStepSize(flag);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        double value = 1.0;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }
}