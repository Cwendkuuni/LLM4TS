import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(null, null);
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        JavaType ref = new JavaType();
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        Assert.assertEquals(sb, result);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        Assert.assertEquals(sb, result);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType();
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        JavaType result = resolvedRecursiveType.withStaticTyping();
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        boolean result = resolvedRecursiveType.isContainerType();
        Assert.assertFalse(result);
    }

    @Test
    public void testToString() {
        String result = resolvedRecursiveType.toString();
        StringBuilder sb = new StringBuilder(40)
                .append("[recursive type; UNRESOLVED");
        Assert.assertEquals(sb.toString(), result);
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(null, null);
        Assert.assertTrue(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsDifferentClass() {
        Object other = new Object();
        Assert.assertFalse(resolvedRecursiveType.equals(other));
    }
}