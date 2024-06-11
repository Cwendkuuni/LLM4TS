import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    private DefaultChart defaultChart;

    @Before
    public void setUp() {
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";
        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }

    @Test
    public void testDefaultChart() {
        assertNotNull(defaultChart);
    }

    @Test
    public void testSetChartDataModel() {
        ChartDataModel model = new ChartDataModel();
        defaultChart.setChartDataModel(model);
        assertEquals(model, defaultChart.getChartDataModel());
    }

    @Test
    public void testSetRowColorModel() {
        RowColorModel rowColorModel = new RowColorModel(defaultChart.getChartDataModel());
        defaultChart.setRowColorModel(rowColorModel);
        assertEquals(rowColorModel, defaultChart.getRowColorModel());
    }

    @Test
    public void testSetLegend() {
        Legend legend = new Legend(defaultChart.getRowColorModel());
        defaultChart.setLegend(legend);
        assertEquals(legend, defaultChart.getLegend());
    }

    @Test
    public void testSetTitle() {
        Title title = new Title("Test Title");
        defaultChart.setTitle(title);
        assertEquals(title, defaultChart.getTitle());
    }

    @Test
    public void testSetCoordSystem_withLinearXLinearY() {
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        defaultChart.setCoordSystem(new CoordSystem(defaultChart.getChartDataModel()));
        assertEquals(coord, defaultChart.getCoordSystem().getCoord());
    }

    @Test
    public void testSetCoordSystem_withNoCoordinateSystem() {
        int coord = DefaultChart.NO_COORDINATE_SYSTEM;
        defaultChart.setCoordSystem(null);
        assertEquals(coord, defaultChart.getCoordSystem().getCoord());
    }

    @Test
    public void testGetPreferredSize() {
        assertNull(defaultChart.getPreferredSize());
    }

    @Test
    public void testRender_withNullGraphics2D() {
        defaultChart.render(null);
        // No assertion to be made, as the method does not perform any operations if given a null Graphics2D object
    }

    @Test
    public void testRender_withValidGraphics2D() {
        // Create a Graphics2D object
        Graphics2D g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        defaultChart.render(g);
        // No assertion to be made, as the method only performs rendering operations on the Graphics2D object
    }
}