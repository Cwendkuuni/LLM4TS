
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChart() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        DefaultChart chart = new DefaultChart(model, title);
        
        // Check if chart is initialized correctly
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }
    
    @Test
    public void testDefaultChartWithCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        DefaultChart chart = new DefaultChart(model, title, coord);
        
        // Check if chart is initialized correctly with coordinate system
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertTrue(chart.getCoordSystem().getXAxisUnit().isEmpty());
        assertTrue(chart.getCoordSystem().getYAxisUnit().isEmpty());
    }
    
    @Test
    public void testDefaultChartWithAxisUnits() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X";
        String yaxis = "Y";
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        // Check if chart is initialized correctly with coordinate system and axis units
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
        
        // Check that getPreferredSize returns null
        assertNull(chart.getPreferredSize());
    }
    
    // Additional tests for render() method can be added, but it may require mocking Graphics2D object

}
