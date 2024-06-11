import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlotAxisTest {
    
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    
    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    
    @Test
    public void testConstructor1() {
        PlotAxis axis = new PlotAxis() {};
        
        assertNotNull(axis);
        assertEquals(1.0, axis.xA, 0.001);
        assertEquals(1.0, axis.xB, 0.001);
        assertEquals(Double.MAX_VALUE, axis.aLB, 0.001);
        assertEquals(-1.7976931348623157E308, axis.aUB, 0.001);
        assertEquals(1, axis.gridStyle);
        assertNotNull(axis.scale);
        assertNotNull(axis.nf);
        assertNull(axis.label);
    }
    
    @Test
    public void testConstructor2() {
        String label = "Test Axis";
        PlotAxisScale scale = new LinearAxisScale();
        NumberFormat nf = NumberFormat.getInstance();
        int gridStyle = 2;
        
        PlotAxis axis = new PlotAxis(label, scale, nf, gridStyle) {};
        
        assertNotNull(axis);
        assertEquals(label, axis.label);
        assertEquals(scale, axis.scale);
        assertEquals(nf, axis.nf);
        assertEquals(gridStyle, axis.gridStyle);
    }
    
    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {};
        
        String label = "Test Axis";
        axis.setLabel(label);
        
        assertEquals(label, axis.getLabel());
    }
    
    @Test
    public void testGetLabel() {
        PlotAxis axis = new PlotAxis() {};
        
        assertNull(axis.getLabel());
    }

    @Test
    public void testSetScale() {
        PlotAxis axis = new PlotAxis() {};
        
        PlotAxisScale scale = new LogAxisScale();
        axis.setScale(scale);
        
        assertEquals(scale, axis.getScale());
    }
    
    @Test
    public void testGetScale() {
        PlotAxis axis = new PlotAxis() {};
        
        assertNotNull(axis.getScale());
        assertEquals(LinearAxisScale.class, axis.getScale().getClass());
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis axis = new PlotAxis() {};
        
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        axis.setNumberFormat(nf);
        
        assertEquals(nf, axis.getNumberFormat());
    }
    
    @Test
    public void testGetNumberFormat() {
        PlotAxis axis = new PlotAxis() {};
        
        assertNotNull(axis.getNumberFormat());
        assertEquals(NumberFormat.class, axis.getNumberFormat().getClass());
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis axis = new PlotAxis() {};
        
        int gridStyle = 0;
        axis.setGridStyle(gridStyle);
        
        assertEquals(gridStyle, axis.getGridStyle());
    }
    
    @Test
    public void testGetGridStyle() {
        PlotAxis axis = new PlotAxis() {};
        
        assertEquals(1, axis.getGridStyle());
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        
        boolean flag = true;
        axis.manualLowerBounds(flag);
        
        assertEquals(flag, axis.manualLB);
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        
        double value = 10.5;
        axis.setLowerBounds(value);
        
        assertEquals(value, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testGetLowerBounds() {
        PlotAxis axis = new PlotAxis() {};
        
        assertEquals(Double.MAX_VALUE, axis.getLowerBounds(), 0.001);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis axis = new PlotAxis() {};
        
        double value = 10.5;
        axis.setLowerBounds(value);
        
        assertEquals(String.valueOf(value), axis.lowerBoundsAsString());
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        
        boolean flag = true;
        axis.manualUpperBounds(flag);
        
        assertEquals(flag, axis.manualUB);
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        
        double value = 20.7;
        axis.setUpperBounds(value);
        
        assertEquals(value, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testGetUpperBounds() {
        PlotAxis axis = new PlotAxis() {};
        
        assertEquals(-1.7976931348623157E308, axis.getUpperBounds(), 0.001);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis axis = new PlotAxis() {};
        
        double value = 20.7;
        axis.setUpperBounds(value);
        
        assertEquals(String.valueOf(value), axis.upperBoundsAsString());
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        
        boolean flag = true;
        axis.manualTickStepSize(flag);
        
        assertEquals(flag, axis.manualQuant);
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        
        double value = 1.0;
        axis.setTickStepSize(value);
        
        assertEquals(value, axis.getTickStepSize(), 0.001);
    }
    
    @Test
    public void testGetTickStepSize() {
        PlotAxis axis = new PlotAxis() {};
        
        assertEquals(0, axis.getTickStepSize(), 0.001);
    }
    
    @Test
    public void testSetAxisBounds() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public void setAxisBounds(PlotRunList p0) {
                System.out.println("Set Axis Bounds");
            }
        };
        
        axis.setAxisBounds(null);
        
        assertEquals("Set Axis Bounds\n", outContent.toString());
    }
    
    @Test
    public void testSetSize() {
        PlotAxis axis = new PlotAxis() {
            @Override
            protected void resizeAxis(Rectangle p0) {
                System.out.println("Resize Axis");
            }
        };
        
        axis.setSize(new Rectangle());
        
        assertEquals("Resize Axis\n", outContent.toString());
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis axis = new PlotAxis() {
            @Override
            public int scaleCoord(double x) {
                return (int) x;
            }
        };
        
        double x = 5.2;
        int expected = 5;
        int result = axis.scaleCoord(x);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testClone() {
        PlotAxis axis = new PlotAxis() {};
        
        Object clone = axis.clone();
        
        assertNotNull(clone);
        assertEquals(PlotAxis.class, clone.getClass());
    }
}