package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DefaultChartTest {
    private DefaultChart chart;
    
    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        chart = new DefaultChart(model, title);
    }
    
    @Test
    public void testDefaultChartConstructor() {
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testDefaultChartFullConstructor() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X-Axis";
        String yaxis = "Y-Axis";
        
        chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals(coord, chart.getCoordSystem().getCoordinateSystem());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }
    
    @Test
    public void testGetPreferredSize() {
        assertNotNull(chart.getPreferredSize());
    }
    
    // Additional test cases can be added for render() method
    
}