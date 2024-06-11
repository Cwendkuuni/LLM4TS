import static org.junit.Assert.*;
import org.junit.*;
import de.progra.charting.model.*;
import java.awt.*;
import de.progra.charting.render.*;
import java.util.*;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstructor1() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart 1";
        
        DefaultChart chart = new DefaultChart(model, title);
        
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNull(chart.getCoordSystem());
    }
    
    @Test
    public void testDefaultChartConstructor2() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart 2";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        
        DefaultChart chart = new DefaultChart(model, title, coord);
        
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals(coord, chart.getCoordSystem().getCoordType());
    }
    
    @Test
    public void testDefaultChartConstructor3() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart 3";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X-Axis";
        String yaxis = "Y-Axis";
        
        DefaultChart chart = new DefaultChart(model, title, coord, xaxis, yaxis);
        
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals(coord, chart.getCoordSystem().getCoordType());
        assertEquals(xaxis, chart.getCoordSystem().getXAxisUnit());
        assertEquals(yaxis, chart.getCoordSystem().getYAxisUnit());
    }
    
    @Test
    public void testGetPreferredSize() {
        DefaultChart chart = new DefaultChart();
        
        Dimension preferredSize = chart.getPreferredSize();
        
        assertNull(preferredSize);
    }
    
    @Test
    public void testRender() {
        DefaultChart chart = new DefaultChart();
        Graphics2D g = new Graphics2D();
        
        chart.render(g);
        
        // TODO: Add assertions for the rendering outcome
        
    }

}