package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.CoordSystem;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.RowColorModel;
import de.progra.charting.view.CoordSystem;
import de.progra.charting.view.Legend;
import de.progra.charting.view.Title;
import org.junit.Before;
import org.junit.Test;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.*;

public class DefaultChartTest {
    private DefaultChart defaultChart;

    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        
        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }

    @Test
    public void testGetPreferredSize() {
        Dimension expectedSize = null;
        assertEquals(expectedSize, defaultChart.getPreferredSize());
    }

    @Test
    public void testRender() {
        Graphics2D g = new Graphics2D();
        int width = (int)defaultChart.getBounds().getWidth();
        int height = (int)defaultChart.getBounds().getHeight();
        Title t = defaultChart.getTitle();
        Legend l = defaultChart.getLegend();
        CoordSystem c = defaultChart.getCoordSystem();
        Collection renderer = defaultChart.getChartRenderer().values();
        Color white = Color.white;
        Color black = Color.black;
        int titleheight = 0;
        int legendwidth = 0;

        g.setColor(white);
        g.fillRect(0, 0, width, height);
        g.setColor(black);

        if (t != null) {
            Dimension title = t.getPreferredSize();
            t.setBounds(new Rectangle(width / 2 - (int)(title.getWidth() / 2.0), 0, (int)title.getWidth(), (int)title.getHeight()));
            t.render(g);
            titleheight = (int)t.getBounds().getHeight();
        }
        if (l != null) {
            Dimension legend = l.getPreferredSize();
            l.setBounds(new Rectangle((int)(width - legend.getWidth()), (int)(height / 2 - legend.getHeight() / 2.0 + titleheight), (int)legend.getWidth(), (int)legend.getHeight()));
            l.render(g);
            legendwidth = (int)l.getBounds().getWidth();
        }
        if (c != null) {
            c.setBounds(new Rectangle(0, titleheight, width - legendwidth, height - titleheight));
        }
        if (!renderer.isEmpty()) {
            for (AbstractChartRenderer cr : renderer) {
                cr.setBounds(new Rectangle(0, titleheight, width - legendwidth, height - titleheight - 5));
                cr.render(g);
            }
        }
        if (c != null) {
            c.render(g);
        }
    }
}