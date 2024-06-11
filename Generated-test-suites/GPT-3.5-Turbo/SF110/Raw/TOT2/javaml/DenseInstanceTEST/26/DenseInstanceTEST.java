import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(2.0, instance.value(1), 0.0);
    }

    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertEquals(0.0, instance.value(1), 0.0);
    }

    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }

    // Add more test methods here based on the public methods in DenseInstance class
    
}