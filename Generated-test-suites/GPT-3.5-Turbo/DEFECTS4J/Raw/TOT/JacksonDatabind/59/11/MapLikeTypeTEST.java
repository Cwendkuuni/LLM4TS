import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testGetKeyType() {
        JavaType keyType = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).getKeyType();
        assertEquals(IntegerType.instance, keyType);
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).getContentType();
        assertEquals(StringType.instance, contentType);
    }

    @Test
    public void testIsTrueMapType() {
        boolean isTrueMap = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).isTrueMapType();
        assertTrue(isTrueMap);

        boolean isNotTrueMap = new MapLikeType(String.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).isTrueMapType();
        assertFalse(isNotTrueMap);
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class);
        JavaType keyType = IntegerType.instance;
        JavaType valueType = StringType.instance;

        MapLikeType upgradedType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(BaseType, upgradedType.getBaseType());
        assertEquals(IntegerType.instance, upgradedType.getKeyType());
        assertEquals(StringType.instance, upgradedType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new SimpleType(Integer.class);
        JavaType keyType = IntegerType.instance;
        JavaType valueType = StringType.instance;

        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testConstruct() {
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, IntegerType.instance, StringType.instance);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(IntegerType.instance, mapLikeType.getKeyType());
        assertEquals(StringType.instance, mapLikeType.getContentType());
    }

    @Test
    public void testConstructWithInvalidRawType() {
        MapLikeType mapLikeType = MapLikeType.construct(String.class, IntegerType.instance, StringType.instance);
        assertEquals(String.class, mapLikeType.getRawType());
        assertEquals(IntegerType.instance, mapLikeType.getKeyType());
        assertEquals(StringType.instance, mapLikeType.getContentType());
    }

    @Test
    public void testWithKeyType() {
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, IntegerType.instance, StringType.instance)
                .withKeyType(LongType.instance);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(LongType.instance, mapLikeType.getKeyType());
        assertEquals(StringType.instance, mapLikeType.getContentType());
    }

    @Test
    public void testWithContentType() {
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, IntegerType.instance, StringType.instance)
                .withContentType(BooleanType.instance);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(IntegerType.instance, mapLikeType.getKeyType());
        assertEquals(BooleanType.instance, mapLikeType.getContentType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new ObjectTypeHandler();
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, IntegerType.instance, StringType.instance)
                .withKeyTypeHandler(keyTypeHandler);
        assertTrue(mapLikeType.getKeyType().hasTypeHandler());
        assertEquals(keyTypeHandler, mapLikeType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object keyValueHandler = new ObjectTypeHandler();
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, IntegerType.instance, StringType.instance)
                .withKeyTypeHandler(keyValueHandler);
        assertTrue(mapLikeType.getKeyType().hasTypeHandler());
        assertEquals(keyValueHandler, mapLikeType.getKeyType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, IntegerType.instance, StringType.instance)
                .withStaticTyping();
        assertTrue(mapLikeType.getContentType().isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[] { new SimpleType(Cloneable.class), new SimpleType(Serializable.class) };

        MapLikeType mapLikeType = MapLikeType.construct(Map.class, IntegerType.instance, StringType.instance)
                .refine(HashMap.class, null, superClass, superInterfaces);

        assertEquals(HashMap.class, mapLikeType.getRawType());
        assertEquals(IntegerType.instance, mapLikeType.getKeyType());
        assertEquals(StringType.instance, mapLikeType.getContentType());
        assertEquals(superClass, mapLikeType.getSuperClass());
        assertEquals(new SimpleType(Cloneable.class), mapLikeType.getInterfaces().get(0));
        assertEquals(new SimpleType(Serializable.class), mapLikeType.getInterfaces().get(1));
    }

    @Test
    public void testBuildCanonicalName() {
        String canonicalName = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).buildCanonicalName();
        assertEquals("java.util.Map<java.lang.Integer,java.lang.String>", canonicalName);
    }

    @Test
    public void testIsContainerType() {
        boolean isContainerType = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).isContainerType();
        assertTrue(isContainerType);
    }

    @Test
    public void testIsMapLikeType() {
        boolean isMapLikeType = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).isMapLikeType();
        assertTrue(isMapLikeType);
    }

    @Test
    public void testHasHandlers() {
        boolean hasHandlers = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, new Object(), new Object(), false).hasHandlers();
        assertTrue(hasHandlers);
    }

    @Test
    public void testGetErasedSignature() {
        String erasedSignature = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).getErasedSignature(new StringBuilder()).toString();
        assertEquals("java.util.Map;", erasedSignature);
    }

    @Test
    public void testGetGenericSignature() {
        String genericSignature = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).getGenericSignature(new StringBuilder()).toString();
        assertEquals("java.util.Map<Ljava/lang/Integer;Ljava/lang/String;>;", genericSignature);
    }

    @Test
    public void testEquals() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false);
        MapLikeType sameType = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false);
        MapLikeType differentType = new MapLikeType(Map.class, null, null, null, LongType.instance, StringType.instance, null, null, false);
        MapLikeType inheritedType = new MapLikeType(HashMap.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false);

        assertTrue(mapLikeType.equals(mapLikeType));
        assertTrue(mapLikeType.equals(sameType));
        assertFalse(mapLikeType.equals(differentType));
        assertFalse(mapLikeType.equals(inheritedType));
    }

    @Test
    public void testToString() {
        String toString = new MapLikeType(Map.class, null, null, null, IntegerType.instance, StringType.instance, null, null, false).toString();
        assertEquals("[map-like type; class java.util.Map, java.lang.Integer -> java.lang.String]", toString);
    }

}