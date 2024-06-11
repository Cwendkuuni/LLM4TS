import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

    private PlotAxis plotAxis;

    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }
            protected void resizeAxis(Rectangle p0) {
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
    }

    @Test
    public void testSetLabel() {
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
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
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        plotAxis.setLowerBounds(10.0);
        assertEquals(10.0, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        plotAxis.setLowerBounds(10.0);
        assertEquals(10.0, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        plotAxis.setLowerBounds(10.0);
        assertEquals("10.0", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        plotAxis.setUpperBounds(20.0);
        assertEquals(20.0, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        plotAxis.setUpperBounds(20.0);
        assertEquals(20.0, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        plotAxis.setUpperBounds(20.0);
        assertEquals("20.0", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        plotAxis.setTickStepSize(5.0);
        assertEquals(5.0, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        plotAxis.setTickStepSize(5.0);
        assertEquals(5.0, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testClone() {
        PlotAxis newAxis = (PlotAxis) plotAxis.clone();
        assertEquals(plotAxis.getClass(), newAxis.getClass());
    }
}