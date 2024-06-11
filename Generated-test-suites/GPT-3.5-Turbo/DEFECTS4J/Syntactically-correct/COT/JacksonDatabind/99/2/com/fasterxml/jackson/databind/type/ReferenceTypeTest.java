import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {

    @Test
    public void testWithContentType() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        JavaType contentType = rt.withContentType(String.class);
        assertEquals(String.class, contentType);
        assertNotEquals(rt, contentType);
    }

    @Test
    public void testWithTypeHandler() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        Object typeHandler = new Object();
        ReferenceType updatedType = rt.withTypeHandler(typeHandler);
        assertEquals(typeHandler, updatedType.getTypeHandler());
        assertNotEquals(rt, updatedType);
    }

    @Test
    public void testWithContentTypeHandler() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        Object contentTypeHandler = new Object();
        ReferenceType updatedType = rt.withContentTypeHandler(contentTypeHandler);
        assertEquals(contentTypeHandler, updatedType.getContentType().getTypeHandler());
        assertNotEquals(rt, updatedType);
    }

    @Test
    public void testWithContentValueHandler() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        Object contentValueHandler = new Object();
        ReferenceType updatedType = rt.withContentValueHandler(contentValueHandler);
        assertEquals(contentValueHandler, updatedType.getContentType().getValueHandler());
        assertNotEquals(rt, updatedType);
    }

    @Test
    public void testWithStaticTyping() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        ReferenceType updatedType = rt.withStaticTyping();
        assertTrue(updatedType.isStaticTyping());
        assertNotEquals(rt, updatedType);
    }
  
    @Test
    public void testRefine() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, null, null, null, null, false);
        JavaType refinedType = rt.refine(String.class, null, null, null);
        assertEquals(String.class, refinedType.getRawClass());
        assertNotEquals(rt, refinedType);
    }
    
    @Test
    public void testGetContentType() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, String.class, null, null, null, false);
        JavaType contentType = rt.getContentType();
        assertEquals(String.class, contentType);
    }

    @Test
    public void testGetReferencedType() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, String.class, null, null, null, false);
        JavaType referencedType = rt.getReferencedType();
        assertEquals(String.class, referencedType);
    }

    @Test
    public void testHasContentType() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, String.class, null, null, null, false);
        assertTrue(rt.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, String.class, null, null, null, false);
        assertTrue(rt.isReferenceType());
    }

    @Test
    public void testGetAnchorType() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, null, String.class, null, null, false);
        JavaType anchorType = rt.getAnchorType();
        assertEquals(String.class, anchorType);
    }

    @Test
    public void testIsAnchorType() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, null, String.class, null, null, false);
        assertFalse(rt.isAnchorType());
    }

    @Test
    public void testToString() {
        ReferenceType rt = new ReferenceType(Object.class, null, null, null, String.class, null, null, null, false);
        String expected = "[reference type, class java.lang.Object<java.lang.String>]";
        assertEquals(expected, rt.toString());
    }

    @Test
    public void testEquals() {
        ReferenceType rt1 = new ReferenceType(Object.class, null, null, null, String.class, null, null, null, false);
        ReferenceType rt2 = new ReferenceType(Object.class, null, null, null, String.class, null, null, null, false);
        assertEquals(rt1, rt2);
    }

}