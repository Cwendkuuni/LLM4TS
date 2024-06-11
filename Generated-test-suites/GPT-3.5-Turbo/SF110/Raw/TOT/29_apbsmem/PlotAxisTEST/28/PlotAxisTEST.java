import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {
    
    private PlotAxis plotAxis;
    
    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                // implementation not required for test
            }
            protected void resizeAxis(Rectangle p0) {
                // implementation not required for test
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // implementation not required for test
            }
        };
    }
    
    @Test
    public void testSetLabel() {
        plotAxis.setLabel("X-axis");
        
        assertEquals("X-axis", plotAxis.getLabel());
    }
    
    @Test
    public void testGetScale() {
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        
        assertEquals(scale, plotAxis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        
        assertEquals(format, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        plotAxis.setGridStyle(PlotAxis.kNoGrid);
        
        assertEquals(PlotAxis.kNoGrid, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        plotAxis.manualLowerBounds(true);
        
        assertTrue(plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        plotAxis.setLowerBounds(0.0);
        
        assertEquals(0.0, plotAxis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        plotAxis.setLowerBounds(0.0);
        
        assertEquals("0.0", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        plotAxis.manualUpperBounds(true);
        
        assertTrue(plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        plotAxis.setUpperBounds(1.0);
        
        assertEquals(1.0, plotAxis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        plotAxis.setUpperBounds(1.0);
        
        assertEquals("1.0", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        plotAxis.manualTickStepSize(true);
        
        assertTrue(plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        plotAxis.setTickStepSize(0.1);
        
        assertEquals(0.1, plotAxis.getTickStepSize(), 0.001);
    }
    
    @Test
    public void testScaleCoord() {
        plotAxis.xA = 2.0;
        plotAxis.xB = 3.0;
        plotAxis.scale = new LinearAxisScale();
        
        int result = plotAxis.scaleCoord(2.5);
        
        assertEquals(8, result);
    }
    
    @Test
    public void testClone() {
        PlotAxis newPlotAxis = (PlotAxis)plotAxis.clone();
        
        assertNotSame(plotAxis, newPlotAxis);
    }
    
}