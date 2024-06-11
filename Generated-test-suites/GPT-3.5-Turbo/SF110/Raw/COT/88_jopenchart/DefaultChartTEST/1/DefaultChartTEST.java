
package de.progra.charting;

import de.progra.charting.model.*;
import java.awt.*;
import de.progra.charting.render.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testDefaultConstructor() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getChartDataModel());
        assertNull(chart.getTitle());
        assertNull(chart.getRowColorModel());
        assertNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testConstructorWithTitle() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        assertSame(model, chart.getChartDataModel());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNull(chart.getCoordSystem());
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testConstructorWithCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertSame(model, chart.getChartDataModel());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());
        assertNull(chart.getCoordSystem().getXAxisUnit());
        assertNull(chart.getCoordSystem().getYAxisUnit());
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testConstructorWithAxisUnits() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X";
        String yaxis = "Y";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertSame(model, chart.getChartDataModel());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
        assertNull(chart.getPreferredSize());
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
        // Unable to test the rendering functionality
    }
}

