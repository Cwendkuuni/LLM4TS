import org.junit.Assert;
import org.junit.Test;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType ref = new JavaType(null, null, null);
        type.setReference(ref);
        Assert.assertEquals(ref, type.getSelfReferencedType());
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(type.getReference().getGenericSignature(sb), type.getGenericSignature(sb));
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        Assert.assertEquals(type.getReference().getErasedSignature(sb), type.getErasedSignature(sb));
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType contentType = new JavaType(null, null, null);
        Assert.assertEquals(type, type.withContentType(contentType));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        Assert.assertEquals(type, type.withTypeHandler(handler));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        Assert.assertEquals(type, type.withContentTypeHandler(handler));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        Assert.assertEquals(type, type.withValueHandler(handler));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Object handler = new Object();
        Assert.assertEquals(type, type.withContentValueHandler(handler));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Assert.assertEquals(type, type.withStaticTyping());
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        JavaType type1 = new JavaType(null, null, null);
        JavaType type2 = new JavaType(null, null, null);
        JavaType[] interfaces = {type1, type2};
        Assert.assertNull(type.refine(null, null, type1, interfaces));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Assert.assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        StringBuilder sb = new StringBuilder();
        if (type.getSelfReferencedType() == null) {
            Assert.assertEquals("[recursive type; UNRESOLVED]", type.toString());
        } else {
            sb.append(type.getSelfReferencedType().getRawClass().getName());
            Assert.assertEquals("[recursive type; " + sb.toString() + "]", type.toString());
        }
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(null, null);
        Assert.assertTrue(type.equals(type));
        Assert.assertFalse(type.equals(null));
        Assert.assertFalse(type.equals(new Object()));
        JavaType ref = new JavaType(null, null, null);
        Assert.assertFalse(type.equals(new ResolvedRecursiveType(null, null)));
        ResolvedRecursiveType sameType = new ResolvedRecursiveType(null, null);
        sameType.setReference(ref);
        Assert.assertTrue(type.equals(sameType));
    }
}