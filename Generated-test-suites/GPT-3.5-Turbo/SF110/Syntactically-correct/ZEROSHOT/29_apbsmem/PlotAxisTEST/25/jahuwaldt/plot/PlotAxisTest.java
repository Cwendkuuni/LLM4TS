package jahuwaldt.plot;

import static org.junit.Assert.*;
import org.junit.*;

public class PlotAxisTest {

    private PlotAxis plotAxis;

    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
    }

    @Test
    public void testSetLabel() {
        plotAxis.setLabel("TestLabel");
        assertEquals("TestLabel", plotAxis.getLabel());
    }

    @Test
    public void testSetAndGetScale() {
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetAndGetNumberFormat() {
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetAndGetGridStyle() {
        int style = 2;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetAndGetLowerBounds() {
        double value = 10.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        double value = 10.0;
        plotAxis.setLowerBounds(value);
        assertEquals("10", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetAndGetUpperBounds() {
        double value = 100.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        double value = 100.0;
        plotAxis.setUpperBounds(value);
        assertEquals("100", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetAndGetTickStepSize() {
        double value = 2.0;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testScaleCoord() {
        plotAxis.xA = 2.0;
        plotAxis.xB = 3.0;
        plotAxis.scale = new PlotAxisScale() {
            @Override
            public double func(double x) {
                return x * 2.0;
            }
            @Override
            public AxisLimitData findGoodLimits(double lb, double ub) {
                return null;
            }
        };
        assertEquals(10, plotAxis.scaleCoord(4.0));
    }

    @Test
    public void testClone() {
        PlotAxis clone = (PlotAxis) plotAxis.clone();
        assertNotNull(clone);
        assertNotSame(plotAxis, clone);
    }
}