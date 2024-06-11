import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.*;
import de.progra.charting.model.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChart() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);

        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }

    @Test
    public void testDefaultChartWithCoordSystem() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);

        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartWithXandYAxis() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);

        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getCoordSystem().getXAxisUnit());
        assertNotNull(chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart();

        Dimension preferredSize = chart.getPreferredSize();

        assertNotNull(preferredSize);
        assertEquals(0, preferredSize.getWidth(), 0);
        assertEquals(0, preferredSize.getHeight(), 0);
    }

    @Test
    public void testRender() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);

        Graphics2D g = new Graphics2D();

        chart.render(g);

        // Assertion for rendered output can be added here
    }
}