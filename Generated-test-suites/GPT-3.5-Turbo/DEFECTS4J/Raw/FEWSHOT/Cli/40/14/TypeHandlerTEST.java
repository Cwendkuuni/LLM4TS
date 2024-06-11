import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithPatternOptionBuilderStringValue() throws ParseException {
        String str = "test";
        String result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderObjectValue() throws ParseException {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertTrue(result instanceof String);
        assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderNumberValue() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertTrue(result instanceof Long);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderDateValue() throws ParseException {
        String str = "2021-01-01";
        Date result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderClassValue() throws ParseException {
        String str = "java.lang.String";
        Class<?> result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderFileValue() throws ParseException {
        String str = "test.txt";
        File result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertEquals(str, result.getName());
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderExistingFileValue() throws ParseException {
        String str = "test.txt";
        assertThrows(FileNotFoundException.class, () -> {
            TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        });
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderFilesValue() throws ParseException {
        String str = "test.txt";
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        });
    }

    @Test
    public void testCreateValueWithPatternOptionBuilderURLValue() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateValueWithInvalidClass() {
        String str = "InvalidClass";
        assertThrows(ParseException.class, () -> {
            TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        });
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assertTrue(result instanceof String);
        assertEquals(classname, result.getClass().getName());
    }

    @Test
    public void testCreateObjectWithInvalidClass() {
        String classname = "InvalidClass";
        assertThrows(ParseException.class, () -> {
            TypeHandler.createObject(classname);
        });
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        String str = "10";
        Number result = TypeHandler.createNumber(str);
        assertTrue(result instanceof Long);
        assertEquals(10L, result);
    }

    @Test
    public void testCreateNumberWithDecimal() throws ParseException {
        String str = "10.5";
        Number result = TypeHandler.createNumber(str);
        assertTrue(result instanceof Double);
        assertEquals(10.5, result);
    }

    @Test
    public void testCreateNumberWithInvalidNumber() {
        String str = "abc";
        assertThrows(ParseException.class, () -> {
            TypeHandler.createNumber(str);
        });
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateClassWithInvalidClass() {
        String classname = "InvalidClass";
        assertThrows(ParseException.class, () -> {
            TypeHandler.createClass(classname);
        });
    }

    @Test
    public void testCreateDate() {
        String str = "2021-01-01";
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createDate(str);
        });
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals(str, result.toString());
    }

    @Test
    public void testCreateURLWithInvalidURL() {
        String str = "invalid_url";
        assertThrows(ParseException.class, () -> {
            TypeHandler.createURL(str);
        });
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertEquals(str, result.getName());
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "test.txt";
        assertThrows(FileNotFoundException.class, () -> {
            TypeHandler.openFile(str);
        });
    }

    @Test
    public void testCreateFiles() {
        String str = "test.txt";
        assertThrows(UnsupportedOperationException.class, () -> {
            TypeHandler.createFiles(str);
        });
    }

}