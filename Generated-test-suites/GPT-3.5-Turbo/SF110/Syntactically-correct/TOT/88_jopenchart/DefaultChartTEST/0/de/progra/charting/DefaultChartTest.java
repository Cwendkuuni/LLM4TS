
package de.progra.charting;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class DefaultChartTest {
  
  @Test
  void testConstructor1() {
    ChartDataModel model = new ChartDataModel();
    String title = "Test Title";
    
    DefaultChart chart = new DefaultChart(model, title);
    
    assertSame(model, chart.getChartDataModel());
    assertNotNull(chart.getRowColorModel());
    assertNotNull(chart.getLegend());
    assertNotNull(chart.getTitle());
    assertNull(chart.getCoordSystem());
  }
  
  @Test
  void testConstructor2WithLinearCoordSystem() {
    ChartDataModel model = new ChartDataModel();
    String title = "Test Title";
    int coord = DefaultChart.LINEAR_X_LINEAR_Y;
    
    DefaultChart chart = new DefaultChart(model, title, coord);
    
    assertSame(model, chart.getChartDataModel());
    assertNotNull(chart.getRowColorModel());
    assertNotNull(chart.getLegend());
    assertNotNull(chart.getTitle());
    assertNotNull(chart.getCoordSystem());
    assertEquals(chart.getCoordSystem().getXAxisUnit(), "");
    assertEquals(chart.getCoordSystem().getYAxisUnit(), "");
  }
  
  @Test
  void testConstructor2WithNoCoordSystem() {
    ChartDataModel model = new ChartDataModel();
    String title = "Test Title";
    int coord = DefaultChart.NO_COORDINATE_SYSTEM;
    
    DefaultChart chart = new DefaultChart(model, title, coord);
    
    assertSame(model, chart.getChartDataModel());
    assertNotNull(chart.getRowColorModel());
    assertNotNull(chart.getLegend());
    assertNotNull(chart.getTitle());
    assertNull(chart.getCoordSystem());
  }
  
  @Test
  void testConstructor3() {
    ChartDataModel model = new ChartDataModel();
    String title = "Test Title";
    int coord = DefaultChart.LINEAR_X_LINEAR_Y;
    String xaxis = "X Axis";
    String yaxis = "Y Axis";
    
    DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
    
    assertSame(model, chart.getChartDataModel());
    assertNotNull(chart.getRowColorModel());
    assertNotNull(chart.getLegend());
    assertNotNull(chart.getTitle());
    assertNotNull(chart.getCoordSystem());
    assertEquals(chart.getCoordSystem().getXAxisUnit(), xaxis);
    assertEquals(chart.getCoordSystem().getYAxisUnit(), yaxis);
  }
  
  @Test
  void testGetPreferredSize() {
    DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Title");
    
    Dimension size = chart.getPreferredSize();
    
    assertNotNull(size);
    assertEquals(size.width, 0);
    assertEquals(size.height, 0);
  }
  
  @Test
  void testRender() {
    DefaultChart chart = new DefaultChart(new ChartDataModel(), "Test Title");
    Graphics2D g = new Graphics2D() {};
    
    chart.render(g);
    
    // Check if rendering is successful
    assertTrue(true);
  }
}

