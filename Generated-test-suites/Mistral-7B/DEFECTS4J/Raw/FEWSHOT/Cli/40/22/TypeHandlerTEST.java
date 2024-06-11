package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_String() {
        String str = "foo";
        assertEquals("foo", TypeHandler.createValue(str, String.class));
    }

    @Test
    public void testCreateValue_String_Class_String() {
        String str = "3.14";
        Double expected = 3.14;
        try {
            Double actual = (Double) TypeHandler.createValue(str, Double.class);
            assertEquals(expected, actual);
        } catch (ParseException e) {
            // Should not reach here
        }

        Long expectedLong = 3;
        try {
            Long actual = (Long) TypeHandler.createValue(str, Long.class);
            assertEquals(expectedLong, actual);
        } catch (ParseException e) {
            // Should not reach here
        }

        try {
            Integer actual = (Integer) TypeHandler.createValue(str, Integer.class);
            assertNull(actual);
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testCreateObject_String() throws ParseException {
        String classname = "java.util.Date";
        Class<?> expected = Class.forName(classname);

        Date actual = (Date) TypeHandler.createObject(classname);
        assertInstanceOf(Date.class, actual);
        assertEquals(expected, actual.getClass());
    }

    @Test
    public void testCreateNumber_String() throws ParseException {
        String str = "3.14";
        Double expected = 3.14;

        Double actual = (Double) TypeHandler.createNumber(str);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateClass_String() throws ParseException {
        String classname = "java.util.Date";
        Class<?> expected = Class.forName(classname);

        Class<?> actual = TypeHandler.createClass(classname);
        assertInstanceOf(Class.class, actual);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateDate_String() throws ParseException {
        String dateString = "2022-12-31";
        Date expected = new Date(0);
        expected.setYear(2022);
        expected.setMonth(11);
        expected.setDate(31);

        Date actual = TypeHandler.createDate(dateString);
        assertEquals(expected.getTime(), actual.getTime());
    }

    @Test
    public void testCreateURL_String() throws ParseException {
        String urlString = "http://example.com";
        URL expected = new URL(urlString);

        URL actual = TypeHandler.createURL(urlString);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testCreateFile_String() {
        String filePath = "test.txt";
        File expected = new File(filePath);

        File actual = TypeHandler.createFile(filePath);
        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    @Test
    public void testOpenFile_String() throws ParseException, IOException {
        String filePath = "test.txt";
        File file = new File(filePath);
        FileInputStream expected = new FileInputStream(file);

        FileInputStream actual = TypeHandler.openFile(filePath);
        assertInstanceOf(FileInputStream.class, actual);
        assertEquals(expected.available(), actual.available());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_String() throws ParseException {
        String filePaths = "test1.txt, test2.txt";
        File[] expected = new File[] { new File("test1.txt"), new File("test2.txt") };

        // TypeHandler.createFiles(filePaths) throws an UnsupportedOperationException
    }
}