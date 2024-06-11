import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
        
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        
        // assert the expected value of sb
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        
        // assert the expected value of sb
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        
        JavaType result = resolvedRecursiveType.withStaticTyping();
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;
        
        JavaType result = resolvedRecursiveType._narrow(subclass);
        
        assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);

        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        
        String expected = "[recursive type; " + referencedType.getRawClass().getName() + "]";
        
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        assertNotEquals(resolvedRecursiveType1, resolvedRecursiveType2);
        
        JavaType referencedType = new JavaType();
        resolvedRecursiveType1.setReference(referencedType);
        resolvedRecursiveType2.setReference(referencedType);
        
        assertEquals(resolvedRecursiveType1, resolvedRecursiveType2);
    }
}