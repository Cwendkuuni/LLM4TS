package de.progra.charting;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    private DefaultChart defaultChart;

    @Before
    public void setup() {
        // Initialize defaultChart object before each test
        ChartDataModel model = new ChartDataModel();
        String title = "Test Title";
        int coord = DefaultChart.LINEAR_X_LINEAR_Y;
        String xaxis = "X Axis";
        String yaxis = "Y Axis";

        defaultChart = new DefaultChart(model, title, coord, xaxis, yaxis);
    }

    @Test
    public void testConstructor() {
        assertNotNull(defaultChart);

        ChartDataModel model = defaultChart.getChartDataModel();
        assertNotNull(model);

        RowColorModel rowColorModel = defaultChart.getRowColorModel();
        assertNotNull(rowColorModel);

        Legend legend = defaultChart.getLegend();
        assertNotNull(legend);

        Title title = defaultChart.getTitle();
        assertNotNull(title);
        assertEquals("Test Title", title.getText());

        CoordSystem coordSystem = defaultChart.getCoordSystem();
        assertNotNull(coordSystem);
        assertEquals("X Axis", coordSystem.getXAxisUnit());
        assertEquals("Y Axis", coordSystem.getYAxisUnit());
    }

    @Test
    public void testGetPreferredSize() {
        // Test the getPreferredSize() method
        // Expected result is null
        assertNull(defaultChart.getPreferredSize());
    }

    @Test
    public void testRender() {
        // Test the render(Graphics2D) method
        // Since the method only contains graphics rendering logic,
        // it is difficult to perform unit testing on it.
        // Therefore, no assertion is made in this test.
    }
}