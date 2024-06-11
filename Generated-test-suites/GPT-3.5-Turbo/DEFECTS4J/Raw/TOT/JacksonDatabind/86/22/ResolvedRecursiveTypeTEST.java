import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    private ResolvedRecursiveType resolvedRecursiveType;

    @Before
    public void setup() {
        resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
    }

    @Test
    public void testSetReference() {
        JavaType reference = new JavaType(String.class);
        resolvedRecursiveType.setReference(reference);

        JavaType actualReference = resolvedRecursiveType.getSelfReferencedType();

        Assert.assertEquals(reference, actualReference);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwiceThrowsException() {
        JavaType reference = new JavaType(String.class);
        resolvedRecursiveType.setReference(reference);

        // Try setting reference again
        resolvedRecursiveType.setReference(reference);
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.setReference(new JavaType(String.class));

        StringBuilder actual = resolvedRecursiveType.getGenericSignature(sb);

        Assert.assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        resolvedRecursiveType.setReference(new JavaType(String.class));

        StringBuilder actual = resolvedRecursiveType.getErasedSignature(sb);

        Assert.assertEquals(expected.toString(), actual.toString());
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
    @Deprecated
    public void testNarrow() {
        Class<?> subclass = String.class;
        JavaType result = resolvedRecursiveType._narrow(subclass);

        Assert.assertEquals(resolvedRecursiveType, result);
    }

    @Test
    public void testRefine() {
        Class<?> rawType = String.class;
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
    public void testToStringWithResolvedReference() {
        resolvedRecursiveType.setReference(new JavaType(String.class));
        String expected = "[recursive type; java.lang.String]";

        String actual = resolvedRecursiveType.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithUnresolvedReference() {
        String expected = "[recursive type; UNRESOLVED]";

        String actual = resolvedRecursiveType.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        JavaType reference = new JavaType(String.class);
        resolvedRecursiveType.setReference(reference);
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, null);
        other.setReference(reference);

        boolean result = resolvedRecursiveType.equals(other);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWithDifferentClassObjects() {
        boolean result = resolvedRecursiveType.equals(new Object());

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        boolean result = resolvedRecursiveType.equals(null);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithUnresolvedReference() {
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, null);

        boolean result = resolvedRecursiveType.equals(other);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentReferenceObjects() {
        JavaType reference1 = new JavaType(String.class);
        JavaType reference2 = new JavaType(Integer.class);
        resolvedRecursiveType.setReference(reference1);
        ResolvedRecursiveType other = new ResolvedRecursiveType(String.class, null);
        other.setReference(reference2);

        boolean result = resolvedRecursiveType.equals(other);

        Assert.assertFalse(result);
    }

}