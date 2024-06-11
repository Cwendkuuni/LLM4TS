package de.progra.charting.test;

import static org.junit.Assert.*;
import org.junit.Test;

import de.progra.charting.DefaultChart;
import de.progra.charting.model.ChartDataModel;
import de.progra.charting.model.RowColorModel;
import de.progra.charting.render.AbstractChartRenderer;

public class DefaultChartTest {

    @Test
    public void testDefaultChartConstructor1() {
        // Arrange
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";

        // Act
        DefaultChart chart = new DefaultChart(model, title);

        // Assert
        assertNotNull(chart);
        assertEquals(chart.getChartDataModel(), model);
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
    }

    @Test
    public void testDefaultChartConstructor2() {
        // Arrange
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coordinateSystem = DefaultChart.LINEAR_X_LINEAR_Y;

        // Act
        DefaultChart chart = new DefaultChart(model, title, coordinateSystem);

        // Assert
        assertNotNull(chart);
        assertEquals(chart.getChartDataModel(), model);
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
    }

    @Test
    public void testDefaultChartConstructor3() {
        // Arrange
        ChartDataModel model = new ChartDataModel();
        String title = "Test Chart";
        int coordinateSystem = DefaultChart.LINEAR_X_LINEAR_Y;
        String xAxisUnit = "X";
        String yAxisUnit = "Y";

        // Act
        DefaultChart chart = new DefaultChart(model, title, coordinateSystem, xAxisUnit, yAxisUnit);

        // Assert
        assertNotNull(chart);
        assertEquals(chart.getChartDataModel(), model);
        assertNotNull(chart.getRowColorModel());
        assertNotNull(chart.getLegend());
        assertNotNull(chart.getTitle());
        assertNotNull(chart.getCoordSystem());
        assertEquals(chart.getCoordSystem().getXAxisUnit(), xAxisUnit);
        assertEquals(chart.getCoordSystem().getYAxisUnit(), yAxisUnit);
    }

    @Test
    public void testGetPreferredSize() {
        // Arrange
        DefaultChart chart = new DefaultChart();

        // Act
        Dimension preferredSize = chart.getPreferredSize();

        // Assert
        assertNull(preferredSize);
    }

    @Test
    public void testRender() {
        // Arrange
        DefaultChart chart = new DefaultChart();

        // Act
        Graphics2D g = new Graphics2D();
        chart.render(g);

        // Assert
        // Add specific assertions for rendering
    }
}