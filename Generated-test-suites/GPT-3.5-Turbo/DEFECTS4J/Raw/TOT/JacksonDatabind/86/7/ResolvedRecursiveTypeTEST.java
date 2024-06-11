import static org.junit.Assert.*;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    // Test case for setReference method
    @Test
    public void testSetReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType referencedType = new JavaType();
        recursiveType.setReference(referencedType);
        assertEquals(referencedType, recursiveType.getSelfReferencedType());
        
        // Test for multiple setReference calls, should throw IllegalStateException
        try {
            recursiveType.setReference(new JavaType());
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Trying to re-set self reference; old value = "+referencedType+", new = "+new JavaType(), e.getMessage());
        }
    }

    // Test case for getGenericSignature method
    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        recursiveType.getGenericSignature(sb);
        // Verify that getGenericSignature method calls are delegated to the referencedType
        assertEquals(referencedType.getGenericSignature(sb).toString(), sb.toString());
    }

    // Test case for getErasedSignature method
    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        recursiveType.getErasedSignature(sb);
        // Verify that getErasedSignature method calls are delegated to the referencedType
        assertEquals(referencedType.getErasedSignature(sb).toString(), sb.toString());
    }

    // Test case for withContentType method
    @Test
    public void testWithContentType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType contentType = new JavaType();
        // withContentType method should always return the current instance
        assertEquals(recursiveType, recursiveType.withContentType(contentType));
    }

    // Test case for withTypeHandler method
    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        // withTypeHandler method should always return the current instance
        assertEquals(recursiveType, recursiveType.withTypeHandler(handler));
    }

    // Test case for withContentTypeHandler method
    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        // withContentTypeHandler method should always return the current instance
        assertEquals(recursiveType, recursiveType.withContentTypeHandler(handler));
    }

    // Test case for withValueHandler method
    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        // withValueHandler method should always return the current instance
        assertEquals(recursiveType, recursiveType.withValueHandler(handler));
    }

    // Test case for withContentValueHandler method
    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Object handler = new Object();
        // withContentValueHandler method should always return the current instance
        assertEquals(recursiveType, recursiveType.withContentValueHandler(handler));
    }

    // Test case for withStaticTyping method
    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        // withStaticTyping method should always return the current instance
        assertEquals(recursiveType, recursiveType.withStaticTyping());
    }

    // Test case for _narrow method
    @Test
    public void test_narrow() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Class<?> subclass = String.class;
        // _narrow method should always return the current instance
        assertEquals(recursiveType, recursiveType._narrow(subclass));
    }

    // Test case for refine method
    @Test
    public void testRefine() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        // refine method should always return null
        assertNull(recursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    // Test case for isContainerType method
    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        // isContainerType method should always return false for this class
        assertFalse(recursiveType.isContainerType());
    }

    // Test case for toString method
    @Test
    public void testToString() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(String.class, null);
        String expectedString = "[recursive type; UNRESOLVED]";
        assertEquals(expectedString, recursiveType.toString());
        
        // Test when referencedType is set, should include the type name in the string
        recursiveType.setReference(new JavaType());
        expectedString = "[recursive type; "+recursiveType.getSelfReferencedType().getRawClass().getName()+"]";
        assertEquals(expectedString, recursiveType.toString());
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(String.class, null);
        // Equals should return true only if the referenced types are equal
        assertFalse(recursiveType1.equals(null));
        assertFalse(recursiveType1.equals(new Object()));
        assertFalse(recursiveType1.equals(recursiveType2));
        recursiveType2.setReference(new JavaType());
        assertFalse(recursiveType1.equals(recursiveType2));
        recursiveType1.setReference(new JavaType());
        assertTrue(recursiveType1.equals(recursiveType2));
    }
}