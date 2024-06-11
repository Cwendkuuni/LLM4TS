
package de.progra.charting;

import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultChartTest {
  
  @Test
  public void testDefaultChartConstructor1() {
    // Create test data
    ChartDataModel model = new ChartDataModel();
    String title = "Test Chart";
    
    // Create instance of DefaultChart
    DefaultChart chart = new DefaultChart(model, title);
    
    // Verify the chart properties
    assertSame(model, chart.getChartDataModel());
    assertNotNull(chart.getRowColorModel());
    assertNotNull(chart.getLegend());
    assertNotNull(chart.getTitle());
  }
  
  @Test
  public void testDefaultChartConstructor2() {
    // Create test data
    ChartDataModel model = new ChartDataModel();
    String title = "Test Chart";
    int coord = DefaultChart.LINEAR_X_LINEAR_Y;
    
    // Create instance of DefaultChart
    DefaultChart chart = new DefaultChart(model, title, coord);
    
    // Verify the chart properties
    assertSame(model, chart.getChartDataModel());
    assertNotNull(chart.getCoordSystem());
    assertNotNull(chart.getCoordSystem().getXAxisUnit());
    assertNotNull(chart.getCoordSystem().getYAxisUnit());
  }
  
  @Test
  public void testDefaultChartConstructor3() {
    // Create test data
    ChartDataModel model = new ChartDataModel();
    String title = "Test Chart";
    int coord = DefaultChart.LINEAR_X_LINEAR_Y;
    String xaxis = "X Axis";
    String yaxis = "Y Axis";
    
    // Create instance of DefaultChart
    DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
    
    // Verify the chart properties
    assertSame(model, chart.getChartDataModel());
    assertNotNull(chart.getCoordSystem());
    assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
    assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
  }
  
  @Test
  public void testGetPreferredSize() {
    // Create instance of DefaultChart
    DefaultChart chart = new DefaultChart();
    
    // Verify the preferred size is null
    assertNull(chart.getPreferredSize());
  }
  
  @Test
  public void testRender() {
    // Create test data
    ChartDataModel model = new ChartDataModel();
    String title = "Test Chart";
    
    // Create instance of DefaultChart
    DefaultChart chart = new DefaultChart(model, title);
    
    // Verify that the render method does not throw any exceptions
    chart.render(null);
  }
}
