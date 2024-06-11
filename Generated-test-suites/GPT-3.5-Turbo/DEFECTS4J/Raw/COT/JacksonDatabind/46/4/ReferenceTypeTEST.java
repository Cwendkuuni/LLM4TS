import org.junit.Assert;
import org.junit.Test;

public class ReferenceTypeTest {

    @Test
    public void testConstruct() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = ReferenceType.construct(cls, refType, null, null);
        Assert.assertEquals(cls, type.getRawClass());
        Assert.assertEquals(refType, type.getReferencedType());
    }

    @Test
    public void testWithTypeHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        Object typeHandler = new Object();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newType = type.withTypeHandler(typeHandler);
        Assert.assertEquals(cls, newType.getRawClass());
        Assert.assertEquals(refType, newType.getReferencedType());
        Assert.assertEquals(typeHandler, newType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        Object contentTypeHandler = new Object();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newType = type.withContentTypeHandler(contentTypeHandler);
        Assert.assertEquals(cls, newType.getRawClass());
        Assert.assertEquals(refType, newType.getReferencedType());
        Assert.assertEquals(contentTypeHandler, newType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        Object valueHandler = new Object();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newType = type.withValueHandler(valueHandler);
        Assert.assertEquals(cls, newType.getRawClass());
        Assert.assertEquals(refType, newType.getReferencedType());
        Assert.assertEquals(valueHandler, newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        Object contentValueHandler = new Object();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newType = type.withContentValueHandler(contentValueHandler);
        Assert.assertEquals(cls, newType.getRawClass());
        Assert.assertEquals(refType, newType.getReferencedType());
        Assert.assertEquals(contentValueHandler, newType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        ReferenceType newType = type.withStaticTyping();
        Assert.assertEquals(cls, newType.getRawClass());
        Assert.assertEquals(refType, newType.getReferencedType());
        Assert.assertTrue(newType.isJavaLangObject());
    }

    @Test
    public void testGetReferencedType() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        Assert.assertEquals(refType, type.getReferencedType());
    }

    @Test
    public void testIsReferenceType() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        Assert.assertTrue(type.isReferenceType());
    }

    @Test
    public void testContainedTypeCount() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        Assert.assertEquals(1, type.containedTypeCount());
    }

    @Test
    public void testContainedType() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        Assert.assertEquals(refType, type.containedType(0));
        Assert.assertNull(type.containedType(1));
    }

    @Test
    public void testContainedTypeName() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        Assert.assertEquals("T", type.containedTypeName(0));
        Assert.assertNull(type.containedTypeName(1));
    }

    @Test
    public void testGetParameterSource() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        Assert.assertEquals(cls, type.getParameterSource());
    }

    @Test
    public void testGetErasedSignature() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        type.getErasedSignature(sb);
        Assert.assertTrue(sb.toString().startsWith("class"));
    }

    @Test
    public void testGetGenericSignature() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        Assert.assertTrue(sb.toString().startsWith("class"));
        Assert.assertTrue(sb.toString().contains("SimpleType"));
    }

    @Test
    public void testToString() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type = new ReferenceType(cls, refType, null, null, false);
        String str = type.toString();
        Assert.assertTrue(str.startsWith("[reference type, class"));
        Assert.assertTrue(str.contains(cls.getName()));
        Assert.assertTrue(str.contains(refType.toString()));
    }

    @Test
    public void testEquals() {
        Class<?> cls = Object.class;
        JavaType refType = new SimpleType();
        ReferenceType type1 = new ReferenceType(cls, refType, null, null, false);
        ReferenceType type2 = new ReferenceType(cls, refType, null, null, false);
        Assert.assertEquals(type1, type2);
    }
}

