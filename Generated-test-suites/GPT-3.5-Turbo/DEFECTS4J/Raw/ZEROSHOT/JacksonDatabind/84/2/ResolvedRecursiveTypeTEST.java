import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        ref.getGenericSignature(expected);
        
        assertEquals(expected, resolvedRecursiveType.getGenericSignature(sb));
    }
    
    @Test
    public void testGetErasedSignature() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        ref.getErasedSignature(expected);
        
        assertEquals(expected, resolvedRecursiveType.getErasedSignature(sb));
    }
    
    @Test
    public void testWithContentType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        JavaType contentType = new JavaType();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }
    
    @Test
    public void testWithTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        Object h = new Object();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        Object h = new Object();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }
    
    @Test
    public void testWithValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        Object h = new Object();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }
    
    @Test
    public void testWithContentValueHandler() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        Object h = new Object();
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }
    
    @Test
    public void testWithStaticTyping() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }
    
    @Test
    public void test_narrow() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        Class<?> subclass = Object.class;
        
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }
    
    @Test
    public void testRefine() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        Class<?> rawType = Object.class;
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }
    
    @Test
    public void testIsContainerType() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToString() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        
        JavaType ref = new JavaType();
        ref.getRawClass().setName("java.lang.Object");
        resolvedRecursiveType.setReference(ref);
        
        expected.append("[recursive type; java.lang.Object]");
        
        assertEquals(expected.toString(), resolvedRecursiveType.toString());
    }
    
    @Test
    public void testEquals() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        
        ResolvedRecursiveType other = new ResolvedRecursiveType(erasedType, bindings);
        other.setReference(ref);
        
        assertTrue(resolvedRecursiveType.equals(other));
    }

}