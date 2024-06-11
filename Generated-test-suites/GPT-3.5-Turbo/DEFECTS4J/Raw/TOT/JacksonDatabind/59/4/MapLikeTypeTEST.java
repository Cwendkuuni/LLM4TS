import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {
    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        mapLikeType = new MapLikeType(Map.class, new TypeBindings(), null, null, null, null, null, null, false);
    }

    @Test
    public void testConstructor() {
        assertNotNull(mapLikeType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertNull(mapLikeType.getKeyType());
        assertNull(mapLikeType.getContentType());
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testUpgradeFrom() {
        MapLikeType upgradedType = MapLikeType.upgradeFrom(mapLikeType, JavaType.INT_TYPE, JavaType.STRING_TYPE);

        assertEquals(mapLikeType.getRawClass(), upgradedType.getRawClass());
        assertEquals(JavaType.INT_TYPE, upgradedType.getKeyType());
        assertEquals(JavaType.STRING_TYPE, upgradedType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromInvalidBaseType() {
        MapLikeType.upgradeFrom(JavaType.INT_TYPE, JavaType.INT_TYPE, JavaType.STRING_TYPE);
    }

    @Test
    public void testConstruct() {
        MapLikeType constructedType = MapLikeType.construct(Map.class, JavaType.INT_TYPE, JavaType.STRING_TYPE);

        assertEquals(Map.class, constructedType.getRawClass());
        assertEquals(JavaType.INT_TYPE, constructedType.getKeyType());
        assertEquals(JavaType.STRING_TYPE, constructedType.getContentType());
    }

    @Test
    public void testNarrow() {
        MapLikeType narrowedType = (MapLikeType) mapLikeType._narrow(HashMap.class);

        assertEquals(HashMap.class, narrowedType.getRawClass());
        assertNull(narrowedType.getBindings());
        assertNull(narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertNull(narrowedType.getKeyType());
        assertNull(narrowedType.getContentType());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithKeyType() {
        MapLikeType withKeyType = mapLikeType.withKeyType(JavaType.INT_TYPE);

        assertEquals(Map.class, withKeyType.getRawClass());
        assertEquals(JavaType.INT_TYPE, withKeyType.getKeyType());
        assertEquals(null, withKeyType.getContentType());
    }

    @Test
    public void testWithContentType() {
        MapLikeType withContentType = mapLikeType.withContentType(JavaType.STRING_TYPE);

        assertEquals(Map.class, withContentType.getRawClass());
        assertEquals(null, withContentType.getKeyType());
        assertEquals(JavaType.STRING_TYPE, withContentType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        MapLikeType withTypeHandler = mapLikeType.withTypeHandler(typeHandler);

        assertEquals(Map.class, withTypeHandler.getRawClass());
        assertEquals(typeHandler, withTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        MapLikeType withContentTypeHandler = mapLikeType.withContentTypeHandler(typeHandler);

        assertEquals(Map.class, withContentTypeHandler.getRawClass());
        assertEquals(typeHandler, withContentTypeHandler.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        MapLikeType withValueHandler = mapLikeType.withValueHandler(valueHandler);

        assertEquals(Map.class, withValueHandler.getRawClass());
        assertEquals(valueHandler, withValueHandler.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        MapLikeType withContentValueHandler = mapLikeType.withContentValueHandler(valueHandler);

        assertEquals(Map.class, withContentValueHandler.getRawClass());
        assertEquals(valueHandler, withContentValueHandler.getContentValueHandler());
    }

    @Test
    public void testParentMethods() {
        assertFalse(mapLikeType.isContainerType());
        assertTrue(mapLikeType.isMapLikeType());
        assertNull(mapLikeType.getKeyType());
        assertNull(mapLikeType.getContentType());
        assertNull(mapLikeType.getContentValueHandler());
        assertNull(mapLikeType.getContentTypeHandler());
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testErasedSignature() {
        StringBuilder sb = new StringBuilder();
        mapLikeType.getErasedSignature(sb);

        assertEquals("java.util.Map", sb.toString());
    }

    @Test
    public void testGenericSignature() {
        StringBuilder sb = new StringBuilder();
        mapLikeType.getGenericSignature(sb);

        assertEquals("java.util.Map<null,null>;", sb.toString());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object typeHandler = new Object();
        MapLikeType withKeyTypeHandler = mapLikeType.withKeyTypeHandler(typeHandler);

        assertEquals(Map.class, withKeyTypeHandler.getRawClass());
        assertEquals(typeHandler, withKeyTypeHandler.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object valueHandler = new Object();
        MapLikeType withKeyValueHandler = mapLikeType.withKeyValueHandler(valueHandler);

        assertEquals(Map.class, withKeyValueHandler.getRawClass());
        assertEquals(valueHandler, withKeyValueHandler.getKeyType().getValueHandler());
    }

    @Test
    public void testIsTrueMapType() {
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        String expectedToString = "[map-like type; class java.util.Map, null -> null]";
        assertEquals(expectedToString, mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        MapLikeType equalType = new MapLikeType(Map.class, new TypeBindings(), null, null, null, null, null, null, false);
        MapLikeType differentKeyType = new MapLikeType(Map.class, new TypeBindings(), null, null, JavaType.INT_TYPE, null, null, null, false);
        MapLikeType differentContentType = new MapLikeType(Map.class, new TypeBindings(), null, null, null, JavaType.STRING_TYPE, null, null, false);

        assertTrue(mapLikeType.equals(mapLikeType));
        assertTrue(mapLikeType.equals(equalType));
        assertFalse(mapLikeType.equals(differentKeyType));
        assertFalse(mapLikeType.equals(differentContentType));
        assertFalse(mapLikeType.equals(null));
        assertFalse(mapLikeType.equals(new Object()));
    }
}