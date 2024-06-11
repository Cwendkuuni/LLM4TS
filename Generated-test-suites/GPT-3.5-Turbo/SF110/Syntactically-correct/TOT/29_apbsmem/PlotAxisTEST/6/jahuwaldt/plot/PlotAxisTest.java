import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLabel("X Axis");
        
        assertEquals("X Axis", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {};
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        
        assertEquals(scale, plotAxis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        
        assertEquals(format, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setGridStyle(1);
        
        assertEquals(1, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualLowerBounds(true);
        
        assertTrue(plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLowerBounds(0.0);
        
        assertEquals(0.0, plotAxis.getLowerBounds(), 0.0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLowerBounds(5.5);
        
        assertEquals("5.5", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualUpperBounds(true);
        
        assertTrue(plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setUpperBounds(10.0);
        
        assertEquals(10.0, plotAxis.getUpperBounds(), 0.0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setUpperBounds(15.5);
        
        assertEquals("15.5", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualTickStepSize(true);
        
        assertTrue(plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setTickStepSize(0.5);
        
        assertEquals(0.5, plotAxis.getTickStepSize(), 0.0);
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.xA = 2.0;
        plotAxis.xB = 3.0;
        
        assertEquals(7, plotAxis.scaleCoord(2.0));
        assertEquals(13, plotAxis.scaleCoord(5.0));
    }
    
    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {};
        PlotAxis clonedPlotAxis = (PlotAxis) plotAxis.clone();
        
        assertNotSame(plotAxis, clonedPlotAxis);
        assertEquals(plotAxis.getClass(), clonedPlotAxis.getClass());
    }
}