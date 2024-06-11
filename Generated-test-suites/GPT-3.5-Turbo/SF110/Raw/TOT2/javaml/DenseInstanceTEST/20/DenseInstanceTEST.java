import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructor() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.size());
        assertEquals(1.0, instance.value(0), 0.001);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        assertEquals(0.0, instance.value(1), 0.001);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(4.0));
    }

    // More test cases for other methods...

}