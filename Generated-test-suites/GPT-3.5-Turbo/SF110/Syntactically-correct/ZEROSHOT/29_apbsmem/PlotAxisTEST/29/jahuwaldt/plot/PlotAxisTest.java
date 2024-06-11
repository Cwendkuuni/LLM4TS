import org.junit.*;
import static org.junit.Assert.*;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }

    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        String label = "Test Label";
        axis.setLabel(label);
        assertEquals(label, axis.getLabel());
    }
    
    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        PlotAxisScale scale = new LinearAxisScale();
        axis.setScale(scale);
        assertEquals(scale, axis.getScale());
    }

    @Test
    public void testGetScale() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
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
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        NumberFormat format = NumberFormat.getInstance();
        axis.setNumberFormat(format);
        assertEquals(format, axis.getNumberFormat());
    }
    
    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
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
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        int style = 2;
        axis.setGridStyle(style);
        assertEquals(style, axis.getGridStyle());
    }
    
    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
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
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

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

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        double value = 10.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0);
    }
    
    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        double value = 10.0;
        axis.setLowerBounds(value);
        assertEquals(value, axis.getLowerBounds(), 0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        double value = 10.0;
        axis.setLowerBounds(value);
        assertEquals(Double.toString(value), axis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

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

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        double value = 10.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0);
    }
    
    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        double value = 10.0;
        axis.setUpperBounds(value);
        assertEquals(value, axis.getUpperBounds(), 0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        double value = 10.0;
        axis.setUpperBounds(value);
        assertEquals(Double.toString(value), axis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

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

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        double value = 5.0;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize(), 0);
    }
    
    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        double value = 5.0;
        axis.setTickStepSize(value);
        assertEquals(value, axis.getTickStepSize(), 0);
    }
    
    @Test
    public void testSetAxisBounds() {
        // Test not implemented
    }
    
    @Test
    public void testSetSize() {
        // Test not implemented
    }
    
    @Test
    public void testResizeAxis() {
        // Test not implemented
    }
    
    @Test
    public void testDraw() {
        // Test not implemented
    }
    
    @Test
    public void testScaleCoord() {
        // Test not implemented
    }
    
    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {

            }

            @Override
            protected void resizeAxis(Rectangle p0) {

            }

            @Override
            public void draw(Graphics p0, Component p1, Rectangle p2) {

            }
        };
        PlotAxis clone = (PlotAxis)axis.clone();
        assertNotSame(axis, clone);
    }

}