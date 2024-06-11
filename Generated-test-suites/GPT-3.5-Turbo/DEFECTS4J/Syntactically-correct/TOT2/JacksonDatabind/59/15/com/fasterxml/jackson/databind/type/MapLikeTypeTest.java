import static org.junit.Assert.*;

import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapLikeType;


public class MapLikeTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new ObjectMapper().constructType(String.class);
        JavaType keyType = new ObjectMapper().constructType(Integer.class);
        JavaType valueType = new ObjectMapper().constructType(Double.class);
        
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        
        assertNotNull(mapLikeType);
        assertEquals(baseType, mapLikeType.getKeyType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testConstruct() {
        JavaType keyType = new ObjectMapper().constructType(String.class);
        JavaType valueType = new ObjectMapper().constructType(Integer.class);
        
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
        
        assertNotNull(mapLikeType);
        assertEquals(Map.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType baseType = new ObjectMapper().constructType(String.class);
        JavaType keyType = new ObjectMapper().constructType(Integer.class);
        JavaType valueType = new ObjectMapper().constructType(Double.class);
        
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        
        Object typeHandler = new Object();
        MapLikeType modifiedType = mapLikeType.withTypeHandler(typeHandler);
        
        assertEquals(typeHandler, modifiedType.getTypeHandler());
    }

    @Test
    public void testToString() {
        JavaType keyType = new ObjectMapper().constructType(String.class);
        JavaType valueType = new ObjectMapper().constructType(Integer.class);
        
        MapLikeType mapLikeType = MapLikeType.construct(Map.class, keyType, valueType);
        
        assertEquals("[map-like type; class java.util.Map, class java.lang.String -> class java.lang.Integer]", mapLikeType.toString());
    }
}