package de.progra.charting;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testConstructorWithModelAndTitle() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        
        assertSame(model, chart.getChartDataModel());        
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testConstructorWithModelAndTitleAndCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        
        assertSame(model, chart.getChartDataModel());        
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testConstructorWithModelAndTitleAndCoordAndAxis() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X-Axis";
        String yaxis = "Y-Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        assertSame(model, chart.getChartDataModel());        
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
    
    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        chart.setBounds(new Rectangle(0, 0, 100, 100));
        chart.render(null);
        
        // No assertions as this method only draws on Graphics2D object
    }
}