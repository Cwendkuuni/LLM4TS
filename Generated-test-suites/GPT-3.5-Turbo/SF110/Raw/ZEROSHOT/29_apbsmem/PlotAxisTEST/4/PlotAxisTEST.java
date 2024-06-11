package jahuwaldt.plot;

import org.junit.*;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {
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
        
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {
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
        
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setGridStyle(PlotAxis.kNoGrid);
        assertEquals(PlotAxis.kNoGrid, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.manualLowerBounds(true);
        assertTrue(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setLowerBounds(10.0);
        assertEquals(10.0, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setLowerBounds(10.0);
        assertEquals(10.0, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setLowerBounds(10.0);
        assertEquals("10.0", axis.lowerBoundsAsString());
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.manualUpperBounds(true);
        assertTrue(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setUpperBounds(100.0);
        assertEquals(100.0, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setUpperBounds(100.0);
        assertEquals(100.0, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setUpperBounds(100.0);
        assertEquals("100.0", axis.upperBoundsAsString());
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.manualTickStepSize(true);
        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
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
        
        axis.setTickStepSize(1.0);
        assertEquals(1.0, axis.getTickStepSize(), 0.0);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis() {
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
        
        assertEquals(10, axis.scaleCoord(5.0));
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis() {
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
        
        PlotAxis clonedAxis = (PlotAxis)axis.clone();
        assertNotSame(clonedAxis, axis);
    }

}