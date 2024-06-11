import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance testInstance;

    @Before
    public void setUp() {
        double[] mockAttributes = {1.0, 2.0, 3.0};
        testInstance = new DenseInstance(mockAttributes);
    }

    @Test
    public void testValue() {
        assertEquals(2.0, testInstance.value(1), 0.001);
    }

    @Test
    public void testClear() {
        testInstance.clear();
        double[] expectedAttributes = new double[3];
        assertArrayEquals(expectedAttributes, testInstance.attributes, 0.001);
    }

    @Test
    public void testContainsKey() {
        assertTrue(testInstance.containsKey(1));
        assertFalse(testInstance.containsKey(5));
    }

    @Test
    public void testContainsValue() {
        assertTrue(testInstance.containsValue(2.0));
        assertFalse(testInstance.containsValue(5.0));
    }

    // Add more test cases for other methods as needed

}