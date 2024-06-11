import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Before;
import org.junit.Test;

public class MapLikeTypeTest {

    private MapLikeType mapLikeType;

    @Before
    public void setUp() {
        mapLikeType = new MapLikeType(Map.class, null, null, new JavaType[]{}, new JavaType(Map.Entry.class), new JavaType(Object.class), null, null, false);
    }

    @Test
    public void testGetType() {
        assertEquals(Map.class, mapLikeType.getType());
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
    public void testGetKeyType() {
        assertEquals(new JavaType(Map.Entry.class), mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        assertEquals(new JavaType(Object.class), mapLikeType.getContentType());
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
    public void testRefine() {
        JavaType subclass = new JavaType(MapLikeType.class);
        JavaType superClass = new JavaType(TypeBase.class);
        JavaType[] superInterfaces = new JavaType[]{new JavaType(Serializable.class)};
        assertEquals(new MapLikeType(Map.class, null, superClass, superInterfaces, new JavaType(Map.Entry.class), new JavaType(Object.class), null, null, false), mapLikeType.refine(Map.class, null, superClass, superInterfaces));
    }

    @Test
    public void testBuildCanonicalName() {
        assertEquals("java.util.Map", mapLikeType.buildCanonicalName());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType(String.class);
        assertEquals(new MapLikeType(Map.class, null, null, new JavaType[]{}, keyType, new JavaType(Object.class), null, null, false), mapLikeType.withKeyType(keyType));
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(Integer.class);
        assertEquals(new MapLikeType(Map.class, null, null, new JavaType[]{}, new JavaType(Map.Entry.class), contentType, null, null, false), mapLikeType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        assertEquals(new MapLikeType(Map.class, null, null, new JavaType[]{}, new JavaType(Map.Entry.class), new JavaType(Object.class), null, typeHandler, false), mapLikeType.withTypeHandler(typeHandler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object contentTypeHandler = new Object();
        assertEquals(new MapLikeType(Map.class, null, null, new JavaType[]{}, new JavaType(Map.Entry.class), new JavaType(Object.class).withTypeHandler(contentTypeHandler), null, null, false), mapLikeType.withContentTypeHandler(contentTypeHandler));
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        assertEquals(new MapLikeType(Map.class, null, null, new JavaType[]{}, new JavaType(Map.Entry.class), new JavaType(Object.class), valueHandler, null, false), mapLikeType.withValueHandler(valueHandler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object contentValueHandler = new Object();
        assertEquals(new MapLikeType(Map.class, null, null, new JavaType[]{}, new JavaType(Map.Entry.class), new JavaType(Object.class).withValueHandler(contentValueHandler), null, null, false), mapLikeType.withContentValueHandler(contentValueHandler));
    }

    @Test
    public void testWithStaticTyping() {
        assertTrue(mapLikeType.withStaticTyping().isStaticTyping());
    }
}