package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;
import org.junit.Before;
import org.junit.Test;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;

public class DefaultChartTest {
    
    private DefaultChart defaultChart;
    private ChartDataModel model;
    private String title;
    private int coord;
    private String xaxis;
    private String yaxis;
    
    @Before
    public void setUp() {
        model = new ChartDataModel();
        title = "Test Chart";
        coord = DefaultChart.LINEAR_X_LINEAR_Y;
        xaxis = "X";
        yaxis = "Y";
        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }
    
    @Test
    public void testDefaultChartConstructor() {
        assertNotNull(defaultChart);
        assertNotNull(defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
    }
    
    @Test
    public void testDefaultChartConstructorNoCoord() {
        DefaultChart defaultChartNoCoord = new DefaultChart(model, title, DefaultChart.NO_COORDINATE_SYSTEM);
        assertNotNull(defaultChartNoCoord);
        assertNotNull(defaultChartNoCoord.getChartDataModel());
        assertNotNull(defaultChartNoCoord.getRowColorModel());
        assertNotNull(defaultChartNoCoord.getLegend());
        assertNotNull(defaultChartNoCoord.getTitle());
        assertNull(defaultChartNoCoord.getCoordSystem());
    }
    
    @Test
    public void testGetPreferredSize() {
        Dimension preferredSize = defaultChart.getPreferredSize();
        assertNull(preferredSize);
    }
    
    @Test
    public void testRender() {
        Graphics2D g = new Graphics2D(null);
        defaultChart.setBounds(new Rectangle(0, 0, 100, 100));
        defaultChart.render(g);
        // Additional checks can be done
    }
    
    @Test
    public void testStaticVariables() {
        assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
        assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
    }
}