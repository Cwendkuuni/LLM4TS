
package de.progra.charting;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultChartTest {

    private DefaultChart defaultChart;

    @Before
    public void setUp() {
        defaultChart = new DefaultChart(null, null);
    }

    @Test
    public void testGetPreferredSize() {
        assertNull(defaultChart.getPreferredSize());
    }

    @Test
    public void testRender() {
        // To be implemented
    }
}