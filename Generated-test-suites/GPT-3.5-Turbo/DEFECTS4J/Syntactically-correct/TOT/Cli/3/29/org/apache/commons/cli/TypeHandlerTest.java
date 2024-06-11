import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringArgument() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObjectArgument() {
        String str = "org.apache.commons.cli.SomeClass";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValueWithNumberArgument() {
        String str = "123";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateValueWithDateArgument() {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test
    public void testCreateValueWithClassArgument() {
        String str = "org.apache.commons.cli.SomeClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(str, result.getClass().getName());
    }

    @Test
    public void testCreateValueWithFileArgument() {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(str, ((File)result).getName());
    }

    @Test
    public void testCreateValueWithExistingFileArgument() {
        String str = "test.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(str, ((File)result).getName());
    }

    @Test
    public void testCreateValueWithFilesArgument() {
        String str = "test1.txt,test2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }

    @Test
    public void testCreateValueWithURLArgument() {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(str, ((URL)result).toString());
    }

    @Test
    public void testCreateValueWithInvalidArgument() {
        String str = "invalid";
        Object obj = "UnknownType";
        Object result = TypeHandler.createValue(str, obj);
        assertNull(result);
    }
}