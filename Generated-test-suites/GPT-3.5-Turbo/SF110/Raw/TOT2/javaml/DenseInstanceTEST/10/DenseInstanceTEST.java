import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructor1() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertNotNull(instance);
    }
    
    @Test
    public void testConstructor2() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0}, "class");
        assertNotNull(instance);
    }
    
    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(2.0, instance.value(1), 0.001);
    }
    
    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.clear();
        assertEquals(0.0, instance.value(1), 0.001);
    }

    // Add more test cases for other methods as needed

}