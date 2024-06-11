import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setUp() {
        resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
    }

    @Test
    public void testSetReference() {
        JavaType ref = new JavaTypeStub(); // Stub implementation of JavaType
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceWhenAlreadySet() {
        JavaType ref1 = new JavaTypeStub();
        JavaType ref2 = new JavaTypeStub();
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        JavaType ref = new JavaTypeStub();
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = resolvedRecursiveType.getGenericSignature(new StringBuilder());
        Assert.assertEquals(ref.getGenericSignature(new StringBuilder()).toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType ref = new JavaTypeStub();
        resolvedRecursiveType.setReference(ref);
        StringBuilder sb = resolvedRecursiveType.getErasedSignature(new StringBuilder());
        Assert.assertEquals(ref.getErasedSignature(new StringBuilder()).toString(), sb.toString());
    }

    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        Object handler = new Object();
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        Class<?> subclass = Object.class;
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = Object.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaTypeStub();
        JavaType[] superInterfaces = new JavaTypeStub[] { new JavaTypeStub() };
        Assert.assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        Assert.assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        Assert.assertNotNull(resolvedRecursiveType.toString());
    }

    @Test
    public void testEqualsWhenSameReference() {
        Assert.assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType));
    }

    @Test
    public void testEqualsWhenNull() {
        Assert.assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEqualsWhenDifferentType() {
        Assert.assertFalse(resolvedRecursiveType.equals(new Object()));
    }

    @Test
    public void testEqualsWhenReferencedTypeNull() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        Assert.assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsWhenDifferentSelfReferencedType() {
        JavaType ref1 = new JavaTypeStub();
        JavaType ref2 = new JavaTypeStub();
        resolvedRecursiveType.setReference(ref1);
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        other.setReference(ref2);
        Assert.assertFalse(resolvedRecursiveType.equals(other));
    }

    @Test
    public void testEqualsWhenEqualSelfReferencedType() {
        JavaType ref = new JavaTypeStub();
        resolvedRecursiveType.setReference(ref);
        ResolvedRecursiveType other = new ResolvedRecursiveType(Object.class, new TypeBindings());
        other.setReference(ref);
        Assert.assertTrue(resolvedRecursiveType.equals(other));
    }

    private static class JavaTypeStub extends JavaType {

        protected JavaTypeStub() {
            super(null, 0, null, null, false, null, null);
        }

        @Override
        public StringBuilder getGenericSignature(StringBuilder sb) {
            return sb.append("GenericSignature");
        }

        @Override
        public StringBuilder getErasedSignature(StringBuilder sb) {
            return sb.append("ErasedSignature");
        }

        @Override
        public Class<?> getRawClass() {
            return Object.class;
        }

        @Override
        public JavaType withContentType(JavaType contentType) {
            return null;
        }
    }
}