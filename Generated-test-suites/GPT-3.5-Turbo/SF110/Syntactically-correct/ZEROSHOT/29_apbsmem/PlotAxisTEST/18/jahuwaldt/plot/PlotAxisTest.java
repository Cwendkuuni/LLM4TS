import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.text.*;
import java.awt.*;

public class PlotAxisTest {
  
  private PlotAxis plotAxis;
  
  @Before
  public void setUp() {
    plotAxis = new PlotAxisStub("Test Axis", null, null, 0);
  }
  
  @Test
  public void testSetLabel() {
    plotAxis.setLabel("New Label");
    assertEquals("New Label", plotAxis.getLabel());
  }
  
  @Test
  public void testSetScale() {
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
    plotAxis.setGridStyle(2);
    assertEquals(2, plotAxis.getGridStyle());
  }
  
  @Test
  public void testManualLowerBounds() {
    plotAxis.manualLowerBounds(true);
    assertTrue(plotAxis.manualLB);
  }
  
  @Test
  public void testSetLowerBounds() {
    plotAxis.setLowerBounds(10.0);
    assertEquals(10.0, plotAxis.getLowerBounds(), 0.001);
  }
  
  @Test
  public void testGetLowerBounds() {
    assertEquals(Double.MAX_VALUE, plotAxis.getLowerBounds(), 0.001);
  }
  
  @Test
  public void testLowerBoundsAsString() {
    plotAxis.setLowerBounds(10.0);
    assertEquals("10.0", plotAxis.lowerBoundsAsString());
  }
  
  @Test
  public void testManualUpperBounds() {
    plotAxis.manualUpperBounds(true);
    assertTrue(plotAxis.manualUB);
  }
  
  @Test
  public void testSetUpperBounds() {
    plotAxis.setUpperBounds(20.0);
    assertEquals(20.0, plotAxis.getUpperBounds(), 0.001);
  }
  
  @Test
  public void testGetUpperBounds() {
    assertEquals(-1.7976931348623157E308, plotAxis.getUpperBounds(), 0.001);
  }
  
  @Test
  public void testUpperBoundsAsString() {
    plotAxis.setUpperBounds(20.0);
    assertEquals("20.0", plotAxis.upperBoundsAsString());
  }
  
  @Test
  public void testManualTickStepSize() {
    plotAxis.manualTickStepSize(true);
    assertTrue(plotAxis.manualQuant);
  }
  
  @Test
  public void testSetTickStepSize() {
    plotAxis.setTickStepSize(1.0);
    assertEquals(1.0, plotAxis.getTickStepSize(), 0.001);
  }
  
  @Test
  public void testGetTickStepSize() {
    assertEquals(0.0, plotAxis.getTickStepSize(), 0.001);
  }
  
  class PlotAxisStub extends PlotAxis {
    
    public PlotAxisStub(String label, PlotAxisScale scale, NumberFormat nf, int gridStyle) {
      super(label, scale, nf, gridStyle);
    }
    
    public void setAxisBounds(PlotRunList p0) {
      
    }
    
    public void resizeAxis(Rectangle p0) {
      
    }
    
    public void draw(Graphics p0, Component p1, Rectangle p2) {
      
    }
  }
}