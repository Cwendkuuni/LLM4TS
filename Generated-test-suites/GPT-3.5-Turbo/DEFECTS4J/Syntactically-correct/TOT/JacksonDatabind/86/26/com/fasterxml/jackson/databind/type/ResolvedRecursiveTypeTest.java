import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);

        resolvedRecursiveType.setReference(javaType);

        Assert.assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_multipleTimes() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType1 = new JavaType(Object.class);
        JavaType javaType2 = new JavaType(Integer.class);

        resolvedRecursiveType.setReference(javaType1);
        resolvedRecursiveType.setReference(javaType2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);
        StringBuilder expectedSignature = new StringBuilder();

        StringBuilder genericSignature = resolvedRecursiveType.getGenericSignature(new StringBuilder());

        Assert.assertEquals(expectedSignature, genericSignature);
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);
        StringBuilder expectedSignature = new StringBuilder();

        StringBuilder erasedSignature = resolvedRecursiveType.getErasedSignature(new StringBuilder());

        Assert.assertEquals(expectedSignature, erasedSignature);
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        JavaType newType = resolvedRecursiveType.withContentType(javaType);

        Assert.assertEquals(resolvedRecursiveType, newType);
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        JavaType newType = resolvedRecursiveType.withTypeHandler(null);

        Assert.assertEquals(resolvedRecursiveType, newType);
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        JavaType newType = resolvedRecursiveType.withContentTypeHandler(null);

        Assert.assertEquals(resolvedRecursiveType, newType);
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        JavaType newType = resolvedRecursiveType.withValueHandler(null);

        Assert.assertEquals(resolvedRecursiveType, newType);
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        JavaType newType = resolvedRecursiveType.withContentValueHandler(null);

        Assert.assertEquals(resolvedRecursiveType, newType);
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        JavaType newType = resolvedRecursiveType.withStaticTyping();

        Assert.assertEquals(resolvedRecursiveType, newType);
    }

    @Test
    public void test_narrow() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);
        Class<?> subclass = Object.class;

        JavaType newType = resolvedRecursiveType._narrow(subclass);

        Assert.assertEquals(resolvedRecursiveType, newType);
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);
        Class<?> rawType = Object.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        JavaType newType = resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces);

        Assert.assertNull(newType);
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        boolean isContainerType = resolvedRecursiveType.isContainerType();

        Assert.assertFalse(isContainerType);
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);

        String toString = resolvedRecursiveType.toString();

        Assert.assertNotNull(toString);
    }

    @Test
    public void testEquals_sameObject() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);

        boolean equals = resolvedRecursiveType.equals(resolvedRecursiveType);

        Assert.assertTrue(equals);
    }

    @Test
    public void testEquals_differentClass() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        Object object = new Object();

        boolean equals = resolvedRecursiveType.equals(object);

        Assert.assertFalse(equals);
    }

    @Test
    public void testEquals_nullReferenceType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(String.class, null);

        boolean equals = resolvedRecursiveType.equals(otherType);

        Assert.assertFalse(equals);
    }

    @Test
    public void testEquals_sameReferenceType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType);
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(String.class, null);
        JavaType otherJavaType = new JavaType(Object.class);
        otherType.setReference(otherJavaType);

        boolean equals = resolvedRecursiveType.equals(otherType);

        Assert.assertTrue(equals);
    }

    @Test
    public void testEquals_differentReferenceType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType1 = new JavaType(Object.class);
        resolvedRecursiveType.setReference(javaType1);
        ResolvedRecursiveType otherType = new ResolvedRecursiveType(String.class, null);
        JavaType javaType2 = new JavaType(Integer.class);
        otherType.setReference(javaType2);

        boolean equals = resolvedRecursiveType.equals(otherType);

        Assert.assertFalse(equals);
    }

}