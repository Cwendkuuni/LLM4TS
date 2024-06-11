
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        TypeBindings typeBindings = new TypeBindings(null, null);
        resolvedRecursiveType = new ResolvedRecursiveType(Object.class, typeBindings);
        referencedType = new JavaType(Object.class);
    }

    @Test
    public void testSetReference_validReference() {
        resolvedRecursiveType.setReference(referencedType);
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSetReference_duplicateReference() {
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.setReference(referencedType);
    }
    
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = referencedType.getGenericSignature(sb);
        
        StringBuilder actual = resolvedRecursiveType.getGenericSignature(new StringBuilder());
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = referencedType.getErasedSignature(sb);
        
        StringBuilder actual = resolvedRecursiveType.getErasedSignature(new StringBuilder());
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(String.class);
        
        JavaType actual = resolvedRecursiveType.withContentType(contentType);
        
        assertEquals(resolvedRecursiveType, actual);
    }
    
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        
        JavaType actual = resolvedRecursiveType.withTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, actual);
    }
    
    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        
        JavaType actual = resolvedRecursiveType.withContentTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, actual);
    }
    
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        
        JavaType actual = resolvedRecursiveType.withValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, actual);
    }
    
    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        
        JavaType actual = resolvedRecursiveType.withContentValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, actual);
    }
    
    @Test
    public void testWithStaticTyping() {
        JavaType actual = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, actual);
    }
    
    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        
        JavaType actual = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, actual);
    }
    
    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings(null, null);
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = {};
        
        JavaType actual = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(actual);
    }
    
    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }
    
    @Test
    public void testToString_unresolvedReference() {
        String expected = "[recursive type; UNRESOLVED]";
        
        String actual = resolvedRecursiveType.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testToString_resolvedReference() {
        resolvedRecursiveType.setReference(referencedType);
        String expected = "[recursive type; java.lang.Object]";
        
        String actual = resolvedRecursiveType.toString();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_equalObjects() {
        resolvedRecursiveType.setReference(referencedType);
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings(null, null));
        other.setReference(referencedType);
        
        assertTrue(resolvedRecursiveType.equals(other));
    }
    
    @Test
    public void testEquals_differentClass() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }
    
    @Test
    public void testEquals_differentReferencedType() {
        resolvedRecursiveType.setReference(referencedType);
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, new TypeBindings(null, null));
        other.setReference(referencedType);
        
        assertFalse(resolvedRecursiveType.equals(other));
    }
    
    @Test
    public void testEquals_nullReferencedType() {
        resolvedRecursiveType.setReference(referencedType);
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings(null, null));
        
        assertFalse(resolvedRecursiveType.equals(other));
    }

}

