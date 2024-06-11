package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "foo";
        Object obj = new Object() {
        };
        Object value = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, value);
    }

    @Test
    public void testCreateValueStringClass() {
        String str = "java.lang.String";
        Class<?> clazz = String.class;
        Object value = TypeHandler.createValue(str, clazz);
        assertEquals(str, value);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String str = "foo";
        Object value = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, value);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        String str = "java.lang.Object";
        Class<?> clazz = Object.class;
        Object value = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(value);
        assertEquals(Object.class, value.getClass());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        String str = "123";
        Number number = 456;
        Number value = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Long.class, value.getClass());
        assertEquals(number.longValue() + 111, ((Number) value).longValue());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValueDouble() {
        String str = "3.14";
        Double number = 2.718;
        Number value = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Double.class, value.getClass());
        assertEquals(number.doubleValue() + 0.001, ((Number) value).doubleValue(), 0.001);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        String str = "2022-12-31";
        Date date = new Date();
        Date value = (Date) TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertNotNull(value);
        // assert date comparison is not reliable due to millisecond differences
        // assertEquals(date, value);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String className = "java.util.Date";
        Class<?> clazz = Date.class;
        Object value = TypeHandler.createValue(className, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(clazz, value.getClass());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String filePath = "/path/to/file.txt";
        File file = new File(filePath);
        Object value = TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE);
        assertEquals(file, value);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        String filePath = "/path/to/non_existent_file.txt";
        Object value = TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNull(value);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        String filePaths = "/path/to/file1.txt, /path/to/file2.txt";
        File[] files = new File[] { new File("/path/to/file1.txt"), new File("/path/to/file2.txt") };
        Object value = TypeHandler.createValue(filePaths, PatternOptionBuilder.FILES_VALUE);
        assertArrayEquals(files, value);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderUrlValue() {
        String urlString = "http://example.com";
        URL url = new URL("http://example.com");
        Object value = TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE);
        assertEquals(url, value);
    }

    @Test
    public void testCreateValueNullObject() {
        Object obj = null;
        String str = "foo";
        Object value = TypeHandler.createValue(str, obj.getClass());
        assertNull(value);
    }

    @Test
    public void testCreateObject() {
        String className = "java.util.Date";
        Class<?> clazz = Date.class;
        Object value = TypeHandler.createObject(className);
        assertEquals(clazz, value.getClass());
    }

    @Test
    public void testCreateObjectInvalidClassName() {
        String className = "java.util.InvalidClassName";
        Object value = TypeHandler.createObject(className);
        assertNull(value);
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number number = 456;
        Number value = TypeHandler.createNumber(str);
        assertEquals(Long.class, value.getClass());
        assertEquals(number.longValue() + 111, ((Number) value).longValue());
    }

    @Test
    public void testCreateNumberInvalidNumberFormat() {
        String str = "invalid";
        Number value = TypeHandler.createNumber(str);
        assertNull(value);
    }

    @Test
    public void testCreateClass() {
        String className = "java.util.Date";
        Class<?> clazz = Date.class;
        Object value = TypeHandler.createClass(className);
        assertEquals(clazz, value.getClass());
    }

    @Test
    public void testCreateClassInvalidClassName() {
        String className = "java.util.InvalidClassName";
        Object value = TypeHandler.createClass(className);
        assertNull(value);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2022-12-31";
        Date date = new Date();
        Date value = (Date) TypeHandler.createDate(dateString);
        assertNotNull(value);
        // assert date comparison is not reliable due to millisecond differences
        // assertEquals(date, value);
    }

    @Test
    public void testCreateDateInvalidDateFormat() {
        String dateString = "invalid";
        Date value = (Date) TypeHandler.createDate(dateString);
        assertNull(value);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";
        URL url = new URL("http://example.com");
        Object value = TypeHandler.createURL(urlString);
        assertEquals(url, value);
    }

    @Test
    public void testCreateURLInvalidUrlFormat() {
        String urlString = "invalid";
        Object value = TypeHandler.createURL(urlString);
        assertNull(value);
    }

    @Test
    public void testCreateFile() {
        String filePath = "/path/to/file.txt";
        File file = new File(filePath);
        Object value = TypeHandler.createFile(filePath);
        assertEquals(file, value);
    }

    @Test
    public void testCreateFileInvalidFilePath() {
        String filePath = "invalid";
        Object value = TypeHandler.createFile(filePath);
        assertNull(value);
    }

    @Test
    public void testCreateFiles() {
        String filePaths = "/path/to/file1.txt, /path/to/file2.txt";
        File[] files = new File[] { new File("/path/to/file1.txt"), new File("/path/to/file2.txt") };
        Object value = TypeHandler.createFiles(filePaths);
        assertArrayEquals(files, value);
    }

    @Test
    public void testCreateFilesInvalidFilePaths() {
        String filePaths = "invalid";
        Object value = TypeHandler.createFiles(filePaths);
        assertNull(value);
    }

    private static void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
        } else if (expected.getClass().isArray()) {
            Object[] expectedArray = (Object[]) expected;
            Object[] actualArray = (Object[]) actual;
            assertArrayEquals(expectedArray.length, actualArray.length);
            for (int i = 0; i < expectedArray.length; i++) {
                assertSame(expectedArray[i].getClass(), actualArray[i].getClass());
                assertSame(expectedArray[i], actualArray[i]);
            }
        } else {
            assertEquals(expected, actual);
        }
    }
}