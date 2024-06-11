package de.progra.charting;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {
    
    private DefaultChart chart;
    private ChartDataModel model;
    private String title;
    private int coord;
    private String xaxis;
    private String yaxis;
    
    @Before
    public void setUp() {
        model = new ChartDataModel();
        title = "Test Chart";
        chart = new DefaultChart(model, title);
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertEquals(title, chart.getTitle().getTitle());
    }
    
    @Test
    public void testConstructor2() {
        coord = DefaultChart.LINEAR_X_LINEAR_Y;
        chart = new DefaultChart(model, title, coord);
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testConstructor3() {
        coord = DefaultChart.LINEAR_X_LINEAR_Y;
        xaxis = "X";
        yaxis = "Y";
        chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertNotNull(chart.getCoordSystem());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }
    
    @Test
    public void testGetPreferredSize() {
        assertEquals(null, chart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        // Test the rendering logic
    }
}