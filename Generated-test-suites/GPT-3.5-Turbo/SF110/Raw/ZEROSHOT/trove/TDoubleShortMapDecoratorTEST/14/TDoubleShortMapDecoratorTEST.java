import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import java.util.Map;
import java.util.HashMap;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator decorator;
    private TDoubleShortMap map;
    
    @Before
    public void setUp() {
        map = new TDoubleShortHashMap();
        decorator = new TDoubleShortMapDecorator(map);
    }

    @After
    public void tearDown() {
        decorator = null;
        map = null;
    }

    @Test
    public void testPutAndGet() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.0).shortValue());
        assertEquals((short) 3, decorator.get(3.0).shortValue());
    }

    @Test
    public void testPutAndGetWithNull() {
        decorator.put(null, (short) 1);
        decorator.put(2.0, null);

        assertNull(decorator.get(null));
        assertNull(decorator.get(2.0));
    }

    @Test
    public void testClear() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        decorator.clear();

        assertEquals(0, decorator.size());
        assertNull(decorator.get(1.0));
        assertNull(decorator.get(2.0));
        assertNull(decorator.get(3.0));
    }

    @Test
    public void testRemove() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        decorator.remove(2.0);

        assertEquals(2, decorator.size());
        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertNull(decorator.get(2.0));
        assertEquals((short) 3, decorator.get(3.0).shortValue());
    }

    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        Map<Double, Short> entrySet = new HashMap<>();
        entrySet.put(1.0, (short) 1);
        entrySet.put(2.0, (short) 2);
        entrySet.put(3.0, (short) 3);

        assertEquals(entrySet.entrySet(), decorator.entrySet());
    }

    @Test
    public void testContainsValue() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertTrue(decorator.containsValue((short) 2));
        assertFalse(decorator.containsValue((short) 4));
    }

    @Test
    public void testContainsKey() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(4.0));
    }

    @Test
    public void testSize() {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        assertEquals(3, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> srcMap = new HashMap<>();
        srcMap.put(1.0, (short) 1);
        srcMap.put(2.0, (short) 2);
        srcMap.put(3.0, (short) 3);

        decorator.putAll(srcMap);

        assertEquals((short) 1, decorator.get(1.0).shortValue());
        assertEquals((short) 2, decorator.get(2.0).shortValue());
        assertEquals((short) 3, decorator.get(3.0).shortValue());
    }

    @Test
    public void testWrapAndUnwrapKey() {
        double key = 2.0;
        assertEquals(key, decorator.unwrapKey(decorator.wrapKey(key)), 0.0);
    }

    @Test
    public void testWrapAndUnwrapValue() {
        short value = 2;
        assertEquals(value, decorator.unwrapValue(decorator.wrapValue(value)));
    }

    @Test
    public void testReadAndWriteExternal() throws IOException, ClassNotFoundException {
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.put(3.0, (short) 3);

        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);

        decorator.writeExternal(objectOut);
        objectOut.flush();

        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        ObjectInputStream objectIn = new ObjectInputStream(byteIn);

        TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
        deserializedDecorator.readExternal(objectIn);

        assertEquals(decorator, deserializedDecorator);
    }
}