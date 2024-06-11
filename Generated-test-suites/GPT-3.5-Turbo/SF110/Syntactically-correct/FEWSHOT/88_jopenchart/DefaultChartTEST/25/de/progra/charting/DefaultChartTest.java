package de.progra.charting;

import static org.junit.Assert.*;

import de.progra.charting.model.ChartDataModel;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Collection;

public class DefaultChartTest {

    private DefaultChart defaultChart;

    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X-Axis";
        String yaxis = "Y-Axis";

        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }

    @Test
    public void testGetPreferredSize() {
        Dimension expected = null;
        Dimension actual = defaultChart.getPreferredSize();

        assertEquals(expected, actual);
    }

    @Test
    public void testRender() {
        Graphics2D g = null;
        defaultChart.render(g);
    }
}