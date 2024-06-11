import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapLikeType result = MapLikeType.upgradeFrom(baseType, keyT, valueT);

        assertNotNull(result);
        assertEquals(MapLikeType.class, result.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFrom_WithInvalidBaseType() {
        JavaType baseType = new JavaType();
        JavaType keyT = null;
        JavaType valueT = null;

        MapLikeType.upgradeFrom(baseType, keyT, valueT);
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;

        MapLikeType result = mapLikeType.withTypeHandler(h);

        assertNotNull(result);
        assertEquals(MapLikeType.class, result.getClass());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;

        MapLikeType result = mapLikeType.withContentTypeHandler(h);

        assertNotNull(result);
        assertEquals(MapLikeType.class, result.getClass());
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;

        MapLikeType result = mapLikeType.withValueHandler(h);

        assertNotNull(result);
        assertEquals(MapLikeType.class, result.getClass());
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;

        MapLikeType result = mapLikeType.withContentValueHandler(h);

        assertNotNull(result);
        assertEquals(MapLikeType.class, result.getClass());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType result = mapLikeType.withStaticTyping();

        assertNotNull(result);
        assertEquals(MapLikeType.class, result.getClass());
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        JavaType result = mapLikeType.refine(rawType, bindings, superClass, superInterfaces);

        assertNotNull(result);
        assertEquals(JavaType.class, result.getClass());
    }

    @Test
    public void testIsContainerType() {
        boolean result = mapLikeType.isContainerType();

        assertTrue(result);
    }

    @Test
    public void testIsMapLikeType() {
        boolean result = mapLikeType.isMapLikeType();

        assertTrue(result);
    }

    @Test
    public void testGetKeyType() {
        JavaType result = mapLikeType.getKeyType();

        assertNull(result);
    }

    @Test
    public void testGetContentType() {
        JavaType result = mapLikeType.getContentType();

        assertNull(result);
    }

    @Test
    public void testGetContentValueHandler() {
        Object result = mapLikeType.getContentValueHandler();

        assertNull(result);
    }

    @Test
    public void testGetContentTypeHandler() {
        Object result = mapLikeType.getContentTypeHandler();

        assertNull(result);
    }

    @Test
    public void testHasHandlers() {
        boolean result = mapLikeType.hasHandlers();

        assertFalse(result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();

        StringBuilder result = mapLikeType.getErasedSignature(sb);

        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();

        StringBuilder result = mapLikeType.getGenericSignature(sb);

        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object h = null;

        MapLikeType result = mapLikeType.withKeyTypeHandler(h);

        assertNotNull(result);
        assertEquals(MapLikeType.class, result.getClass());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object h = null;

        MapLikeType result = mapLikeType.withKeyValueHandler(h);

        assertNotNull(result);
        assertEquals(MapLikeType.class, result.getClass());
    }

    @Test
    public void testIsTrueMapType() {
        boolean result = mapLikeType.isTrueMapType();

        assertFalse(result);
    }

    @Test
    public void testToString() {
        String result = mapLikeType.toString();

        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testEqualsWithEqualObjects() {
        Object o = new MapLikeType(null, null, null, null, null, null, null, null, false);

        boolean result = mapLikeType.equals(o);

        assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        boolean result = mapLikeType.equals(null);

        assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Object o = new Object();

        boolean result = mapLikeType.equals(o);

        assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentClassObject() {
        Object o = new MapLikeTypeTest();

        boolean result = mapLikeType.equals(o);

        assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentKeyType() {
        Object o = new MapLikeType(null, null, null, null, new JavaType(), null, null, null, false);

        boolean result = mapLikeType.equals(o);

        assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentValueType() {
        Object o = new MapLikeType(null, null, null, null, null, new JavaType(), null, null, false);

        boolean result = mapLikeType.equals(o);

        assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentClassAndDifferentKeyTypeAndDifferentValueType() {
        Object o = new MapLikeType(null, null, null, null, new JavaType(), new JavaType(), null, null, false);

        boolean result = mapLikeType.equals(o);

        assertFalse(result);
    }
}