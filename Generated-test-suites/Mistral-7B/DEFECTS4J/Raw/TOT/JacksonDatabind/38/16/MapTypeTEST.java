import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapTypeTest {

    @Test
    public void testConstructWithSimpleTypes() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testConstructWithCustomClasses() {
        JavaType keyType = JavaType.constructType(CustomKey.class, CustomKey.class);
        JavaType valueType = JavaType.constructType(CustomValue.class, CustomValue.class);
        MapType mapType = MapType.construct(CustomMap.class, null, null, null, keyType, valueType);
        assertNotNull(mapType);
        assertEquals(CustomMap.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithNullTypes() {
        MapType mapType = MapType.construct(Map.class, null, null, null, null, null);
    }

    @Test
    public void testRefineWithNewTypeBindings() {
        JavaType keyType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(Integer.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType refinedMapType = mapType.refine(HashMap.class, new SimpleTypeBindings(), null, null);
        assertNotNull(refinedMapType);
        assertEquals(HashMap.class, refinedMapType.getRawClass());
        assertEquals(keyType, refinedMapType.getKeyType());
        assertEquals(valueType, refinedMapType.getValueType());
    }

    @Test
    public void testRefineWithSameTypeBindings() {
        JavaType keyType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(Integer.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType refinedMapType = mapType.refine(Map.class, null, null, null);
        assertNotNull(refinedMapType);
        assertEquals(Map.class, refinedMapType.getRawClass());
        assertEquals(keyType, refinedMapType.getKeyType());
        assertEquals(valueType, refinedMapType.getValueType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRefineWithNullType() {
        JavaType keyType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(Integer.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        mapType.refine(null, null, null, null);
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(Integer.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new MapValueHandler();
        MapType withHandler = mapType.withValueHandler(handler);
        assertNotNull(withHandler);
        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(mapType.getKeyType(), withHandler.getKeyType());
        assertEquals(valueType.withValueHandler(handler), withHandler.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(Integer.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new MapKeyHandler();
        MapType withHandler = mapType.withKeyTypeHandler(handler);
        assertNotNull(withHandler);
        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(keyType.withTypeHandler(handler), withHandler.getKeyType());
        assertEquals(valueType, withHandler.getValueType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(Integer.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new MapContentHandler();
        MapType withHandler = mapType.withContentTypeHandler(handler);
        assertNotNull(withHandler);
        assertEquals(mapType.getRawClass(), withHandler.getRawClass());
        assertEquals(keyType, withHandler.getKeyType());
        assertEquals(valueType.withTypeHandler(handler), withHandler.getValueType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = JavaType.constructType(String.class, String.class);
        JavaType valueType = JavaType.constructType(Integer.class, Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        MapType staticMapType = mapType.withStaticTyping();
        assertNotNull(staticMapType);
        assertEquals(mapType.getRawClass(), staticMapType.getRawClass());
        assertEquals(keyType.withStaticTyping(), staticMapType.getKeyType());
        assertEquals(valueType.withStaticTyping(), staticMapType.getValueType());
        assertTrue(staticMapType.isStatic());
    }
}