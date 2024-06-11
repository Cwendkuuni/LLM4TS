import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
        assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        assertNotNull(resolvedRecursiveType.getBindings());
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = Mockito.mock(JavaType.class);
        resolvedRecursiveType.setReference(ref);

        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(ref, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceIllegalStateException() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref1 = Mockito.mock(JavaType.class);
        resolvedRecursiveType.setReference(ref1);

        JavaType ref2 = Mockito.mock(JavaType.class);
        resolvedRecursiveType.setReference(ref2);

        fail("Should throw IllegalStateException");
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = Mockito.mock(JavaType.class);
        resolvedRecursiveType.setReference(ref);

        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(ref, selfReferencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = Mockito.mock(JavaType.class);
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);

        String expected = "[" + ref.getGenericSignature(sb).toString() + "; ";
        String actual = sb.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref = Mockito.mock(JavaType.class);
        resolvedRecursiveType.setReference(ref);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);

        String expected = ref.getErasedSignature(sb).toString();
        String actual = sb.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testWithContentType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType contentType = Mockito.mock(JavaType.class);

        ResolvedRecursiveType result = resolvedRecursiveType.withContentType(contentType);

        assertSame(resolvedRecursiveType, result);
    }

    // Add test cases for the remaining methods with similar structure

    @Test(expected = UnsupportedOperationException.class)
    public void testWithTypeHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = Mockito.mock(Object.class);
        ResolvedRecursiveType result = resolvedRecursiveType.withTypeHandler(handler);

        fail("Should throw UnsupportedOperationException");
    }

    // Add test cases for the remaining methods with similar structure

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentTypeHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = Mockito.mock(Object.class);
        ResolvedRecursiveType result = resolvedRecursiveType.withContentTypeHandler(handler);

        fail("Should throw UnsupportedOperationException");
    }

    // Add test cases for the remaining methods with similar structure

    @Test(expected = UnsupportedOperationException.class)
    public void testWithValueHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = Mockito.mock(Object.class);
        ResolvedRecursiveType result = resolvedRecursiveType.withValueHandler(handler);

        fail("Should throw UnsupportedOperationException");
    }

    // Add test cases for the remaining methods with similar structure

    @Test(expected = UnsupportedOperationException.class)
    public void testWithContentValueHandler() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        Object handler = Mockito.mock(Object.class);
        ResolvedRecursiveType result = resolvedRecursiveType.withContentValueHandler(handler);

        fail("Should throw UnsupportedOperationException");
    }

    // Add test cases for the remaining methods with similar structure

    @Test
    public void testWithStaticTyping() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        ResolvedRecursiveType result = resolvedRecursiveType.withStaticTyping();

        assertSame(resolvedRecursiveType, result);
    }

    // Add test cases for the remaining methods with similar structure

    @Test
    public void testToString() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);

        JavaType ref = Mockito.mock(JavaType.class);
        resolvedRecursiveType.setReference(ref);

        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    // Add test cases for the remaining methods with similar structure

    @Test
    public void testEquals() {
        Class<String> erasedType1 = String.class;
        Class<String> erasedType2 = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings);

        JavaType ref1 = Mockito.mock(JavaType.class);
        resolvedRecursiveType1.setReference(ref1);
        JavaType ref2 = Mockito.mock(JavaType.class);
        resolvedRecursiveType2.setReference(ref2);

        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
        assertFalse(resolvedRecursiveType1.equals(""));
        assertFalse(resolvedRecursiveType1.equals(null));
        assertFalse(resolvedRecursiveType1.equals(new Object()));
        assertFalse(resolvedRecursiveType1.equals(new TypeBase<>()));
    }

    // Add test cases for the remaining methods with similar structure

}