import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import de.progra.charting.DefaultChart;
import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;

import java.awt.*;
import java.util.Collection;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        DefaultChart chart = new DefaultChart(model, title);
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertEquals(title, chart.getTitle().getText());
    }

    @Test
    public void testDefaultChartConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getTitle());
        assertEquals(title, chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem().getXAxisUnit());
        assertNotNull(chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testDefaultChartConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getTitle());
        assertEquals(title, chart.getTitle().getText());
        assertNotNull(chart.getCoordSystem().getXAxisUnit());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertNotNull(chart.getCoordSystem().getYAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        DefaultChart chart = new DefaultChart(model, title);

        Dimension preferredSize = chart.getPreferredSize();

        assertNull(preferredSize);
    }

    @Test
    public void testRender() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        DefaultChart chart = new DefaultChart(model, title);

        Graphics2D g = new Graphics2D();

        chart.render(g);

        // Check if rendering is successful by asserting some conditions
        int width = (int) chart.getBounds().getWidth();
        int height = (int) chart.getBounds().getHeight();
        Title t = chart.getTitle();
        Legend l = chart.getLegend();
        CoordSystem c = chart.getCoordSystem();
        Collection<AbstractChartRenderer> renderer = chart.getChartRenderer().values();

        assertEquals(Color.white, g.getColor());
        assertEquals(new Rectangle(0, 0, width, height), g.getFilledRectangle());

        // Additional assertions to cover all cases of rendering
        assertNotNull(t);
        if (t != null) {
            Dimension titleSize = t.getPreferredSize();
            assertEquals(new Rectangle(width / 2 - (int) (titleSize.getWidth() / 2.0), 0, (int) titleSize.getWidth(),
                    (int) titleSize.getHeight()), t.getBounds());
        }

        assertNotNull(l);
        if (l != null) {
            Dimension legendSize = l.getPreferredSize();
            assertEquals(
                    new Rectangle((int) (width - legendSize.getWidth()), (int) (height / 2 - legendSize.getHeight() / 2.0),
                            (int) legendSize.getWidth(), (int) legendSize.getHeight()), l.getBounds());
        }

        assertNotNull(c);
        if (c != null) {
            assertEquals(new Rectangle(0, 0, width - (int) l.getBounds().getWidth(), height), c.getBounds());
        }

        assertFalse(renderer.isEmpty());

        for (AbstractChartRenderer cr : renderer) {
            assertEquals(new Rectangle(0, 0, width - (int) l.getBounds().getWidth(), height - 5), cr.getBounds());
        }

        if (c != null) {
            assertEquals(new Rectangle(0, 0, width - (int) l.getBounds().getWidth(), height), c.getBounds());
        }
    }
}