package jahuwaldt.plot;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setScale(new LinearAxisScale());
        assertTrue(plotAxis.getScale() instanceof LinearAxisScale);
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setNumberFormat(NumberFormat.getInstance());
        assertNotNull(plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setLowerBounds(10);
        assertEquals(10.0, plotAxis.getLowerBounds(), 0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setLowerBounds(10.567);
        assertEquals("10.57", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setUpperBounds(50);
        assertEquals(50.0, plotAxis.getUpperBounds(), 0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setUpperBounds(50.1234);
        assertEquals("50.12", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
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
        plotAxis.setTickStepSize(0.5);
        assertEquals(0.5, plotAxis.getTickStepSize(), 0);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {
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
        int result = plotAxis.scaleCoord(10.5);
        // Can't test the exact value since it depends on the implementation of scaleCoord method
        assertNotNull(result);
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {
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
        PlotAxis clonedAxis = (PlotAxis) plotAxis.clone();
        assertNotSame(plotAxis, clonedAxis);
    }
}