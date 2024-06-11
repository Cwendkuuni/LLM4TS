package de.progra.charting;
import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultChartTest {
  
  @Test
  public void testConstructor1() {
    DefaultChart defaultChart = new DefaultChart(null, "Title");
    assertNotNull(defaultChart);
    assertEquals("Title", defaultChart.getTitle().getText());
    assertNull(defaultChart.getCoordSystem());
  }
  
  @Test
  public void testConstructor2() {
    DefaultChart defaultChart = new DefaultChart(null, "Title", DefaultChart.LINEAR_X_LINEAR_Y);
    assertNotNull(defaultChart);
    assertEquals("Title", defaultChart.getTitle().getText());
    assertNotNull(defaultChart.getCoordSystem());
  }
  
  @Test
  public void testConstructor3() {
    DefaultChart defaultChart = new DefaultChart(null, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
    assertNotNull(defaultChart);
    assertEquals("Title", defaultChart.getTitle().getText());
    assertNotNull(defaultChart.getCoordSystem());
    assertEquals("X Axis", defaultChart.getCoordSystem().getXAxisUnit());
    assertEquals("Y Axis", defaultChart.getCoordSystem().getYAxisUnit());
  }
  
  @Test
  public void testGetPreferredSize() {
    DefaultChart defaultChart = new DefaultChart(null, "Title");
    assertNull(defaultChart.getPreferredSize());
  }
  
  @Test
  public void testRender() {
    DefaultChart defaultChart = new DefaultChart(null, "Title");
    // Test the rendering method
    // Include edge cases if applicable
  }
  
  @Test
  public void testStaticVariables() {
    assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
    assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
  }

}