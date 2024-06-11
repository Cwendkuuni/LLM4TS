import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {
    
    @Test
    public void testCreateValueWithStringObject() throws ParseException {
        Object value = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", value);
    }
    
    @Test
    public void testCreateValueWithObjectValue() throws ParseException {
        Object value = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        Object expected = TypeHandler.createObject("test");
        assertEquals(expected, value);
    }
    
    @Test
    public void testCreateValueWithNumberValue() throws ParseException {
        Object value = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        Number expected = TypeHandler.createNumber("10");
        assertEquals(expected, value);
    }
    
    @Test
    public void testCreateValueWithDateValue() throws ParseException {
        Object value = TypeHandler.createValue("2022-10-01", PatternOptionBuilder.DATE_VALUE);
        Date expected = TypeHandler.createDate("2022-10-01");
        assertEquals(expected, value);
    }
    
    @Test
    public void testCreateValueWithClassValue() throws ParseException {
        Object value = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        Class<?> expected = TypeHandler.createClass("java.lang.String");
        assertEquals(expected, value);
    }
    
    @Test
    public void testCreateValueWithFileValue() throws ParseException {
        Object value = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        File expected = TypeHandler.createFile("test.txt");
        assertEquals(expected, value);
    }
    
    @Test
    public void testCreateValueWithExistingFileValue() throws ParseException {
        Object value = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        FileInputStream expected = TypeHandler.openFile("test.txt");
        assertEquals(expected, value);
    }
    
    @Test
    public void testCreateValueWithFilesValue() throws ParseException {
        Object value = TypeHandler.createValue("test.txt,test2.txt", PatternOptionBuilder.FILES_VALUE);
        File[] expected = TypeHandler.createFiles("test.txt,test2.txt");
        assertEquals(expected, value);
    }
    
    @Test
    public void testCreateValueWithURLValue() throws ParseException {
        Object value = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        URL expected = TypeHandler.createURL("http://www.example.com");
        assertEquals(expected, value);
    }
    
    @Test
    public void testCreateValueWithInvalidValue() throws ParseException {
        Object value = TypeHandler.createValue("invalid", null);
        assertNull(value);
    }
    
    @Test
    public void testCreateObject() throws ParseException {
        Object value = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, value.getClass());
    }
    
    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassname() throws ParseException {
        TypeHandler.createObject("InvalidClass");
    }
    
    @Test
    public void testCreateNumberWithIntegerValue() throws ParseException {
        Number value = TypeHandler.createNumber("10");
        assertEquals(Long.valueOf(10), value);
    }
    
    @Test
    public void testCreateNumberWithDecimalValue() throws ParseException {
        Number value = TypeHandler.createNumber("10.5");
        assertEquals(Double.valueOf(10.5), value);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidValue() throws ParseException {
        TypeHandler.createNumber("invalid");
    }
    
    @Test
    public void testCreateClass() throws ParseException {
        Class<?> value = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, value);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassname() throws ParseException {
        TypeHandler.createClass("InvalidClass");
    }
    
    @Test
    public void testCreateDate() {
        Date value = TypeHandler.createDate("2022-10-01");
        assertNotNull(value);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateAlwaysThrowsException() {
        TypeHandler.createDate("2022-10-01");
    }
    
    @Test
    public void testCreateURL() throws ParseException {
        URL value = TypeHandler.createURL("http://www.example.com");
        assertNotNull(value);
    }
    
    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        TypeHandler.createURL("invalid_url");
    }
    
    @Test
    public void testCreateFile() {
        File value = TypeHandler.createFile("test.txt");
        assertNotNull(value);
    }
    
    @Test
    public void testOpenFile() throws ParseException {
        FileInputStream value = TypeHandler.openFile("test.txt");
        assertNotNull(value);
    }
    
    @Test(expected = ParseException.class)
    public void testOpenFileWithNonExistingFile() throws ParseException {
        TypeHandler.openFile("non_existing_file.txt");
    }
    
    @Test
    public void testCreateFiles() {
        File[] value = TypeHandler.createFiles("test.txt,test2.txt");
        assertNotNull(value);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFilesAlwaysThrowsException() {
        TypeHandler.createFiles("test.txt,test2.txt");
    }
}