import com.fasterxml.jackson.databind.JavaType;
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
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        ref.getGenericSignature(expectedResult);
        resolvedRecursiveType.getGenericSignature(sb);
        Assert.assertEquals(expectedResult.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        ref.getErasedSignature(expectedResult);
        resolvedRecursiveType.getErasedSignature(sb);
        Assert.assertEquals(expectedResult.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Deprecated
    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Assert.assertNull(resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        Assert.assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder expectedResult = new StringBuilder();
        if (resolvedRecursiveType.getSelfReferencedType() == null) {
            expectedResult.append("UNRESOLVED");
        } else {
            expectedResult.append(resolvedRecursiveType.getSelfReferencedType().getRawClass().getName());
        }
        resolvedRecursiveType.toString(sb);
        Assert.assertEquals(expectedResult.toString(), sb.toString());
    }

    @Test
    public void testEquals() {
        Object o = new ResolvedRecursiveType(null, null);
        Assert.assertFalse(resolvedRecursiveType.equals(o));
        resolvedRecursiveType.setReference(null);
        Assert.assertFalse(resolvedRecursiveType.equals(o));
        resolvedRecursiveType.setReference(new JavaType());
        Assert.assertFalse(resolvedRecursiveType.equals(o));
        resolvedRecursiveType.setReference(((ResolvedRecursiveType) o).getSelfReferencedType());
        Assert.assertTrue(resolvedRecursiveType.equals(o));
    }
}