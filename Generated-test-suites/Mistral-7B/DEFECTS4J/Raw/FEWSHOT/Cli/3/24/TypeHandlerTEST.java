package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new String("test");
        String value = "test";
        assertEquals(value, TypeHandler.createValue(value, obj.getClass()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueStringInvalidClass() {
        TypeHandler.createValue("test", int.class);
    }

    @Test
    public void testCreateValueStringString() {
        assertEquals("test", TypeHandler.createValue("test", String.class));
    }

    @Test
    public void testCreateValueStringClassObject() {
        String value = "test";
        Class clazz = String.class;
        Object obj = new String("test");
        assertEquals(obj, TypeHandler.createValue(value, clazz));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String value = "test";
        assertEquals(value, TypeHandler.createValue(value, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        assertNull(TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        String value = "123";
        Number number = 123;
        assertEquals(number, TypeHandler.createValue(value, PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        String value = "2023-02-28";
        Date date = new Date();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertEquals(date, TypeHandler.createValue(value, PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String className = "java.lang.String";
        String value = "test";
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assertEquals(obj, TypeHandler.createValue(value, clazz));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String value = "/path/to/file";
        File file = new File(value);
        assertEquals(file, TypeHandler.createValue(value, PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        String value = "/path/to/existing/file";
        File file = new File(value);
        assertTrue(file.exists());
        assertEquals(file, TypeHandler.createValue(value, PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        String value = "/path/to/file1, /path/to/file2";
        File[] files = new File[]{new File("/path/to/file1"), new File("/path/to/file2")};
        assertEquals(files, TypeHandler.createValue(value, PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() {
        String value = "http://test.com";
        URL url = null;
        try {
            url = new URL(value);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assertEquals(url, TypeHandler.createValue(value, PatternOptionBuilder.URL_VALUE));
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        String value = "test";
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        String newValue = "newTest";
        Object newObj = TypeHandler.createObject(newValue);
        assertNotNull(newObj);
        assertSame(clazz, newObj.getClass());
        assertEquals(newValue, newObj.toString());
    }

    @Test
    public void testCreateNumber() {
        String value = "123";
        Number number = 123;
        assertEquals(number, TypeHandler.createNumber(value));
    }

    @Test
    public void testCreateDate() {
        String value = "2023-02-28";
        Date date = new Date();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date createdDate = TypeHandler.createDate(value);
        assertNotNull(createdDate);
        assertEquals(date, createdDate);
    }

    @Test
    public void testCreateURL() {
        String value = "http://test.com";
        URL url = null;
        try {
            url = new URL(value);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL createdURL = TypeHandler.createURL(value);
        assertNotNull(createdURL);
        assertEquals(url, createdURL);
    }

    @Test
    public void testCreateFile() {
        String value = "/path/to/file";
        File file = new File(value);
        File createdFile = TypeHandler.createFile(value);
        assertNotNull(createdFile);
        assertEquals(file, createdFile);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileNotFound() {
        String value = "/path/to/non_existent_file";
        File createdFile = TypeHandler.createFile(value);
    }

    @Test
    public void testCreateFiles() {
        String value = "/path/to/file1, /path/to/file2";
        File file1 = new File("/path/to/file1");
        File file2 = new File("/path/to/file2");
        File[] files = {file1, file2};
        File[] createdFiles = TypeHandler.createFiles(value);
        assertArrayEquals(files, createdFiles);
    }
}