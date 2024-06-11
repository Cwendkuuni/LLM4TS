package de.progra.charting;

import static org.junit.Assert.*;
import org.junit.Test;
import de.progra.charting.model.*;
import java.awt.*;
import de.progra.charting.render.*;

public class DefaultChartTest {
    
    @Test
    public void testDefaultChartConstructor1() {
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
    public void testDefaultChartConstructor2() {
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
    public void testDefaultChartConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X";
        String yaxis = "Y";
        
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
        
        Dimension size = chart.getPreferredSize();
        
        assertNull(size);
    }
    
    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        Graphics2D g = new Graphics2D();
        
        chart.render(g);
        
        // TODO: add assertion for render method
    }
}