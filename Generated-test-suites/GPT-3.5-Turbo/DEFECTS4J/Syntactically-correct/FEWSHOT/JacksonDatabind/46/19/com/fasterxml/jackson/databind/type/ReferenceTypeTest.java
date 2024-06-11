import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        ReferenceType referenceType = ReferenceType.construct(String.class, JavaType.STRING, null, null);
        assertNotNull(referenceType);
        assertEquals(String.class, referenceType.getRawClass());
        assertEquals(JavaType.STRING, referenceType.getReferencedType());
        assertNull(referenceType.getValueHandler());
        assertNull(referenceType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType result = referenceType.withContentTypeHandler(handler);
        assertEquals(handler, referenceType.getContentType().getTypeHandler());
        assertEquals(JavaType.STRING, referenceType.getContentType().getReferencedType());
        assertTrue(result instanceof ReferenceType);
        assertEquals(result, referenceType);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType result = referenceType.withContentValueHandler(handler);
        assertEquals(handler, referenceType.getContentType().getValueHandler());
        assertEquals(JavaType.STRING, referenceType.getContentType().getReferencedType());
        assertTrue(result instanceof ReferenceType);
        assertEquals(result, referenceType);
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType result = referenceType.withStaticTyping();
        assertTrue(result.isStatic());
        assertEquals(JavaType.STRING, result.getReferencedType());
        assertTrue(result instanceof ReferenceType);
        assertEquals(result, referenceType);
    }
    
    @Test
    public void testContainedTypeCount() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(1, referenceType.containedTypeCount());
    }
    
    @Test
    public void testContainedType() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(JavaType.STRING, referenceType.containedType(0));
        assertNull(referenceType.containedType(1));
    }
    
    @Test
    public void testContainedTypeName() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals("T", referenceType.containedTypeName(0));
        assertNull(referenceType.containedTypeName(1));
    }
    
    @Test
    public void testGetParameterSource() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        assertEquals(String.class, referenceType.getParameterSource());
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        StringBuilder result = referenceType.getErasedSignature(sb);
        assertEquals("class java.lang.String", result.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        StringBuilder result = referenceType.getGenericSignature(sb);
        assertEquals("class java.lang.String<T>;", result.toString());
    }
    
    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        String expected = "[reference type, class java.lang.String<java.lang.String>]";
        assertEquals(expected, referenceType.toString());
    }
    
    @Test
    public void testEquals() {
        ReferenceType referenceType1 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType referenceType2 = new ReferenceType(String.class, JavaType.STRING, null, null, false);
        ReferenceType referenceType3 = new ReferenceType(Integer.class, JavaType.STRING, null, null, false);
        ReferenceType referenceType4 = new ReferenceType(String.class, JavaType.INT, null, null, false);
        
        assertTrue(referenceType1.equals(referenceType2));
        assertTrue(referenceType2.equals(referenceType1));
        assertTrue(!referenceType1.equals(referenceType3));
        assertTrue(!referenceType1.equals(referenceType4));
    }
}