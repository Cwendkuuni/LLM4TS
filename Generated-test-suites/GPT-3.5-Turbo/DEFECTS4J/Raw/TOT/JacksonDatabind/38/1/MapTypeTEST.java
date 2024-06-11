import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        assertEquals(HashMap.class, mapType.getRawClass());
        assertEquals(0, mapType.getKeyType().getBindings().size());
        assertEquals(HashMap.class, mapType.getSuperClass().getRawClass());
        assertEquals(0, mapType.getSuperInterfaceCount());
        assertEquals(ClassType.construct(String.class), mapType.getKeyType());
        assertEquals(ClassType.construct(Integer.class), mapType.getValueType());
    }

    @Test
    public void testConstructWithNullRawType() {
        MapType mapType = MapType.construct(null, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        assertNull(mapType);
    }

    @Test
    public void testConstructWithNullKeyType() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                null, ClassType.construct(Integer.class));
        assertNull(mapType);
    }

    @Test
    public void testConstructWithNullValueType() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), null);
        assertNull(mapType);
    }

    @Test
    public void testNarrow() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        MapType narrowedMapType = (MapType) mapType.narrow(LinkedHashMap.class);
        assertEquals(LinkedHashMap.class, narrowedMapType.getRawClass());
        assertEquals(mapType.getKeyType(), narrowedMapType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedMapType.getValueType());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        JavaType newContentType = ClassType.construct(Long.class);
        MapType newMapType = (MapType) mapType.withContentType(newContentType);
        assertEquals(newContentType, newMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        JavaType newKeyType = ClassType.construct(Long.class);
        MapType newMapType = (MapType) mapType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapType.getKeyType());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        MapType newMapType = (MapType) mapType.withStaticTyping();
        assertTrue(newMapType.isStatic());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        Object handler = new Object();
        MapType newMapType = (MapType) mapType.withKeyValueHandler(handler);
        assertEquals(handler, newMapType.getKeyType().getValueHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        Object handler = new Object();
        MapType newMapType = (MapType) mapType.withKeyTypeHandler(handler);
        assertEquals(handler, newMapType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        Object handler = new Object();
        MapType newMapType = (MapType) mapType.withContentValueHandler(handler);
        assertEquals(handler, newMapType.getValueType().getValueHandler());
    }

    @Test
    public void testWithValueTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        Object handler = new Object();
        MapType newMapType = (MapType) mapType.withValueTypeHandler(handler);
        assertEquals(handler, newMapType.getValueType().getTypeHandler());
    }

    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        MapType refinedMapType = (MapType) mapType.refine(LinkedHashMap.class,
                TypeBindings.emptyBindings(), ClassType.construct(Object.class), new JavaType[]{});
        assertEquals(LinkedHashMap.class, refinedMapType.getRawClass());
        assertEquals(mapType.getKeyType(), refinedMapType.getKeyType());
        assertEquals(mapType.getValueType(), refinedMapType.getValueType());
    }

    @Test
    public void testToString() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                ClassType.construct(Object.class), new JavaType[]{},
                ClassType.construct(String.class), ClassType.construct(Integer.class));
        assertEquals("[map type; class java.util.HashMap, java.lang.String -> java.lang.Integer]", mapType.toString());
    }
}