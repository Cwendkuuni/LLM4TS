import org.junit.Test;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        String label = "Axis Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }

    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        int style = 2;
        axis.setGridStyle(style);
        assertEquals(style, axis.getGridStyle());
    }

    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        boolean flag = true;
        axis.manualLowerBounds(flag);
        assertTrue(axis.manualLB);
    }

    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        double value = 5.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0.0);
    }

    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        boolean flag = true;
        axis.manualUpperBounds(flag);
        assertTrue(axis.manualUB);
    }

    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        double value = 10.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0.0);
    }

    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        boolean flag = true;
        axis.manualTickStepSize(flag);
        assertTrue(axis.manualQuant);
    }

    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                
            }
            protected void resizeAxis(Rectangle p0) {
                
            }
            public void draw(Graphics p0, Component p1, Rectangle p2) {
                
            }
        };
        
        double value = 0.5;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize(), 0.0);
    }
}