package org.apache.commons.cli.test;

import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = String.class;

        Object result = TypeHandler.createValue(str, obj);

        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        String str = className;

        Object result = TypeHandler.createObject(str);

        Assert.assertNotNull(result);
        Assert.assertEquals(className, result.getClass().getName());
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);

        Assert.assertNotNull(result);
        Assert.assertEquals(123, result.intValue());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValue_NullObject() {
        String str = "test";
        Object obj = null;

        TypeHandler.createValue(str, obj);
    }

    @Test(expected = ClassCastException.class)
    public void testCreateValue_UnsupportedClass() {
        String str = "java.util.Date";
        Object obj = Integer.class;

        TypeHandler.createValue(str, obj);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL() {
        String urlString = "http://test.com/test";
        Class clazz = URL.class;

        TypeHandler.createValue(urlString, clazz);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile() {
        String filePath = "non-existent-file.txt";
        Class clazz = File.class;

        TypeHandler.createValue(filePath, clazz);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_InvalidNumber() {
        String str = "invalid-number";
        Number result = TypeHandler.createNumber(str);

        Assert.assertNull(result);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_InvalidClass() {
        String className = "InvalidClass";
        Class result = TypeHandler.createClass(className);

        Assert.assertNull(result);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_InvalidClass() {
        String className = "java.util.Stack";
        String str = className;

        Object result = TypeHandler.createObject(str);

        Assert.assertNull(result);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCreateObject_InvalidClass() {
        String className = "java.util.Stack";
        String str = className;

        Object result = TypeHandler.createObject(str);

        Assert.assertNull(result);
    }

}