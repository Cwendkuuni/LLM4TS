import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ReferenceType;
import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testWithContentType() {
        JavaType refType = new ReferenceType(String.class);
        JavaType contentType = new ReferenceType(Integer.class);
        JavaType result = refType.withContentType(contentType);
        Assert.assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType refType = new ReferenceType(String.class);
        Object handler = new Object();
        JavaType result = refType.withTypeHandler(handler);
        Assert.assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType refType = new ReferenceType(String.class);
        Object handler = new Object();
        JavaType result = refType.withContentTypeHandler(handler);
        Assert.assertEquals(handler, result.getContentType().getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType refType = new ReferenceType(String.class);
        Object handler = new Object();
        JavaType result = refType.withValueHandler(handler);
        Assert.assertEquals(handler, result.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType refType = new ReferenceType(String.class);
        Object handler = new Object();
        JavaType result = refType.withContentValueHandler(handler);
        Assert.assertEquals(handler, result.getContentType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType refType = new ReferenceType(String.class);
        JavaType result = refType.withStaticTyping();
        Assert.assertTrue(result.isStaticTyping());
    }

    @Test
    public void testRefine() {
        JavaType refType = new ReferenceType(String.class);
        JavaType superClass = new ReferenceType(Number.class);
        JavaType[] superInterfaces = {new ReferenceType(CharSequence.class)};
        JavaType result = refType.refine(Integer.class, null, superClass, superInterfaces);
        Assert.assertEquals(Integer.class, result.getRawClass());
        Assert.assertEquals(superClass, result.getSuperClass());
        Assert.assertArrayEquals(superInterfaces, result.getInterfaces());
    }

    @Test
    public void testGetContentType() {
        JavaType refType1 = new ReferenceType(String.class);
        Assert.assertEquals(String.class, refType1.getContentType().getRawClass());

        JavaType refType2 = new ReferenceType(Integer.class);
        Assert.assertEquals(Integer.class, refType2.getContentType().getRawClass());
    }

    @Test
    public void testGetReferencedType() {
        JavaType refType1 = new ReferenceType(String.class);
        Assert.assertEquals(String.class, refType1.getReferencedType().getRawClass());

        JavaType refType2 = new ReferenceType(Integer.class);
        Assert.assertEquals(Integer.class, refType2.getReferencedType().getRawClass());
    }

    @Test
    public void testHasContentType() {
        JavaType refType1 = new ReferenceType(String.class);
        Assert.assertTrue(refType1.hasContentType());

        JavaType refType2 = new ReferenceType(Integer.class);
        Assert.assertTrue(refType2.hasContentType());
    }

    @Test
    public void testIsReferenceType() {
        JavaType refType1 = new ReferenceType(String.class);
        Assert.assertTrue(refType1.isReferenceType());

        JavaType refType2 = new ReferenceType(Integer.class);
        Assert.assertTrue(refType2.isReferenceType());
    }
}