
import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

public class DenseInstanceTest {
    
    private DenseInstance denseInstance;
    
    @Before
    public void setUp() {
        denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
    }
    
    @Test
    public void testValue_PositiveCase() {
        assertEquals(1.0, denseInstance.value(0), 0.0001);
        assertEquals(2.0, denseInstance.value(1), 0.0001);
        assertEquals(3.0, denseInstance.value(2), 0.0001);
    }
    
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testValue_IndexOutOfBounds() {
        denseInstance.value(3);
    }
    
    @Test
    public void testClear() {
        denseInstance.clear();
        double[] expectedAttributes = new double[]{0.0, 0.0, 0.0};
        assertArrayEquals(expectedAttributes, denseInstance.getAttributes(), 0.0001);
    }
    
    @Test
    public void testContainsKey_PositiveCase() {
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
    }
    
    @Test
    public void testContainsKey_NegativeCase() {
        assertFalse(denseInstance.containsKey(3));
        assertFalse(denseInstance.containsKey(-1));
    }
    
    @Test
    public void testContainsValue_PositiveCase() {
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
    }
    
    @Test
    public void testContainsValue_NegativeCase() {
        assertFalse(denseInstance.containsValue(0.0));
        assertFalse(denseInstance.containsValue(4.0));
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> expectedEntrySet = new HashSet<>();
        expectedEntrySet.add(new AbstractMap.SimpleEntry<>(0,  1.0));
        expectedEntrySet.add(new AbstractMap.SimpleEntry<>(1,  2.0));
        expectedEntrySet.add(new AbstractMap.SimpleEntry<>(2,  3.0));
        
        Set<Map.Entry<Integer, Double>> actualEntrySet = denseInstance.entrySet();
        
        assertEquals(expectedEntrySet, actualEntrySet);
    }
    
    // Rest of the test methods for the other methods in DenseInstance class
    
}

