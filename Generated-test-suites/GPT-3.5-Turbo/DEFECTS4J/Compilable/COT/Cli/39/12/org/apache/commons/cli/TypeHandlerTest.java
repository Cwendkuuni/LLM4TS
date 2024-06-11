package org.apache.commons.cli;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assert(result instanceof String);
        assert(result.equals("test"));
    }

    @Test
    public void testCreateValueWithStringClass() throws ParseException {
        Object result = TypeHandler.createValue("test", String.class);
        assert(result instanceof String);
        assert(result.equals("test"));
    }

    @Test
    public void testCreateValueWithObjectValue() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assert(result instanceof Object);
        assert(result.equals(TypeHandler.createObject("test")));
    }

    @Test
    public void testCreateValueWithNumberValue() throws ParseException {
        Object result = TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE);
        assert(result instanceof Number);
        assert(result.equals(TypeHandler.createNumber("123")));
    }

    @Test
    public void testCreateValueWithDateValue() throws ParseException {
        Object result = TypeHandler.createValue("2022-01-01", PatternOptionBuilder.DATE_VALUE);
        assert(result instanceof Date);
        assert(result.equals(TypeHandler.createDate("2022-01-01")));
    }

    @Test
    public void testCreateValueWithClassValue() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assert(result instanceof Class);
        assert(result.equals(TypeHandler.createClass("java.lang.String")));
    }

    @Test
    public void testCreateValueWithFileValue() throws ParseException {
        Object result = TypeHandler.createValue("/path/to/file", PatternOptionBuilder.FILE_VALUE);
        assert(result instanceof File);
        assert(result.equals(TypeHandler.createFile("/path/to/file")));
    }

    @Test
    public void testCreateValueWithExistingFileValue() throws ParseException {
        Object result = TypeHandler.createValue("/path/to/file", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assert(result instanceof File);
        assert(result.equals(TypeHandler.createFile("/path/to/file")));
    }

    @Test
    public void testCreateValueWithFilesValue() throws ParseException {
        Object result = TypeHandler.createValue("/path/to/files", PatternOptionBuilder.FILES_VALUE);
        assert(result instanceof File[]);
        assert(result.equals(TypeHandler.createFiles("/path/to/files")));
    }

    @Test
    public void testCreateValueWithURLValue() throws ParseException {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assert(result instanceof URL);
        assert(result.equals(TypeHandler.createURL("http://www.example.com")));
    }
}