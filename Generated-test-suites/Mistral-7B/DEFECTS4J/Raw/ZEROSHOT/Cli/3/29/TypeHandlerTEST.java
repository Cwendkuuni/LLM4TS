import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_String() {
        String str = "test";
        Object obj = new String("test");
        Object result = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(obj, result);
    }

    @Test
    public void testCreateValue_String_Class_StringType() {
        String str = "test";
        Class<String> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Class_ObjectType() {
        String str = "com.test.MyClass";
        Class<Object> clazz = Object.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
        Assert.assertTrue(ClassUtils.isAssignable(Class.forName(str), clazz));
    }

    @Test
    public void testCreateValue_String_Class_NumberType() {
        String str = "123";
        Class<Number> clazz = Number.class;
        Number result = (Number) TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
        Assert.assertEquals(123L, result.longValue());
    }

    @Test
    public void testCreateValue_String_Class_DateType() {
        String str = "2022-12-25";
        Class<Date> clazz = Date.class;
        Date result = (Date) TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateValue_String_Class_ClassType() {
        String str = "com.test.MyClass";
        Class<Class<?>> clazz = Class.class;
        Class<?> result = (Class<?>) TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
        Assert.assertEquals(Class.forName(str), result);
    }

    @Test
    public void testCreateValue_String_Class_FileType() {
        String str = "path/to/file.txt";
        Class<File> clazz = File.class;
        File result = (File) TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
        Assert.assertTrue(new File(str).exists());
    }

    @Test
    public void testCreateValue_String_Class_URLType() {
        String str = "http://example.com";
        Class<URL> clazz = URL.class;
        URL result = (URL) TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_String_Class_InvalidType() {
        String str = "test";
        Class<Integer> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }
}