import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PlotAxisTest {
    
    @Test
    public void testSetLabel() {
        PlotAxis axis = new PlotAxis() {};
        String expectedLabel = "Test Label";
        axis.setLabel(expectedLabel);
        assertEquals(expectedLabel, axis.getLabel());
    }
}