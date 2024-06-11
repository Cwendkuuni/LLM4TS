
package gnu.trove.decorator;

import org.junit.Test;

import java.util.AbstractMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.0).shortValue());
        assertEquals((short) 3, decorator.get(3.0).shortValue());
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.0).shortValue());
        assertEquals((short) 3, decorator.get(3.0).shortValue());
        assertNull(decorator.get(4.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        decorator.remove(1.0);
        assertNull(decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0).shortValue());
        assertEquals((short) 3, decorator.get(3.0).shortValue());
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        for (Map.Entry<Double, Short> entry : decorator.entrySet()) {
            Double key = entry.getKey();
            Short value = entry.getValue();
            assertTrue(decorator.containsKey(key));
            assertEquals(value, decorator.get(key));
        }
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertTrue(decorator.containsValue((short) 3));
        assertFalse(decorator.containsValue((short) 4));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsKey(3.0));
        assertFalse(decorator.containsKey(4.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertEquals(3, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
        decorator2.putAll(decorator);

        assertEquals(decorator.size(), decorator2.size());
        assertTrue(decorator2.containsKey(1.0));
        assertTrue(decorator2.containsKey(2.0));
        assertEquals(decorator.get(1.0), decorator2.get(1.0));
        assertEquals(decorator.get(2.0), decorator2.get(2.0));
    }

    @Test
    public void testReadExternal() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            decorator.writeExternal(objectOut);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream objectIn = new ObjectInputStream(byteIn);
            TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
            decorator2.readExternal(objectIn);

            assertEquals(decorator.size(), decorator2.size());
            for (Map.Entry<Double, Short> entry : decorator.entrySet()) {
                Double key = entry.getKey();
                Short value = entry.getValue();
                assertTrue(decorator2.containsKey(key));
                assertEquals(value, decorator2.get(key));
            }
        } catch (Exception e) {
            fail("Caught exception: " + e.getMessage());
        }
    }

    @Test
    public void testWriteExternal() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            decorator.writeExternal(objectOut);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream objectIn = new ObjectInputStream(byteIn);
            TDoubleShortMapDecorator decorator2 = new TDoubleShortMapDecorator();
            decorator2.readExternal(objectIn);

            assertEquals(decorator.size(), decorator2.size());
            for (Map.Entry<Double, Short> entry : decorator.entrySet()) {
                Double key = entry.getKey();
                Short value = entry.getValue();
                assertTrue(decorator2.containsKey(key));
                assertEquals(value, decorator2.get(key));
            }
        } catch (Exception e) {
            fail("Caught exception: " + e.getMessage());
        }
    }
}
