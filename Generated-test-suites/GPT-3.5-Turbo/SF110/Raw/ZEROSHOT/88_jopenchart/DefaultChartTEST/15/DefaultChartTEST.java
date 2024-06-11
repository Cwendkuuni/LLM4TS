
package de.progra.charting;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Collection;

import static org.junit.Assert.*;

public class DefaultChartTest {

    private DefaultChart defaultChart;

    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        defaultChart = new DefaultChart(model, title);
    }

    @After
    public void tearDown() {
        defaultChart = null;
    }

    @Test
    public void testGetPreferredSize_Null() {
        Dimension result = defaultChart.getPreferredSize();
        assertNull(result);
    }

    @Test
    public void testRender() {
        Graphics2D g = (Graphics2D) new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        defaultChart.render(g);
        // Add assertions for the rendering if necessary
    }

    @Test
    public void testLinearXLinearYCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;

        DefaultChart chart = new DefaultChart(model, title, coord);

        CoordSystem coordSystem = chart.getCoordSystem();
        assertNotNull(coordSystem);
        assertEquals(model, coordSystem.getChartDataModel());
    }

    @Test
    public void testGetXAxisUnit() {
        String xaxis = "X";
        defaultChart.getCoordSystem().setXAxisUnit(xaxis);
        String result = defaultChart.getCoordSystem().getXAxisUnit();
        assertEquals(xaxis, result);
    }

    @Test
    public void testGetYAxisUnit() {
        String yaxis = "Y";
        defaultChart.getCoordSystem().setYAxisUnit(yaxis);
        String result = defaultChart.getCoordSystem().getYAxisUnit();
        assertEquals(yaxis, result);
    }

    // Add more test methods for other public methods in DefaultChart if necessary

}

