
package de.progra.charting;

import org.junit.*;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChart() {
        DefaultChart defaultChart = new DefaultChart();
        assertNotNull(defaultChart);
    }

    @Test
    public void testDefaultChartWithDataAndTitle() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        DefaultChart defaultChart = new DefaultChart(model, title);
        assertNotNull(defaultChart);
        assertEquals(model, defaultChart.getChartDataModel());
        // Add more assertions for other properties
    }

    @Test
    public void testDefaultChartWithCoordSystem() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart defaultChart = new DefaultChart(model, title, coord);
        assertNotNull(defaultChart);
        assertEquals(model, defaultChart.getChartDataModel());
        assertEquals(DefaultChart.LINEAR_X_LINEAR_Y, defaultChart.getCoordSystem().getCoord());
    }

    @Test
    public void testDefaultChartWithXAxisAndYAxis() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertNotNull(defaultChart);
        assertEquals(model, defaultChart.getChartDataModel());
        assertEquals(DefaultChart.LINEAR_X_LINEAR_Y, defaultChart.getCoordSystem().getCoord());
        assertEquals(xaxis, defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, defaultChart.getCoordSystem().getYAxisUnit());
    }

    // Add test cases for other public methods

}

