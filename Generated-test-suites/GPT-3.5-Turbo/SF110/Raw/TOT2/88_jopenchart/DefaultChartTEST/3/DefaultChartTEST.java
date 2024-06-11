import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import de.progra.charting.DefaultChart;
import de.progra.charting.model.ChartDataModel;
import java.awt.Graphics2D;

public class DefaultChartTest {
    
    private DefaultChart defaultChart;
    
    @Before
    public void setUp() {
        // Create a DefaultChart object for testing
        ChartDataModel model = new ChartDataModel();
        defaultChart = new DefaultChart(model, "Test Title");
    }
    
    @Test
    public void testGetPreferredSize() {
        assertNotNull(defaultChart.getPreferredSize());
    }
    
    @Test
    public void testRender() {
        Graphics2D g = null; // Need to mock this object for testing
        defaultChart.render(g);
        // Add assertions to check the rendering functionality
    }
    
    @Test
    public void testLinearXLinearYConstructor() {
        // Test the constructor with linear coordinate system
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Title", DefaultChart.LINEAR_X_LINEAR_Y);
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testNoCoordinateSystemConstructor() {
        // Test the constructor with no coordinate system
        ChartDataModel model = new ChartDataModel();
        DefaultChart chart = new DefaultChart(model, "Test Title", DefaultChart.NO_COORDINATE_SYSTEM);
        assertNull(chart.getCoordSystem());
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullGraphicsInRender() {
        // Test rendering with null Graphics2D object
        defaultChart.render(null);
    }
}