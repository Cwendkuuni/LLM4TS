import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

public class JavaTypeTest {

    @Test
    public void testJavaTypeConstructor() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        Assert.assertEquals(String.class, javaType.getRawClass());
        Assert.assertEquals(0, javaType.hashCode());
        Assert.assertNull(javaType._valueHandler);
        Assert.assertNull(javaType._typeHandler);
        Assert.assertFalse(javaType._asStatic);
    }

    @Test
    public void testJavaTypeWithTypeHandler() {
        Class<String> rawClass = String.class;
        Object valueHandler = new Object();
        JavaType javaType = new JavaType(rawClass, 0, valueHandler, null, false).withValueHandler(valueHandler);
        Assert.assertEquals(String.class, javaType.getRawClass());
        Assert.assertEquals(0, javaType.hashCode());
        Assert.assertEquals(valueHandler, javaType.getValueHandler());
        Assert.assertNull(javaType._typeHandler);
        Assert.assertFalse(javaType._asStatic);
    }

    @Test
    public void testJavaTypeWithContentTypeHandler() {
        Class<String> rawClass = String[].class;
        Object typeHandler = new Object();
        JavaType javaType = new JavaType(rawClass, 0, null, typeHandler, false).withTypeHandler(typeHandler);
        Assert.assertEquals(String[].class, javaType.getRawClass());
        Assert.assertEquals(0, javaType.hashCode());
        Assert.assertNull(javaType._valueHandler);
        Assert.assertEquals(typeHandler, javaType.getTypeHandler());
        Assert.assertFalse(javaType._asStatic);
    }

    @Test
    public void testJavaTypeWithStaticTyping() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false).withStaticTyping();
        Assert.assertEquals(String.class, javaType.getRawClass());
        Assert.assertEquals(0, javaType.hashCode());
        Assert.assertFalse(javaType._asStatic);
        Assert.assertTrue(javaType.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeNarrowByInvalidClass() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        javaType.narrowBy(Integer.class);
    }

    @Test
    public void testJavaTypeNarrowByValidClass() {
        Class<String> rawClass = String.class;
        JavaType stringJavaType = new JavaType(rawClass, 0, null, null, false);
        JavaType subclassJavaType = new JavaType(StringBuffer.class, 0, null, null, false)._narrow(String.class);
        JavaType narrowedJavaType = stringJavaType.narrowBy(StringBuffer.class);
        Assert.assertEquals(StringBuffer.class, narrowedJavaType.getRawClass());
        Assert.assertEquals(stringJavaType.hashCode(), narrowedJavaType.hashCode());
        Assert.assertEquals(stringJavaType._valueHandler, narrowedJavaType._valueHandler);
        Assert.assertEquals(stringJavaType._typeHandler, narrowedJavaType._typeHandler);
        Assert.assertEquals(stringJavaType._asStatic, narrowedJavaType._asStatic);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeWidenByInvalidClass() {
        Class<String> rawClass = String.class;
        JavaType javaType = new JavaType(rawClass, 0, null, null, false);
        javaType.widenBy(Integer.class);
    }

    @Test
    public void testJavaTypeWidenByValidClass() {
        Class<String> rawClass = String.class;
        JavaType stringJavaType = new JavaType(rawClass, 0, null, null, false);
        JavaType superclassJavaType = new JavaType(Object.class, 0, null, null, false)._widen(String.class);
        JavaType widenedJavaType = stringJavaType.widenBy(Object.class);
        Assert.assertEquals(Object.class, widenedJavaType.getRawClass());
        Assert.assertEquals(stringJavaType.hashCode(), widenedJavaType.hashCode());
        Assert.assertEquals(stringJavaType._valueHandler, widenedJavaType._valueHandler);
        Assert.assertEquals(stringJavaType._typeHandler, widenedJavaType._typeHandler);
        Assert.assertEquals(stringJavaType._asStatic, widenedJavaType._asStatic);
    }
}