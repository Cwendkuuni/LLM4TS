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
        String label = "test label";
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
        NumberFormat format = NumberFormat.getCurrencyInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        int style = 2;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        boolean flag = false;
        plotAxis.manualLowerBounds(flag);
        assertEquals(flag, plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        double value = 10.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        double value = -5.0;
        plotAxis.setLowerBounds(value);
        assertEquals(String.valueOf(value), plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        boolean flag = false;
        plotAxis.manualUpperBounds(flag);
        assertEquals(flag, plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        double value = 200.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        double value = 150.0;
        plotAxis.setUpperBounds(value);
        assertEquals(String.valueOf(value), plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        boolean flag = false;
        plotAxis.manualTickStepSize(flag);
        assertEquals(flag, plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        double value = 0.5;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testScaleCoord() {
        double x = 0.8;
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
}