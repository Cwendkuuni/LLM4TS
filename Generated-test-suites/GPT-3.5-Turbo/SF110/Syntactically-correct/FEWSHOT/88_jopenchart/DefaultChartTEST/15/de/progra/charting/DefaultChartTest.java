package de.progra.charting;

import de.progra.charting.model.*;
import java.awt.*;
import de.progra.charting.render.*;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class DefaultChartTest {
    private DefaultChart defaultChart;
    
    @Before
    public void setup() {
        defaultChart = new DefaultChart();
    }
    
    @Test
    public void testDefaultConstructor() {
        DefaultChart defaultChart = new DefaultChart();
        
        assertNull(defaultChart.getChartDataModel());
        assertNull(defaultChart.getRowColorModel());
        assertNull(defaultChart.getLegend());
        assertNull(defaultChart.getTitle());
    }

    @Test
    public void testConstructorWithModelAndTitle() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        
        DefaultChart defaultChart = new DefaultChart(model, title);
        
        assertEquals(model, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertEquals(title, defaultChart.getTitle().getTitleText());
    }
    
    @Test
    public void testConstructorWithModelTitleAndCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        
        DefaultChart defaultChart = new DefaultChart(model, title, coord);
        
        assertEquals(model, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertEquals(title, defaultChart.getTitle().getTitleText());
        assertNotNull(defaultChart.getCoordSystem());
    }
    
    @Test
    public void testConstructorWithModelTitleCoordXAxisAndYAxis() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        
        DefaultChart defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        assertEquals(model, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertEquals(title, defaultChart.getTitle().getTitleText());
        assertNotNull(defaultChart.getCoordSystem());
        assertEquals(xaxis, defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, defaultChart.getCoordSystem().getYAxisUnit());
    }
    
    @Test
    public void testGetPreferredSize() {
        Dimension expected = null;
        assertEquals(expected, defaultChart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        Graphics2D g = new Graphics2D();
        
        // Test that the method executes without errors
        defaultChart.render(g);
    }
}