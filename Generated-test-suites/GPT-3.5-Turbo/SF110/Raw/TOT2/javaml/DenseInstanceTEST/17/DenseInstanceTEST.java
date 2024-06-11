import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {
    
    @Test
    public void testConstructor() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertNotNull(instance);
    }
    
    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(2.0, instance.value(1), 0.001);
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertArrayEquals(new double[]{0.0, 0.0, 0.0}, instance.attributes, 0.001);
    }
    
    // Additional test cases for other methods can be added here

    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.removeAttribute(1);
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.001);
    }
    
    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.removeAttributes(Set.of(0, 2));
        assertArrayEquals(new double[]{2.0}, instance.attributes, 0.001);
    }
}