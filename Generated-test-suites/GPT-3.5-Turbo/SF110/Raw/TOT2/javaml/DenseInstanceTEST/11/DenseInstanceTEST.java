import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;



public class DenseInstanceTest {

    private DenseInstance instance;

    @Test
    public void testDenseInstanceConstructor_EmptyArray() {
        instance = new DenseInstance(new double[]{});
        assertNotNull(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove_NonExistentKey() {
        instance = new DenseInstance(new double[]{});
        instance.remove(5);
    }

    // Add more test cases for other methods as per identified edge cases and exception handling scenarios

}

