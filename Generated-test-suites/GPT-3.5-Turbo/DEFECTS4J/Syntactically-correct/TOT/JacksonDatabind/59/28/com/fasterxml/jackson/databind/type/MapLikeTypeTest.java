import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.JavaType;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        JavaType keyType = newTestType("KeyType");
        JavaType valueType = newTestType("ValueType");
        mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, valueType, null, null, false);
    }

    @Test
    public void testGetKeyType() {
        JavaType expectedKeyType = newTestType("KeyType");
        assertEquals(expectedKeyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType expectedContentType = newTestType("ValueType");
        assertEquals(expectedContentType, mapLikeType.getContentType());
    }

    @Test
    public void testIsTrueMapType() {
        assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testWithKeyType() {
        JavaType newKeyType = newTestType("NewKeyType");
        MapLikeType newMapLikeType = mapLikeType.withKeyType(newKeyType);
        assertEquals(newKeyType, newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = newTestType("NewValueType");
        MapLikeType newMapLikeType = mapLikeType.withContentType(newContentType);
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(newContentType, newMapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newMapLikeType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentTypeHandler(typeHandler);
        assertEquals(typeHandler, newMapLikeType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newMapLikeType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Object valueHandler = new Object();
        MapLikeType newMapLikeType = mapLikeType.withContentValueHandler(valueHandler);
        assertEquals(valueHandler, newMapLikeType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapLikeType newMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType keyType = newTestType("KeyType");
        JavaType valueType = newTestType("ValueType");
        MapLikeType newMapLikeType = mapLikeType.refine(HashMap.class, null, null, null);
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
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
    public void testEquals() {
        assertTrue(mapLikeType.equals(mapLikeType));
        assertFalse(mapLikeType.equals(null));
        assertFalse(mapLikeType.equals(new Object()));
        assertFalse(mapLikeType.equals(newTestType("NewMapLikeType")));
    }

    /**
     * Utility method to create a new JavaType with a given name.
     * @param name the name of the JavaType
     * @return the created JavaType object
     */
    private JavaType newTestType(String name) {
        return new JavaType(null, null, name, new TypeVariable[]{}, 0, null, null, false);
    }

}