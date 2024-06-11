
package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PlotAxisTest {

    private static final double DELTA = 1e-15;

    @Mock
    private NumberFormat nfMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDefaultConstructor() {
        PlotAxis plotAxis = new TestPlotAxis();
        
        assertNull(plotAxis.getLabel());
        assertEquals(PlotAxisScale.class, plotAxis.getScale().getClass());
        assertEquals(1, plotAxis.getGridStyle());
        assertEquals(1.0, plotAxis.getLowerBounds(), DELTA);
        assertEquals(1.0, plotAxis.getUpperBounds(), DELTA);
        assertEquals(NumberFormat.getInstance(), plotAxis.getNumberFormat());
    }

    @Test
    public void testParameterizedConstructor() {
        PlotAxisScale scaleMock = mock(PlotAxisScale.class);
        NumberFormat nf = new DecimalFormat("#0.00");
        int gridStyle = 2;
        
        PlotAxis plotAxis = new TestPlotAxis("X Axis", scaleMock, nf, gridStyle);
        
        assertEquals("X Axis", plotAxis.getLabel());
        assertEquals(scaleMock, plotAxis.getScale());
        assertEquals(nf, plotAxis.getNumberFormat());
        assertEquals(gridStyle, plotAxis.getGridStyle());
    }

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new TestPlotAxis();
        
        plotAxis.setLabel("New Label");
        
        assertEquals("New Label", plotAxis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new TestPlotAxis();
        PlotAxisScale scaleMock = mock(PlotAxisScale.class);
        
        plotAxis.setScale(scaleMock);
        
        assertEquals(scaleMock, plotAxis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new TestPlotAxis();
        
        plotAxis.setNumberFormat(nfMock);
        
        assertEquals(nfMock, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new TestPlotAxis();
        int gridStyle = 2;
        
        plotAxis.setGridStyle(gridStyle);
        
        assertEquals(gridStyle, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new TestPlotAxis();
        
        plotAxis.manualLowerBounds(true);
        
        assertEquals(true, plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new TestPlotAxis();
        double lowerBounds = 2.5;
        
        plotAxis.setLowerBounds(lowerBounds);
        
        assertEquals(lowerBounds, plotAxis.getLowerBounds(), DELTA);
    }

    @Test
    public void testLowerBoundsAsString() {
        when(nfMock.format(anyFloat())).thenReturn("2.50");
        
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setNumberFormat(nfMock);
        plotAxis.setLowerBounds(2.5);
        
        assertEquals("2.50", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new TestPlotAxis();
        
        plotAxis.manualUpperBounds(true);
        
        assertEquals(true, plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new TestPlotAxis();
        double upperBounds = 10.0;
        
        plotAxis.setUpperBounds(upperBounds);
        
        assertEquals(upperBounds, plotAxis.getUpperBounds(), DELTA);
    }

    @Test
    public void testUpperBoundsAsString() {
        when(nfMock.format(anyFloat())).thenReturn("10.00");
        
        PlotAxis plotAxis = new TestPlotAxis();
        plotAxis.setNumberFormat(nfMock);
        plotAxis.setUpperBounds(10.0);
        
        assertEquals("10.00", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new TestPlotAxis();
        
        plotAxis.manualTickStepSize(true);
        
        assertEquals(true, plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new TestPlotAxis();
        double tickStepSize = 0.1;
        
        plotAxis.setTickStepSize(tickStepSize);
        
        assertEquals(tickStepSize, plotAxis.getTickStepSize(), DELTA);
    }
    
    @Test
    public void testSetSize() {
        PlotAxis plotAxis = new TestPlotAxis();
        Component plotFrame = mock(Component.class);
        Rectangle rectangle = new Rectangle(0, 0, 100, 100);
        
        when(nfMock.format(anyFloat())).thenReturn("2.50", "10.00");
        plotAxis.setNumberFormat(nfMock);
        plotAxis.setLowerBounds(0);
        plotAxis.setUpperBounds(100);
        
        plotAxis.setSize(rectangle);
        
        verify(plotAxis, times(1)).setAxisTickLimits();
        verify(plotAxis, times(1)).resizeAxis(rectangle);
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new TestPlotAxis();
        double x = 2.5;
        
        int coord = plotAxis.scaleCoord(x);
        
        assertEquals(2, coord);
    }

    class TestPlotAxis extends PlotAxis {
        @Override
        public void setAxisBounds(PlotRunList p0) {
            // Do nothing
        }

        @Override
        protected void resizeAxis(Rectangle p0) {
            // Do nothing
        }

        @Override
        public void draw(Graphics p0, Component p1, Rectangle p2) {
            // Do nothing
        }
    }

}
