import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultChartTest {
    
    @Test
    public void testDefaultChart1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        DefaultChart chart = new DefaultChart(model, title);
        
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testDefaultChart2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        
        assertNotNull(chart);
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testDefaultChart3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
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
        DefaultChart chart = new DefaultChart();
        assertNull(chart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        // TODO: Write test case for render method
    }
}