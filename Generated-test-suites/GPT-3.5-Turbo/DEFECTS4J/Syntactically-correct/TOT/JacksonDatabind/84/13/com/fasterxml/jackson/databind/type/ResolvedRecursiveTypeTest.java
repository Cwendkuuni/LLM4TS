import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        recursiveType.setReference(ref);
        Assert.assertEquals(ref, recursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        JavaType ref1 = new JavaType(ResolvedRecursiveTypeTest.class);
        recursiveType.setReference(ref1);
        JavaType ref2 = new JavaType(String.class);
        recursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        recursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getGenericSignature(expected);
        Assert.assertEquals(expected.toString(), recursiveType.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        JavaType ref = new JavaType(ResolvedRecursiveTypeTest.class);
        recursiveType.setReference(ref);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        ref.getErasedSignature(expected);
        Assert.assertEquals(expected.toString(), recursiveType.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        JavaType contentType = new JavaType(ResolvedRecursiveTypeTest.class);
        JavaType result = recursiveType.withContentType(contentType);
        Assert.assertEquals(recursiveType, result);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = recursiveType.withTypeHandler(handler);
        Assert.assertEquals(recursiveType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = recursiveType.withContentTypeHandler(handler);
        Assert.assertEquals(recursiveType, result);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = recursiveType.withValueHandler(handler);
        Assert.assertEquals(recursiveType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        Object handler = new Object();
        JavaType result = recursiveType.withContentValueHandler(handler);
        Assert.assertEquals(recursiveType, result);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        JavaType result = recursiveType.withStaticTyping();
        Assert.assertEquals(recursiveType, result);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        Class<?> rawType = ResolvedRecursiveTypeTest.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(Object.class);
        JavaType[] superInterfaces = {};
        JavaType result = recursiveType.refine(rawType, bindings, superClass, superInterfaces);
        Assert.assertNull(result);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        Assert.assertFalse(recursiveType.isContainerType());
    }

    @Test
    public void testToStringWithoutReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        Assert.assertEquals("[recursive type; UNRESOLVED]", recursiveType.toString());
    }

    @Test
    public void testToStringWithReference() {
        ResolvedRecursiveType recursiveType = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        recursiveType.setReference(new JavaType(ResolvedRecursiveTypeTest.class));
        Assert.assertEquals("[recursive type; " + ResolvedRecursiveTypeTest.class.getName() + "]", recursiveType.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        recursiveType1.setReference(new JavaType(ResolvedRecursiveTypeTest.class));
        recursiveType2.setReference(new JavaType(ResolvedRecursiveTypeTest.class));
        Assert.assertEquals(recursiveType1, recursiveType2);
    }

    @Test
    public void testEqualsDifferentReference() {
        ResolvedRecursiveType recursiveType1 = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        ResolvedRecursiveType recursiveType2 = new ResolvedRecursiveType(ResolvedRecursiveTypeTest.class, new TypeBindings());
        recursiveType1.setReference(new JavaType(ResolvedRecursiveTypeTest.class));
        recursiveType2.setReference(new JavaType(String.class));
        Assert.assertNotEquals(recursiveType1, recursiveType2);
    }
}