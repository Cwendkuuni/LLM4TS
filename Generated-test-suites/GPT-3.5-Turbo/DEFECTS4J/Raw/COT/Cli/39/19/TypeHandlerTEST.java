import org.junit.Test;
import org.apache.commons.cli.ParseException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringArgument() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithClassArgument() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueWithObjectValueArgument() throws ParseException {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Object expected = TypeHandler.createObject(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueWithNumberValueArgument() throws ParseException {
        String str = "1.5";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Object expected = TypeHandler.createNumber(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueWithDateValueArgument() throws ParseException {
        String str = "2022-01-01";
        Class<?> clazz = PatternOptionBuilder.DATE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Object expected = TypeHandler.createDate(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueWithClassValueArgument() throws ParseException {
        String str = "java.lang.String";
        Class<?> clazz = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Object expected = TypeHandler.createClass(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueWithFileValueArgument() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.FILE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Object expected = TypeHandler.createFile(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueWithExistingFileValueArgument() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Object expected = TypeHandler.createFile(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueWithFilesValueArgument() throws ParseException {
        String str = "test1.txt,test2.txt";
        Class<?> clazz = PatternOptionBuilder.FILES_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Object expected = TypeHandler.createFiles(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValueWithURLValueArgument() throws ParseException {
        String str = "http://www.example.com";
        Class<?> clazz = PatternOptionBuilder.URL_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        Object expected = TypeHandler.createURL(str);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidClassArgument() throws ParseException {
        String str = "invalidClass";
        Class<?> clazz = PatternOptionBuilder.CLASS_VALUE;
        TypeHandler.createValue(str, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidNumberArgument() throws ParseException {
        String str = "invalidNumber";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;
        TypeHandler.createValue(str, clazz);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        String str = "2022-01-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://www.example.com";
        URL expected = new URL(str);
        URL result = TypeHandler.createURL(str);
        assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLWithInvalidURL() throws ParseException {
        String str = "invalidUrl";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File expected = new File(str);
        File result = TypeHandler.createFile(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateFiles() {
        String str = "test1.txt,test2.txt";
        File[] expected = new File[] { new File("test1.txt"), new File("test2.txt") };
        File[] result = TypeHandler.createFiles(str);
        assertArrayEquals(expected, result);
    }
}

