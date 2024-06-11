import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class MapLikeTypeTest {

    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        JavaType keyType = JavaTypeBuilder.build(String.class);
        JavaType valueType = JavaTypeBuilder.build(Integer.class);
        mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType, null, null, false);
    }

    @Test
    public void testKeyType() {
        JavaType expectedKeyType = JavaTypeBuilder.build(String.class);
        assertEquals(expectedKeyType, mapLikeType.getKeyType());
    }

    @Test
    public void testValueType() {
        JavaType expectedValueType = JavaTypeBuilder.build(Integer.class);
        assertEquals(expectedValueType, mapLikeType.getValueType());
    }

    @Test
    public void testIsTrueMapType() {
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testToString() {
        String expectedString = "[map-like type; class java.util.HashMap, java.lang.String -> java.lang.Integer]";
        assertEquals(expectedString, mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        JavaType keyType = JavaTypeBuilder.build(String.class);
        JavaType valueType = JavaTypeBuilder.build(Integer.class);
        MapLikeType sameMapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType, null, null, false);
        assertTrue(mapLikeType.equals(sameMapLikeType));
    }

}