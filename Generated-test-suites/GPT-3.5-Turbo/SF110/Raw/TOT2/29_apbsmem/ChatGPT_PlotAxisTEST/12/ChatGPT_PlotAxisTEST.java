import org.junit.*;
import static org.junit.Assert.*;
import java.awt.*;
import java.text.*;

public class ChatGPT_PlotAxisTest {
    private PlotAxis plotAxis;

    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) { }
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
    }

    @Test
    public void testSetLabel() {
        String label = "x-axis";
        plotAxis.setLabel(label);
        assertEquals(label, plotAxis.getLabel());
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
        int style = 1;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        boolean flag = true;
        plotAxis.manualLowerBounds(flag);
        assertEquals(flag, plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        double value = 0.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        double value = 0.0;
        plotAxis.setLowerBounds(value);
        assertEquals(String.valueOf(value), plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        boolean flag = true;
        plotAxis.manualUpperBounds(flag);
        assertEquals(flag, plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        double value = 100.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        double value = 100.0;
        plotAxis.setUpperBounds(value);
        assertEquals(String.valueOf(value), plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        boolean flag = true;
        plotAxis.manualTickStepSize(flag);
        assertEquals(flag, plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        double value = 0.1;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testScaleCoord() {
        double x = 0.5;
        int expected = (int)(plotAxis.xA * plotAxis.scale.func(x) + plotAxis.xB);
        int actual = plotAxis.scaleCoord(x);
        assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        PlotAxis cloned = (PlotAxis) plotAxis.clone();
        assertNotSame(plotAxis, cloned);
        assertEquals(plotAxis.getClass(), cloned.getClass());
    }

    @Test
    public void testSetInvalidLabel() {
        String label = null;
        plotAxis.setLabel(label);
        assertNull(plotAxis.getLabel());
    }

    @Test
    public void testSetInvalidScale() {
        PlotAxisScale scale = null;
        plotAxis.setScale(scale);
        assertNull(plotAxis.getScale());
    }

    @Test
    public void testSetInvalidNumberFormat() {
        NumberFormat format = null;
        plotAxis.setNumberFormat(format);
        assertNull(plotAxis.getNumberFormat());
    }

    @Test
    public void testSetInvalidGridStyle() {
        int style = -1;
        plotAxis.setGridStyle(style);
        assertEquals(0, plotAxis.getGridStyle());
    }

    @Test
    public void testSetInvalidLowerBounds() {
        double value = -100.0;
        plotAxis.setLowerBounds(value);
        assertNotEquals(value, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testSetInvalidUpperBounds() {
        double value = -100.0;
        plotAxis.setUpperBounds(value);
        assertNotEquals(value, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testSetInvalidTickStepSize() {
        double value = -0.1;
        plotAxis.setTickStepSize(value);
        assertNotEquals(value, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testCloneDeepCopy() {
        PlotAxis cloned = (PlotAxis) plotAxis.clone();
        plotAxis.setLowerBounds(10.0);
        plotAxis.setUpperBounds(20.0);
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        PlotAxis cloned2 = (PlotAxis) cloned.clone();
        assertNotEquals(plotAxis.getLowerBounds(), cloned2.getLowerBounds());
        assertNotEquals(plotAxis.getUpperBounds(), cloned2.getUpperBounds());
        assertNotEquals(plotAxis.getScale(), cloned2.getScale());
    }
}