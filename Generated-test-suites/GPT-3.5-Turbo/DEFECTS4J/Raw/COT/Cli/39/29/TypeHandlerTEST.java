import org.junit.Test;
import org.apache.commons.cli.*;
import java.text.ParseException;
import java.net.MalformedURLException;
import java.io.File;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringArgument() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assert(result instanceof String);
        assert(result.equals("test"));
    }

    @Test
    public void testCreateValueWithObjectArgument() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.Object", PatternOptionBuilder.OBJECT_VALUE);
        assert(result instanceof Object);
    }

    @Test
    public void testCreateValueWithNumberArgument() throws ParseException {
        Object result = TypeHandler.createValue("12.34", PatternOptionBuilder.NUMBER_VALUE);
        assert(result instanceof Number);
        assert(result.equals(12.34));
    }

    @Test
    public void testCreateValueWithDateArgument() throws ParseException {
        Object result = TypeHandler.createValue("01/01/2022", PatternOptionBuilder.DATE_VALUE);
        assert(result instanceof Date);
    }

    @Test
    public void testCreateValueWithClassArgument() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assert(result instanceof Class);
        assert(result.equals(String.class));
    }

    @Test
    public void testCreateValueWithFileArgument() throws ParseException {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.FILE_VALUE);
        assert(result instanceof File);
    }

    @Test
    public void testCreateValueWithExistingFileArgument() throws ParseException {
        Object result = TypeHandler.createValue("test.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assert(result instanceof File);
    }

    @Test
    public void testCreateValueWithFilesArgument() throws ParseException {
        Object result = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        assert(result instanceof File[]);
    }

    @Test
    public void testCreateValueWithURLArgument() throws ParseException {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assert(result instanceof URL);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidClassArgument() throws ParseException {
        TypeHandler.createValue("invalid.class", PatternOptionBuilder.CLASS_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidURLArgument() throws ParseException {
        TypeHandler.createValue("invalid.url", PatternOptionBuilder.URL_VALUE);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assert(result instanceof String);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectWithInvalidClassName() throws ParseException {
        TypeHandler.createObject("invalid.class");
    }

    @Test
    public void testCreateNumberWithInteger() throws ParseException {
        Object result = TypeHandler.createNumber("123");
        assert(result instanceof Long);
        assert(result.equals(123L));
    }

    @Test
    public void testCreateNumberWithDecimal() throws ParseException {
        Object result = TypeHandler.createNumber("12.34");
        assert(result instanceof Double);
        assert(result.equals(12.34));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberWithInvalidFormat() throws ParseException {
        TypeHandler.createNumber("invalid.number");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Object result = TypeHandler.createClass("java.util.List");
        assert(result instanceof Class);
        assert(result.equals(java.util.List.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateClassWithInvalidClassName() throws ParseException {
        TypeHandler.createClass("invalid.class");
    }

    @Test
    public void testCreateURL() throws ParseException {
        Object result = TypeHandler.createURL("http://www.example.com");
        assert(result instanceof URL);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        TypeHandler.createURL("invalid.url");
    }

    @Test
    public void testCreateFile() throws ParseException {
        Object result = TypeHandler.createFile("test.txt");
        assert(result instanceof File);
    }

    @Test
    public void testCreateFiles() throws ParseException {
        Object result = TypeHandler.createFiles("file1.txt,file2.txt");
        assert(result instanceof File[]);
    }
}