package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;

import de.progra.charting.model.ChartDataModel;
import org.junit.Before;
import org.junit.Test;

public class DefaultChartTest {
    private ChartDataModel model;
    private String title;
    private int coord;
    private String xaxis;
    private String yaxis;
    private DefaultChart chart;

    @Before
    public void setUp() {
        model = new ChartDataModel();
        title = "Test Chart";
        coord = DefaultChart.LINEAR_X_LINEAR_Y;
        xaxis = "X Axis";
        yaxis = "Y Axis";
        chart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }

    @Test
    public void testDefaultChart() {
        assertNotNull(chart);

        DefaultChart chart2 = new DefaultChart();
        assertNotNull(chart2);
        assertNotEquals(chart, chart2);
    }

    @Test
    public void testDefaultChartWithTitle() {
        assertNotNull(chart);
        assertEquals(title, chart.getTitle().getText());
    }

    @Test
    public void testDefaultChartWithCoordSystem() {
        assertNotNull(chart);
        assertNotNull(chart.getCoordSystem());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        // Cannot directly test the render method as it involves graphics rendering
        // and UI interactions.
    }

    @Test
    public void testStaticFields() {
        assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
        assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
        assertNotEquals(DefaultChart.LINEAR_X_LINEAR_Y, DefaultChart.NO_COORDINATE_SYSTEM);
    }
}