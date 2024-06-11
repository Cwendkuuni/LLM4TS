import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChart() {
        // Given
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";

        // When
        DefaultChart chart = new DefaultChart(model, title);

        // Then
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartWithCoordinateSystem() {
        // Given
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;

        // When
        DefaultChart chart = new DefaultChart(model, title, coord);

        // Then
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartWithAxisUnits() {
        // Given
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";

        // When
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);

        // Then
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        // Given
        DefaultChart chart = new DefaultChart();

        // When
        Dimension preferredSize = chart.getPreferredSize();

        // Then
        assertNull(preferredSize);
    }

    @Test
    public void testRender() {
        // Given
        DefaultChart chart = new DefaultChart();
        Graphics2D g = new Graphics2D();

        // When
        chart.render(g);

        // Then
        // Verify rendering functionality
    }
}