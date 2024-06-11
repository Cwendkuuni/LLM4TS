package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.progra.charting.model.ChartDataModel;

public class DefaultChartTest {

    @Test
    public void testDefaultChart() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testDefaultChartWithCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testDefaultChartWithCoordAndAxis() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }
    
    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getPreferredSize());
    }

    // This test case can be modified to test the rendering method
    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        assertNotNull(chart);
    }
}