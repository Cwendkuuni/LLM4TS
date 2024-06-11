package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

    private DefaultChart defaultChart;

    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";

        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }

    @Test
    public void testConstructor() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";

        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);

        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getChartRenderer());

        assertEquals(title, chart.getTitle().getTitle());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        assertNull(defaultChart.getPreferredSize());
    }

    @Test
    public void testRender() {
        AbstractChartRenderer renderer1 = new AbstractChartRenderer();
        AbstractChartRenderer renderer2 = new AbstractChartRenderer();

        defaultChart.getChartRenderer().put("renderer1", renderer1);
        defaultChart.getChartRenderer().put("renderer2", renderer2);

        // Test rendering with no title and no legend
        defaultChart.render(null);

        assertEquals(2, renderer1.getBounds().getWidth(), 0);
        assertEquals(2, renderer1.getBounds().getHeight(), 0);
        assertEquals(2, renderer2.getBounds().getWidth(), 0);
        assertEquals(2, renderer2.getBounds().getHeight(), 0);
    }
}