package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() {
        // test valid values for numberNeighbors and backgroundColor
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertEquals(ConnectedComponents.FOUR_CONNECTED, cc.neighbors);
        assertEquals(0, cc.background);
        
        cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
        assertEquals(ConnectedComponents.EIGHT_CONNECTED, cc.neighbors);
        assertEquals(1, cc.background);
        
        // test invalid values for numberNeighbors and backgroundColor
        try {
            cc = new ConnectedComponents(2, 0);
            fail("InvalidArgumentExcpetion should have been thrown");
        } catch (InvalidArgumentException ex) {
            // expected exception
        }
        
        try {
            cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 2);
            fail("InvalidArgumentExcpetion should have been thrown");
        } catch (InvalidArgumentException ex) {
            // expected exception
        }
    }
    
    @Test
    public void testApply() {
        // TODO: Add tests for the apply method
        
    }
}