
package de.progra.charting;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        // TODO: Implement test for render() method
    }
    
    @Test
    public void testDefaultConstructor() {
        DefaultChart chart = new DefaultChart();
        assertNotNull(chart);
    }
    
    @Test
    public void testConstructorWithTitle() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        DefaultChart chart = new DefaultChart(model, title);
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertEquals(title, chart.getTitle().getText());
    }
    
    @Test
    public void testConstructorWithCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals(title, chart.getTitle().getText());
    }
    
    @Test
    public void testConstructorWithAxis() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals(title, chart.getTitle().getText());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }
}
