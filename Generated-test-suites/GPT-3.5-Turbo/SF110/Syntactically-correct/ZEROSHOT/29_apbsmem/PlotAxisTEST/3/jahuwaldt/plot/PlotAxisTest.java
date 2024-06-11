package jahuwaldt.plot;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.text.NumberFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
    public void setLabel_shouldSetLabel() {
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }

    @Test
    public void getLabel_shouldReturnLabel() {
        String label = plotAxis.getLabel();
        assertNull(label);
    }

    @Test
    public void setScale_withNonNullScale_shouldSetScale() {
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void setScale_withNullScale_shouldSetDefaultScale() {
        plotAxis.setScale(null);
        assertNotNull(plotAxis.getScale());
    }

    @Test
    public void getScale_shouldReturnScale() {
        PlotAxisScale scale = plotAxis.getScale();
        assertNotNull(scale);
    }

    @Test
    public void setNumberFormat_withNonNullFormat_shouldSetFormat() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void setNumberFormat_withNullFormat_shouldSetDefaultFormat() {
        plotAxis.setNumberFormat(null);
        assertNotNull(plotAxis.getNumberFormat());
    }

    @Test
    public void getNumberFormat_shouldReturnFormat() {
        NumberFormat format = plotAxis.getNumberFormat();
        assertNotNull(format);
    }

    @Test
    public void setGridStyle_shouldSetGridStyle() {
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle());
    }

    @Test
    public void getGridStyle_shouldReturnGridStyle() {
        int style = plotAxis.getGridStyle();
        assertEquals(1, style);
    }

    @Test
    public void manualLowerBounds_shouldSetManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        assertEquals(true, plotAxis.manualLB);
    }

    @Test
    public void setLowerBounds_shouldSetLowerBounds() {
        plotAxis.setLowerBounds(10);
        assertEquals(10, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void getLowerBounds_shouldReturnLowerBounds() {
        double lowerBounds = plotAxis.getLowerBounds();
        assertEquals(Double.MAX_VALUE, lowerBounds, 0.0);
    }

    @Test
    public void lowerBoundsAsString_shouldReturnLowerBoundsAsString() {
        String lowerBounds = plotAxis.lowerBoundsAsString();
        assertEquals(String.valueOf(Double.MAX_VALUE), lowerBounds);
    }

    @Test
    public void manualUpperBounds_shouldSetManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        assertEquals(true, plotAxis.manualUB);
    }

    @Test
    public void setUpperBounds_shouldSetUpperBounds() {
        plotAxis.setUpperBounds(20);
        assertEquals(20, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void getUpperBounds_shouldReturnUpperBounds() {
        double upperBounds = plotAxis.getUpperBounds();
        assertEquals(-1.7976931348623157E308, upperBounds, 0.0);
    }

    @Test
    public void upperBoundsAsString_shouldReturnUpperBoundsAsString() {
        String upperBounds = plotAxis.upperBoundsAsString();
        assertEquals(String.valueOf(-1.7976931348623157E308), upperBounds);
    }

    @Test
    public void manualTickStepSize_shouldSetManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        assertEquals(true, plotAxis.manualQuant);
    }

    @Test
    public void setTickStepSize_shouldSetTickStepSize() {
        plotAxis.setTickStepSize(5);
        assertEquals(5, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void getTickStepSize_shouldReturnTickStepSize() {
        double tickStepSize = plotAxis.getTickStepSize();
        assertEquals(1.0, tickStepSize, 0.0);
    }
}