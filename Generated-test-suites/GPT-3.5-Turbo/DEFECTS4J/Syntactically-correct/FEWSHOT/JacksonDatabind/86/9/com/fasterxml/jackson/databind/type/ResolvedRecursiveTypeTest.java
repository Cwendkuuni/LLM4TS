import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);
        
        assertEquals(ref, type.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getGenericSignature(sb), type.getGenericSignature(sb));
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        assertEquals(ref.getErasedSignature(sb), type.getErasedSignature(sb));
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withContentType(null));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withValueHandler(null));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withContentValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals(type, type._narrow(null));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertNull(type.refine(null, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
        
        JavaType ref = new JavaType(Object.class);
        type.setReference(ref);
        assertEquals("[recursive type; java.lang.Object]", type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertFalse(type.equals(null));
        assertFalse(type.equals(new Object()));
        assertTrue(type.equals(new ResolvedRecursiveType(Object.class, null)));
        
        JavaType ref1 = new JavaType(Object.class);
        type.setReference(ref1);
        
        ResolvedRecursiveType newType = new ResolvedRecursiveType(Object.class, null);
        assertFalse(type.equals(newType));
        
        JavaType ref2 = new JavaType(Object.class);
        newType.setReference(ref2);
        assertTrue(type.equals(newType));
    }
}