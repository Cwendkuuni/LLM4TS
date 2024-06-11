package de.progra.charting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.Collection;
import java.util.HashMap;

public class DefaultChartTest {

    DefaultChart defaultChart;

    @Before
    public void setup() {
        ChartDataModel model = mock(ChartDataModel.class);
        defaultChart = new DefaultChart(model, "title");
    }

    @Test
    public void testConstructor() {
        ChartDataModel model = mock(ChartDataModel.class);
        DefaultChart chart = new DefaultChart(model, "title");
        assertEquals(model, chart.getChartDataModel());
    }

    @Test
    public void testConstructorWithCoords() {
        ChartDataModel model = mock(ChartDataModel.class);
        DefaultChart chart = new DefaultChart(model, "title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testConstructorWithAxis() {
        ChartDataModel model = mock(ChartDataModel.class);
        DefaultChart chart = new DefaultChart(model, "title", DefaultChart.LINEAR_X_LINEAR_Y, "xaxis", "yaxis");
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getCoordSystem());
        assertEquals("xaxis", chart.getCoordSystem().getXAxisUnit());
        assertEquals("yaxis", chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        Dimension expectedSize = new Dimension(100, 100);
        defaultChart.setBounds(new Rectangle(expectedSize));
        assertEquals(expectedSize, defaultChart.getPreferredSize());
    }

    @Test
    public void testRender() {
        Graphics2D g = mock(Graphics2D.class);
        int width = 100;
        int height = 100;
        defaultChart.setBounds(new Rectangle(width, height));

        Title title = mock(Title.class);
        defaultChart.setTitle(title);

        Legend legend = mock(Legend.class);
        defaultChart.setLegend(legend);

        CoordSystem coordSystem = mock(CoordSystem.class);
        defaultChart.setCoordSystem(coordSystem);

        Collection<AbstractChartRenderer> renderer = new HashMap<String, AbstractChartRenderer>().values();
        defaultChart.setChartRenderer(renderer);

        defaultChart.render(g);

        // Verify that the necessary methods are called with correct arguments
        verify(g).setColor(Color.white);
        verify(g).setColor(Color.black);
        verify(g).fillRect(0, 0, width, height);
        verify(legend).setBounds(any(Rectangle.class));
        verify(legend).render(g);
        verify(coordSystem).setBounds(any(Rectangle.class));
        verify(coordSystem).render(g);

        // Verify that render method is called for each AbstractChartRenderer in the collection
        for (AbstractChartRenderer cr : renderer) {
            verify(cr).setBounds(any(Rectangle.class));
            verify(cr).render(g);
        }
    }
}