package de.progra.charting;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class DefaultChartTest {
    
    DefaultChart defaultChart;
    
    @Before
    public void setUp() {
        defaultChart = new DefaultChart();
    }
    
    @Test
    public void testGetPreferredSize() {
        assertNull(defaultChart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        Graphics2D g = null;
        defaultChart.render(g);
        // Test render method
    }
    
    @Test
    public void testConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        DefaultChart defaultChart = new DefaultChart(model, title);
        
        // Test constructor with ChartDataModel and title
        assertNotNull(defaultChart);
        assertNotNull(defaultChart.getChartDataModel());
        assertEquals(title, defaultChart.getTitle().toString());
    }
    
    @Test
    public void testConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart defaultChart = new DefaultChart(model, title, coord);
        
        // Test constructor with ChartDataModel, title and coord
        assertNotNull(defaultChart);
        assertNotNull(defaultChart.getChartDataModel());
        assertEquals(coord, defaultChart.getCoordSystem().getCoord());
        assertEquals(title, defaultChart.getTitle().toString());
    }
    
    @Test
    public void testConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        // Test constructor with ChartDataModel, title, coord, xaxis and yaxis
        assertNotNull(defaultChart);
        assertNotNull(defaultChart.getChartDataModel());
        assertEquals(coord, defaultChart.getCoordSystem().getCoord());
        assertEquals(title, defaultChart.getTitle().toString());
        assertEquals(xaxis, defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, defaultChart.getCoordSystem().getYAxisUnit());
    }
    
}