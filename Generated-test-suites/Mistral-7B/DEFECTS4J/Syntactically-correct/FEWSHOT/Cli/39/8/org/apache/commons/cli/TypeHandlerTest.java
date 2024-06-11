package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringString() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringString_ParseException() throws ParseException {
        TypeHandler.createValue("foo", PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test
    public void testCreateValue_StringClass() throws ParseException {
        assertEquals(Integer.valueOf(1), TypeHandler.createValue("1", Integer.class));
        assertEquals(Double.valueOf(1.0), TypeHandler.createValue("1.0", Double.class));
        assertEquals(CreateObjectTest.TestClass.class, TypeHandler.createValue(CreateObjectTest.TestClass.class.getName(), CreateObjectTest.TestClass.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringClass_ParseException() throws ParseException {
        TypeHandler.createValue("1", PatternOptionBuilder.DATE_VALUE);
    }

    @Test
    public void testCreateDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = TypeHandler.createDate("2023-03-12");
        assertEquals(sdf.parse("2023-03-12"), date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_String() {
        TypeHandler.createDate("2023-03-12");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL url = TypeHandler.createURL("http://example.com");
        assertNotNull(url);
        assertEquals("http://example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_String_ParseException() throws ParseException {
        TypeHandler.createURL("http://example.com:1234/");
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("path/to/file.txt");
        assertNotNull(file);
        assertEquals("path/to/file.txt", file.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInputStream_String() throws IOException, FileNotFoundException {
        String filePath = "non-existent-file.txt";
        TypeHandler.createFileInputStream(filePath);
    }

    @Test
    public void testCreateFileInputStream_File() throws FileNotFoundException, IOException {
        String filePath = "path/to/file.txt";
        File file = new File(filePath);
        FileInputStream inputStream = TypeHandler.createFileInputStream(file);
        assertNotNull(inputStream);
    }

    @Test(expected = ParseException.class)
    public void testCreateFiles_String() {
        TypeHandler.createFiles("path/to/non-existing-file.txt,path/to/non-existing-file2.txt");
    }

    @Test
    public void testCreateFiles_StringArray() {
        String[] filePaths = {"path/to/file1.txt", "path/to/file2.txt"};
        File[] files = TypeHandler.createFiles(filePaths);
        assertNotNull(files);
        assertEquals(2, files.length);
    }
}

class CreateObjectTest {

    static class TestClass {
    }
}