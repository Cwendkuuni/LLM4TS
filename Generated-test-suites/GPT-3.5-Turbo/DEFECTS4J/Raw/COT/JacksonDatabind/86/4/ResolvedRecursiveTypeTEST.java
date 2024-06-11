
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType reference = new JavaType();
        resolvedRecursiveType.setReference(reference);
        assertEquals(reference, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        // Test with referencedType null
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals("", result.toString());

        // Test with referencedType not null
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        result = resolvedRecursiveType.getGenericSignature(sb);
        assertEquals(referencedType.getGenericSignature(new StringBuilder()).toString(), result.toString());
    }

    @Test
    public void testGetErasedSignature() {
        // Test with referencedType null
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals("", result.toString());

        // Test with referencedType not null
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        result = resolvedRecursiveType.getErasedSignature(sb);
        assertEquals(referencedType.getErasedSignature(new StringBuilder()).toString(), result.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        // Deprecated method, no need to test
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = null;
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
        // Test with referencedType null
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        String expectedResult = "[recursive type; UNRESOLVED]";
        String result = resolvedRecursiveType.toString();
        assertEquals(expectedResult, result);

        // Test with referencedType not null
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        expectedResult = "[recursive type; " + referencedType.getRawClass().getName() + "]";
        result = resolvedRecursiveType.toString();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        JavaType referencedType1 = new JavaType();
        JavaType referencedType2 = new JavaType();

        resolvedRecursiveType1.setReference(referencedType1);
        resolvedRecursiveType2.setReference(referencedType2);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        resolvedRecursiveType2.setReference(referencedType1);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

}
