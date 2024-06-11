import static org.junit.Assert.*;
import org.junit.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.setLabel("Test Label");
        assertEquals("Test Label", plotAxis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.setScale(new LinearAxisScale());
        assertEquals(LinearAxisScale.class, plotAxis.getScale().getClass());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.setNumberFormat(NumberFormat.getInstance());
        assertEquals(NumberFormat.class, plotAxis.getNumberFormat().getClass());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, plotAxis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.manualLowerBounds(true);
        assertTrue(plotAxis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.setLowerBounds(10.0);
        assertEquals(10.0, plotAxis.getLowerBounds(), 0.0001);
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.manualUpperBounds(true);
        assertTrue(plotAxis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.setUpperBounds(20.0);
        assertEquals(20.0, plotAxis.getUpperBounds(), 0.0001);
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.manualTickStepSize(true);
        assertTrue(plotAxis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.setTickStepSize(0.1);
        assertEquals(0.1, plotAxis.getTickStepSize(), 0.0001);
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        plotAxis.xA = 2.0;
        plotAxis.xB = 3.0;
        
        int result = plotAxis.scaleCoord(5.0);
        
        assertEquals(13, result);
    }
    
    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            protected void resizeAxis(Rectangle p0) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                // TODO Auto-generated method stub
                
            }
        };
        
        PlotAxis clonedAxis = (PlotAxis) plotAxis.clone();
        
        assertNotSame(plotAxis, clonedAxis);
        assertEquals(plotAxis.getClass(), clonedAxis.getClass());
    }

}