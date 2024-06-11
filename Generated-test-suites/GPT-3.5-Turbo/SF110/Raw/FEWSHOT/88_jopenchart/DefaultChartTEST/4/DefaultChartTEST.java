
package de.progra.charting;

import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultChartTest {
    @Test
    public void testDefaultChart_noArgs() {
        DefaultChart defaultChart = new DefaultChart();
        assertNull(defaultChart.getPreferredSIze());
        assertNull(defaultChart.getChartDataModel());
        assertNull(defaultChart.getRowColorModel());
        assertNull(defaultChart.getLegend());
        assertNull(defaultChart.getTitle());
        assertNull(defaultChart.getCoordSystem());
        defaultChart.render(null);
    }
    
    @Test
    public void testDefaultChart_withModelAndTitle() {
        DefaultChart defaultChart = new DefaultChart(new ChartDataModel(), "Test Title");
        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertNull(defaultChart.getCoordSystem());
        defaultChart.render(null);
    }
    
    @Test
    public void testDefaultChart_withModelTitleAndCoord() {
        DefaultChart defaultChart = new DefaultChart(new ChartDataModel(), "Test Title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertNotNull(defaultChart.getCoordSystem());
        defaultChart.render(null);
    }
    
    @Test
    public void testDefaultChart_withModelTitleCoordXaxisAndYaxis() {
        DefaultChart defaultChart = new DefaultChart(new ChartDataModel(), "Test Title", DefaultChart.LINEAR_X_LINEAR_Y, "x-axis", "y-axis");
        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertNotNull(defaultChart.getCoordSystem());
        assertEquals("x-axis", defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals("y-axis", defaultChart.getCoordSystem().getYAxisUnit());
        defaultChart.render(null);
    }
}
