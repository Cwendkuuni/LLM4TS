package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new String("foo");
        String str = "foo";
        assertEquals(obj, TypeHandler.createValue(str, obj.getClass()));
    }

    @Test
    public void testCreateValueStringClassStringClass() {
        String str = "java.lang.String";
        Class clazz = String.class;
        Object obj = new String("foo");
        assertEquals(obj, TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String str = "foo";
        assertEquals(str, TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        String str = "java.lang.String";
        assertNull(TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        String str = "123";
        Number number = 123;
        assertEquals(number, TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        String str = "2023-03-31";
        Date date = new Date();
        assertEquals(date, TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String str = "java.lang.Integer";
        Class<?> clazz = Integer.class;
        Object obj = 123;
        assertEquals(obj, TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String str = "./test.txt";
        File file = new File(str);
        assertEquals(file, TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        String str = "/path/to/existing/file.txt";
        File file = new File(str);
        assertEquals(file, TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        String str = "./test1.txt,./test2.txt";
        File[] files = new File[] { new File("./test1.txt"), new File("./test2.txt") };
        assertEquals(files, TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() {
        String str = "http://www.example.com";
        URL url = new URL(str);
        assertEquals(url, TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE));
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValueNullString() {
        TypeHandler.createValue(null, PatternOptionBuilder.STRING_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValueNullObject() {
        String str = "foo";
        TypeHandler.createValue(str, null);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectInvalidClassName() {
        String str = "java.lang.InvalidClass";
        TypeHandler.createObject(str);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObjectNoDefaultConstructor() {
        String className = "java.lang.StringBuffer";
        TypeHandler.createObject(className);
    }

    @Test(expected = IllegalAccessException.class)
    public void testCreateObjectIllegalAccess() {
        String className = "java.lang.String";
        TypeHandler.createObject(className);
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            return className.getClass().getDeclaredConstructor().newInstance();
        });
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumber() {
        Number number = 123;
        assertEquals(number, TypeHandler.createNumber("123"));
        assertEquals(123L, TypeHandler.createNumber("123"));
        assertEquals(123.0, TypeHandler.createNumber("123.0"));
        assertNull(TypeHandler.createNumber("foo"));
    }

    @Test
    public void testCreateDate() {
        Date date = new Date();
        assertEquals(date, TypeHandler.createDate("2023-03-31"));
        assertNull(TypeHandler.createDate("foo"));
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        URL url = new URL(urlString);
        assertEquals(url, TypeHandler.createURL(urlString));
        assertNull(TypeHandler.createURL("foo"));
    }

    @Test
    public void testCreateFile() {
        String filePath = "./test.txt";
        File file = new File(filePath);
        assertEquals(file, TypeHandler.createFile(filePath));
    }

    @Test
    public void testCreateFiles() {
        String filesString = "./test1.txt,./test2.txt";
        File[] files = new File[] { new File("./test1.txt"), new File("./test2.txt") };
        assertEquals(files, TypeHandler.createFiles(filesString));
    }
}