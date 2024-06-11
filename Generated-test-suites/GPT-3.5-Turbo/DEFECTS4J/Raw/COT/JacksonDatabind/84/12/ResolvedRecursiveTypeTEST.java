
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType() {};

        type.setReference(ref);

        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType() {};
        JavaType ref2 = new JavaType() {};

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType() {};

        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getGenericSignature(new StringBuilder());

        assertEquals(expected.toString(), type.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType() {};

        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = ref.getErasedSignature(new StringBuilder());

        assertEquals(expected.toString(), type.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType() {};

        JavaType result = type.withContentType(contentType);

        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        JavaType result = type.withTypeHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        JavaType result = type.withContentTypeHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        JavaType result = type.withValueHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();

        JavaType result = type.withContentValueHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);

        JavaType result = type.withStaticTyping();

        assertEquals(type, result);
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> subclass = Object.class;

        JavaType result = type._narrow(subclass);

        assertEquals(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);

        assertFalse(type.isContainerType());
    }

    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);

        String expected = "[recursive type; UNRESOLVED]";

        assertEquals(expected, type.toString());
    }

    @Test
    public void testToStringResolved() {
        Class<?> erasedType = Object.class;
        ResolvedRecursiveType type = new ResolvedRecursiveType(erasedType, null);
        JavaType ref = new JavaType() {};

        type.setReference(ref);

        String expected = "[recursive type; " + erasedType.getName() + "]";

        assertEquals(expected, type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType() {};

        type1.setReference(ref);
        type2.setReference(ref);

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEqualsDifferentSelfReferencedType() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(null, null);
        JavaType ref1 = new JavaType() {};
        JavaType ref2 = new JavaType() {};

        type1.setReference(ref1);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType() {};

        type1.setReference(ref);

        assertFalse(type1.equals(new Object()));
    }
}
