package de.progra.charting;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X-Axis";
        String yaxis = "Y-Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getCoordSystem().getXAxisUnit());
        assertNotNull(chart.getCoordSystem().getYAxisUnit());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testGetPreferredSize() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        
        assertNull(chart.getPreferredSize());
    }
    
    // Add more test methods for other methods in DefaultChart class
    
}