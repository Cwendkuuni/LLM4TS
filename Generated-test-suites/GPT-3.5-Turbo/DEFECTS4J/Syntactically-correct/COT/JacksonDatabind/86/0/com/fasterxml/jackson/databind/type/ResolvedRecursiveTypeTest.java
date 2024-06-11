import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }
    
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        type.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        type.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType contentType = new JavaType();
        assertEquals(type, type.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(type, type.withTypeHandler(handler));
    }
    
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(type, type.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(type, type.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Object handler = new Object();
        assertEquals(type, type.withContentValueHandler(handler));
    }
    
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertEquals(type, type.withStaticTyping());
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(type.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        sb.append("[recursive type; UNRESOLVED");
        assertEquals(sb.toString(), type.toString());
    }

    @Test
    public void testToStringResolved() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        JavaType referencedType = new JavaType();
        type.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        sb.append("[recursive type; ").append(String.class.getName());
        assertEquals(sb.toString(), type.toString());
    }

    @Test
    public void testEqualsNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type.equals(null));
    }

    @Test
    public void testEqualsSameObject() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertTrue(type.equals(type));
    }

    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, new TypeBindings());
        assertFalse(type.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentReferencedType() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Integer.class, new TypeBindings());
        type1.setReference(new JavaType());
        type2.setReference(new JavaType());
        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsSameReferencedType() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        type1.setReference(new JavaType());
        type2.setReference(new JavaType());
        assertTrue(type1.equals(type2));
    }
}