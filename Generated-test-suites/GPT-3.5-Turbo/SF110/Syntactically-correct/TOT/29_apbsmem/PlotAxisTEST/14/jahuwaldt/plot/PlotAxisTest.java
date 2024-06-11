import static org.junit.Assert.*;
import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        axis.setLabel("x-axis");

        assertEquals("x-axis", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);

        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        NumberFormat format = NumberFormat.getCurrencyInstance();
        axis.setNumberFormat(format);

        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        axis.setGridStyle(PlotAxis.kMajorGrid);

        assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        axis.manualLowerBounds(true);

        assertTrue(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        axis.setLowerBounds(0.5);

        assertEquals(0.5, axis.getLowerBounds(), 0);
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        axis.manualUpperBounds(true);

        assertTrue(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        axis.setUpperBounds(10.0);

        assertEquals(10.0, axis.getUpperBounds(), 0);
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        axis.manualTickStepSize(true);

        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            public void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        axis.setTickStepSize(0.1);

        assertEquals(0.1, axis.getTickStepSize(), 0);
    }
}