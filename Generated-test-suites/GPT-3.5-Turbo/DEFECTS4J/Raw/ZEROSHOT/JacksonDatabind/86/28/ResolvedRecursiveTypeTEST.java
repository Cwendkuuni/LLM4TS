import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setup() {
        resolvedRecursiveType = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        assertEquals(expected.toString(), resolvedRecursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        assertEquals(expected.toString(), resolvedRecursiveType.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = new Object();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = ResolvedRecursiveType.class;
        assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder(40)
                    .append("[recursive type; UNRESOLVED");
        assertEquals(sb.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEqualsNull() {
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentReferencedType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsSameReferencedType() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(ResolvedRecursiveType.class, new TypeBindings());
        resolvedRecursiveType.setReference(new JavaType());
        other.setReference(resolvedRecursiveType.getSelfReferencedType());
        assertTrue(resolvedRecursiveType.equals(other));
    }
}