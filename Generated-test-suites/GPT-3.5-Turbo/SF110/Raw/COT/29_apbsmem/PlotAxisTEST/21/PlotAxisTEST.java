import static org.junit.Assert.*;
import org.junit.Test;
import jahuwaldt.plot.*;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Graphics;
import java.text.NumberFormat;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.setLabel("Test Label");

        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.setScale(new LinearAxisScale());

        assertEquals(LinearAxisScale.class, axis.getScale().getClass());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.setNumberFormat(NumberFormat.getInstance());

        assertEquals(NumberFormat.getInstance(), axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.setGridStyle(2);

        assertEquals(2, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.manualLowerBounds(true);

        assertTrue(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.setLowerBounds(10.5);

        assertEquals(10.5, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.manualUpperBounds(true);

        assertTrue(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.setUpperBounds(20.0);

        assertEquals(20.0, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.manualTickStepSize(true);

        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
            @Override
            public void setAxisBounds(PlotRunList p0) {}
            @Override
            protected void resizeAxis(Rectangle p0) {}
        };

        axis.setTickStepSize(0.5);

        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }
}