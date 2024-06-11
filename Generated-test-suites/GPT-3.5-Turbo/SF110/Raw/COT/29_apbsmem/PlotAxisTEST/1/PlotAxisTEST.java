import org.junit.Test;

public class PlotAxisTest {

    @Test
    public void testSetLabel() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLabel("Test Label");
        assert(plotAxis.getLabel().equals("Test Label"));
    }
    
    @Test
    public void testGetLabel() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLabel("Test Label");
        assert(plotAxis.getLabel().equals("Test Label"));
    }
    
    @Test
    public void testSetScale() {
        PlotAxis plotAxis = new PlotAxis() {};
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assert(plotAxis.getScale().equals(scale));
    }
    
    @Test
    public void testGetScale() {
        PlotAxis plotAxis = new PlotAxis() {};
        PlotAxisScale scale = new LinearAxisScale();
        plotAxis.setScale(scale);
        assert(plotAxis.getScale().equals(scale));
    }
    
    @Test
    public void testSetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assert(plotAxis.getNumberFormat().equals(format));
    }
    
    @Test
    public void testGetNumberFormat() {
        PlotAxis plotAxis = new PlotAxis() {};
        NumberFormat format = NumberFormat.getInstance();
        plotAxis.setNumberFormat(format);
        assert(plotAxis.getNumberFormat().equals(format));
    }
    
    @Test
    public void testSetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setGridStyle(PlotAxis.kTick);
        assert(plotAxis.getGridStyle() == PlotAxis.kTick);
    }
    
    @Test
    public void testGetGridStyle() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setGridStyle(PlotAxis.kTick);
        assert(plotAxis.getGridStyle() == PlotAxis.kTick);
    }
    
    @Test
    public void testManualLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualLowerBounds(true);
        assert(plotAxis.getLowerBounds());
    }
    
    @Test
    public void testSetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLowerBounds(10.0);
        assert(plotAxis.getLowerBounds() == 10.0);
    }
    
    @Test
    public void testGetLowerBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLowerBounds(10.0);
        assert(plotAxis.getLowerBounds() == 10.0);
    }
    
    @Test
    public void testLowerBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setLowerBounds(10.0);
        assert(plotAxis.lowerBoundsAsString().equals("10.0"));
    }
    
    @Test
    public void testManualUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualUpperBounds(true);
        assert(plotAxis.getUpperBounds());
    }
    
    @Test
    public void testSetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setUpperBounds(20.0);
        assert(plotAxis.getUpperBounds() == 20.0);
    }
    
    @Test
    public void testGetUpperBounds() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setUpperBounds(20.0);
        assert(plotAxis.getUpperBounds() == 20.0);
    }
    
    @Test
    public void testUpperBoundsAsString() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setUpperBounds(20.0);
        assert(plotAxis.upperBoundsAsString().equals("20.0"));
    }
    
    @Test
    public void testManualTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.manualTickStepSize(true);
        assert(plotAxis.getTickStepSize());
    }
    
    @Test
    public void testSetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setTickStepSize(5.0);
        assert(plotAxis.getTickStepSize() == 5.0);
    }
    
    @Test
    public void testGetTickStepSize() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setTickStepSize(5.0);
        assert(plotAxis.getTickStepSize() == 5.0);
    }
    
    @Test
    public void testSetAxisBounds() {
        // To be implemented
    }
    
    @Test
    public void testSetSize() {
        // To be implemented
    }
    
    @Test
    public void testDraw() {
        // To be implemented
    }
    
    @Test
    public void testScaleCoord() {
        PlotAxis plotAxis = new PlotAxis() {};
        plotAxis.setScale(new LinearAxisScale());
        int value = plotAxis.scaleCoord(10.0);
        assert(value == plotAxis.xA * plotAxis.scale.func(10.0) + plotAxis.xB);
    }
    
    @Test
    public void testClone() {
        PlotAxis plotAxis = new PlotAxis() {};
        PlotAxis cloneAxis = (PlotAxis) plotAxis.clone();
        assert(plotAxis.equals(cloneAxis));
    }
}