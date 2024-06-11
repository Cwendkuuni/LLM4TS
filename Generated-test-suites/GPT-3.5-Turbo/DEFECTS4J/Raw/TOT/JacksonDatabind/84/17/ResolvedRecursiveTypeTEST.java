import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;
    private JavaType referencedType;

    @Before
    public void setUp() {
        this.referencedType = new JavaType(String.class);
        this.resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        resolvedRecursiveType.setReference(referencedType);
        Assert.assertEquals(referencedType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_IllegalStateException() {
        resolvedRecursiveType.setReference(referencedType);
        resolvedRecursiveType.setReference(new JavaType(Object.class));
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getGenericSignature(sb);
        Assert.assertEquals(result, sb);
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = resolvedRecursiveType.getErasedSignature(sb);
        Assert.assertEquals(result, sb);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(Integer.class);
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

    @SuppressWarnings("deprecation")
    @Test
    public void testNarrow() {
        JavaType subclass = new JavaType(Integer.class);
        JavaType result = resolvedRecursiveType._narrow(subclass);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = {};
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
        if (resolvedRecursiveType.getSelfReferencedType() == null) {
            Assert.assertTrue(result.contains("UNRESOLVED"));
        } else {
            Assert.assertTrue(result.contains(resolvedRecursiveType.getSelfReferencedType().getRawClass().getName()));
        }
    }

    @Test
    public void testEquals() {
        Object o = resolvedRecursiveType;
        boolean result = resolvedRecursiveType.equals(o);
        Assert.assertTrue(result);
    }
}