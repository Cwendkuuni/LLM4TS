package jahuwaldt.plot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.text.NumberFormat;

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
    public void testSetLabel() {
        String label = "Test Label";
        plotAxis.setLabel(label);
        Assert.assertEquals(label, plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        Assert.assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        plotAxis.setNumberFormat(numberFormat);
        Assert.assertEquals(numberFormat, plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        int gridStyle = 2;
        plotAxis.setGridStyle(gridStyle);
        Assert.assertEquals(gridStyle, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        boolean flag = true;
        plotAxis.manualLowerBounds(flag);
        Assert.assertEquals(flag, plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        double value = 10.0;
        plotAxis.setLowerBounds(value);
        Assert.assertEquals(value, plotAxis.getLowerBounds(), 0.0001);
    }

    @Test
    public void testLowerBoundsAsString() {
        // TODO: Implement this test
    }

    @Test
    public void testSetUpperBounds() {
        double value = 20.0;
        plotAxis.setUpperBounds(value);
        Assert.assertEquals(value, plotAxis.getUpperBounds(), 0.0001);
    }

    @Test
    public void testUpperBoundsAsString() {
        // TODO: Implement this test
    }

    @Test
    public void testManualTickStepSize() {
        boolean flag = true;
        plotAxis.manualTickStepSize(flag);
        Assert.assertEquals(flag, plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        double value = 0.5;
        plotAxis.setTickStepSize(value);
        Assert.assertEquals(value, plotAxis.getTickStepSize(), 0.0001);
    }

}