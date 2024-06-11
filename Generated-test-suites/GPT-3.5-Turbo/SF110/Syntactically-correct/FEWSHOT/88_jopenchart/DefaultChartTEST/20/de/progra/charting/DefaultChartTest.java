package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.awt.*;

public class DefaultChartTest {
    @Test
    public void testConstructor1() {
        DefaultChart chart = new DefaultChart(null, "");
        assertNull(chart.getChartDataModel());
        assertNull(chart.getRowColorModel());
        assertNull(chart.getLegend());
        assertNull(chart.getTitle());
    }

    @Test
    public void testConstructor2() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title");
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }

    @Test
    public void testConstructor3() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testConstructor4() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y, "X Axis", "Y Axis");
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals("X Axis", chart.getCoordSystem().getXAxisUnit());
        assertEquals("Y Axis", chart.getCoordSystem().getYAxisUnit());
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
        // Add assertions for render method
    }
}