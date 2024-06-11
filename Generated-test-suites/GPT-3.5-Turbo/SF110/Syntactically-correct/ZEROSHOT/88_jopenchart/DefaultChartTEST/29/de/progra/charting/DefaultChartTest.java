package de.progra.charting;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {
    
    private DefaultChart defaultChart;
    private ChartDataModel model;
    private String title;
    private int coord;
    private String xaxis;
    private String yaxis;
    
    @Before
    public void setUp() {
        model = new ChartDataModel();
        title = "Test Chart";
        coord = DefaultChart.LINEAR_X_LINEAR_Y;
        xaxis = "X Axis";
        yaxis = "Y Axis";
        
        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }
    
    @Test
    public void testDefaultChartConstructor() {
        assertNotNull(defaultChart);
        assertEquals(model, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
    }
    
    @Test
    public void testDefaultChartConstructorWithCoord() {
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testGetPreferredSize() {
        assertNull(defaultChart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        // Test render method
    }
    
}