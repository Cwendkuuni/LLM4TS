package de.progra.charting;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DefaultChartTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDefaultChart() {
        // Test the DefaultChart constructor
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        DefaultChart chart = new DefaultChart(model, title);
        assertEquals(model, chart.getChartDataModel());
        assertEquals(title, chart.getTitle().getText());
    }

    @Test
    public void testDefaultChartWithCoordSystem() {
        // Test the DefaultChart constructor with coordinate system
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertEquals(model, chart.getChartDataModel());
        assertEquals(title, chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartWithAxisUnits() {
        // Test the DefaultChart constructor with axis units
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertEquals(model, chart.getChartDataModel());
        assertEquals(title, chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        // Test the getPreferredSize method
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        // Test the render method
        // Unable to write test for render method as it requires Graphics2D object
    }

}