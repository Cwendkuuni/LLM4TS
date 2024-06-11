import static org.junit.Assert.*;
import java.io.File;
import java.net.URL;
import java.util.Date;
import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_stringValue() {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object expected = str;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_objectValue() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.OBJECT_VALUE;
        Object expected = TypeHandler.createObject(str);
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_numberValue() {
        String str = "10";
        Object obj = PatternOptionBuilder.NUMBER_VALUE;
        Object expected = 10;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_dateValue() {
        String str = "2021-01-01";
        Object obj = PatternOptionBuilder.DATE_VALUE;
        Date expected = TypeHandler.createDate(str);
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_classValue() {
        String str = "java.lang.String";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Class<?> expected = TypeHandler.createClass(str);
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_fileValue() {
        String str = "path/to/file.txt";
        Object obj = PatternOptionBuilder.FILE_VALUE;
        File expected = TypeHandler.createFile(str);
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_existingFileValue() {
        String str = "path/to/file.txt";
        Object obj = PatternOptionBuilder.EXISTING_FILE_VALUE;
        File expected = TypeHandler.createFile(str);
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_filesValue() {
        String str = "path/to/file1.txt,path/to/file2.txt";
        Object obj = PatternOptionBuilder.FILES_VALUE;
        File[] expected = TypeHandler.createFiles(str);
        Object result = TypeHandler.createValue(str, obj);
        assertArrayEquals(expected, (File[]) result);
    }

    @Test
    public void testCreateValue_urlValue() {
        String str = "http://www.example.com";
        Object obj = PatternOptionBuilder.URL_VALUE;
        URL expected = TypeHandler.createURL(str);
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateValue_invalidValue() {
        String str = "invalid";
        Object obj = "";
        assertNull(TypeHandler.createValue(str, obj));
    }

    @Test
    public void testCreateObject_validClass() {
        String str = "java.lang.String";
        Object expected = "java.lang.String";
        Object result = TypeHandler.createObject(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateObject_invalidClass() {
        String str = "invalid.class";
        assertNull(TypeHandler.createObject(str));
    }

    @Test
    public void testCreateNumber_validNumber() {
        String str = "10";
        Number expected = 10;
        Number result = TypeHandler.createNumber(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateNumber_invalidNumber() {
        String str = "invalid";
        assertNull(TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateClass_validClass() {
        String str = "java.lang.String";
        Class<?> expected = String.class;
        Class<?> result = TypeHandler.createClass(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateClass_invalidClass() {
        String str = "invalid.class";
        assertNull(TypeHandler.createClass(str));
    }

    @Test
    public void testCreateDate_validDate() {
        String str = "2021-01-01";
        Date expected = new Date(2021, 0, 1);
        Date result = TypeHandler.createDate(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateDate_invalidDate() {
        String str = "2021-13-01";
        assertNull(TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL_validURL() {
        String str = "http://www.example.com";
        URL expected = new URL(str);
        URL result = TypeHandler.createURL(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateURL_invalidURL() {
        String str = "invalid.url";
        assertNull(TypeHandler.createURL(str));
    }

    @Test
    public void testCreateFile_validPath() {
        String str = "path/to/file.txt";
        File expected = new File(str);
        File result = TypeHandler.createFile(str);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateFiles_validPaths() {
        String str = "path/to/file1.txt,path/to/file2.txt";
        File[] expected = new File[] { new File("path/to/file1.txt"), new File("path/to/file2.txt") };
        File[] result = TypeHandler.createFiles(str);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testCreateFiles_invalidPaths() {
        String str = "path/to/file1.txt,path/to/invalid.txt";
        assertNull(TypeHandler.createFiles(str));
    }

}