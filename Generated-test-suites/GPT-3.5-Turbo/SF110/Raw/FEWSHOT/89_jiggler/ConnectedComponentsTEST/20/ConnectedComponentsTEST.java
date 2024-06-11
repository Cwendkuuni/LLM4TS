package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConnectedComponentsTest {
    @Test
    public void testConstructor() {
        // Test with valid arguments
        ConnectedComponents cc1 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        assertNotNull(cc1);
        
        ConnectedComponents cc2 = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 0);
        assertNotNull(cc2);
        
        // Test with invalid arguments
        try {
            ConnectedComponents cc3 = new ConnectedComponents(2, 0);
        } catch (InvalidArgumentException e) {
            assertNotNull(e);
        }
        
        try {
            ConnectedComponents cc4 = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
        } catch (InvalidArgumentException e) {
            assertNotNull(e);
        }
    }
    
    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        
        // TODO: Write test cases to test the apply method
    }
}