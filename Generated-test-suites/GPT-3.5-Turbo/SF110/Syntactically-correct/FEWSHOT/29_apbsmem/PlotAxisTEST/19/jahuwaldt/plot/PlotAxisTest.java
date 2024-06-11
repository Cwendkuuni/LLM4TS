package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

import java.awt.*;
import java.io.*;
import java.text.*;
import jahuwaldt.plot.PlotAxis;
import jahuwaldt.plot.PlotAxisScale;
import jahuwaldt.plot.AxisLimitData;

public class PlotAxisTest {
    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLabel("testLabel");
        assertEquals("testLabel", plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLabel("testLabel");
        assertEquals("testLabel", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {};
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis plotAxis = new PlotAxis() {};
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {};
        int style = 2;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {};
        int style = 2;
        plotAxis.setGridStyle(style);
        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualLowerBounds(true);
        assertNull(plotAxis.getLowerBounds());
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        double value = 2.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        double value = 2.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {};
        double value = 2.0;
        plotAxis.setLowerBounds(value);
        assertEquals("2.0", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualUpperBounds(true);
        assertNull(plotAxis.getUpperBounds());
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        double value = 2.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        double value = 2.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {};
        double value = 2.0;
        plotAxis.setUpperBounds(value);
        assertEquals("2.0", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualTickStepSize(true);
        assertNull(plotAxis.getTickStepSize());
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        double value = 2.0;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        double value = 2.0;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }
    
    @Test
    public void testSetAxisBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                super.setAxisBounds(p0);
            }
        };
        PlotRunList p0 = new PlotRunList();
        plotAxis.setAxisBounds(p0);
        // Add assertions here
    }

    @Test
    public void testResizeAxis() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            protected void resizeAxis(Rectangle p0) {
                super.resizeAxis(p0);
            }
        };
        Rectangle p0 = new Rectangle();
        plotAxis.resizeAxis(p0);
        // Add assertions here
    }

    @Test
    public void testDraw() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                super.draw(p0, p1, p2);
            }
        };
        Graphics p0 = new Graphics();
        Component p1 = new Component();
        Rectangle p2 = new Rectangle();
        plotAxis.draw(p0, p1, p2);
        // Add assertions here
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public int scaleCoord(double x) {
                return super.scaleCoord(x);
            }
        };
        double x = 2.0;
        assertEquals(1, plotAxis.scaleCoord(x));
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public Object clone() {
                return super.clone();
            }
        };
        assertEquals(plotAxis, plotAxis.clone());
    }
}