import com.fasterxml.jackson.databind.type.JavaType;
import com.fasterxml.jackson.databind.type.TypeBindings;
import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        JavaType ref = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        resolvedRecursiveType.setReference(ref);
        Assert.assertEquals(ref, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getGenericSignature(sb);
        Assert.assertEquals(sb.toString(), ref.getGenericSignature(sb).toString());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        JavaType ref = null;
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.getErasedSignature(sb);
        Assert.assertEquals(sb.toString(), ref.getErasedSignature(sb).toString());
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(h));
    }

    @Test
    public void testWithContentTypeHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(h));
    }

    @Test
    public void testWithValueHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(h));
    }

    @Test
    public void testWithContentValueHandler() {
        Object h = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(h));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test
    public void test_narrow() {
        Class<?> subclass = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(resolvedRecursiveType, resolvedRecursiveType._narrow(subclass));
    }

    @Test
    public void testRefine() {
        Class<?> rawType = null;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(null, resolvedRecursiveType.refine(rawType, bindings, superClass, superInterfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertFalse(resolvedRecursiveType.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder(40)
                .append("[recursive type; UNRESOLVED");
        Assert.assertEquals(sb.toString(), resolvedRecursiveType.toString());
    }

    @Test
    public void testEquals() {
        Object o = null;
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(null, null);
        Assert.assertFalse(resolvedRecursiveType.equals(o));
    }
}