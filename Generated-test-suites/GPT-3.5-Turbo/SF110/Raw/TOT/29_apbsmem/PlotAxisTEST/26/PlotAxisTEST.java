import static org.junit.Assert.*;
import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        String label = "X-axis";
        plotAxis.setLabel(label);

        assertEquals(label, plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);

        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);

        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        int style = kTickMarksOnly;
        plotAxis.setGridStyle(style);

        assertEquals(style, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        boolean flag = true;
        plotAxis.manualLowerBounds(flag);

        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        double value = 10.5;
        plotAxis.setLowerBounds(value);

        assertEquals(value, plotAxis.getLowerBounds(), 0.0001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        double value = 10.5;
        plotAxis.setLowerBounds(value);

        assertEquals(String.valueOf(value), plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        boolean flag = true;
        plotAxis.manualUpperBounds(flag);

        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        double value = 10.5;
        plotAxis.setUpperBounds(value);

        assertEquals(value, plotAxis.getUpperBounds(), 0.0001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        double value = 10.5;
        plotAxis.setUpperBounds(value);

        assertEquals(String.valueOf(value), plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        boolean flag = true;
        plotAxis.manualTickStepSize(flag);

        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        double value = 0.5;
        plotAxis.setTickStepSize(value);

        assertEquals(value, plotAxis.getTickStepSize(), 0.0001);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        double x = 10.5;
        int scaleCoord = plotAxis.scaleCoord(x);

        assertEquals((int)(plotAxis.xA * plotAxis.scale.func(x) + plotAxis.xB), scaleCoord);
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // Do nothing
            }

            protected void resizeAxis(Rectangle p0) {
                // Do nothing
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // Do nothing
            }
        };

        PlotAxis clonedPlotAxis = (PlotAxis) plotAxis.clone();

        assertEquals(plotAxis.getClass(), clonedPlotAxis.getClass());
    }
}