import static org.junit.Assert.*;
import org.junit.Test;
import java.text.NumberFormat;
import java.awt.*;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new TestPlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new TestPlotAxis();
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new TestPlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertSame(scale, axis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis axis = new TestPlotAxis();
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertSame(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new TestPlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertSame(format, axis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new TestPlotAxis();
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertSame(format, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new TestPlotAxis();
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new TestPlotAxis();
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new TestPlotAxis();
        axis.manualLowerBounds(true);
        assertTrue(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new TestPlotAxis();
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getLowerBounds(), 0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new TestPlotAxis();
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getLowerBounds(), 0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new TestPlotAxis();
        axis.setLowerBounds(0.0);
        assertEquals("0", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new TestPlotAxis();
        axis.manualUpperBounds(true);
        assertTrue(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new TestPlotAxis();
        axis.setUpperBounds(10.0);
        assertEquals(10.0, axis.getUpperBounds(), 0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new TestPlotAxis();
        axis.setUpperBounds(10.0);
        assertEquals(10.0, axis.getUpperBounds(), 0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new TestPlotAxis();
        axis.setUpperBounds(10.0);
        assertEquals("10", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new TestPlotAxis();
        axis.manualTickStepSize(true);
        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new TestPlotAxis();
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new TestPlotAxis();
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new TestPlotAxis();
        axis.setSize(new Rectangle());
        int coord = axis.scaleCoord(1.0);
        assertEquals(0, coord);
    }

    private class TestPlotAxis extends PlotAxis {
        public void setAxisBounds(final PlotRunList p0) {}
        protected void resizeAxis(final Rectangle p0) {}
        public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
    }
}