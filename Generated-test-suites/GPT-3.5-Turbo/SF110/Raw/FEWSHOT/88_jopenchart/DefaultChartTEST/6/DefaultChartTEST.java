package de.progra.charting;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultChartTest {

    private DefaultChart defaultChart;

    @Before
    public void setup() {
        defaultChart = new DefaultChart();
    }

    @Test
    public void testConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart 1";
        defaultChart = new DefaultChart(model, title);

        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());

        assertEquals(model, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertEquals(title, defaultChart.getTitle().getTitle());
    }

    @Test
    public void testConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart 2";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        defaultChart = new DefaultChart(model, title, coord);

        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertNotNull(defaultChart.getCoordSystem());

        assertEquals(model, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertEquals(title, defaultChart.getTitle().getTitle());
        assertNotNull(defaultChart.getCoordSystem());
    }

    @Test
    public void testConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart 3";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X";
        String yaxis = "Y";
        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);

        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertNotNull(defaultChart.getCoordSystem());

        assertEquals(model, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertEquals(title, defaultChart.getTitle().getTitle());
        assertNotNull(defaultChart.getCoordSystem());
        assertEquals(xaxis, defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, defaultChart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        Dimension size = defaultChart.getPreferredSize();

        assertNull(size);
    }

    @Test
    public void testRender() {
        Graphics2D g = null; // Create a mock Graphics2D object for testing

        defaultChart.render(g);

        // Add assertions to verify rendering behavior
    }
}