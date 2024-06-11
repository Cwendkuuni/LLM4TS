package jahuwaldt.plot;

import org.junit.Test;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Graphics;
import java.text.NumberFormat;

import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.setLabel("X Axis");
        assertEquals("X Axis", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.setLowerBounds(0.0);
        assertEquals(0.0, plotAxis.getLowerBounds(), 0.0001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.setLowerBounds(0.0);
        assertEquals("0.0", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.setUpperBounds(100.0);
        assertEquals(100.0, plotAxis.getUpperBounds(), 0.0001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.setUpperBounds(100.0);
        assertEquals("100.0", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        plotAxis.setTickStepSize(0.1);
        assertEquals(0.1, plotAxis.getTickStepSize(), 0.0001);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        int scaleCoord = plotAxis.scaleCoord(50.0);
        assertEquals(1, scaleCoord);
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList plotRunList) {

            }

            @Override
            protected void resizeAxis(Rectangle rectangle) {

            }

            @Override
            public void draw(Graphics graphics, Component component, Rectangle rectangle) {

            }
        };

        PlotAxis clonedAxis = (PlotAxis) plotAxis.clone();
        assertNotNull(clonedAxis);
    }

}