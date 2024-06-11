package jahuwaldt.plot;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.text.NumberFormat;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        String label = "new label";
        plotAxis.setLabel(label);

        Assert.assertEquals(label, plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);

        Assert.assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        NumberFormat numberFormat = NumberFormat.getInstance();
        plotAxis.setNumberFormat(numberFormat);

        Assert.assertEquals(numberFormat, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        int gridStyle = 2;
        plotAxis.setGridStyle(gridStyle);

        Assert.assertEquals(gridStyle, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        boolean flag = true;
        plotAxis.manualLowerBounds(flag);

        Assert.assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        double value = 1.0;
        plotAxis.setLowerBounds(value);

        Assert.assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        boolean flag = true;
        plotAxis.manualUpperBounds(flag);

        Assert.assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        double value = 1.0;
        plotAxis.setUpperBounds(value);

        Assert.assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        boolean flag = true;
        plotAxis.manualTickStepSize(flag);

        Assert.assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        double value = 1.0;
        plotAxis.setTickStepSize(value);

        Assert.assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }

    // Additional Test Cases can be added to cover all methods in the PlotAxis class

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {}

            @Override
            protected void resizeAxis(Rectangle p0) {}

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };

        PlotAxis clonedAxis = (PlotAxis) plotAxis.clone();

        Assert.assertNotSame(plotAxis, clonedAxis);
    }
}