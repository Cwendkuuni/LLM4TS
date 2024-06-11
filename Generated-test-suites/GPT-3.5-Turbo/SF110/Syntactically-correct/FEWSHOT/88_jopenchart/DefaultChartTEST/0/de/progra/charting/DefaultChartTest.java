package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.RowColorModel;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.render.CoordSystem;
import de.progra.charting.render.Legend;
import de.progra.charting.render.Title;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;


public class DefaultChartTest {

    private ChartDataModel model;
    private String title;
    private int coord;
    private String xaxis;
    private String yaxis;
    
    private DefaultChart defaultChart;
    
    @Before
    public void setup() {
        model = mock(ChartDataModel.class);
        title = "Test Chart";
        coord = DefaultChart.LINEAR_X_LINEAR_Y;
        xaxis = "x-axis";
        yaxis = "y-axis";
        
        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }
    
    @Test
    public void testConstructor1() {
        assertEquals(model, defaultChart.getChartDataModel());
        
        RowColorModel rowColorModel = defaultChart.getRowColorModel();
        assertNotNull(rowColorModel);
        assertEquals(model, rowColorModel.getChartDataModel());
        
        Legend legend = defaultChart.getLegend();
        assertNotNull(legend);
        assertEquals(rowColorModel, legend.getRowColorModel());
        
        Title title = defaultChart.getTitle();
        assertNotNull(title);
        assertEquals(this.title, title.getText());
        
        CoordSystem coordSystem = defaultChart.getCoordSystem();
        assertNotNull(coordSystem);
        assertEquals(model, coordSystem.getChartDataModel());
        
        assertEquals(xaxis, coordSystem.getXAxisUnit());
        assertEquals(yaxis, coordSystem.getYAxisUnit());
    }
    
    @Test
    public void testConstructor2() {
        DefaultChart chart = new DefaultChart(model, title);
        
        assertEquals(model, chart.getChartDataModel());
        
        RowColorModel rowColorModel = chart.getRowColorModel();
        assertNotNull(rowColorModel);
        assertEquals(model, rowColorModel.getChartDataModel());
        
        Legend legend = chart.getLegend();
        assertNotNull(legend);
        assertEquals(rowColorModel, legend.getRowColorModel());
        
        Title title = chart.getTitle();
        assertNotNull(title);
        assertEquals(this.title, title.getText());
        
        assertNull(chart.getCoordSystem());
    }
    
    @Test
    public void testRender() {
        Graphics2D graphics = mock(Graphics2D.class);
        Rectangle bounds = new Rectangle(0, 0, 100, 100);
        defaultChart.setBounds(bounds);
        
        defaultChart.render(graphics);
        
        verify(graphics).setColor(Color.white);
        verify(graphics).fillRect(0, 0, 100, 100);
        
        verify(graphics).setColor(Color.black);
        
        verify(model).getPreferredSize();
        verify(model).getBounds();
        
        verify(graphics).setColor(Color.white);
        
        verify(model).getPreferredSize();
        verify(model).getBounds();
        
        verify(graphics).setColor(Color.black);
        
        verify(model).getBounds();
        
        Collection<AbstractChartRenderer> renderers = defaultChart.getChartRenderer().values();
        for (AbstractChartRenderer renderer : renderers) {
            verify(renderer).setBounds(new Rectangle(0, 0, 95, 95));
            verify(renderer).render(graphics);
        }
        
        verify(model, times(2)).getBounds();
        
        CoordSystem coordSystem = defaultChart.getCoordSystem();
        verify(coordSystem).setBounds(new Rectangle(0, 0, 95, 95));
        verify(coordSystem).render(graphics);
    }

}