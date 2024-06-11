import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType(null, null, 0, null, null, null, false);
        resolvedRecursiveType.setReference(javaType);
        Assert.assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType javaType = new JavaType(null, null, 0, null, null, null, false);
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.setReference(javaType); // Calling setReference twice should throw IllegalStateException
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        Assert.assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        Assert.assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType(null, null, 0, null, null, null, false);
        JavaType result = resolvedRecursiveType.withContentType(contentType);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withTypeHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentTypeHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withValueHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        JavaType result = resolvedRecursiveType.withContentValueHandler(handler);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType result = resolvedRecursiveType.withStaticTyping();
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> subclass = null;
        JavaType result = resolvedRecursiveType._narrow(subclass);
        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        JavaType result = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals("[recursive type; UNRESOLVED]", resolvedRecursiveType.toString());
    }

    @Test
    public void testEqualsSameObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEqualsDifferentClass() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEqualsDifferentReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType javaType1 = new JavaType(null, null, 0, null, null, null, false);
        JavaType javaType2 = new JavaType(null, null, 0, null, null, null, false);
        resolvedRecursiveType1.setReference(javaType1);
        resolvedRecursiveType2.setReference(javaType2);
        Assert.assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsSameReferencedType() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, null);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(null, null);
        JavaType javaType1 = new JavaType(null, null, 0, null, null, null, false);
        resolvedRecursiveType1.setReference(javaType1);
        resolvedRecursiveType2.setReference(javaType1);
        Assert.assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }
}

