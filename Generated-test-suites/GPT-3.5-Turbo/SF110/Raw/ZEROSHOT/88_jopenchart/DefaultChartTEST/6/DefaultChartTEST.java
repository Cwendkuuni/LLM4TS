package de.progra.charting;

import de.progra.charting.model.ChartDataModel;
import java.awt.*;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.CoordSystem;
import de.progra.charting.render.Legend;
import de.progra.charting.render.RowColorModel;
import de.progra.charting.render.Title;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultChartTest {
    private DefaultChart defaultChart;

    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X";
        String yaxis = "Y";

        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }

    @Test
    public void testGetPreferredSize() {
        Dimension dimension = defaultChart.getPreferredSize();
        assertNull(dimension);
    }

    @Test
    public void testRender() {
        // TODO: Add test code for render method
    }
}