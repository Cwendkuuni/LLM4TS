package jahuwaldt.plot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import java.awt.Rectangle;
import java.text.NumberFormat;

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
        axis.setLabel("x-axis");
        assertEquals("x-axis", axis.getLabel());
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
        assertNotNull(axis.getScale());
        axis.setScale(null);
        assertTrue(axis.getScale() instanceof LinearAxisScale);
        axis.setScale(new LogarithmicAxisScale());
        assertTrue(axis.getScale() instanceof LogarithmicAxisScale);
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
        assertNotNull(axis.getNumberFormat());
        axis.setNumberFormat(null);
        assertNotNull(axis.getNumberFormat());
        axis.setNumberFormat(NumberFormat.getCurrencyInstance());
        assertTrue(axis.getNumberFormat() instanceof NumberFormat);
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
        axis.setGridStyle(PlotAxis.kNoGrid);
        assertEquals(PlotAxis.kNoGrid, axis.getGridStyle());
        axis.setGridStyle(PlotAxis.kTickMarksOnly);
        assertEquals(PlotAxis.kTickMarksOnly, axis.getGridStyle());
        axis.setGridStyle(PlotAxis.kMajorGrid);
        assertEquals(PlotAxis.kMajorGrid, axis.getGridStyle());
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
        axis.manualLowerBounds(true);
        axis.manualLowerBounds(false);
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
        axis.setLowerBounds(10.0);
        assertEquals(10.0, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.001);
    }

    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        assertNull(axis.lowerBoundsAsString());
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
        axis.manualUpperBounds(true);
        axis.manualUpperBounds(false);
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
        axis.setUpperBounds(100.0);
        assertEquals(100.0, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.001);
    }

    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        assertNull(axis.upperBoundsAsString());
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
        axis.manualTickStepSize(true);
        axis.manualTickStepSize(false);
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
        axis.setTickStepSize(0.5);
        assertEquals(0.5, axis.getTickStepSize(), 0.001);
    }

    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        assertEquals(1.0, axis.getTickStepSize(), 0.001);
    }

    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
                assertEquals("PlotRunList object", p0);
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        axis.setAxisBounds(new PlotRunList());
    }

    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
                assertEquals(new Rectangle(), p0);
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        axis.setSize(new Rectangle());
    }

    @Test
    public void testDraw() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
                assertNotNull(p0);
                assertNotNull(p1);
                assertNotNull(p2);
            }
        };
        axis.draw(null, null, null);
    }

    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        assertEquals(0, axis.scaleCoord(0.0));
        assertEquals(10, axis.scaleCoord(10.0));
    }

    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis() {
            public void setAxisBounds(PlotRunList p0) {
            }

            protected void resizeAxis(Rectangle p0) {
            }

            public void draw(Graphics p0, Component p1, Rectangle p2) {
            }
        };
        assertNotNull(axis.clone());
    }

}