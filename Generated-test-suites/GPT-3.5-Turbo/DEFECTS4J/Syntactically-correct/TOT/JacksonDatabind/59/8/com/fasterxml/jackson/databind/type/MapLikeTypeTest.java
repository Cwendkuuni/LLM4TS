import static org.junit.Assert.*;

import java.lang.reflect.TypeVariable;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTests {

    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        mapLikeType = new MapLikeType(TestClass.class, null, null, null, null, null, null, null, false);
    }

    @Test
    public void testConstructor() {
        assertEquals(TestClass.class, mapLikeType.getRawClass());
        assertNull(mapLikeType.getKeyType());
        assertNull(mapLikeType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(TestClass.class);
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(String.class);

        MapLikeType upgradedType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertNotNull(upgradedType);
        assertEquals(baseType.getRawClass(), upgradedType.getRawClass());
        assertEquals(keyType, upgradedType.getKeyType());
        assertEquals(valueType, upgradedType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithNonTypeBase() {
        JavaType baseType = new SimpleType(String.class);
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(String.class);

        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType(Integer.class);
        JavaType valueType = new SimpleType(String.class);

        MapLikeType constructedType = MapLikeType.construct(TestClass.class, keyType, valueType);

        assertNotNull(constructedType);
        assertEquals(TestClass.class, constructedType.getRawClass());
        assertEquals(keyType, constructedType.getKeyType());
        assertEquals(valueType, constructedType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        MapLikeType type = mapLikeType.withTypeHandler(handler);

        assertNotNull(type);
        assertEquals(mapLikeType.getRawClass(), type.getRawClass());
        assertSame(handler, type.getTypeHandler());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new SimpleType(Integer.class);
        MapLikeType type = mapLikeType.withKeyType(keyType);

        assertNotNull(type);
        assertEquals(mapLikeType.getRawClass(), type.getRawClass());
        assertEquals(keyType, type.getKeyType());
        assertNull(type.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType(String.class);
        MapLikeType type = mapLikeType.withContentType(contentType);

        assertNotNull(type);
        assertEquals(mapLikeType.getRawClass(), type.getRawClass());
        assertNull(type.getKeyType());
        assertEquals(contentType, type.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        MapLikeType type = mapLikeType.withContentValueHandler(handler);

        assertNotNull(type);
        assertEquals(mapLikeType.getRawClass(), type.getRawClass());
        assertSame(handler, type.getContentTypeHandler());
    }

    @Test
    public void testHasKeyValueHandler() {
        Object handler = new Object();
        MapLikeType type = mapLikeType.withKeyValueHandler(handler);

        assertNotNull(type);
        assertEquals(mapLikeType.getRawClass(), type.getRawClass());
        assertSame(handler, type.getKeyType().getValueHandler());
    }

    @Test
    public void testBuildCanonicalName() {
        StringBuilder sb = new StringBuilder();
        String expectedName = "com.fasterxml.jackson.databind.type.MapLikeType<Integer,String>";

        mapLikeType.buildCanonicalName(sb);

        assertEquals(expectedName, sb.toString());
    }

    @Test
    public void testIsContainerType() {
        assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetType() {
        assertEquals(TestClass.class, mapLikeType.getRawClass());
    }

    @Test
    public void testKeyType() {
        assertNull(mapLikeType.getKeyType());
    }

    @Test
    public void testContentType() {
        assertNull(mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        assertNull(mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        assertNull(mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasHandlers() {
        assertFalse(mapLikeType.hasHandlers());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        String expectedSignature = "Lcom.fasterxml.jackson.databind.type.MapLikeType;";

        mapLikeType.getErasedSignature(sb);

        assertEquals(expectedSignature, sb.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        String expectedSignature = "Lcom.fasterxml.jackson.databind.type.MapLikeType<Ljava/lang/Integer;Ljava/lang/String;>;";

        mapLikeType.getGenericSignature(sb);

        assertEquals(expectedSignature, sb.toString());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType type = mapLikeType.withStaticTyping();

        assertNotNull(type);
        assertTrue(type.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{ new SimpleType(Serializable.class) };
        MapLikeType refinedType = mapLikeType.refine(TestSubClass.class, null, superClass, superInterfaces);

        assertNotNull(refinedType);
        assertEquals(TestSubClass.class, refinedType.getRawClass());
        assertNull(refinedType.getKeyType());
        assertNull(refinedType.getContentType());
        assertEquals(superClass, refinedType.getSuperClass());
        assertEquals(superInterfaces.length, refinedType.getInterfaces().size());
        assertEquals(superInterfaces[0], refinedType.getInterfaces().get(0));
    }

    @Test
    public void testToString() {
        String expectedToString = "[map-like type; class com.fasterxml.jackson.databind.type.TestClass, null -> null]";

        assertEquals(expectedToString, mapLikeType.toString());
    }

    @Test
    public void testEqualsWithEqualObjects() {
        MapLikeType equalType = new MapLikeType(TestClass.class, null, null, null, null, null, null, null, false);

        assertEquals(mapLikeType, equalType);
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        MapLikeType differentType = new MapLikeType(TestClass.class, null, null, null, null, null, null, null, true);

        assertNotEquals(mapLikeType, differentType);
    }

    private class TestClass {}

    private class TestSubClass extends TestClass implements Serializable {}
}