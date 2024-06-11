import org.junit.Assert;
import org.junit.Test;

import java.awt.Rectangle;
import java.io.Serializable;
import java.text.NumberFormat;

public class PlotAxisTest implements Serializable {

    @Test
    public void testConstructor1() {
        PlotAxis plotAxis = new PlotAxisImpl();
        Assert.assertEquals(1.0, plotAxis.xA, 0.0001);
        Assert.assertEquals(1.0, plotAxis.xB, 0.0001);
        Assert.assertEquals(Double.MAX_VALUE, plotAxis.aLB, 0.0001);
        Assert.assertEquals(-1.7976931348623157E308, plotAxis.aUB, 0.0001);
        Assert.assertEquals(1, plotAxis.gridStyle);
        Assert.assertNotNull(plotAxis.scale);
        Assert.assertNotNull(plotAxis.nf);
    }

    @Test
    public void testConstructor2() {
        String label = "Test Label";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 2;

        PlotAxis plotAxis = new PlotAxisImpl(label, scale, nf, gridStyle);

        Assert.assertEquals(label, plotAxis.getLabel());
        Assert.assertEquals(scale, plotAxis.getScale());
        Assert.assertEquals(nf, plotAxis.getNumberFormat());
        Assert.assertEquals(gridStyle, plotAxis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxisImpl();
        String label = "Test Label";
        plotAxis.setLabel(label);
        Assert.assertEquals(label, plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxisImpl();
        String label = plotAxis.getLabel();
        Assert.assertNull(label);
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new LogarithmicAxisScale();
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setScale(scale);
        Assert.assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis plotAxis = new PlotAxisImpl();
        PlotAxisScale scale = plotAxis.getScale();
        Assert.assertNotNull(scale);
    }

    @Test
    public void testSetNumberFormat() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.setNumberFormat(nf);
        Assert.assertEquals(nf, plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new PlotAxisImpl();
        NumberFormat nf = plotAxis.getNumberFormat();
        Assert.assertNotNull(nf);
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxisImpl();
        int gridStyle = 0;
        plotAxis.setGridStyle(gridStyle);
        Assert.assertEquals(gridStyle, plotAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        PlotAxis plotAxis = new PlotAxisImpl();
        int gridStyle = plotAxis.getGridStyle();
        Assert.assertEquals(1, gridStyle);
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.manualLowerBounds(true);
        Assert.assertTrue(plotAxis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        double lowerBounds = 1.0;
        plotAxis.setLowerBounds(lowerBounds);
        Assert.assertEquals(lowerBounds, plotAxis.getLowerBounds(), 0.0001);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        double lowerBounds = plotAxis.getLowerBounds();
        Assert.assertEquals(Double.MAX_VALUE, lowerBounds, 0.0001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxisImpl();
        String lowerBoundsAsString = plotAxis.lowerBoundsAsString();
        Assert.assertEquals("1.0", lowerBoundsAsString);
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.manualUpperBounds(true);
        Assert.assertTrue(plotAxis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        double upperBounds = 2.0;
        plotAxis.setUpperBounds(upperBounds);
        Assert.assertEquals(upperBounds, plotAxis.getUpperBounds(), 0.0001);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        double upperBounds = plotAxis.getUpperBounds();
        Assert.assertEquals(-1.7976931348623157E308, upperBounds, 0.0001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxisImpl();
        String upperBoundsAsString = plotAxis.upperBoundsAsString();
        Assert.assertEquals("-1.7976931348623157E308", upperBoundsAsString);
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        plotAxis.manualTickStepSize(true);
        Assert.assertTrue(plotAxis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        double tickStepSize = 0.1;
        plotAxis.setTickStepSize(tickStepSize);
        Assert.assertEquals(tickStepSize, plotAxis.getTickStepSize(), 0.0001);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        double tickStepSize = plotAxis.getTickStepSize();
        Assert.assertEquals(0.0, tickStepSize, 0.0001);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis plotAxis = new PlotAxisImpl();
        PlotRunList p0 = new PlotRunList();
        plotAxis.setAxisBounds(p0);
        // Assert specific behavior for the implementation of setAxisBounds
    }

    @Test
    public void testSetSize() {
        PlotAxis plotAxis = new PlotAxisImpl();
        Rectangle plotFrame = new Rectangle();
        plotAxis.setSize(plotFrame);
        // Assert specific behavior for the implementation of setSize
    }

    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxisImpl();
        double x = 1.0;
        int scaleCoord = plotAxis.scaleCoord(x);
        // Assert specific behavior for the implementation of scaleCoord
    }

    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxisImpl();
        PlotAxis newObject = (PlotAxis) plotAxis.clone();

        Assert.assertNotNull(newObject);
        Assert.assertNotSame(plotAxis, newObject);
    }

    private static class PlotAxisImpl extends PlotAxis {
        @Override
        public void setAxisBounds(PlotRunList p0) {
            // Implementation not relevant for this test
        }

        @Override
        protected void resizeAxis(Rectangle p0) {
            // Implementation not relevant for this test
        }

        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
            // Implementation not relevant for this test
        }
    }
}