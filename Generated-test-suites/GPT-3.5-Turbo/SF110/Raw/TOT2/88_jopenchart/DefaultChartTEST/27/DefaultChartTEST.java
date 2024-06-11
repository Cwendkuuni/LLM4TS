package de.progra.charting;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstructor1() {
        // Typical use case
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title");
        
        assertNotNull(chart);
    }

    @Test
    public void testDefaultChartConstructor2() {
        // Edge case - Testing constructor with coordinate system
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.LINEAR_X_LINEAR_Y);
        
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartConstructor3() {
        // Error scenario - Testing constructor with invalid coordinate system
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title", DefaultChart.NO_COORDINATE_SYSTEM);
        
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testGetPreferredSize() {
        // Typical use case
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title");
        
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        // Typical use case - Testing render method
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Title");
        
        // Test rendering without errors
        Graphics2D g = new Graphics2D();
        chart.render(g);
    }
}