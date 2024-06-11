package jahuwaldt.plot;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setLabel("Test Label");
        String label = plotAxis.getLabel();
        assertEquals("Test Label", label);
    }

    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setLabel("Test Label");
        String label = plotAxis.getLabel();
        assertEquals("Test Label", label);
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new CustomPlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);

        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis plotAxis = new CustomPlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);

        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new CustomPlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);

        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new CustomPlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);

        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setGridStyle(PlotAxis.kNoGrid);

        assertEquals(PlotAxis.kNoGrid, plotAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setGridStyle(PlotAxis.kNoGrid);

        assertEquals(PlotAxis.kNoGrid, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.manualLowerBounds(true);

        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setLowerBounds(10.0);

        assertEquals(10.0, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setLowerBounds(10.0);

        assertEquals(10.0, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setLowerBounds(10.0);

        assertEquals("10.0", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.manualUpperBounds(true);

        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setUpperBounds(20.0);

        assertEquals(20.0, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setUpperBounds(20.0);

        assertEquals(20.0, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setUpperBounds(20.0);

        assertEquals("20.0", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.manualTickStepSize(true);

        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setTickStepSize(1.0);

        assertEquals(1.0, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new CustomPlotAxis();
        plotAxis.setTickStepSize(1.0);

        assertEquals(1.0, plotAxis.getTickStepSize(), 0.0);
    }

    private class CustomPlotAxis extends PlotAxis {
        @Override
        public void setAxisBounds(final PlotRunList p0) {
            // not implemented for test purposes
        }

        @Override
        protected void resizeAxis(final Rectangle p0) {
            // not implemented for test purposes
        }

        @Override
        public void draw(final Graphics p0, final Component p1, final Rectangle p2) {
            // not implemented for test purposes
        }
    }
}