import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {
    
    @Test
    public void testDefaultChartConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart 1";
        DefaultChart chart = new DefaultChart(model, title);
        
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testDefaultChartConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart 2";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testDefaultChartConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart 3";
        int coord = DefaultChart.NO_COORDINATE_SYSTEM;
        String xaxis = "x";
        String yaxis = "y";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        assertEquals(model, chart.getChartDataModel());
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
        // unable to test render method since it depends on Graphics2D and other components
    }
    
}