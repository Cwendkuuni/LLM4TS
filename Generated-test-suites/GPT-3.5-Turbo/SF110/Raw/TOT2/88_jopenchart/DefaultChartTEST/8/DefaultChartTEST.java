import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultChartTest {
    
    @Test
    public void testDefaultChartConstructor() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertNotNull(chart);
        assertEquals("Test Chart", chart.getTitle().getText());
    }
    
    @Test
    public void testDefaultChartWithCoordSystem() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testPreferredSizeNotNull() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertNotNull(chart.getPreferredSize());
    }
    
    @Test
    public void testRenderWithNullGraphics() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        assertThrows(NullPointerException.class, () -> chart.render(null));
    }
    
    @Test
    public void testNoLegendRendering() {
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Chart");
        Graphics2D g = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).createGraphics();
        chart.render(g);
        // Check that no legend is rendered
    }
    
    // Add more test cases for edge cases and error scenarios
    
}