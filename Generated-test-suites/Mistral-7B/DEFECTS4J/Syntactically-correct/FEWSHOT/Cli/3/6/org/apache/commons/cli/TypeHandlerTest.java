package org.apache.commons.cli;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test
    public void testCreateValueStringClass() {
        assertEquals("foo", TypeHandler.createValue("foo", String[].class)[0]);
    }

    @Test
    public void testCreateValueStringClassForName() {
        Class clazz = null;
        try {
            clazz = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        assertNotNull(clazz);
        String str = "foo";
        assertEquals(str, TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateValueStringClassNull() {
        assertNull(TypeHandler.createValue("foo", null));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        try {
            Number number = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
            assertEquals(123, number.intValue());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            fail("NumberFormatException expected when creating Number from non-numeric String.");
        }

        try {
            Number number = TypeHandler.createValue("123.45", PatternOptionBuilder.NUMBER_VALUE);
            assertEquals(123.45d, number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            fail("NumberFormatException expected when creating Number from non-numeric String.");
        }

        try {
            Number number = TypeHandler.createValue("123a", PatternOptionBuilder.NUMBER_VALUE);
            assertNull(number);
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        try {
            Class<?> clazz = ClassUtils.forName("java.util.Date", null);
            Date date = (Date) TypeHandler.createValue("2022-12-31", clazz);
            assertNotNull(date);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException expected when trying to load Date class.");
        }

        try {
            Class<?> clazz = ClassUtils.forName("java.io.File", null);
            File file = (File) TypeHandler.createValue("/path/to/file.txt", clazz);
            assertNotNull(file);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException expected when trying to load File class.");
        }

        try {
            Class<?> clazz = ClassUtils.forName("java.lang.String", null);
            String[] strings = (String[]) TypeHandler.createValue("foo bar baz", clazz.getComponentType());
            assertEquals(3, strings.length);
            assertEquals("foo", strings[0]);
            assertEquals("bar", strings[1]);
            assertEquals("baz", strings[2]);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail("ClassNotFoundException expected when trying to load String class.");
        }
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        File file = new File("path/to/file.txt");
        File expectedFile = new File("path/to/file.txt");
        assertEquals(expectedFile, TypeHandler.createValue("path/to/file.txt", PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        File file = new File("path/to/file.txt");
        File expectedFile = new File("path/to/file.txt");
        assertEquals(expectedFile, TypeHandler.createValue("path/to/existing/file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        File[] files = new File[] { new File("path/to/file1.txt"), new File("path/to/file2.txt") };
        File[] expectedFiles = new File[] { new File("path/to/file1.txt"), new File("path/to/file2.txt") };
        assertEquals(expectedFiles, TypeHandler.createValues("path/to/file1.txt,path/to/file2.txt", PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderUrlValue() {
        URL url = new URL("http://example.com/");
        URL expectedUrl = new URL("http://example.com/");
        assertEquals(expectedUrl, TypeHandler.createValue("http://example.com/", PatternOptionBuilder.URL_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderUnknownValue() {
        assertNull(TypeHandler.createValue("foo", PatternOptionBuilder.FILE_VALUE));
    }
}