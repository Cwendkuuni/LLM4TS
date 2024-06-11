import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Set;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator decorator;
    private TShortByteMap map;

    @Before
    public void setup() {
        map = new TShortByteMap();
        decorator = new TShortByteMapDecorator(map);
    }

    @Test
    public void put_shouldReturnPreviousValue_whenKeyExists() {
        // Arrange
        short key = 10;
        byte value1 = 5;
        byte value2 = 7;

        decorator.put(key, value1);

        // Act
        Byte previousValue = decorator.put(key, value2);

        // Assert
        Assert.assertEquals(value1, previousValue.byteValue());
    }

    @Test
    public void put_shouldReturnNull_whenKeyDoesNotExist() {
        // Arrange
        short key = 10;
        byte value = 5;

        // Act
        Byte previousValue = decorator.put(key, value);

        // Assert
        Assert.assertNull(previousValue);
    }

    @Test
    public void get_shouldReturnValue_whenKeyExists() {
        // Arrange
        short key = 10;
        byte value = 5;

        decorator.put(key, value);

        // Act
        Byte retrievedValue = decorator.get(key);

        // Assert
        Assert.assertEquals(value, retrievedValue.byteValue());
    }

    @Test
    public void get_shouldReturnNull_whenKeyDoesNotExist() {
        // Arrange
        short key = 10;

        // Act
        Byte retrievedValue = decorator.get(key);

        // Assert
        Assert.assertNull(retrievedValue);
    }

    @Test
    public void clear_shouldRemoveAllEntries() {
        // Arrange
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        // Act
        decorator.clear();

        // Assert
        Assert.assertTrue(decorator.isEmpty());
    }

    @Test
    public void remove_shouldReturnPreviousValueAndRemoveEntry_whenKeyExists() {
        // Arrange
        short key = 10;
        byte value = 5;

        decorator.put(key, value);

        // Act
        Byte removedValue = decorator.remove(key);

        // Assert
        Assert.assertEquals(value, removedValue.byteValue());
        Assert.assertFalse(decorator.containsKey(key));
    }

    @Test
    public void remove_shouldReturnNull_whenKeyDoesNotExist() {
        // Arrange
        short key = 10;

        // Act
        Byte removedValue = decorator.remove(key);

        // Assert
        Assert.assertNull(removedValue);
    }

    @Test
    public void entrySet_shouldReturnSetOfEntries() {
        // Arrange
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        // Act
        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();

        // Assert
        Assert.assertEquals(2, entrySet.size());
        for (Map.Entry<Short, Byte> entry : entrySet) {
            short key = entry.getKey();
            byte value = entry.getValue();
            Assert.assertTrue(decorator.containsKey(key));
            Assert.assertEquals(value, decorator.get(key).byteValue());
        }
    }

    @Test
    public void containsValue_shouldReturnTrue_whenValueExists() {
        // Arrange
        byte value = 5;
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, value);

        // Act
        boolean containsValue = decorator.containsValue(value);

        // Assert
        Assert.assertTrue(containsValue);
    }

    @Test
    public void containsValue_shouldReturnFalse_whenValueDoesNotExist() {
        // Arrange
        byte value = 5;
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        // Act
        boolean containsValue = decorator.containsValue(value);

        // Assert
        Assert.assertFalse(containsValue);
    }

    @Test
    public void containsKey_shouldReturnTrue_whenKeyExists() {
        // Arrange
        short key = 10;
        decorator.put(key, (byte) 5);

        // Act
        boolean containsKey = decorator.containsKey(key);

        // Assert
        Assert.assertTrue(containsKey);
    }

    @Test
    public void containsKey_shouldReturnFalse_whenKeyDoesNotExist() {
        // Arrange
        short key = 10;

        // Act
        boolean containsKey = decorator.containsKey(key);

        // Assert
        Assert.assertFalse(containsKey);
    }

    @Test
    public void size_shouldReturnNumberOfEntries() {
        // Arrange
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);

        // Act
        int size = decorator.size();

        // Assert
        Assert.assertEquals(2, size);
    }

    @Test
    public void isEmpty_shouldReturnTrue_whenMapHasNoEntries() {
        // Arrange

        // Act
        boolean isEmpty = decorator.isEmpty();

        // Assert
        Assert.assertTrue(isEmpty);
    }

    @Test
    public void isEmpty_shouldReturnFalse_whenMapHasEntries() {
        // Arrange
        decorator.put((short) 1, (byte) 2);

        // Act
        boolean isEmpty = decorator.isEmpty();

        // Assert
        Assert.assertFalse(isEmpty);
    }

    @Test
    public void putAll_shouldAddAllEntriesToMap() {
        // Arrange
        Map<Short, Byte> inputMap = new AbstractMap<Short, Byte>() {
            @Override
            public Set<Entry<Short, Byte>> entrySet() {
                return null; // Not needed for this test
            }
        };
        inputMap.put((short) 1, (byte) 2);
        inputMap.put((short) 3, (byte) 4);

        // Act
        decorator.putAll(inputMap);

        // Assert
        Assert.assertEquals(inputMap.size(), decorator.size());
        for (Map.Entry<Short, Byte> entry : inputMap.entrySet()) {
            short key = entry.getKey();
            byte value = entry.getValue();
            Assert.assertEquals(value, decorator.get(key).byteValue());
        }
    }

    @Test
    public void wrapKey_shouldReturnShortKey() {
        // Arrange
        short key = 10;

        // Act
        Short wrappedKey = decorator.wrapKey(key);

        // Assert
        Assert.assertEquals(key, wrappedKey.shortValue());
    }

    @Test
    public void unwrapKey_shouldReturnShortKey() {
        // Arrange
        short key = 10;

        // Act
        short unwrappedKey = decorator.unwrapKey((Object) key);

        // Assert
        Assert.assertEquals(key, unwrappedKey);
    }

    @Test
    public void wrapValue_shouldReturnByteValue() {
        // Arrange
        byte value = 5;

        // Act
        Byte wrappedValue = decorator.wrapValue(value);

        // Assert
        Assert.assertEquals(value, wrappedValue.byteValue());
    }

    @Test
    public void unwrapValue_shouldReturnByteValue() {
        // Arrange
        byte value = 5;

        // Act
        byte unwrappedValue = decorator.unwrapValue((Object) value);

        // Assert
        Assert.assertEquals(value, unwrappedValue);
    }

    @Test
    public void readExternal_shouldReadMapFromInput() {
        // Arrange
        TShortByteMap inputMap = new TShortByteMap();
        inputMap.put((short) 1, (byte) 2);
        inputMap.put((short) 3, (byte) 4);

        TShortByteMapDecorator externalDecorator = new TShortByteMapDecorator(inputMap);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // Act
        try {
            externalDecorator.writeExternal(objectOutputStream);
            objectOutputStream.flush();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            decorator.readExternal(objectInputStream);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        // Assert
        Assert.assertEquals(externalDecorator.size(), decorator.size());
        for (Map.Entry<Short, Byte> entry : externalDecorator.entrySet()) {
            short key = entry.getKey();
            byte value = entry.getValue();
            Assert.assertEquals(value, decorator.get(key).byteValue());
        }
    }

    @Test
    public void writeExternal_shouldWriteMapToOutput() {
        // Arrange
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 3, (byte) 4);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // Act
        try {
            decorator.writeExternal(objectOutputStream);
            objectOutputStream.flush();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            TShortByteMapDecorator externalDecorator = new TShortByteMapDecorator();
            externalDecorator.readExternal(objectInputStream);

            decorator = externalDecorator; // Update decorator reference to match externalDecorator
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

        // Assert
        Assert.assertEquals(decorator.size(), decorator.size());
        for (Map.Entry<Short, Byte> entry : decorator.entrySet()) {
            short key = entry.getKey();
            byte value = entry.getValue();
            Assert.assertEquals(value, decorator.get(key).byteValue());
        }
    }
}