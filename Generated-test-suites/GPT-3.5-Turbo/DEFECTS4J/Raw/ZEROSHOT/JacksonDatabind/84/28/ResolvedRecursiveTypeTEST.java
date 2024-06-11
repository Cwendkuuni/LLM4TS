import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType type;

    @Before
    public void setUp() {
        TypeBindings bindings = new TypeBindings();
        type = new ResolvedRecursiveType(Object.class, bindings);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        type.setReference(ref);
        assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        type.setReference(new JavaType());
        type.setReference(new JavaType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = type.withContentType(contentType);
        assertEquals(type, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = type.withTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = type.withContentTypeHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = type.withValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = type.withContentValueHandler(handler);
        assertEquals(type, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = type.withStaticTyping();
        assertEquals(type, result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        JavaType result = type._narrow(subclass);
        assertEquals(type, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = type.refine(rawType, bindings, superClass, superInterfaces);
        assertEquals(null, result);
    }

    @Test
    public void testIsContainerType() {
        boolean result = type.isContainerType();
        assertEquals(false, result);
    }

    @Test
    public void testToStringWithUnresolvedType() {
        type.setReference(null);
        String result = type.toString();
        assertEquals("[recursive type; UNRESOLVED]", result);
    }

    @Test
    public void testToStringWithResolvedType() {
        JavaType ref = new JavaType();
        ref.setRawClass(Object.class);
        type.setReference(ref);
        String result = type.toString();
        assertEquals("[recursive type; java.lang.Object]", result);
    }

    @Test
    public void testEqualsSameObject() {
        boolean result = type.equals(type);
        assertEquals(true, result);
    }

    @Test
    public void testEqualsNullObject() {
        boolean result = type.equals(null);
        assertEquals(false, result);
    }

    @Test
    public void testEqualsDifferentClass() {
        boolean result = type.equals(new Object());
        assertEquals(false, result);
    }

    @Test
    public void testEqualsUnresolvedTypes() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        boolean result = type.equals(other);
        assertEquals(false, result);
    }

    @Test
    public void testEqualsResolvedTypes() {
        JavaType ref = new JavaType();
        ref.setRawClass(Object.class);
        type.setReference(ref);

        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        other.setReference(ref);

        boolean result = type.equals(other);
        assertEquals(true, result);
    }
}