import org.junit.Test;
import static org.junit.Assert.*;
import java.text.*;
import java.awt.*;

class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(null, plotAxis.getLabel());
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
        PlotAxisScale scale = new LinearAxisScale();
        assertEquals(scale, plotAxis.getScale());
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
        NumberFormat format = NumberFormat.getInstance();
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
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
        assertEquals(true, plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setLowerBounds(10.0);
        assertEquals(10.0, plotAxis.aLB, 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(Double.toString(plotAxis.aLB), plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.manualUpperBounds(true);
        assertEquals(true, plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setUpperBounds(20.0);
        assertEquals(20.0, plotAxis.aUB, 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(Double.toString(plotAxis.aUB), plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.manualTickStepSize(true);
        assertEquals(true, plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setTickStepSize(1.0);
        assertEquals(1.0, plotAxis.quantum, 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        assertEquals(1.0, plotAxis.getTickStepSize(), 0.0);
    }
    
    private static class PlotAxisImpl extends PlotAxis {

        @Override
        public void setAxisBounds(PlotRunList p0) {
            // Not needed for testing
        }

        @Override
        protected void resizeAxis(Rectangle p0) {
            // Not needed for testing
        }

        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
            // Not needed for testing
        }
    }
}