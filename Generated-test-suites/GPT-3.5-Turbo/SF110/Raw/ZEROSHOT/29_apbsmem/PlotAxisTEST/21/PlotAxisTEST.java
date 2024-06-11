import org.junit.*;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {};
        axis.setLabel("Label");
        assertEquals("Label", axis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {};
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {};
        axis.setGridStyle(2);
        assertEquals(2, axis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        axis.manualLowerBounds(true);
        assertTrue(axis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        axis.setLowerBounds(1.0);
        assertEquals(1.0, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        axis.manualUpperBounds(true);
        assertTrue(axis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        axis.setUpperBounds(1.0);
        assertEquals(1.0, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        axis.manualTickStepSize(true);
        assertTrue(axis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getTickStepSize(), 0.001);
    }
    
}