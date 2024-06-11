import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {
    
    private ResolvedRecursiveType resolvedRecursiveType;
    
    @Before
    public void setUp() {
        Class<?> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
    }
    
    @Test
    public void testSetReference_SanityCheck() {
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReference_MultipleCalls() {
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref); // Should throw IllegalStateException
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(expected, resolvedRecursiveType.getGenericSignature(sb));
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(expected, resolvedRecursiveType.getErasedSignature(sb));
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        Object h = null;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }
    
    @Test
    public void testWithValueHandler() {
        Object h = null;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }
    
    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }
    
    @Test
    public void test_narrow() {
        Class<?> subclass = String.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToString_UnresolvedReference() {
        String expected = "[recursive type; UNRESOLVED]";
        
        StringBuilder sb = new StringBuilder();
        String result = resolvedRecursiveType.toString();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testToString_ResolvedReference() {
        String expected = "[recursive type; java.lang.String]";
        
        JavaType ref = JavaType.nullType();
        resolvedRecursiveType.setReference(ref);
        
        StringBuilder sb = new StringBuilder();
        String result = resolvedRecursiveType.toString();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testEquals_SameObject() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }
    
    @Test
    public void testEquals_NullObject() {
        assertFalse(resolvedRecursiveType.equals(null));
    }
    
    @Test
    public void testEquals_WrongClassObject() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }
    
    @Test
    public void testEquals_UnresolvedReferenceObject() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.equals(other));
    }
    
    @Test
    public void testEquals_ResolvedReferenceObject() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, new TypeBindings());
        other.setReference(JavaType.nullType());
        assertFalse(resolvedRecursiveType.equals(other));
    }

}