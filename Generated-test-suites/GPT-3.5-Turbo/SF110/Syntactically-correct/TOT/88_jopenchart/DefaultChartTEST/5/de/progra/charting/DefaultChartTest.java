import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Collection;
import de.progra.charting.DefaultChart;
import de.progra.charting.model.ChartDataModel;
import de.progra.charting.render.AbstractChartRenderer;
import de.progra.charting.model.RowColorModel;

public class DefaultChartTest {

    @Test
    public void testDefaultChart() {
        // Test DefaultChart constructor with ChartDataModel and title
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart defaultChart = new DefaultChart(model, title);
        assertNotNull(defaultChart);
        assertNotNull(defaultChart.getCoordSystem());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getRowColorModel());
        assertEquals(title, defaultChart.getTitle().getText());
    }

    @Test
    public void testDefaultChartWithCoordSystem() {
        // Test DefaultChart constructor with ChartDataModel, title, and coord
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart defaultChart = new DefaultChart(model, title, coord);
        assertNotNull(defaultChart);
        assertNotNull(defaultChart.getCoordSystem());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getRowColorModel());
        assertEquals(title, defaultChart.getTitle().getText());
    }
    
    @Test
    public void testDefaultChartWithAxes() {
        // Test DefaultChart constructor with ChartDataModel, title, coord, x-axis, and y-axis
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X";
        String yaxis = "Y";
        DefaultChart defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertNotNull(defaultChart);
        assertNotNull(defaultChart.getCoordSystem());
        assertNotNull(defaultChart.getLegend());
        assertNotNull(defaultChart.getRowColorModel());
        assertEquals(title, defaultChart.getTitle().getText());
        assertEquals(xaxis, defaultChart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, defaultChart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        // Test getPreferredSize() method
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart defaultChart = new DefaultChart(model, title);
        assertNull(defaultChart.getPreferredSize());
    }

    @Test
    public void testRender() {
        // Test render method with complete chart rendering
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart defaultChart = new DefaultChart(model, title);
        Graphics2D g = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        defaultChart.render(g);
        assertNotNull(defaultChart.getBounds());
    }

    @Test
    public void testRenderWithCoordSystem() {
        // Test render method with complete chart rendering including CoordSystem
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart defaultChart = new DefaultChart(model, title, coord);
        Graphics2D g = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        defaultChart.render(g);
        assertNotNull(defaultChart.getBounds());
        assertNotNull(defaultChart.getCoordSystem().getBounds());
    }

    @Test
    public void testRenderWithRenderer() {
        // Test render method with complete chart rendering including AbstractChartRenderer
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart defaultChart = new DefaultChart(model, title);
        defaultChart.getChartRenderer().put("TestRenderer", new AbstractChartRenderer());
        Graphics2D g = (Graphics2D) new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        defaultChart.render(g);
        assertNotNull(defaultChart.getBounds());
        for (AbstractChartRenderer renderer : defaultChart.getChartRenderer().values()) {
            assertNotNull(renderer.getBounds());
        }
    }

}