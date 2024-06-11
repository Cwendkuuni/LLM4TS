package jahuwaldt.plot;

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
    public void testSetLabel() {
        plotAxis.setLabel("test label");
        assertEquals("test label", plotAxis.getLabel());
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
        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        plotAxis.setLowerBounds(10.5);
        assertEquals(10.5, plotAxis.getLowerBounds(), 0.01);
    }

    @Test
    public void testLowerBoundsAsString() {
        plotAxis.setLowerBounds(10.5);
        assertEquals("10.5", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        plotAxis.setUpperBounds(20.5);
        assertEquals(20.5, plotAxis.getUpperBounds(), 0.01);
    }

    @Test
    public void testUpperBoundsAsString() {
        plotAxis.setUpperBounds(20.5);
        assertEquals("20.5", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        plotAxis.setTickStepSize(0.5);
        assertEquals(0.5, plotAxis.getTickStepSize(), 0.01);
    }

    @Test
    public void testScaleCoord() {
        plotAxis.xA = 2.0;
        plotAxis.xB = 3.0;
        plotAxis.setScale(new LinearAxisScale());

        int scaleCoord = plotAxis.scaleCoord(5.0);
        assertEquals(13, scaleCoord);
    }
}