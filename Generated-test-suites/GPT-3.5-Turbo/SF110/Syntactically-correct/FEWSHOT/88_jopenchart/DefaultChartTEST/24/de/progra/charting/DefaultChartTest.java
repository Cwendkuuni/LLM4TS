package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {
    private DefaultChart defaultChart;
    private ChartDataModel chartDataModel;
    private String title;
    private int coord;
    private String xaxis;
    private String yaxis;
    private Graphics2D graphics2d;
    
    @Before
    public void setup() {
        chartDataModel = mock(ChartDataModel.class);
        title = "Test Chart";
        coord = 0;
        xaxis = "X";
        yaxis = "Y";
        graphics2d = mock(Graphics2D.class);
        defaultChart = new DefaultChart(chartDataModel, title, coord, xaxis, yaxis);
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(chartDataModel, defaultChart.getChartDataModel());
        assertNull(defaultChart.getRowColorModel());
        assertNull(defaultChart.getLegend());
        assertNull(defaultChart.getTitle());
        assertNull(defaultChart.getCoordSystem());
    }
    
    @Test
    public void testConstructor2() {
        DefaultChart defaultChart2 = new DefaultChart(chartDataModel, title, coord);
        assertEquals(chartDataModel, defaultChart2.getChartDataModel());
        assertNull(defaultChart2.getRowColorModel());
        assertNull(defaultChart2.getLegend());
        assertNull(defaultChart2.getTitle());
        assertNull(defaultChart2.getCoordSystem());
    }
    
    @Test
    public void testConstructor3() {
        DefaultChart defaultChart3 = new DefaultChart(chartDataModel, title);
        assertEquals(chartDataModel, defaultChart3.getChartDataModel());
        assertNull(defaultChart3.getRowColorModel());
        assertNull(defaultChart3.getLegend());
        assertNull(defaultChart3.getTitle());
        assertNull(defaultChart3.getCoordSystem());
    }
    
    @Test
    public void testGetPreferredSize() {
        assertNull(defaultChart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        int width = 100;
        int height = 100;
        defaultChart.setBounds(new Rectangle(width, height));
        Title titleMock = mock(Title.class);
        defaultChart.setTitle(titleMock);
        Legend legendMock = mock(Legend.class);
        defaultChart.setLegend(legendMock);
        CoordSystem coordSystemMock = mock(CoordSystem.class);
        defaultChart.setCoordSystem(coordSystemMock);
        Collection<AbstractChartRenderer> rendererMock = mock(Collection.class);
        defaultChart.setChartRenderer(rendererMock);
        
        defaultChart.render(graphics2d);
        
        verify(graphics2d).setColor(Color.white);
        verify(graphics2d).setColor(Color.black);
        verify(titleMock).setBounds(Mockito.any(Rectangle.class));
        verify(titleMock).render(graphics2d);
        verify(legendMock).setBounds(Mockito.any(Rectangle.class));
        verify(legendMock).render(graphics2d);
        verify(coordSystemMock).setBounds(Mockito.any(Rectangle.class));
        verify(rendererMock, Mockito.times(2)).isEmpty();
        verify(rendererMock).iterator();
        verify(graphics2d, Mockito.times(2)).setColor(Color.white);
        verify(graphics2d).fillRect(0, 0, width, height);
        verify(graphics2d).setColor(Color.black);
        verify(coordSystemMock).render(graphics2d);
    }
}