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
    public void testIsTrueMapType() {
        assertFalse(mapLikeType.isTrueMapType());
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
        assertNull(mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
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
        assertNotNull(mapLikeType.getErasedSignature(new StringBuilder()));
    }

    @Test
    public void testGetGenericSignature() {
        assertNotNull(mapLikeType.getGenericSignature(new StringBuilder()));
    }

    @Test
    public void testWithKeyType() {
        assertEquals(mapLikeType, mapLikeType.withKeyType(null));
    }

    @Test
    public void testWithContentType() {
        assertEquals(mapLikeType, mapLikeType.withContentType(null));
    }

    @Test
    public void testWithTypeHandler() {
        assertEquals(mapLikeType, mapLikeType.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        assertEquals(mapLikeType, mapLikeType.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        assertEquals(mapLikeType, mapLikeType.withValueHandler(null));
    }

    @Test
    public void testWithContentValuehHandler() {
        assertEquals(mapLikeType, mapLikeType.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(mapLikeType, mapLikeType.withStaticTyping());
    }

    @Test
    public void testRefine() {
        assertNotNull(mapLikeType.refine(null, null, null, null));
    }

    @Test
    public void testToString() {
        assertNotNull(mapLikeType.toString());
    }

    @Test
    public void testEquals() {
        assertTrue(mapLikeType.equals(mapLikeType));
        assertFalse(mapLikeType.equals(null));
        assertFalse(mapLikeType.equals(""));
    }
}