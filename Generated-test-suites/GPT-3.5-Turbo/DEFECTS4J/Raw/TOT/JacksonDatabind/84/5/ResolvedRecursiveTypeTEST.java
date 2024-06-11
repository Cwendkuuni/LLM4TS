import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(Object.class, null);
    }

    @Test
    public void testSetReference_WithValidReference_Success() {
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_WithMultipleCalls_ThrowsIllegalStateException() {
        JavaType referencedType1 = new JavaType();
        JavaType referencedType2 = new JavaType();
        resolvedRecursiveType.setReference(referencedType1);
        resolvedRecursiveType.setReference(referencedType2);
    }

    @Test
    public void testGetGenericSignature_WithValidReference_Success() {
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = referencedType.getGenericSignature(sb);
        assertEquals(expected, resolvedRecursiveType.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature_WithValidReference_Success() {
        JavaType referencedType = new JavaType();
        resolvedRecursiveType.setReference(referencedType);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = referencedType.getErasedSignature(sb);
        assertEquals(expected, resolvedRecursiveType.getErasedSignature(sb));
    }

    @Test
    public void testWithValueHandler_ReturnSameInstance() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler_ReturnSameInstance() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler_ReturnSameInstance() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping_ReturnSameInstance() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void test_narrow_ReturnSameInstance() {
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        assertSame(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine_ReturnNull() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[0];
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        assertNull(result);
    }

    @Test
    public void testIsContainerType_ReturnFalse() {
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString_WithNullReferencedType_ReturnsUNRESOLVED() {
        String expected = "[recursive type; UNRESOLVED]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testToString_WithNonNullReferencedType_ReturnsTypeName() {
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);
        String expected = "[recursive type; java.lang.Object]";
        assertEquals(expected, resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals_WithSameInstance_ReturnsTrue() {
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEquals_WithNull_ReturnsFalse() {
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEquals_WithDifferentClass_ReturnsFalse() {
        assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEquals_WithUnresolvedReference_ReturnsFalse() {
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(Object.class, null);
        assertFalse(resolvedRecursiveType.equals(otherType));
    }

    @Test
    public void testEquals_WithDifferentReference_ReturnsFalse() {
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new JavaType(Object.class);
        otherType.setReference(referencedType);
        assertFalse(resolvedRecursiveType.equals(otherType));
    }

    @Test
    public void testEquals_WithSameReferences_ReturnsTrue() {
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(Object.class, null);
        JavaType referencedType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(referencedType);
        otherType.setReference(referencedType);
        assertTrue(resolvedRecursiveType.equals(otherType));
    }
}