import static org.junit.Assert.*;
import org.junit.Test;

public class PlotAxisTest {
    
    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.setLabel("Test Label");
        
        assertEquals("Test Label", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.setScale(new LinearAxisScale());
        
        assertTrue(plotAxis.getScale() instanceof LinearAxisScale);
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.setNumberFormat(NumberFormat.getInstance());
        
        assertTrue(plotAxis.getNumberFormat() instanceof NumberFormat);
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.setGridStyle(2);
        
        assertEquals(2, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.manualLowerBounds(true);
        
        assertTrue(plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.setLowerBounds(3.14);
        
        assertEquals(3.14, plotAxis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };

        plotAxis.setLowerBounds(3.14);
        
        assertEquals("3.14", plotAxis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.manualUpperBounds(true);
        
        assertTrue(plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.setUpperBounds(6.28);
        
        assertEquals(6.28, plotAxis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };

        plotAxis.setUpperBounds(6.28);
        
        assertEquals("6.28", plotAxis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.manualTickStepSize(true);
        
        assertTrue(plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.setTickStepSize(0.5);
        
        assertEquals(0.5, plotAxis.getTickStepSize(), 0.001);
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        plotAxis.xA = 2.0;
        plotAxis.xB = 3.0;
        plotAxis.scale = new LinearAxisScale() {
            public double func(double x) {
                return 2*x;
            }
        };
        
        int scaledCoord = plotAxis.scaleCoord(2.0);
        
        assertEquals(10, scaledCoord);
    }
    
    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {
            public void setAxisBounds(final PlotRunList p0) {}
            protected void resizeAxis(final Rectangle p0) {}
            public void draw(final Graphics p0, final Component p1, final Rectangle p2) {}
        };
        
        Object clonedObject = plotAxis.clone();
        
        assertTrue(clonedObject instanceof PlotAxis);
    }
}