import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(); // Create a dummy JavaType
        type.setReference(ref);

        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref1 = new JavaType(); // Create a dummy JavaType
        JavaType ref2 = new JavaType(); // Create another dummy JavaType

        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(); // Create a dummy JavaType
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);

        assertEquals(expected.toString(), type.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(); // Create a dummy JavaType
        type.setReference(ref);

        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);

        assertEquals(expected.toString(), type.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType contentType = new JavaType(); // Create a dummy JavaType
        JavaType result = type.withContentType(contentType);

        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);

        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType result = type.withStaticTyping();

        assertEquals(type, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Class<?> subclass = Object.class;
        JavaType result = type._narrow(subclass);

        assertEquals(type, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);

        assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        boolean result = type.isContainerType();

        assertFalse(result);
    }

    @Test
    public void testToStringUnresolved() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        String expected = "[recursive type; UNRESOLVED]";
        String result = type.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testToStringResolved() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(); // Create a dummy JavaType
        type.setReference(ref);

        String expected = "[recursive type; " + ref.getRawClass().getName() + "]";
        String result = type.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testEqualsSameObject() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);

        assertTrue(type.equals(type));
    }

    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        Object object = new Object();

        assertFalse(type.equals(object));
    }

    @Test
    public void testEqualsSameClassAndEqualSelfReferencedType() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(); // Create a dummy JavaType
        type1.setReference(ref);
        type2.setReference(ref);

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testEqualsSameClassAndDifferentSelfReferencedType() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        JavaType ref1 = new JavaType(); // Create a dummy JavaType
        JavaType ref2 = new JavaType(); // Create another dummy JavaType
        type1.setReference(ref1);
        type2.setReference(ref2);

        assertFalse(type1.equals(type2));
    }

    @Test
    public void testEqualsSameClassAndNullSelfReferencedType() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new JavaType(); // Create a dummy JavaType
        type1.setReference(ref);

        assertFalse(type1.equals(type2));
    }

}