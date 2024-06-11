import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.text.NumberFormat;

public class PlotAxisTest {
    private static final double DELTA = 1e-15;
    private PlotAxis plotAxis;

    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
    }

    @After
    public void tearDown() {
        plotAxis = null;
    }

    @Test
    public void testSetLabel() {
        plotAxis.setLabel("X Axis");
        assertEquals("X Axis", plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        plotAxis.setLabel("Y Axis");
        assertEquals("Y Axis", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxisScale scale = new LogarithmicAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        NumberFormat format = NumberFormat.getPercentInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        plotAxis.setGridStyle(PlotAxis.kTickMarksOnly);
        assertEquals(PlotAxis.kTickMarksOnly, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        plotAxis.setLowerBounds(0.0);
        assertEquals(0.0, plotAxis.getLowerBounds(), DELTA);
    }

    @Test
    public void testGetLowerBounds() {
        plotAxis.setLowerBounds(10.0);
        assertEquals(10.0, plotAxis.getLowerBounds(), DELTA);
    }

    @Test
    public void testLowerBoundsAsString() {
        plotAxis.setLowerBounds(100.0);
        assertEquals("100", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        plotAxis.setUpperBounds(1000.0);
        assertEquals(1000.0, plotAxis.getUpperBounds(), DELTA);
    }

    @Test
    public void testGetUpperBounds() {
        plotAxis.setUpperBounds(100.0);
        assertEquals(100.0, plotAxis.getUpperBounds(), DELTA);
    }

    @Test
    public void testUpperBoundsAsString() {
        plotAxis.setUpperBounds(100.0);
        assertEquals("100", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        plotAxis.setTickStepSize(0.1);
        assertEquals(0.1, plotAxis.getTickStepSize(), DELTA);
    }

    @Test
    public void testGetTickStepSize() {
        plotAxis.setTickStepSize(1.0);
        assertEquals(1.0, plotAxis.getTickStepSize(), DELTA);
    }

    @Test
    public void testScaleCoord() {
        plotAxis.setLowerBounds(0.0);
        plotAxis.setUpperBounds(10.0);
        plotAxis.setSize(new Rectangle(100, 100));

        int scaledCoord = plotAxis.scaleCoord(5.0);
        assertEquals(50, scaledCoord);
    }

    @Test
    public void testClone() {
        PlotAxis clonedPlotAxis = (PlotAxis) plotAxis.clone();
        assertNotSame(clonedPlotAxis, plotAxis);
        assertEquals(clonedPlotAxis.getClass(), plotAxis.getClass());
    }
}