
import static org.junit.Assert.*;
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testConstructor() {
        try {
            ConnectedComponents cc = new ConnectedComponents(0, 0);
            assertNotNull(cc);
        } catch (InvalidArgumentException e) {
            fail("InvalidArgumentException should not be thrown");
        }
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidValues() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(2, 3);
    }

}
