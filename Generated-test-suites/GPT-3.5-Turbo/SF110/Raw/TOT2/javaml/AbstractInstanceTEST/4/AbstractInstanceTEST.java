import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new ConcreteInstance(1); // Assuming ConcreteInstance is a subclass of AbstractInstance
        assertEquals(1, instance.getID());
    }
}