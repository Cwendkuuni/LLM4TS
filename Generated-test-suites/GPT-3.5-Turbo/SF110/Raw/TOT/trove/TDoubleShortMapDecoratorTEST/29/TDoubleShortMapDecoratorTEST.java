import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }

    @After
    public void tearDown() {
        mapDecorator.clear();
        mapDecorator = null;
    }

    @Test
    public void testPutAndGet_ValidKeyAndValue_ReturnsCorrectValue() {
        // Arrange
        Double key = 1.0;
        Short value = 2;

        // Act
        mapDecorator.put(key, value);
        Short retrievedValue = mapDecorator.get(key);

        // Assert
        assertEquals(value, retrievedValue);
    }

    @Test
    public void testPutAndGet_NullKeyAndValue_ReturnsNull() {
        // Arrange
        Double key = null;
        Short value = null;

        // Act
        mapDecorator.put(key, value);
        Short retrievedValue = mapDecorator.get(key);

        // Assert
        assertNull(retrievedValue);
    }

    @Test
    public void testPutAndGet_ExistingKey_ReplacesOldValue() {
        // Arrange
        Double key = 1.0;
        Short value1 = 2;
        Short value2 = 3;

        // Act
        mapDecorator.put(key, value1);
        mapDecorator.put(key, value2);
        Short retrievedValue = mapDecorator.get(key);

        // Assert
        assertEquals(value2, retrievedValue);
    }

    @Test
    public void testRemove_ValidKey_ReturnsCorrectValue() {
        // Arrange
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);

        // Act
        Short removedValue = mapDecorator.remove(key);

        // Assert
        assertEquals(value, removedValue);
        assertFalse(mapDecorator.containsKey(key));
    }

    @Test
    public void testRemove_NullKey_ReturnsNull() {
        // Arrange
        Double key = null;

        // Act
        Short removedValue = mapDecorator.remove(key);

        // Assert
        assertNull(removedValue);
    }

    @Test
    public void testRemove_NonExistingKey_ReturnsNull() {
        // Arrange
        Double key = 1.0;

        // Act
        Short removedValue = mapDecorator.remove(key);

        // Assert
        assertNull(removedValue);
    }

    @Test
    public void testClear_EmptyMap_MakesMapEmpty() {
        // Arrange

        // Act
        mapDecorator.clear();

        // Assert
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testClear_NonEmptyMap_MakesMapEmpty() {
        // Arrange
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);

        // Act
        mapDecorator.clear();

        // Assert
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testContainsKey_ValidKey_ReturnsTrue() {
        // Arrange
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);

        // Act
        boolean containsKey = mapDecorator.containsKey(key);

        // Assert
        assertTrue(containsKey);
    }

    @Test
    public void testContainsKey_NullKey_ReturnsFalse() {
        // Arrange
        Double key = null;

        // Act
        boolean containsKey = mapDecorator.containsKey(key);

        // Assert
        assertFalse(containsKey);
    }

    @Test
    public void testContainsKey_NonExistingKey_ReturnsFalse() {
        // Arrange
        Double key = 1.0;

        // Act
        boolean containsKey = mapDecorator.containsKey(key);

        // Assert
        assertFalse(containsKey);
    }

    @Test
    public void testContainsValue_ValidValue_ReturnsTrue() {
        // Arrange
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);

        // Act
        boolean containsValue = mapDecorator.containsValue(value);

        // Assert
        assertTrue(containsValue);
    }

    @Test
    public void testContainsValue_NullValue_ReturnsFalse() {
        // Arrange
        Short value = null;

        // Act
        boolean containsValue = mapDecorator.containsValue(value);

        // Assert
        assertFalse(containsValue);
    }

    @Test
    public void testContainsValue_NonExistingValue_ReturnsFalse() {
        // Arrange
        Short value = 2;

        // Act
        boolean containsValue = mapDecorator.containsValue(value);

        // Assert
        assertFalse(containsValue);
    }

    @Test
    public void testSize_EmptyMap_ReturnsZero() {
        // Arrange

        // Act
        int size = mapDecorator.size();

        // Assert
        assertEquals(0, size);
    }

    @Test
    public void testSize_NonEmptyMap_ReturnsCorrectSize() {
        // Arrange
        Double key1 = 1.0;
        Short value1 = 2;
        Double key2 = 3.0;
        Short value2 = 4;
        mapDecorator.put(key1, value1);
        mapDecorator.put(key2, value2);

        // Act
        int size = mapDecorator.size();

        // Assert
        assertEquals(2, size);
    }

    @Test
    public void testIsEmpty_EmptyMap_ReturnsTrue() {
        // Arrange

        // Act
        boolean isEmpty = mapDecorator.isEmpty();

        // Assert
        assertTrue(isEmpty);
    }

    @Test
    public void testIsEmpty_NonEmptyMap_ReturnsFalse() {
        // Arrange
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);

        // Act
        boolean isEmpty = mapDecorator.isEmpty();

        // Assert
        assertFalse(isEmpty);
    }

    @Test
    public void testPutAll_ValidMap_PutsAllEntriesIntoTheMap() {
        // Arrange
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(1.0, 2);
        otherMap.put(3.0, 4);

        // Act
        mapDecorator.putAll(otherMap);

        // Assert
        assertEquals(otherMap.size(), mapDecorator.size());
        assertTrue(mapDecorator.containsKey(1.0));
        assertTrue(mapDecorator.containsKey(3.0));
        assertTrue(mapDecorator.containsValue((short)2));
        assertTrue(mapDecorator.containsValue((short)4));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_AddEntry_ThrowsUnsupportedOperationException() {
        // Arrange
        Double key = 1.0;
        Short value = 2;
        Map.Entry<Double, Short> entry = new Map.Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return key;
            }

            @Override
            public Short getValue() {
                return value;
            }

            @Override
            public Short setValue(Short value) {
                return null;
            }
        };

        // Act
        mapDecorator.entrySet().add(entry);

        // Assert
        // Expects UnsupportedOperationException to be thrown
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_RemoveEntry_ThrowsUnsupportedOperationException() {
        // Arrange
        Double key = 1.0;
        Short value = 2;
        mapDecorator.put(key, value);
        Map.Entry<Double, Short> entry = new Map.Entry<Double, Short>() {
            @Override
            public Double getKey() {
                return key;
            }

            @Override
            public Short getValue() {
                return value;
            }

            @Override
            public Short setValue(Short value) {
                return null;
            }
        };

        // Act
        mapDecorator.entrySet().remove(entry);

        // Assert
        // Expects UnsupportedOperationException to be thrown
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_AddAllEntries_ThrowsUnsupportedOperationException() {
        // Arrange
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(1.0, 2);
        otherMap.put(3.0, 4);

        // Act
        mapDecorator.entrySet().addAll(otherMap.entrySet());

        // Assert
        // Expects UnsupportedOperationException to be thrown
    }

    @Test
    public void testSerialization() {
        // Arrange
        try {
            Double key = 1.0;
            Short value = 2;
            mapDecorator.put(key, value);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            // Act
            oos.writeObject(mapDecorator);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            TDoubleShortMapDecorator deserializedMapDecorator = (TDoubleShortMapDecorator) ois.readObject();

            // Assert
            assertEquals(mapDecorator.size(), deserializedMapDecorator.size());
            assertEquals(mapDecorator.get(key), deserializedMapDecorator.get(key));
        } catch (Exception ex) {
            // Handle exception
        }
    }
}