package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {

    private PlotAxis plotAxis;
    
    @Before
    public void setUp() {
        plotAxis = new PlotAxisSubclass("Test Axis", new LinearAxisScale(), NumberFormat.getInstance(), PlotAxis.kTickMarksOnly);
    }
    
    @Test
    public void testSetLabel() {
        plotAxis.setLabel("New Label");
        assertEquals("New Label", plotAxis.getLabel());
    }

    @Test
    public void testGetLabel() {
        assertEquals("Test Axis", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxisScale scale = new LogarithmicAxisScale();
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }

    @Test
    public void testGetScale() {
        assertEquals(new LinearAxisScale(), plotAxis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        NumberFormat format = NumberFormat.getCurrencyInstance();
        plotAxis.setNumberFormat(format);
        assertEquals(format, plotAxis.getNumberFormat());
    }

    @Test
    public void testGetNumberFormat() {
        assertEquals(NumberFormat.getInstance(), plotAxis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
    }

    @Test
    public void testGetGridStyle() {
        assertEquals(PlotAxis.kTickMarksOnly, plotAxis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        assertEquals(true, plotAxis.isManualLowerBounds());
    }

    @Test
    public void testSetLowerBounds() {
        plotAxis.setLowerBounds(0.5);
        assertEquals(0.5, plotAxis.getLowerBounds(), 0.01);
    }

    @Test
    public void testGetLowerBounds() {
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.01);
    }

    @Test
    public void testLowerBoundsAsString() {
        assertEquals("3.4028235E38", plotAxis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        assertEquals(true, plotAxis.isManualUpperBounds());
    }

    @Test
    public void testSetUpperBounds() {
        plotAxis.setUpperBounds(10.5);
        assertEquals(10.5, plotAxis.getUpperBounds(), 0.01);
    }

    @Test
    public void testGetUpperBounds() {
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.01);
    }

    @Test
    public void testUpperBoundsAsString() {
        assertEquals("-1.7976931348623157E308", plotAxis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        assertEquals(true, plotAxis.isManualTickStepSize());
    }

    @Test
    public void testSetTickStepSize() {
        plotAxis.setTickStepSize(1.5);
        assertEquals(1.5, plotAxis.getTickStepSize(), 0.01);
    }

    @Test
    public void testGetTickStepSize() {
        assertEquals(1.0, plotAxis.getTickStepSize(), 0.01);
    }

    @Test
    public void testSetAxisBounds() {
        PlotRunList plotRunList = new PlotRunList();
        plotAxis.setAxisBounds(plotRunList);
        // TODO: Add assertions
    }

    @Test
    public void testSetSize() {
        Rectangle rectangle = new Rectangle();
        plotAxis.setSize(rectangle);
        // TODO: Add assertions
    }

    @Test
    public void testScaleCoord() {
        assertEquals(5, plotAxis.scaleCoord(1.0));
        // TODO: Add more assertions
    }
    

    private class PlotAxisSubclass extends PlotAxis {
        public PlotAxisSubclass(String label, PlotAxisScale scale, NumberFormat nf, int gridStyle) {
            super(label, scale, nf, gridStyle);
        }
        
        @Override
        public void setAxisBounds(PlotRunList p0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        protected void resizeAxis(Rectangle p0) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
            // TODO Auto-generated method stub
            
        }
    }

}