import org.junit.*;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        DefaultChart chart = new DefaultChart(model, title);
        assertNotNull(chart);
        assertEquals(model, chart.getChartDataModel());
        assertEquals(title, chart.getTitle().getText());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
    }

    @Test
    public void testConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertNotNull(chart);
        assertNotNull(chart.getCoordSystem());
        assertEquals(coord, chart.getCoordSystem().getCoord());
    }

    @Test
    public void testConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        assertNotNull(chart);
        assertNotNull(chart.getCoordSystem());
        assertEquals(coord, chart.getCoordSystem().getCoord());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getPreferredSize());
    }

    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        // test rendering process
    }
}