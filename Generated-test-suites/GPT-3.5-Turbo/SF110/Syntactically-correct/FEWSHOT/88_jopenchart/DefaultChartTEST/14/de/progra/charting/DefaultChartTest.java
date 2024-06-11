package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;
import org.junit.Test;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class DefaultChartTest {
    
    @Test
    public void testDefaultChartConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        DefaultChart chart = new DefaultChart(model, title);
        assertEquals(title, chart.getTitle().getTitleText());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getChartDataModel());
    }
    
    @Test
    public void testDefaultChartConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertEquals(title, chart.getTitle().getTitleText());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getChartDataModel());
    }
    
    @Test
    public void testDefaultChartConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X-Axis";
        String yaxis = "Y-Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertEquals(title, chart.getTitle().getTitleText());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getChartDataModel());
    }
    
    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart();
        Dimension expected = new Dimension(0, 0);
        assertEquals(expected, chart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        Graphics2D g = new Graphics2D();
        chart.render(g);
        int width = (int)chart.getBounds().getWidth();
        int height = (int)chart.getBounds().getHeight();
        Title t = chart.getTitle();
        Legend l = chart.getLegend();
        CoordSystem c = chart.getCoordSystem();
        Collection<AbstractChartRenderer> renderer = chart.getChartRenderer().values();
        assertEquals(Color.white, g.getColor());
        assertEquals(Color.black, g.getColor());
        int titleheight = 0;
        int legendwidth = 0;
        if (t != null) {
            Dimension title = t.getPreferredSize();
            t.setBounds(new Rectangle(width / 2 - (int)(title.getWidth() / 2.0), 0, (int)title.getWidth(), (int)title.getHeight()));
            titleheight = (int)t.getBounds().getHeight();
        }
        if (l != null) {
            Dimension legend = l.getPreferredSize();
            l.setBounds(new Rectangle((int)(width - legend.getWidth()), (int)(height / 2 - legend.getHeight() / 2.0 + titleheight), (int)legend.getWidth(), (int)legend.getHeight()));
            legendwidth = (int)l.getBounds().getWidth();
        }
        if (c != null) {
            c.setBounds(new Rectangle(0, titleheight, width - legendwidth, height - titleheight));
        }
        if (!renderer.isEmpty()) {
            for (AbstractChartRenderer cr : renderer) {
                cr.setBounds(new Rectangle(0, titleheight, width - legendwidth, height - titleheight - 5));
            }
        }
        if (c != null) {
            c.render(g);
        }
    }
}