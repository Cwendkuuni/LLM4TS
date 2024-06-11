import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref = new JavaType();
        resolvedType.setReference(ref);
        assertEquals(ref, resolvedType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedType.setReference(ref1);
        resolvedType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType ref = new JavaType();
        ref.getGenericSignature(expected);
        resolvedType.setReference(ref);
        resolvedType.getGenericSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        JavaType ref = new JavaType();
        ref.getErasedSignature(expected);
        resolvedType.setReference(ref);
        resolvedType.getErasedSignature(sb);
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType contentType = new JavaType();
        JavaType result = resolvedType.withContentType(contentType);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = resolvedType.withTypeHandler(handler);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = resolvedType.withContentTypeHandler(handler);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = resolvedType.withValueHandler(handler);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = resolvedType.withContentValueHandler(handler);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        JavaType result = resolvedType.withStaticTyping();
        assertEquals(resolvedType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> subclass = Object.class;
        JavaType result = resolvedType._narrow(subclass);
        assertEquals(resolvedType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertFalse(resolvedType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertEquals("[recursive type; UNRESOLVED]", resolvedType.toString());
        JavaType ref = new JavaType();
        ref.setRawClass(Object.class);
        resolvedType.setReference(ref);
        assertEquals("[recursive type; java.lang.Object]", resolvedType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedType1 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        ResolvedRecursiveType resolvedType2 = new ResolvedRecursiveType(Object.class, new TypeBindings());
        assertTrue(resolvedType1.equals(resolvedType1));
        assertFalse(resolvedType1.equals(null));
        assertFalse(resolvedType1.equals(new Object()));
        assertTrue(resolvedType1.equals(resolvedType2));
        JavaType ref1 = new JavaType();
        resolvedType1.setReference(ref1);
        assertFalse(resolvedType1.equals(resolvedType2));
        JavaType ref2 = new JavaType();
        resolvedType2.setReference(ref2);
        assertFalse(resolvedType1.equals(resolvedType2));
        resolvedType2.setReference(ref1);
        assertTrue(resolvedType1.equals(resolvedType2));
    }

}
