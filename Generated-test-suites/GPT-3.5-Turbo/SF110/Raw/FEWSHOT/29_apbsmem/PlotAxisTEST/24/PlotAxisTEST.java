package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

    private PlotAxis plotAxis;

    @Before
    public void setup() {
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

    @Test
    public void testDefaultConstructor() {
        assertNull(plotAxis.getLabel());
        assertNotNull(plotAxis.getScale());
        assertNotNull(plotAxis.getNumberFormat());
        assertEquals(1, plotAxis.getGridStyle());
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        assertEquals("", plotAxis.lowerBoundsAsString());
        assertEquals("", plotAxis.upperBoundsAsString());
        assertEquals(false, plotAxis.manualQuant);
        assertEquals(0, plotAxis.quantum, 0);
    }

    @Test
    public void testParameterizedConstructor() {
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = new DecimalFormat();
        int gridStyle = 2;

        PlotAxis plotAxis = new PlotAxis("Label", scale, nf, gridStyle);

        assertEquals("Label", plotAxis.getLabel());
        assertEquals(scale, plotAxis.getScale());
        assertEquals(nf, plotAxis.getNumberFormat());
        assertEquals(2, plotAxis.getGridStyle());
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        assertEquals("", plotAxis.lowerBoundsAsString());
        assertEquals("", plotAxis.upperBoundsAsString());
        assertEquals(false, plotAxis.manualQuant);
        assertEquals(0, plotAxis.quantum, 0);
    }

    @Test
    public void testSetLabel() {
        plotAxis.setLabel("New Label");

        assertEquals("New Label", plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        plotAxis.setLabel("Label");

        assertEquals("Label", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new LogarithmicAxisScale();

        plotAxis.setScale(scale);

        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testGetScale() {
        assertNotNull(plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        NumberFormat nf = new DecimalFormat("#.#####");

        plotAxis.setNumberFormat(nf);

        assertEquals(nf, plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        assertNotNull(plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        plotAxis.setGridStyle(0);

        assertEquals(0, plotAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        assertEquals(1, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);

        assertEquals(true, plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        plotAxis.setLowerBounds(10);

        assertEquals(10, plotAxis.getLowerBounds(), 0);
    }

    @Test
    public void testGetLowerBounds() {
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
    }

    @Test
    public void testLowerBoundsAsString() {
        assertEquals("", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);

        assertEquals(true, plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        plotAxis.setUpperBounds(20);

        assertEquals(20, plotAxis.getUpperBounds(), 0);
    }

    @Test
    public void testGetUpperBounds() {
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
    }

    @Test
    public void testUpperBoundsAsString() {
        assertEquals("", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);

        assertEquals(true, plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        plotAxis.setTickStepSize(0.1);

        assertEquals(0.1, plotAxis.getTickStepSize(), 0);
    }

    @Test
    public void testGetTickStepSize() {
        assertEquals(0, plotAxis.getTickStepSize(), 0);
    }

    @Test
    public void testClone() {
        PlotAxis clonedAxis = (PlotAxis) plotAxis.clone();

        assertNotNull(clonedAxis);
        assertNotNull(plotAxis.getScale());
        assertNotNull(plotAxis.getNumberFormat());
        assertEquals(1, plotAxis.getGridStyle());
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        assertEquals("", plotAxis.lowerBoundsAsString());
        assertEquals("", plotAxis.upperBoundsAsString());
        assertEquals(false, plotAxis.manualQuant);
        assertEquals(0, plotAxis.quantum, 0);
    }
}