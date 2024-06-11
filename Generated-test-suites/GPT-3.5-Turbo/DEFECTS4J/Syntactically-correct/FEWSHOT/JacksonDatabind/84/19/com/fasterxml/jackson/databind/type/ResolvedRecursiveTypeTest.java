import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertNull(type.getSelfReferencedType());
        
        JavaType ref = new ResolvedRecursiveType(Object.class, new TypeBindings());
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
        
        try {
            type.setReference(ref);
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = "+ref+", new = "+ref, e.getMessage());
        }
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        assertEquals(type.getSelfReferencedType().getGenericSignature(sb), sb);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        assertEquals(type.getSelfReferencedType().getErasedSignature(sb), sb);
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(type, type.withContentType(null));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(type, type.withTypeHandler(null));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(type, type.withContentTypeHandler(null));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(type, type.withValueHandler(null));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(type, type.withContentValueHandler(null));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(type, type.withStaticTyping());
    }
    
    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(type, type._narrow(null));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertNull(type.refine(null, null, null, null));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(false, type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", type.toString());
        
        type.setReference(new ResolvedRecursiveType(Object.class, new TypeBindings()));
        assertEquals("[recursive type; java.lang.Object]", type.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals(false, type1.equals(null));
        assertEquals(false, type1.equals("test"));
        assertEquals(false, type1.equals(type2));
        
        JavaType ref = new ResolvedRecursiveType(Object.class, new TypeBindings());
        type1.setReference(ref);
        type2.setReference(ref);
        assertEquals(true, type1.equals(type2));
    }
}