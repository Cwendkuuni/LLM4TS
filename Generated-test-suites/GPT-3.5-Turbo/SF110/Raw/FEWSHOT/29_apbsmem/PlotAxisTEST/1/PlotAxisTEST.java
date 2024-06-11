
package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {
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

        axis.setLabel("Test label");
        assertEquals("Test label", axis.getLabel());
    }

    @Test
    public void testGetScale() {
        PlotAxis axis = new PlotAxis() {
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

        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {
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

        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {
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

        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {
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

        axis.manualLowerBounds(true);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
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

        axis.setLowerBounds(2.5);
        assertEquals(2.5, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
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

        axis.setLowerBounds(2.5);
        assertEquals("2.5", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {
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

        axis.manualUpperBounds(true);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
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

        axis.setUpperBounds(10.5);
        assertEquals(10.5, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
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

        axis.setUpperBounds(10.5);
        assertEquals("10.5", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {
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

        axis.manualTickStepSize(true);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
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

        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }
}
