package jahuwaldt.plot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class PlotAxisTest {

    @Mock
    private PlotRunList plotRunListMock;
    @Mock
    private Rectangle plotFrameMock;
    @Mock
    private Graphics graphicsMock;
    @Mock
    private Component componentMock;
    
    private PlotAxis plotAxis;
    
    @Before
    public void setUp() {
        plotAxis = new PlotAxis() {
            
            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }
        };
    }
    
    @Test
    public void testSetLabel() {
        String label = "label";
        plotAxis.setLabel(label);
        assertEquals(label, plotAxis.getLabel());
    }
    
    @Test
    public void testGetLabel() {
        String label = "label";
        plotAxis.setLabel(label);
        assertEquals(label, plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxisScale scale = mock(PlotAxisScale.class);
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }
    
    @Test
    public void testGetScale() {
        PlotAxisScale scale = mock(PlotAxisScale.class);
        plotAxis.setScale(scale);
        assertEquals(scale, plotAxis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        NumberFormat numberFormat = mock(NumberFormat.class);
        plotAxis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testGetNumberFormat() {
        NumberFormat numberFormat = mock(NumberFormat.class);
        plotAxis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        int gridStyle = 2;
        plotAxis.setGridStyle(gridStyle);
        assertEquals(gridStyle, plotAxis.getGridStyle());
    }
    
    @Test
    public void testGetGridStyle() {
        int gridStyle = 2;
        plotAxis.setGridStyle(gridStyle);
        assertEquals(gridStyle, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        boolean flag = true;
        plotAxis.manualLowerBounds(flag);
        plotAxis.setLowerBounds(1.0);
        assertEquals(flag, plotAxis.getLowerBounds() == 1.0);
    }
    
    @Test
    public void testSetLowerBounds() {
        double value = 1.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }
    
    @Test
    public void testGetLowerBounds() {
        double value = 1.0;
        plotAxis.setLowerBounds(value);
        assertEquals(value, plotAxis.getLowerBounds(), 0.0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        double value = 1.0;
        plotAxis.setLowerBounds(value);
        plotAxis.setNumberFormat(NumberFormat.getInstance());
        assertEquals(Double.toString(value), plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        boolean flag = true;
        plotAxis.manualUpperBounds(flag);
        plotAxis.setUpperBounds(10.0);
        assertEquals(flag, plotAxis.getUpperBounds() == 10.0);
    }
    
    @Test
    public void testSetUpperBounds() {
        double value = 10.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }
    
    @Test
    public void testGetUpperBounds() {
        double value = 10.0;
        plotAxis.setUpperBounds(value);
        assertEquals(value, plotAxis.getUpperBounds(), 0.0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        double value = 10.0;
        plotAxis.setUpperBounds(value);
        plotAxis.setNumberFormat(NumberFormat.getInstance());
        assertEquals(Double.toString(value), plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        boolean flag = true;
        plotAxis.manualTickStepSize(flag);
        plotAxis.setTickStepSize(2.0);
        assertEquals(flag, plotAxis.getTickStepSize() == 2.0);
    }
    
    @Test
    public void testSetTickStepSize() {
        double value = 2.0;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }
    
    @Test
    public void testGetTickStepSize() {
        double value = 2.0;
        plotAxis.setTickStepSize(value);
        assertEquals(value, plotAxis.getTickStepSize(), 0.0);
    }

    @Test
    public void testSetAxisBounds() {
        plotAxis.setAxisBounds(plotRunListMock);
        // Add verification and assertions
    }
    
    @Test
    public void testSetSize() {
        plotAxis.setSize(plotFrameMock);
        // Add verification and assertions
    }
    
    @Test
    public void testResizeAxis() {
        plotAxis.resizeAxis(plotFrameMock);
        // Add verification and assertions
    }
    
    @Test
    public void testDraw() {
        plotAxis.draw(graphicsMock, componentMock, plotFrameMock);
        // Add verification and assertions
    }
    
    @Test
    public void testScaleCoord() {
        double x = 1.0;
        int result = plotAxis.scaleCoord(x);
        // Add verification and assertions
    }
    
    @Test
    public void testClone() {
        Object clonedObject = plotAxis.clone();
        // Add verification and assertions
    }
}