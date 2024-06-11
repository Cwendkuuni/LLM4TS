package jahuwaldt.plot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
public class PlotAxisTest {
    @Test
    public void testConstructor1() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertNull(plotAxis.getLabel());
        assertEquals(1, plotAxis.getGridStyle());
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        assertEquals(1, plotAxis.getTickStepSize(), 0);
        assertEquals(1.0, plotAxis.getScale().func(1.0), 0);
    }
    
    @Test
    public void testConstructor2() {
        String label = "Label";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 2;
        
        PlotAxis plotAxis = new PlotAxis(label, scale, nf, gridStyle){};
        assertEquals(label, plotAxis.getLabel());
        assertEquals(gridStyle, plotAxis.getGridStyle());
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        assertEquals(1, plotAxis.getTickStepSize(), 0);
        assertEquals(1.0, plotAxis.getScale().func(1.0), 0);
    }
    
    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertNull(plotAxis.getLabel());
        plotAxis.setLabel("New Label");
        assertEquals("New Label", plotAxis.getLabel());
    }
    
    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertNull(plotAxis.getLabel());
        plotAxis.setLabel("New Label");
        assertEquals("New Label", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(1.0, plotAxis.getScale().func(1.0), 0);
        plotAxis.setScale(new LinearAxisScale());
        assertEquals(1.0, plotAxis.getScale().func(1.0), 0);
    }
    
    @Test
    public void testGetScale() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(1.0, plotAxis.getScale().func(1.0), 0);
        plotAxis.setScale(new LinearAxisScale());
        assertEquals(1.0, plotAxis.getScale().func(1.0), 0);
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        plotAxis.setNumberFormat(NumberFormat.getCurrencyInstance());
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
    }
    
    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        plotAxis.setNumberFormat(NumberFormat.getCurrencyInstance());
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(1, plotAxis.getGridStyle(), 0);
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle(), 0);
    }
    
    @Test
    public void testGetGridStyle() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(1, plotAxis.getGridStyle(), 0);
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle(), 0);
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertFalse(plotAxis.manualLowerBounds());
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLowerBounds());
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        plotAxis.setLowerBounds(10);
        assertEquals(10, plotAxis.getLowerBounds(), 0);
    }
    
    @Test
    public void testGetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        plotAxis.setLowerBounds(10);
        assertEquals(10, plotAxis.getLowerBounds(), 0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        plotAxis.setLowerBounds(10);
        assertEquals("10.0", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertFalse(plotAxis.manualUpperBounds());
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUpperBounds());
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        plotAxis.setUpperBounds(100);
        assertEquals(100, plotAxis.getUpperBounds(), 0);
    }
    
    @Test
    public void testGetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        plotAxis.setUpperBounds(100);
        assertEquals(100, plotAxis.getUpperBounds(), 0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        plotAxis.setUpperBounds(100);
        assertEquals("100.0", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertFalse(plotAxis.manualTickStepSize());
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualTickStepSize());
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(1, plotAxis.getTickStepSize(), 0);
        plotAxis.setTickStepSize(2);
        assertEquals(2, plotAxis.getTickStepSize(), 0);
    }
    
    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(1, plotAxis.getTickStepSize(), 0);
        plotAxis.setTickStepSize(2);
        assertEquals(2, plotAxis.getTickStepSize(), 0);
    }
    
    @Test
    public void testSetAxisBounds() {
        PlotAxis plotAxis = new PlotAxis(){};
        PlotRunList p = new PlotRunList();
        plotAxis.setAxisBounds(p);
        assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0);
        assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0);
        assertEquals(1, plotAxis.getTickStepSize(), 0);
    }
    
    @Test
    public void testSetSize() {
        PlotAxis plotAxis = new PlotAxis(){};
        Rectangle plotFrame = new Rectangle();
        plotAxis.setSize(plotFrame);
        assertEquals(1, plotAxis.getTickStepSize(), 0);
    }
    
    @Test
    public void testResizeAxis() {
        PlotAxis plotAxis = new PlotAxis(){};
        Rectangle plotFrame = new Rectangle();
        plotAxis.resizeAxis(plotFrame);
        assertEquals(1, plotAxis.getTickStepSize(), 0);
    }
    
    @Test
    public void testDraw() {
        PlotAxis plotAxis = new PlotAxis(){};
        Graphics g = new Graphics();
        Component c = new Component();
        Rectangle r = new Rectangle();
        plotAxis.draw(g, c, r);
        assertEquals(1, plotAxis.getTickStepSize(), 0);
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(1, plotAxis.scaleCoord(1), 0);
    }
    
    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis(){};
        assertEquals(1.0, plotAxis.getTickStepSize(), 0);
        PlotAxis clone = (PlotAxis) plotAxis.clone();
        assertEquals(1.0, clone.getTickStepSize(), 0);
    }
}