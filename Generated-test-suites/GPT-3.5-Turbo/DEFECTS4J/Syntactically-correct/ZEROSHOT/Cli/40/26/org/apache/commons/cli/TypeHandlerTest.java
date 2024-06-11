import org.junit.Test;
import static org.junit.Assert.*;
import java.text.ParseException;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_string_value() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_object_value() throws ParseException {
        Object result = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValue_number_value() throws ParseException {
        Object result = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(10, result);
    }

    @Test
    public void testCreateValue_date_value() throws ParseException {
        Object result = TypeHandler.createValue("2021-01-01", PatternOptionBuilder.DATE_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValue_class_value() throws ParseException {
        Object result = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_file_value() throws ParseException {
        Object result = TypeHandler.createValue("file.txt", PatternOptionBuilder.FILE_VALUE);
        assertEquals(new File("file.txt"), result);
    }

    @Test
    public void testCreateValue_existing_file_value() throws ParseException {
        Object result = TypeHandler.createValue("file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_files_value() throws ParseException {
        Object result = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_url_value() throws ParseException {
        Object result = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(result);
    }
  
  @Test
    public void testCreateValue_null_value() throws ParseException {
        Object result = TypeHandler.createValue("test", null);
        assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        Object result = TypeHandler.createObject("java.lang.String");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_class_not_found() throws ParseException {
        TypeHandler.createObject("com.example.InvalidClass");
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_invalid_instance() throws ParseException {
        TypeHandler.createObject("java.util.Date");
    }

    @Test
    public void testCreateNumber_long_value() throws ParseException {
        Number result = TypeHandler.createNumber("10");
        assertEquals(10L, result);
    }

    @Test
    public void testCreateNumber_double_value() throws ParseException {
        Number result = TypeHandler.createNumber("10.5");
        assertEquals(10.5, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_invalid_value() throws ParseException {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException {
        Class<?> result = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_class_not_found() throws ParseException {
        TypeHandler.createClass("com.example.InvalidClass");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2021-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        URL result = TypeHandler.createURL("http://www.example.com");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_invalid_url() throws ParseException {
        TypeHandler.createURL("invalid_url");
    }

    @Test
    public void testCreateFile() {
        File result = TypeHandler.createFile("file.txt");
        assertNotNull(result);
    }

    @Test
    public void testOpenFile() throws ParseException {
        FileInputStream result = TypeHandler.openFile("file.txt");
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_file_not_found() throws ParseException {
        TypeHandler.openFile("invalid_file.txt");
    }

    @Test
    public void testCreateFiles() {
        File[] result = TypeHandler.createFiles("file1.txt,file2.txt");
        assertNotNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_not_implemented() {
        TypeHandler.createFiles("file1.txt,file2.txt");
    }

}