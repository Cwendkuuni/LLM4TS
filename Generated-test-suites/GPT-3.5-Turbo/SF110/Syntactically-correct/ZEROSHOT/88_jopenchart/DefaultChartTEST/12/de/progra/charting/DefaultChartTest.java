package de.progra.charting;

import de.progra.charting.model.*;
import java.awt.*;
import de.progra.charting.render.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {
    
    private DefaultChart defaultChart;
    private ChartDataModel chartDataModel;
    private String title;
    private int coord;
    private String xaxis;
    private String yaxis;
    
    @Before
    public void setup() {
        chartDataModel = new ChartDataModel();
        title = "Test Chart";
        coord = DefaultChart.LINEAR_X_LINEAR_Y;
        xaxis = "X Axis";
        yaxis = "Y Axis";
        defaultChart = new DefaultChart(chartDataModel, title, coord, xaxis, yaxis);
    }
    
    @Test
    public void testDefaultChartConstructor() {
        assertNotNull(defaultChart);
        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertNotNull(defaultChart.getCoordSystem());
        assertEquals(title, defaultChart.getTitle().getText());
        assertEquals(xaxis, defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, defaultChart.getCoordSystem().getYAxisUnit());
    }
    
    @Test
    public void testPreferredSize() {
        Dimension dimension = defaultChart.getPreferredSize();
        assertNull(dimension);
    }
    
    @Test
    public void testRender() {
        Graphics2D g = new Graphics2D(null);
        defaultChart.render(g);
        // Assert render behavior
    }
    
}