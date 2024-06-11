
package de.progra.charting;

import org.junit.Test;
import static org.junit.Assert.*;
import de.progra.charting.model.ChartDataModel;
import java.awt.*;

public class DefaultChartTest {
    
    @Test
    public void testConstructor_withModelAndTitle() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testConstructor_withModelTitleAndCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testConstructor_withModelTitleCoordXAxisYAxis() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X-Axis";
        String yaxis = "Y-Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
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
    
    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        Graphics2D g = new Graphics2D();
        chart.render(g);
        // Add assertions for rendering
    }
}
