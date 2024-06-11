import static org.junit.Assert.*;
import org.junit.*;

public class ConnectedComponentsTest {

    // Test constructor
    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
            assertNotNull(cc);
            
            cc = new ConnectedComponents(ConnectedComponents.EIGHT_CONNECTED, 1);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("Exception thrown");
        }
    }
    
    // Test constructor with invalid arguments
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArguments() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 0);
    }
    
    // Test apply method
    @Test
    public void testApply() {
        // IMPLEMENT TEST CASE HERE
    }

}