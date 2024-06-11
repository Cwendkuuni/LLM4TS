
import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class ReferenceTypeTest {
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(contentType);
        
        JavaType newType = referenceType.withContentType(new SimpleType());
        
        assertNotNull(newType);
        assertEquals(SimpleType.class, newType.getClass());
        assertEquals(contentType, newType.getContentType());
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(new SimpleType());
        
        ReferenceType newType = referenceType.withTypeHandler(typeHandler);
        
        assertNotNull(newType);
        assertEquals(ReferenceType.class, newType.getClass());
        assertEquals(typeHandler, newType.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(new SimpleType());
        
        ReferenceType newType = referenceType.withContentTypeHandler(typeHandler);
        
        assertNotNull(newType);
        assertEquals(ReferenceType.class, newType.getClass());
        assertEquals(typeHandler, newType.getContentType().getTypeHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        ReferenceType referenceType = new ReferenceType(new SimpleType());
        
        ReferenceType newType = referenceType.withStaticTyping();
        
        assertNotNull(newType);
        assertEquals(ReferenceType.class, newType.getClass());
        assertTrue(newType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        ReferenceType referenceType = new ReferenceType(new SimpleType());
        JavaType superClass = new SimpleType();
        JavaType[] superInterfaces = new JavaType[2];
        superInterfaces[0] = new SimpleType();
        superInterfaces[1] = new SimpleType();
        
        JavaType newType = referenceType.refine(ReferenceType.class, superClass, superInterfaces);
        
        assertNotNull(newType);
        assertEquals(ReferenceType.class, newType.getClass());
        assertEquals(ReferenceType.class, newType.getRawClass());
        assertEquals(superClass, newType.getSuperClass());
        assertArrayEquals(superInterfaces, newType.getInterfaces());
    }
    
    @Test
    public void testGetContentType() {
        JavaType contentType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(contentType);
        
        JavaType actualContentType = referenceType.getContentType();
        
        assertNotNull(actualContentType);
        assertEquals(contentType, actualContentType);
    }
    
    @Test
    public void testGetReferencedType() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(refType);
        
        JavaType actualRefType = referenceType.getReferencedType();
        
        assertNotNull(actualRefType);
        assertEquals(refType, actualRefType);
    }
    
    @Test
    public void testHasContentType() {
        ReferenceType referenceType = new ReferenceType(new SimpleType());
        
        assertTrue(referenceType.hasContentType());
    }
    
    @Test
    public void testIsReferenceType() {
        ReferenceType referenceType = new ReferenceType(new SimpleType());
        
        assertTrue(referenceType.isReferenceType());
    }
    
    @Test
    public void testGetAnchorType() {
        JavaType anchorType = new SimpleType();
        ReferenceType referenceType = new ReferenceType(new SimpleType(), anchorType);
        
        JavaType actualAnchorType = referenceType.getAnchorType();
        
        assertNotNull(actualAnchorType);
        assertEquals(anchorType, actualAnchorType);
    }
    
    @Test
    public void testIsAnchorType() {
        ReferenceType referenceType = new ReferenceType(new SimpleType());
        
        assertTrue(referenceType.isAnchorType());
    }
    
    @Test
    public void testToString() {
        ReferenceType referenceType = new ReferenceType(new SimpleType());
        
        String actualString = referenceType.toString();
        
        assertNotNull(actualString);
        assertTrue(actualString.startsWith("[reference type, class"));
        assertTrue(actualString.endsWith("]"));
    }
    
    @Test
    public void testEquals() {
        JavaType refType = new SimpleType();
        ReferenceType referenceType1 = new ReferenceType(refType);
        ReferenceType referenceType2 = new ReferenceType(refType);
        ReferenceType referenceType3 = new ReferenceType(new SimpleType());
        
        assertTrue(referenceType1.equals(referenceType2));
        assertFalse(referenceType1.equals(referenceType3));
    }
    
}
