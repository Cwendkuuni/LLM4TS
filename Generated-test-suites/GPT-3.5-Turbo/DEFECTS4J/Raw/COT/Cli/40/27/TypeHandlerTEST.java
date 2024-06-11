import org.junit.Test;

import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_stringValue() throws ParseException {
        String str = "test";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_numberValue_integer() throws ParseException {
        String str = "123";
        Class<Integer> clazz = PatternOptionBuilder.NUMBER_VALUE;
        Integer result = TypeHandler.createValue(str, clazz);
        assertEquals(Integer.valueOf(str), result);
    }

    @Test
    public void testCreateValue_numberValue_double() throws ParseException {
        String str = "10.5";
        Class<Double> clazz = PatternOptionBuilder.NUMBER_VALUE;
        Double result = TypeHandler.createValue(str, clazz);
        assertEquals(Double.valueOf(str), result);
    }

    @Test
    public void testCreateValue_dateValue() throws ParseException {
        String str = "2020-01-01";
        Class<Date> clazz = PatternOptionBuilder.DATE_VALUE;
        Date result = TypeHandler.createValue(str, clazz);
        // assert result is a valid date
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_classValue() throws ParseException {
        String str = "java.lang.String";
        Class<Class> clazz = PatternOptionBuilder.CLASS_VALUE;
        Class result = TypeHandler.createValue(str, clazz);
        assertEquals(String.class, result);
    }

    @Test
    public void testCreateValue_fileValue() throws ParseException {
        String str = "/path/to/file";
        Class<File> clazz = PatternOptionBuilder.FILE_VALUE;
        File result = TypeHandler.createValue(str, clazz);
        assertEquals(new File(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_existingFileValue_notExist() throws ParseException {
        String str = "/path/to/nonexistent/file";
        Class<FileInputStream> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValue_existingFileValue_exist() throws ParseException {
        String str = "/path/to/existing/file";
        Class<FileInputStream> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        FileInputStream result = TypeHandler.createValue(str, clazz);
        assertNotNull(result);
    }

    @Test
    public void testCreateValue_URLValue() throws ParseException {
        String str = "https://www.example.com";
        Class<URL> clazz = PatternOptionBuilder.URL_VALUE;
        URL result = TypeHandler.createValue(str, clazz);
        assertEquals(new URL(str), result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = String.class;
        assertEquals(result, TypeHandler.createObject(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_invalidClass() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_integer() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(Integer.valueOf(str), result);
    }

    @Test
    public void testCreateNumber_double() throws ParseException {
        String str = "10.5";
        Number result = TypeHandler.createNumber(str);
        assertEquals(Double.valueOf(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_invalidNumber() throws ParseException {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_validClass() throws ParseException {
        String classname = "java.lang.String";
        Class result = String.class;
        assertEquals(result, TypeHandler.createClass(classname));
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_invalidClass() throws ParseException {
        String classname = "InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateURL_validURL() throws ParseException {
        String str = "https://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals(new URL(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_invalidURL() throws ParseException {
        String str = "invalidURL";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile() {
        String str = "/path/to/file";
        File result = TypeHandler.createFile(str);
        assertEquals(new File(str), result);
    }

    @Test
    public void testOpenFile_existingFile() throws ParseException {
        String str = "/path/to/existing/file";
        FileInputStream result = TypeHandler.openFile(str);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_nonexistentFile() throws ParseException {
        String str = "/path/to/nonexistent/file";
        TypeHandler.openFile(str);
    }

    @Test
    public void testCreateFiles() {
        // not implemented yet
    }

}