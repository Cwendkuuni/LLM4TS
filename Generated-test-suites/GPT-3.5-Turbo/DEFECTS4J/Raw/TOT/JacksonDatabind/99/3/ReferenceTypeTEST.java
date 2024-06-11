
import org.junit.Test;
import static org.junit.Assert.*;

public class ReferenceTypeTest {
    
    /**
     * Test that the constructor sets the referencedType correctly
     */
    @Test
    public void testConstructor() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        assertSame(refType, referenceType.getReferencedType());
    }
    
    /**
     * Test the withContentType() method
     */
    @Test
    public void testWithContentType() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        JavaType newContentType = new JavaType();
        ReferenceType newReferenceType = referenceType.withContentType(newContentType);
        
        assertSame(newContentType, newReferenceType.getContentType());
    }
    
    /**
     * Test the withTypeHandler() method
     */
    @Test
    public void testWithTypeHandler() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        Object newTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withTypeHandler(newTypeHandler);
        
        assertSame(newTypeHandler, newReferenceType.getTypeHandler());
    }
    
    /**
     * Test the withContentTypeHandler() method
     */
    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        Object newContentTypeHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentTypeHandler(newContentTypeHandler);
        
        assertSame(newContentTypeHandler, newReferenceType.getContentTypeHandler());
    }
    
    /**
     * Test the withValueHandler() method
     */
    @Test
    public void testWithValueHandler() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        Object newValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withValueHandler(newValueHandler);
        
        assertSame(newValueHandler, newReferenceType.getValueHandler());
    }
    
    /**
     * Test the withContentValueHandler() method
     */
    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        Object newContentValueHandler = new Object();
        ReferenceType newReferenceType = referenceType.withContentValueHandler(newContentValueHandler);
        
        assertSame(newContentValueHandler, newReferenceType.getContentValueHandler());
    }
    
    /**
     * Test the withStaticTyping() method
     */
    @Test
    public void testWithStaticTyping() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        ReferenceType newReferenceType = referenceType.withStaticTyping();
        
        assertTrue(newReferenceType.isStaticTyping());
    }
    
    /**
     * Test the refine() method
     */
    @Test
    public void testRefine() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = {};
        
        JavaType refinedType = referenceType.refine(Object.class, new TypeBindings(), superClass, superInterfaces);
        
        assertEquals(Object.class, refinedType.getRawClass());
        assertEquals(superClass, refinedType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedType.getInterfaces());
    }
    
    /**
     * Test the getErasedSignature() method
     */
    @Test
    public void testGetErasedSignature() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        StringBuilder sb = new StringBuilder();
        String erasedSignature = referenceType.getErasedSignature(sb).toString();
        
        assertEquals("class java.lang.Object", erasedSignature);
    }
    
    /**
     * Test the getGenericSignature() method
     */
    @Test
    public void testGetGenericSignature() {
        JavaType refType = new JavaType();
        ReferenceType referenceType = new ReferenceType(Object.class, new TypeBindings(), null, null, refType, null, null, null, false);
        
        StringBuilder sb = new StringBuilder();
        String genericSignature = referenceType.getGenericSignature(sb).toString();
        
        assertEquals("java.lang.Object;<java.lang.Object>;", genericSignature);
    }
    
}

