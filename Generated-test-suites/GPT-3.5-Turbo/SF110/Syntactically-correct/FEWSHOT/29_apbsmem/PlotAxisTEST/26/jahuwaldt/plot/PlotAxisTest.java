import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.text.NumberFormat;

import org.junit.Test;

public class PlotAxisTest {
    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setLabel("x-axis");
        assertEquals("x-axis", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setScale(new LinearAxisScale());
        assertEquals(LinearAxisScale.class, plotAxis.getScale().getClass());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        NumberFormat numberFormat = NumberFormat.getInstance();
        plotAxis.setNumberFormat(numberFormat);
        assertEquals(numberFormat, plotAxis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setGridStyle(2);
        assertEquals(2, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.manualLowerBounds(true);
        assertEquals(true, plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setLowerBounds(10);
        assertEquals(10, plotAxis.getLowerBounds(), 0.0001);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setLowerBounds(10);
        assertEquals("10.0", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.manualUpperBounds(true);
        assertEquals(true, plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setUpperBounds(20);
        assertEquals(20, plotAxis.getUpperBounds(), 0.0001);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setUpperBounds(20);
        assertEquals("20.0", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.manualTickStepSize(true);
        assertEquals(true, plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setTickStepSize(0.5);
        assertEquals(0.5, plotAxis.getTickStepSize(), 0.0001);
    }
    
    @Test
    public void testSetSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.setSize(new Rectangle(0, 0, 100, 100));
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        plotAxis.xA = 2.5;
        plotAxis.xB = 3.5;
        assertEquals(4, plotAxis.scaleCoord(1), 0.0001);
    }
    
    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) { }

            @Override
            protected void resizeAxis(Rectangle p0) { }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) { }
        };
        
        PlotAxis clonedPlotAxis = (PlotAxis) plotAxis.clone();
        assertEquals(plotAxis.getClass(), clonedPlotAxis.getClass());
    }
}