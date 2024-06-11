package de.progra.charting;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.CoordSystem;
import de.progra.charting.render.Legend;
import de.progra.charting.render.RowColorModel;
import de.progra.charting.render.Title;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class DefaultChartTest {

    DefaultChart defaultChart;
    ChartDataModel chartDataModel;
    String title;
    int coord;
    String xaxis;
    String yaxis;
    Graphics2D graphics2D;

    @Before
    public void setUp() {
        chartDataModel = new ChartDataModel();
        title = "Test Title";
        coord = DefaultChart.LINEAR_X_LINEAR_Y;
        xaxis = "X Axis";
        yaxis = "Y Axis";
        defaultChart = new DefaultChart(chartDataModel, title, coord, xaxis, yaxis);
        graphics2D = new Graphics2D();
    }

    @Test
    public void testConstructor1() {
        assertNotNull(defaultChart);
        assertEquals(chartDataModel, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
    }

    @Test
    public void testConstructor2() {
        assertNotNull(defaultChart);
        assertEquals(chartDataModel, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertNotNull(defaultChart.getCoordSystem());
    }

    @Test
    public void testConstructor3() {
        assertNotNull(defaultChart);
        assertEquals(chartDataModel, defaultChart.getChartDataModel());
        assertNotNull(defaultChart.getRowColorModel());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getTitle());
        assertNotNull(defaultChart.getCoordSystem());
        assertEquals(xaxis, defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, defaultChart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        Dimension preferredSize = defaultChart.getPreferredSize();
        assertNull(preferredSize);
    }

    @Test
    public void testRender() {
        defaultChart.setBounds(new Rectangle(0, 0, 100, 100));
        defaultChart.render(graphics2D);
        // Validate results
        // ...
    }

    @Test
    public void testStaticVariables() {
        assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
        assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
    }

}