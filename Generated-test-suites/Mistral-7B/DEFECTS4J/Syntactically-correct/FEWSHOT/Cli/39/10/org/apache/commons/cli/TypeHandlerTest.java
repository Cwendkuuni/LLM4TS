package org.apache.commons.cli;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_STRING_VALUE() {
        String str = "test";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertTrue(obj instanceof String);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_OBJECT_VALUE() {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createValue("test", TypeHandler.class.getClassLoader().loadClass(classname));
        assertTrue(obj instanceof String);
        assertEquals("test", obj);
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_NUMBER_VALUE() {
        String str = "42";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(obj instanceof Number);
        assertEquals(Long.parseLong(str), obj);
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_NUMBER_VALUE_Double() {
        String str = "3.14";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(obj instanceof Number);
        assertEquals(Double.parseDouble(str), obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_StringAndPatternOptionBuilder_NUMBER_VALUE_Invalid() {
        String str = "abc";
        TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_DATE_VALUE() {
        String str = "2023-03-14";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertTrue(obj instanceof Date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_StringAndPatternOptionBuilder_DATE_VALUE_Unsupported() {
        String str = "2023-03-14T12:34:56.789Z"; // ISO-8601 with time and timezone
        TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_CLASS_VALUE() {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createValue("test", Class.forName(classname));
        assertTrue(obj instanceof String);
        assertEquals("test", obj);
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_FILE_VALUE() {
        String filePath = "test.txt";
        Object obj = TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE);
        assertTrue(obj instanceof File);
        File expectedFile = new File(filePath);
        assertEquals(expectedFile.getAbsolutePath(), obj.toString());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_StringAndPatternOptionBuilder_FILE_VALUE_FileNotFound() {
        String filePath = "/nonexistentfile.txt";
        TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE);
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_EXISTING_FILE_VALUE() {
        String filePath = "test.txt";
        File expectedFile = new File(filePath);
        Object obj = TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertTrue(obj instanceof File);
        assertEquals(expectedFile.getAbsolutePath(), obj.toString());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_StringAndPatternOptionBuilder_EXISTING_FILE_VALUE_FileNotFound() {
        String filePath = "/nonexistentfile.txt";
        TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE);
    }

    @Test
    public void testCreateValue_StringAndPatternOptionBuilder_URL_VALUE() {
        String urlString = "http://example.com";
        Object obj = TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE);
        assertTrue(obj instanceof URL);
        URL expectedURL = new URL(urlString);
        assertEquals(expectedURL.toString(), obj.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValue_StringAndPatternOptionBuilder_URL_VALUE_Malformed() {
        String urlString = "http://example.com/test?invalid";
        TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE);
    }

    @Test
    public void testCreateFileInputStream() throws IOException {
        String filePath = "test.txt";
        File file = new File(filePath);
        byte[] expectedContent = "test content".getBytes();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(expectedContent);
        fileWriter.close();

        Object obj = TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE);
        FileInputStream fileInputStream = (FileInputStream) obj;
        byte[] content = new byte[file.length()];
        fileInputStream.read(content);
        fileInputStream.close();

        assertArrayEquals(expectedContent, content);
    }

    @Test
    public void testCreateFiles() {
        String path = "test";
        File directory = new File(path);
        directory.mkdirs();

        File file1 = new File(directory, "file1.txt");
        File file2 = new File(directory, "file2.txt");

        Object obj = TypeHandler.createValue(path, PatternOptionBuilder.FILES_VALUE);
        File[] files = (File[]) obj;

        assertTrue(files.length == 2);
        assertTrue(files[0].isFile());
        assertTrue(files[1].isFile());
        assertEquals(file1.getName(), files[0].getName());
        assertEquals(file2.getName(), files[1].getName());

        directory.delete();
    }
}