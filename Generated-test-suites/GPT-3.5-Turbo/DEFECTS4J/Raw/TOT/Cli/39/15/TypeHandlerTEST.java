import org.junit.Test;
import org.apache.commons.cli.ParseException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_string() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        assert(result instanceof String);
        assert(result.equals(str));
    }

    @Test
    public void testCreateValue_object() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        assert(result instanceof String);
        assert(result.equals(str));
    }

    @Test
    public void testCreateValue_number() throws ParseException {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        assert(result instanceof Number);
        assert(result.equals(Long.valueOf(str)));

        str = "10.5";
        result = TypeHandler.createValue(str, obj);

        assert(result instanceof Number);
        assert(result.equals(Double.valueOf(str)));
    }

    @Test
    public void testCreateValue_date() {
        // To be implemented
    }

    @Test
    public void testCreateValue_class() throws ParseException {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        assert(result instanceof Class);
        assert(result.equals(String.class));
    }

    @Test
    public void testCreateValue_file() throws ParseException {
        String str = "/path/to/file";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        assert(result instanceof File);
        assert(result.equals(new File(str)));
    }

    @Test
    public void testCreateValue_existingFile() throws ParseException {
        String str = "/path/to/existingFile";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        assert(result instanceof File);
        assert(result.equals(new File(str)));
    }

    @Test
    public void testCreateValue_files() {
        // To be implemented
    }

    @Test
    public void testCreateValue_url() throws ParseException, MalformedURLException {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);

        assert(result instanceof URL);
        assert(result.equals(new URL(str)));
    }

}