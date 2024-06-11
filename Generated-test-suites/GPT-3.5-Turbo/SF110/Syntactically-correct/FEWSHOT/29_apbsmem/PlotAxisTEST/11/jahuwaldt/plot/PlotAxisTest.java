package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PlotAxisTest {
    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.setLabel("x-axis");
        assertEquals("x-axis", axis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        LinearAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        int style = PlotAxis.kMajorGrid;
        axis.setGridStyle(style);
        assertEquals(style, axis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.manualLowerBounds(true);
        axis.manualLowerBounds(false);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.setLowerBounds(0.0);
        assertEquals(0.0, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testlowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.setLowerBounds(0.0);
        assertEquals("0.0", axis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.manualUpperBounds(true);
        axis.manualUpperBounds(false);
    }
    
    @Test
    public void testsetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.setUpperBounds(10.0);
        assertEquals(10.0, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testupperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.setUpperBounds(10.0);
        assertEquals("10.0", axis.upperBoundsAsString());
    }
    
    @Test
    public void testmanualTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.manualTickStepSize(true);
        axis.manualTickStepSize(false);
    }
    
    @Test
    public void testsetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {}
            protected void resizeAxis(Rectangle p0) {}
            public void draw(Graphics p0, Component p1, Rectangle p2) {}
        };
        
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }
    
    // Test remaining methods in PlotAxis that require Abstract methods to be implemented.
}