
package de.progra.charting;

import org.junit.Test;
import java.awt.*;
import java.util.*;

import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart defaultChart = new DefaultChart(model, title);

        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
    }

    @Test
    public void testConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;

        DefaultChart defaultChart = new DefaultChart(model, title, coord);

        assertNotNull(defaultChart.getCoordSystem());
    }

    @Test
    public void testConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";

        DefaultChart defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);

        assertNotNull(defaultChart.getCoordSystem());
        assertEquals(xaxis, defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, defaultChart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        DefaultChart defaultChart = new DefaultChart();

        Dimension preferredSize = defaultChart.getPreferredSize();

        assertNull(preferredSize);       
    }

    @Test
    public void testRender() {
        DefaultChart defaultChart = new DefaultChart();
        Graphics2D g = (Graphics2D) new Canvas().getGraphics();

        defaultChart.render(g);

        // TODO: Add assertions for rendering behavior
    }
}
