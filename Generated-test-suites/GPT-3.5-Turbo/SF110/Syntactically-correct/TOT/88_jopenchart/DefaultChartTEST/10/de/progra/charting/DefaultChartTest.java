import static org.junit.Assert.*;
import org.junit.*;
import java.awt.*;
import de.progra.charting.render.*;
import de.progra.charting.model.*;

public class DefaultChartTest {

    private DefaultChart chart;

    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = 0;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        chart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }
    
    @Test
    public void testConstructor1() {
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
    }
    
    @Test
    public void testConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        chart = new DefaultChart(model, title);
        
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
    }
    
    @Test
    public void testConstructor3_WithLinearCoord() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        chart = new DefaultChart(model, title, coord);
        
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }
    
    @Test
    public void testConstructor4() {
        ChartDataModel model = new ChartDataModel();
        String title = "Chart Title";
        int coord = DefaultChart.NO_COORDINATE_SYSTEM;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        assertNotNull(chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
    }
    
    @Test
    public void testGetPreferredSize() {
        Dimension preferredSize = chart.getPreferredSize();
        
        assertNull(preferredSize);
    }
    
    @Test
    public void testRender() {
        Graphics2D g = new Graphics2D();
        chart.render(g);
        
        // assert rendering logic
    }
}