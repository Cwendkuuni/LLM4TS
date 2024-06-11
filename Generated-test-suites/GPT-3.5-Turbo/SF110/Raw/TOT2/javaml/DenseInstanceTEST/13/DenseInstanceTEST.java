import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {

    // Test case for DenseInstance constructor with double array
    @Test
    public void testDenseInstanceDoubleArrayConstructor() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertArrayEquals(att, instance.getAttributes(), 0);
    }

    // Test case for DenseInstance constructor with double array and class value
    @Test
    public void testDenseInstanceDoubleArrayClassValueConstructor() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "class";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertEquals(classValue, instance.getClassValue());
    }

    // Test case for value method
    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(2.0, instance.value(1), 0);
    }

    // Test case for containsKey method
    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    // Additional test cases for other methods can be added here...

}