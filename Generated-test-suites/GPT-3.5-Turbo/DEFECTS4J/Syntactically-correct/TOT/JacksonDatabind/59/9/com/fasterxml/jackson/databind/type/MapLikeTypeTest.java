import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        JavaType keyType = new MapLikeType(Key.class);
        JavaType valueType = new MapLikeType(Value.class);
        mapLikeType = new MapLikeType(Map.class, keyType, valueType);
    }

    @Test
    public void testConstructor() {
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(Key.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Value.class, mapLikeType.getValueType().getRawClass());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new MapLikeType(Base.class);
        JavaType keyType = new MapLikeType(Key.class);
        JavaType valueType = new MapLikeType(Value.class);
        MapLikeType upgradedType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        assertEquals(Base.class, upgradedType.getRawClass());
        assertEquals(Key.class, upgradedType.getKeyType().getRawClass());
        assertEquals(Value.class, upgradedType.getValueType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpgradeFromWithInvalidBaseType() {
        JavaType baseType = new SimpleType(Base.class);
        JavaType keyType = new MapLikeType(Key.class);
        JavaType valueType = new MapLikeType(Value.class);
        MapLikeType.upgradeFrom(baseType, keyType, valueType);
    }

    @Test
    public void testConstruct() {
        JavaType keyType = new MapLikeType(Key.class);
        JavaType valueType = new MapLikeType(Value.class);
        MapLikeType constructedType = MapLikeType.construct(Map.class, keyType, valueType);
        assertEquals(Map.class, constructedType.getRawClass());
        assertEquals(Key.class, constructedType.getKeyType().getRawClass());
        assertEquals(Value.class, constructedType.getValueType().getRawClass());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new MapLikeType(NewKeyType.class);
        MapLikeType typeWithNewKeyType = mapLikeType.withKeyType(keyType);
        assertEquals(Map.class, typeWithNewKeyType.getRawClass());
        assertEquals(NewKeyType.class, typeWithNewKeyType.getKeyType().getRawClass());
        assertEquals(Value.class, typeWithNewKeyType.getValueType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        JavaType valueType = new MapLikeType(NewValueType.class);
        MapLikeType typeWithNewValueType = mapLikeType.withContentType(valueType);
        assertEquals(Map.class, typeWithNewValueType.getRawClass());
        assertEquals(Key.class, typeWithNewValueType.getKeyType().getRawClass());
        assertEquals(NewValueType.class, typeWithNewValueType.getValueType().getRawClass());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object handler = new Object();
        MapLikeType typeWithKeyTypeHandler = mapLikeType.withKeyTypeHandler(handler);
        assertEquals(handler, typeWithKeyTypeHandler.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object handler = new Object();
        MapLikeType typeWithKeyValueHandler = mapLikeType.withKeyValueHandler(handler);
        assertEquals(handler, typeWithKeyValueHandler.getKeyType().getValueHandler());
    }

    @Test
    public void testIsTrueMapType() {
        assertFalse(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        assertEquals("[map-like type; class java.util.Map, class Key -> class Value]", mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        JavaType keyType = new MapLikeType(Key.class);
        JavaType valueType = new MapLikeType(Value.class);
        MapLikeType equalType = new MapLikeType(Map.class, keyType, valueType);
        assertEquals(mapLikeType, equalType);
    }
}

class Base {}
class Key {}
class NewKeyType {}
class Value {}
class NewValueType {}