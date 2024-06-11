import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {
    
    private MultiKey<Integer> multiKey1;
    private MultiKey<Integer> multiKey2;
    private MultiKey<Integer> multiKey3;
    
    @Before
    public void setup() {
        multiKey1 = new MultiKey<>(1, 2, 3);
        multiKey2 = new MultiKey<>(1, 2, 3);
        multiKey3 = new MultiKey<>(4, 5, 6);
    }
    
    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        assertTrue(multiKey1.equals(multiKey1));
    }
    
    @Test
    public void testEquals_SameKeys_ReturnsTrue() {
        assertTrue(multiKey1.equals(multiKey2));
    }
    
    @Test
    public void testEquals_DifferentKeys_ReturnsFalse() {
        assertFalse(multiKey1.equals(multiKey3));
    }
    
    @Test
    public void testEquals_Null_ReturnsFalse() {
        assertFalse(multiKey1.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        assertFalse(multiKey1.equals("Not a MultiKey object"));
    }
    
    @Test
    public void testHashCode_SameKeys_ReturnsTrue() {
        assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
    }
    
    @Test
    public void testHashCode_DifferentKeys_ReturnsFalse() {
        assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
    }
    
    @Test
    public void testGetKeys_ReturnsCorrectArray() {
        Integer[] expectedKeys = {1, 2, 3};
        assertArrayEquals(expectedKeys, multiKey1.getKeys());
    }
    
    @Test
    public void testGetKey_ValidIndex_ReturnsCorrectKey() {
        assertEquals((Integer) 1, multiKey1.getKey(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKey_InvalidIndex_ThrowsIndexOutOfBoundsException() {
        multiKey1.getKey(3);
    }
    
    @Test
    public void testSize_ReturnsCorrectSize() {
        assertEquals(3, multiKey1.size());
    }
    
    @Test
    public void testToString_ReturnsFormattedString() {
        String expectedString = "MultiKey[1,2,3]";
        assertEquals(expectedString, multiKey1.toString());
    }
    
    // Additional test cases can be added to cover edge cases and error scenarios
    
}