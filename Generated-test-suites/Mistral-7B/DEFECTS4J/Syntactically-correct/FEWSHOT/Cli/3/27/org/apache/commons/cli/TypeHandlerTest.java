package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.ObjectUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_StringObject() {
        Object stringObj = "test";
        String str = "test";
        Object result = TypeHandler.createValue(str, stringObj.getClass());
        assertEquals(stringObj, result);
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "test";
        Class<String> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass_String() {
        String str = "test";
        Class<String> clazz = String.class;
        String expected = "test";
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_StringClass_Number() {
        String str = "123";
        Class<Number> clazz = Number.class;
        Number expected = 123;
        Number result = (Number) TypeHandler.createValue(str, clazz);
        assertEquals(expected.longValue(), result.longValue());
    }

    @Test
    public void testCreateValue_StringClass_Date() {
        String str = "2022-12-31";
        Class<Date> clazz = Date.class;
        Date expected = new Date(2022, 11, 31);
        Date result = (Date) TypeHandler.createValue(str, clazz);
        assertEquals(expected.getTime(), result.getTime());
    }

    @Test
    public void testCreateValue_StringClass_Class() {
        String className = "java.lang.String";
        Class<String> expected = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
        Class<?> result = (Class<?>) TypeHandler.createValue(className, Class.class);
        assertInstanceOf(String.class, result);
        assertEquals(expected.getName(), result.getName());
    }

    @Test
    public void testCreateValue_StringClass_URL() {
        String str = "http://test.com";
        Class<URL> clazz = URL.class;
        URL expected = new URL("http://test.com");
        URL result = (URL) TypeHandler.createValue(str, clazz);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testCreateValue_StringClass_File() {
        String str = "test/file.txt";
        Class<File> clazz = File.class;
        File expected = new File("test/file.txt");
        File result = (File) TypeHandler.createValue(str, clazz);
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testCreateValue_StringClass_FileArray() {
        String str = "test/file1.txt, test/file2.txt";
        Class<File[]> clazz = File[].class;
        File[] expectedFiles = new File[] {new File("test/file1.txt"), new File("test/file2.txt")};
        File[] resultFiles = (File[]) TypeHandler.createValue(str, clazz);
        assertNotNull(resultFiles);
        assertEquals(expectedFiles.length, resultFiles.length);
        for (int i = 0; i < expectedFiles.length; i++) {
            assertEquals(expectedFiles[i].getAbsolutePath(), resultFiles[i].getAbsolutePath());
        }
    }

    @Test
    public void testCreateValue_StringClass_Null() {
        String str = null;
        Class<?> clazz = Integer.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        String str = "new String()";
        Class<String> expected = ClassUtils.forName(className, ClassUtils.getDefaultClassLoader());
        Object result = TypeHandler.createObject(str);
        assertInstanceOf(String.class, result);
        assertEquals(expected.getName(), result.getClass().getName());
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number expected = 123;
        Number result = TypeHandler.createNumber(str);
        assertEquals(expected.longValue(), result.longValue());
    }

    @Test
    public void testCreateDate() {
        String str = "2022-12-31";
        Date expected = new Date(2022, 11, 31);
        Date result = TypeHandler.createDate(str);
        assertEquals(expected.getTime(), result.getTime());
    }

    @Test
    public void testCreateURL() {
        String str = "http://test.com";
        URL expected = new URL("http://test.com");
        URL result = TypeHandler.createURL(str);
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testCreateFile() {
        String str = "test/file.txt";
        File expected = new File("test/file.txt");
        File result = TypeHandler.createFile(str);
        assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateFilesNullString() {
        String str = null;
        Class<File[]> clazz = File[].class;
        TypeHandler.createFiles(str);
    }
}