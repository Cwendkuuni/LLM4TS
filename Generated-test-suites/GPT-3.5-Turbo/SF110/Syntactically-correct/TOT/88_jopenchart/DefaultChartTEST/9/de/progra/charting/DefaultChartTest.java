import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testDefaultConstructor() {
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getPreferredSize());
        chart.render(null); // Should not produce any exceptions
    }

    @Test
    public void testChartDataModelConstructor() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        chart.render(null); // Should not produce any exceptions
    }

    @Test
    public void testCoordSystemConstructor() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        assertNotNull(chart.getCoordSystem());
        chart.getCoordSystem().setXAxisUnit("X");
        chart.getCoordSystem().setYAxisUnit("Y");
        chart.render(null); // Should not produce any exceptions
    }

    @Test
    public void testFullConstructor() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xAxis = "X";
        String yAxis = "Y";
        DefaultChart chart = new DefaultChart(model, title, coord, xAxis, yAxis);
        assertNotNull(chart.getCoordSystem());
        assertNotNull(chart.getCoordSystem().getXAxisUnit());
        assertNotNull(chart.getCoordSystem().getYAxisUnit());
        chart.render(null); // Should not produce any exceptions
    }

    @Test
    public void testRender() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        chart.render(null); // Should not produce any exceptions
    }
}