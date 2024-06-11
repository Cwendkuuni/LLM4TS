import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.PatternOptionBuilder;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Class() {
        String str = "test";
        Class clazz = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_Object() {
        String str = "org.apache.commons.cli.TypeHandler";
        Object result = TypeHandler.createObject(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateClass() {
        String str = "org.apache.commons.cli.TypeHandler";
        Class result = TypeHandler.createClass(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateDate() {
        String str = "2020-01-01";
        Date result = TypeHandler.createDate(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateURL() {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals(str, result.getPath());
    }

    @Test
    public void testCreateFiles() {
        String str = "/path/to/file1,/path/to/file2";
        File[] result = TypeHandler.createFiles(str);
        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("/path/to/file1", result[0].getPath());
        assertEquals("/path/to/file2", result[1].getPath());
    }
}