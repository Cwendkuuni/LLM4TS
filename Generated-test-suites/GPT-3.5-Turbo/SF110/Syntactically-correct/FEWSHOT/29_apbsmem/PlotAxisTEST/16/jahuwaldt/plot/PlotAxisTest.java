package jahuwaldt.plot;

import static org.junit.Assert.*;
import java.awt.Rectangle;
import java.text.NumberFormat;
import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {
    private PlotAxis plotAxis;

    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            public void draw(Graphics g, Component c, Rectangle r) {}
            public void setAxisBounds(PlotRunList p) {}
            protected void resizeAxis(Rectangle r) {}
        };
    }

    @Test
    public void testSetGetLabel() {
        plotAxis.setLabel("xAxis");
        assertEquals("xAxis", plotAxis.getLabel());
    }

    @Test
    public void testSetGetScale() {
        LinearAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetGetNumberFormat() {
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGetGridStyle() {
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetGetLowerBounds() {
        plotAxis.setLowerBounds(0.0);
        assertEquals(0.0, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        plotAxis.setLowerBounds(0.0);
        assertEquals("0.0", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetGetUpperBounds() {
        plotAxis.setUpperBounds(10.0);
        assertEquals(10.0, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        plotAxis.setUpperBounds(10.0);
        assertEquals("10.0", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetGetTickStepSize() {
        plotAxis.setTickStepSize(0.1);
        assertEquals(0.1, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        PlotRunList p = new PlotRunList();
        plotAxis.setAxisBounds(p);
    }

    @Test
    public void testSetSize() {
        Rectangle plotFrame = new Rectangle();
        plotAxis.setSize(plotFrame);
    }

    @Test
    public void testScaleCoord() {
        int coord = plotAxis.scaleCoord(1.0);
        assertEquals(1, coord);
    }

    @Test
    public void testClone() {
        PlotAxis clone = (PlotAxis) plotAxis.clone();
        assertTrue(clone instanceof PlotAxis);
    }
}