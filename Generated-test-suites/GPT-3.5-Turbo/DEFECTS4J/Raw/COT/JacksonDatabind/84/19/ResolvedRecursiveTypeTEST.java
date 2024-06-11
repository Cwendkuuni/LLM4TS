import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref = new JavaType();
        type.setReference(ref);
        Assert.assertEquals(ref, type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        JavaType ref1 = new JavaType();
        JavaType ref2 = new JavaType();
        type.setReference(ref1);
        type.setReference(ref2);
    }

    @Test
    public void testGetGenericSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        type.getGenericSignature(sb);
        Assert.assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        type.getErasedSignature(sb);
        Assert.assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testWithContentType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertEquals(type, type.withContentType(null));
    }

    @Test
    public void testWithTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertEquals(type, type.withTypeHandler(null));
    }

    @Test
    public void testWithContentTypeHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertEquals(type, type.withContentTypeHandler(null));
    }

    @Test
    public void testWithValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertEquals(type, type.withValueHandler(null));
    }

    @Test
    public void testWithContentValueHandler() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertEquals(type, type.withContentValueHandler(null));
    }

    @Test
    public void testWithStaticTyping() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertEquals(type, type.withStaticTyping());
    }

    @Test
    public void testNarrow() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertEquals(type, type._narrow(null));
    }

    @Test
    public void testRefine() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertNull(type.refine(null, null, null, null));
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertFalse(type.isContainerType());
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertEquals("[recursive type; UNRESOLVED]", type.toString());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(ResolvedRecursiveType.class, null);
        Assert.assertTrue(type1.equals(type1));
        Assert.assertFalse(type1.equals(null));
        Assert.assertFalse(type1.equals(new Object()));
        Assert.assertFalse(type1.equals(type2));
    }
}