
package de.progra.charting;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.CoordSystem;
import de.progra.charting.model.Legend;
import de.progra.charting.model.RowColorModel;
import de.progra.charting.model.Title;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

    private DefaultChart defaultChart;
    private ChartDataModel mockModel;
    private String mockTitle;

    @Before
    public void setUp() {
        mockModel = new ChartDataModel();
        mockTitle = "Mock Chart";
        defaultChart = new DefaultChart(mockModel, mockTitle);
    }

    @Test
    public void testConstructor_ChartDataModelAndTitle_SetCorrectValues() {
        Assert.assertEquals(mockModel, defaultChart.getChartDataModel());
        Assert.assertEquals(mockTitle, defaultChart.getTitle().getText());
        Assert.assertNotNull(defaultChart.getRowColorModel());
        Assert.assertNotNull(defaultChart.getLegend());
    }

    @Test
    public void testConstructor_ChartDataModelTitleAndCoord_SetCoordSystemIfLinearXLinearY() {
        DefaultChart chart = new DefaultChart(mockModel, mockTitle, DefaultChart.LINEAR_X_LINEAR_Y);
        Assert.assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testConstructor_ChartDataModelTitleAndCoord_DontSetCoordSystemIfNotLinearXLinearY() {
        DefaultChart chart = new DefaultChart(mockModel, mockTitle, DefaultChart.NO_COORDINATE_SYSTEM);
        Assert.assertNull(chart.getCoordSystem());
    }

    @Test
    public void testConstructor_ChartDataModelTitleCoordXAxisAndYAxis_SetXAxisAndYAxis() {
        String mockXAxis = "X Axis";
        String mockYAxis = "Y Axis";
        DefaultChart chart = new DefaultChart(mockModel, mockTitle, DefaultChart.LINEAR_X_LINEAR_Y, mockXAxis, mockYAxis);
        Assert.assertEquals(mockXAxis, chart.getCoordSystem().getXAxisUnit());
        Assert.assertEquals(mockYAxis, chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize_ReturnNull() {
        Dimension preferredSize = defaultChart.getPreferredSize();
        Assert.assertNull(preferredSize);
    }

    @Test
    public void testRender_RenderChartComponents() {
        Graphics2D mockGraphics = null; // Provide a mock Graphics2D object
        defaultChart.render(mockGraphics);

        // Assert rendering of chart components
    }
}
