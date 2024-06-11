import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    private DenseInstance denseInstance;

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(att);
        assertEquals(2.0, denseInstance.value(1), 0);
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(att);
        denseInstance.clear();
        assertEquals(0.0, denseInstance.value(0), 0);
    }

    @Test
    public void testContainsKey() {
        double[] att = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(att);
        assertTrue(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsKey(4));
    }

    // More test cases for other public methods

    @Test
    public void testRemoveException() {
        double[] att = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(att);
        assertThrows(UnsupportedOperationException.class, () -> denseInstance.remove(1));
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(att);
        denseInstance.removeAttribute(1);
        assertEquals(2.0, denseInstance.value(1), 0);
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(att1);
        DenseInstance denseInstance2 = new DenseInstance(att2);
        assertTrue(denseInstance1.equals(denseInstance2));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(att);
        DenseInstance copyInstance = (DenseInstance) denseInstance.copy();
        assertArrayEquals(att, copyInstance.getAttributes(), 0);
    }

    // Add more test cases for other methods
    
}