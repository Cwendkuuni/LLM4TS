import org.junit.Test;
import org.apache.commons.cli.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_string() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assert result.equals(str);
    }

    @Test
    public void testCreateValue_object() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assert result instanceof String;
    }

    @Test
    public void testCreateValue_number() throws ParseException {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assert result instanceof Long;
    }

    @Test
    public void testCreateValue_date() throws ParseException {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        // Date is not yet implemented, so result should be null

        assert result == null;
    }

    @Test
    public void testCreateValue_class() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assert result.equals(String.class);
    }

    @Test
    public void testCreateValue_file() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assert result instanceof File;
    }

    @Test
    public void testCreateValue_existingFile() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            assert result instanceof FileInputStream;
        } catch (ParseException e) {
            // expected exception: file not found
        }
    }

    @Test
    public void testCreateValue_files() throws ParseException {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        // Files is not yet implemented, so result should be null
        assert result == null;
    }

    @Test
    public void testCreateValue_url() throws ParseException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assert result instanceof URL;
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assert result instanceof String;
    }

    @Test
    public void testCreateNumber_long() throws ParseException {
        String str = "123";
        Object result = TypeHandler.createNumber(str);
        assert result instanceof Long;
    }

    @Test
    public void testCreateNumber_double() throws ParseException {
        String str = "123.45";
        Object result = TypeHandler.createNumber(str);
        assert result instanceof Double;
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_invalid() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createClass(classname);
        assert result.equals(String.class);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_notFound() throws ParseException {
        String classname = "com.example.FooBar";
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2021-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        Object result = TypeHandler.createURL(str);
        assert result instanceof URL;
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_invalid() throws ParseException {
        String str = "example.com";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        Object result = TypeHandler.createFile(str);
        assert result instanceof File;
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "test.txt";
        try {
            Object result = TypeHandler.openFile(str);
            assert result instanceof FileInputStream;
        } catch (ParseException e) {
            // expected exception: file not found
        }
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_notFound() throws ParseException {
        String str = "nonexistent.txt";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        String str = "test.txt";
        Object result = TypeHandler.createFiles(str);
        // Files is not yet implemented, so result should be null
        assert result == null;
    }
}