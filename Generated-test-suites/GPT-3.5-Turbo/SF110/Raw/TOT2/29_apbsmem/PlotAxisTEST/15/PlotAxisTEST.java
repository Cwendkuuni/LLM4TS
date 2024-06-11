import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PlotAxisTest {

    // Expert 1 test cases
    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {};
        axis.setLabel("Test Label");
        assertEquals("Test Label", axis.getLabel());
    }

    // Expert 2 test cases
    @Test
    public void testSetLabelEdgeCase() {
        PlotAxis axis = new PlotAxis() {};
        axis.setLabel("");
        assertEquals("", axis.getLabel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLabelException() {
        PlotAxis axis = new PlotAxis() {};
        axis.setLabel(null);
    }

}