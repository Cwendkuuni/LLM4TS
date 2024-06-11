import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

    private PlotAxis plotAxis;

    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                //Implementation not required for test
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                //Implementation not required for test
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                //Implementation not required for test
            }
        };
    }

    @Test
    public void testSetLabel() {
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
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
    public void testGetScale() {
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
    public void testGetNumberFormat() {
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
    public void testGetGridStyle() {
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
        plotAxis.setTickStepSize(0.05);
        assertEquals(0.05, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        plotAxis.setTickStepSize(0.05);
        assertEquals(0.05, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testClone() {
        PlotAxis clonedAxis = (PlotAxis) plotAxis.clone();
        assertEquals(plotAxis.getClass(), clonedAxis.getClass());
    }
}