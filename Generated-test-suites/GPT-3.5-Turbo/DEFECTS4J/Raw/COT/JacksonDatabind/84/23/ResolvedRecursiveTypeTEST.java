
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        
        type.setReference(ref);
        
        assertEquals(ref, type.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        
        type.setReference(ref1);
        type.setReference(ref2);
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());

        assertEquals(new StringBuilder(), type.getGenericSignature(new StringBuilder()));
    }
    
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());

        assertEquals(new StringBuilder(), type.getErasedSignature(new StringBuilder()));
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType contentType = new JavaType();
        
        assertEquals(type, type.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        
        assertEquals(type, type.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        
        assertEquals(type, type.withContentTypeHandler(handler));
    }
    
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        
        assertEquals(type, type.withValueHandler(handler));
    }
    
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        
        assertEquals(type, type.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        assertEquals(type, type.withStaticTyping());
    }
    
    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        assertEquals(type, type._narrow(Object.class));
    }
    
    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        
        assertNull(type.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, new TypeBindings());
        
        assertFalse(type.isContainerType());
    }
    
    @Test
    public void testToString() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, new TypeBindings());
        
        assertEquals("[recursive type; java.lang.Object]", type1.toString());
        assertEquals("[recursive type; UNRESOLVED]", type2.toString());
    }
    
    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(null, new TypeBindings());
        assertFalse(type1.equals(null));
        assertFalse(type1.equals(new Object()));
        assertFalse(type3.equals(type1));
        assertTrue(type1.equals(type2));
    }
}
