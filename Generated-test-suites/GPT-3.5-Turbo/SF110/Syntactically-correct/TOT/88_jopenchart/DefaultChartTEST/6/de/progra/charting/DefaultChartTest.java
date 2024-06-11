import org.junit.Before;
import org.junit.Test;
import java.awt.Graphics2D;
import static org.junit.Assert.*;

public class DefaultChartTest {

    private DefaultChart chart;
    private ChartDataModel model;
    private CoordSystem coordSystem;

    @Before
    public void setUp() {
        model = new ChartDataModel();
        chart = new DefaultChart(model, "Test Chart", DefaultChart.LINEAR_X_LINEAR_Y, "X", "Y");
        coordSystem = new CoordSystem(model);
    }

    @Test
    public void testConstructor1() {
        assertEquals(model, chart.getChartDataModel());
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }

    @Test
    public void testConstructor2() {
        DefaultChart chart = new DefaultChart(model, "Test Chart", DefaultChart.NO_COORDINATE_SYSTEM);
        assertNull(chart.getCoordSystem());
    }

    @Test
    public void testConstructor3() {
        assertEquals("X", chart.getCoordSystem().getXAxisUnit());
        assertEquals("Y", chart.getCoordSystem().getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        Dimension size = chart.getPreferredSize();
        assertNotNull(size);
    }

    @Test
    public void testRender() {
        Graphics2D g = createGraphics2D();
        chart.render(g);
        // Test rendering logic
    }

    private Graphics2D createGraphics2D() {
        // Create a dummy Graphics2D object for testing render method
        return new Graphics2D();
    }

    @Test
    public void testStaticVariables() {
        assertEquals(0, DefaultChart.LINEAR_X_LINEAR_Y);
        assertEquals(1, DefaultChart.NO_COORDINATE_SYSTEM);
    }

}