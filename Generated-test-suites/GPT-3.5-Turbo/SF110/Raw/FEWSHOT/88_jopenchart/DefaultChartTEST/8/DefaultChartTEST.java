
package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.CoordSystem;
import de.progra.charting.render.Legend;
import de.progra.charting.render.RowColorModel;
import de.progra.charting.render.Title;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultChartTest {
    @Test
    public void testDefaultChart() {
        ChartDataModel model = mock(ChartDataModel.class);
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;

        DefaultChart chart = new DefaultChart(model, title);
        assertNull(chart.getPreferredSize());

        chart = new DefaultChart(model, title, coord);
        assertNull(chart.getPreferredSize());
        assertNotNull(chart.getCoordSystem());
        assertEquals(0, chart.getCoordSystem().getXAxisUnit().length());
        assertEquals(0, chart.getCoordSystem().getYAxisUnit().length());

        String xaxis = "X-Axis";
        String yaxis = "Y-Axis";
        chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertNull(chart.getPreferredSize());
        assertNotNull(chart.getCoordSystem());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();

        Graphics2D g2d = mock(Graphics2D.class);
        Dimension bounds = new Dimension(100, 100);
        chart.setBounds(new Rectangle(bounds));
        chart.render(g2d);

        verify(g2d).setColor(Color.white);
        verify(g2d).setColor(Color.black);
    }
    
    @Test
    public void testRenderWithTitleAndLegend() {
        ChartDataModel model = mock(ChartDataModel.class);
        String titleText = "Test Title";
        String xAxisUnit = "X-Axis";
        String yAxisUnit = "Y-Axis";

        DefaultChart chart = new DefaultChart(model, titleText);
        Graphics2D g2d = mock(Graphics2D.class);
        Dimension bounds = new Dimension(100, 100);
        chart.setBounds(new Rectangle(bounds));
        chart.render(g2d);

        Legend legend = chart.getLegend();
        verify(g2d, times(2)).setColor(Color.white);
        verify(g2d, times(2)).setColor(Color.black);
        verify(legend).render(g2d);
    }
    
    @Test
    public void testRenderWithCoordSystemAndRenderer() {
        ChartDataModel model = mock(ChartDataModel.class);
        String titleText = "Test Title";
        String xAxisUnit = "X-Axis";
        String yAxisUnit = "Y-Axis";
        
        DefaultChart chart = new DefaultChart(model, titleText, DefaultChart.LINEAR_X_LINEAR_Y, xAxisUnit, yAxisUnit);
        Graphics2D g2d = mock(Graphics2D.class);
        Dimension bounds = new Dimension(100, 100);
        chart.setBounds(new Rectangle(bounds));
        
        Map<String, AbstractChartRenderer> chartRenderer = new HashMap<String, AbstractChartRenderer>();
        AbstractChartRenderer renderer = mock(AbstractChartRenderer.class);
        chartRenderer.put("renderer", renderer);
        chart.setChartRenderer(chartRenderer);
        
        chart.render(g2d);
        
        CoordSystem coordSystem = chart.getCoordSystem();
        verify(g2d, times(3)).setColor(Color.white);
        verify(g2d, times(4)).setColor(Color.black);
        verify(coordSystem).render(g2d);
        verify(renderer).render(g2d);
    }
}
