
package de.progra.charting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DefaultChartTest {
   
   DefaultChart defaultChart;
   ChartDataModel model;
   String title;
   int coord;
   String xaxis;
   String yaxis;
   
   @Before
   public void setUp() {
       model = new ChartDataModel();
       title = "Chart Title";
       coord = DefaultChart.LINEAR_X_LINEAR_Y;
       xaxis = "X Axis";
       yaxis = "Y Axis";
       
       defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
   }
   
   @Test
   public void testGetPreferredSize() {
       Assert.assertEquals(null, defaultChart.getPreferredSize());
   }
   
   @Test
   public void testRender() {
       // Create a graphics object
       // Call the render method of DefaultChart
       // Assert expected behavior
   }
   
}
