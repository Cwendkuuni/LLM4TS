import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;

    @Before
    public void setUp() {
        TShortByteMap innerMap = new TShortByteMap();  // Creating an instance of TShortByteMap for testing
        mapDecorator = new TShortByteMapDecorator(innerMap);
    }

    // Test cases for put() method
    @Test
    public void testPut_WithValidKeyAndValue_ShouldReturnPreviousValue() {
        Short key = 10;
        Byte value = 20;
        Byte previousValue = mapDecorator.put(key, value);
        assertNull(previousValue);
    }

    @Test
    public void testPut_WithNullKey_ShouldReturnPreviousValue() {
        Short key = null;
        Byte value = 20;
        Byte previousValue = mapDecorator.put(key, value);
        assertNull(previousValue);
    }

    @Test
    public void testPut_WithNullValue_ShouldReturnPreviousValue() {
        Short key = 10;
        Byte value = null;
        Byte previousValue = mapDecorator.put(key, value);
        assertNull(previousValue);
    }

    // Test cases for get() method
    @Test
    public void testGet_WithExistingKey_ShouldReturnValue() {
        Short key = 10;
        Byte value = 20;
        mapDecorator.put(key, value);
        Byte retrievedValue = mapDecorator.get(key);
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testGet_WithNullKey_ShouldReturnNoEntryValue() {
        Short key = null;
        Byte value = mapDecorator.get(key);
        assertNull(value);
    }

    // Test cases for clear() method
    @Test
    public void testClear_AfterPuttingEntries_ShouldRemoveAllEntries() {
        mapDecorator.put((short) 10, (byte) 20);
        mapDecorator.put((short) 30, (byte) 40);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    // Test cases for remove() method
    @Test
    public void testRemove_WithExistingKey_ShouldReturnValue() {
        Short key = 10;
        Byte value = 20;
        mapDecorator.put(key, value);
        Byte removedValue = mapDecorator.remove(key);
        assertEquals(value, removedValue);
    }

    @Test
    public void testRemove_WithNonExistingKey_ShouldReturnNull() {
        Short key = 10;
        Byte removedValue = mapDecorator.remove(key);
        assertNull(removedValue);
    }

    // Test cases for entrySet() method
    @Test
    public void testEntrySet_Size_AfterPuttingEntries_ShouldReturnCorrectSize() {
        mapDecorator.put((short) 10, (byte) 20);
        mapDecorator.put((short) 30, (byte) 40);
        int expectedSize = 2;
        assertEquals(expectedSize, mapDecorator.entrySet().size());
    }

    @Test
    public void testEntrySet_Contains_WithExistingEntry_ShouldReturnTrue() {
        Short key = 10;
        Byte value = 20;
        mapDecorator.put(key, value);
        Map.Entry<Short, Byte> entry = new Map.Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return key;
            }

            @Override
            public Byte getValue() {
                return value;
            }

            @Override
            public Byte setValue(Byte value) {
                return null;
            }
        };
        boolean containsEntry = mapDecorator.entrySet().contains(entry);
        assertTrue(containsEntry);
    }

    // Test cases for containsValue() method
    @Test
    public void testContainsValue_WithExistingValue_ShouldReturnTrue() {
        Byte value = 20;
        mapDecorator.put((short) 10, value);
        boolean containsValue = mapDecorator.containsValue(value);
        assertTrue(containsValue);
    }

    @Test
    public void testContainsValue_WithNonExistingValue_ShouldReturnFalse() {
        Byte value = 20;
        boolean containsValue = mapDecorator.containsValue(value);
        assertFalse(containsValue);
    }

    // Test cases for containsKey() method
    @Test
    public void testContainsKey_WithExistingKey_ShouldReturnTrue() {
        Short key = 10;
        mapDecorator.put(key, (byte) 20);
        boolean containsKey = mapDecorator.containsKey(key);
        assertTrue(containsKey);
    }

    @Test
    public void testContainsKey_WithNonExistingKey_ShouldReturnFalse() {
        Short key = 10;
        boolean containsKey = mapDecorator.containsKey(key);
        assertFalse(containsKey);
    }

    // Test cases for size() method
    @Test
    public void testSize_AfterPuttingEntries_ShouldReturnCorrectSize() {
        mapDecorator.put((short) 10, (byte) 20);
        mapDecorator.put((short) 30, (byte) 40);
        int expectedSize = 2;
        assertEquals(expectedSize, mapDecorator.size());
    }

    // Test cases for isEmpty() method
    @Test
    public void testIsEmpty_WithEmptyMap_ShouldReturnTrue() {
        boolean isEmpty = mapDecorator.isEmpty();
        assertTrue(isEmpty);
    }

    @Test
    public void testIsEmpty_WithNonEmptyMap_ShouldReturnFalse() {
        mapDecorator.put((short) 10, (byte) 20);
        boolean isEmpty = mapDecorator.isEmpty();
        assertFalse(isEmpty);
    }

    // Test cases for putAll() method
    @Test
    public void testPutAll_WithValidMap_ShouldAddAllEntries() {
        Map<Short, Byte> sourceMap = new HashMap<>();
        sourceMap.put((short) 10, (byte) 20);
        sourceMap.put((short) 30, (byte) 40);
        mapDecorator.putAll(sourceMap);
        int expectedSize = 2;
        assertEquals(expectedSize, mapDecorator.size());
    }

    // Custom test cases for wrapKey() method
    @Test
    public void testWrapKey_WithPositiveKey_ShouldReturnSameKey() {
        short key = 10;
        Short wrappedKey = mapDecorator.wrapKey(key);
        assertEquals(key, wrappedKey.shortValue());
    }

    @Test
    public void testWrapKey_WithNegativeKey_ShouldReturnSameKey() {
        short key = -10;
        Short wrappedKey = mapDecorator.wrapKey(key);
        assertEquals(key, wrappedKey.shortValue());
    }

    // Custom test cases for unwrapKey() method
    @Test
    public void testUnwrapKey_WithValidKey_ShouldReturnSameKey() {
        short key = 10;
        Short wrappedKey = mapDecorator.wrapKey(key);
        short unwrappedKey = mapDecorator.unwrapKey(wrappedKey);
        assertEquals(key, unwrappedKey);
    }

    // Custom test cases for wrapValue() method
    @Test
    public void testWrapValue_WithPositiveValue_ShouldReturnSameValue() {
        byte value = 20;
        Byte wrappedValue = mapDecorator.wrapValue(value);
        assertEquals(value, wrappedValue.byteValue());
    }

    @Test
    public void testWrapValue_WithNegativeValue_ShouldReturnSameValue() {
        byte value = -20;
        Byte wrappedValue = mapDecorator.wrapValue(value);
        assertEquals(value, wrappedValue.byteValue());
    }

    // Custom test cases for unwrapValue() method
    @Test
    public void testUnwrapValue_WithValidValue_ShouldReturnSameValue() {
        byte value = 20;
        Byte wrappedValue = mapDecorator.wrapValue(value);
        byte unwrappedValue = mapDecorator.unwrapValue(wrappedValue);
        assertEquals(value, unwrappedValue);
    }

    // Custom test cases for readExternal() and writeExternal() methods
    @Test
    public void testReadExternal_WriteExternal() throws Exception {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        mapDecorator.put((short) 10, (byte) 20);
        mapDecorator.put((short) 30, (byte) 40);
        mapDecorator.writeExternal(objectOutputStream);

        byte[] byteArray = byteOutputStream.toByteArray();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        TShortByteMapDecorator readMapDecorator = new TShortByteMapDecorator();
        readMapDecorator.readExternal(objectInputStream);

        assertEquals(mapDecorator.size(), readMapDecorator.size());
        assertEquals(mapDecorator.get((short) 10), readMapDecorator.get((short) 10));
        assertEquals(mapDecorator.get((short) 30), readMapDecorator.get((short) 30));
    }

}