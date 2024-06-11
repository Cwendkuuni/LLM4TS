import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_stringType() throws ParseException {
        String str = "testString";
        String obj = "java.lang.String";
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_objectType() throws ParseException {
        String str = "java.lang.Object";
        String obj = "java.lang.Class";
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(Class.forName(str), result.getClass());
    }

    @Test
    public void testCreateValue_numberTypeLong() throws ParseException {
        String str = "123";
        String obj = "java.lang.Long";
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateValue_numberTypeDouble() throws ParseException {
        String str = "3.14";
        String obj = "java.lang.Double";
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(Double.valueOf(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_dateType() throws ParseException {
        String str = "2019-03-15";
        String obj = "java.util.Date";
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_classType() throws ParseException {
        String str = "java.lang.String";
        String obj = "java.lang.Class";
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(Class.forName(str), result);
    }

    @Test
    public void testCreateValue_fileType() throws ParseException {
        String str = "testFile.txt";
        String obj = "java.io.File";
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(new File(str).getAbsolutePath(), ((File) result).getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_existingFileType() throws ParseException {
        String str = "nonExistingFile.txt";
        String obj = "java.io.FileInputStream";
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateValue_filesType() throws ParseException {
        String str = "testFile1.txt,testFile2.txt,testFile3.txt";
        String obj = "java.io.File[]";
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertTrue(result instanceof File[]);
        assertEquals(3, ((File[]) result).length);
    }

    @Test
    public void testCreateValue_urlType() throws ParseException {
        String str = "http://www.example.com";
        String obj = "java.net.URL";
        Object result = TypeHandler.createValue(str, obj);
        assertNotNull(result);
        assertEquals(new URL(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_unknownType() throws ParseException {
        String str = "unknownType";
        String obj = "java.lang.UnknownType";
        TypeHandler.createValue(str, obj);
    }

    @Test
    public void testCreateObject_existingClass() throws ParseException {
        String classname = "java.lang.Object";
        Object result = TypeHandler.createObject(classname);
        assertNotNull(result);
        assertEquals(classname, result.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_nonExistingClass() throws ParseException {
        String classname = "non.existing.Class";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_longValue() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertEquals(Long.valueOf(str), result);
    }

    @Test
    public void testCreateNumber_doubleValue() throws ParseException {
        String str = "3.14";
        Number result = TypeHandler.createNumber(str);
        assertNotNull(result);
        assertEquals(Double.valueOf(str), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_invalidNumber() throws ParseException {
        String str = "invalid";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_existingClass() throws ParseException {
        String classname = "java.lang.Object";
        Class<?> result = TypeHandler.createClass(classname);
        assertNotNull(result);
        assertEquals(classname, result.getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_nonExistingClass() throws ParseException {
        String classname = "non.existing.Class";
        TypeHandler.createClass(classname);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_notImplemented() throws ParseException {
        String str = "2019-03-15";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL_validURL() throws ParseException {
        String str = "http://www.example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
        assertEquals(str, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_invalidURL() throws ParseException {
        String str = "invalidURL";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_existingFile() throws ParseException {
        String str = "testFile.txt";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
        assertEquals(new File(str).getAbsolutePath(), result.getAbsolutePath());
    }

    @Test
    public void testOpenFile_existingFile() throws ParseException {
        String str = "testFile.txt";
        FileInputStream result = TypeHandler.openFile(str);
        assertNotNull(result);
        assertTrue(result instanceof FileInputStream);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_nonExistingFile() throws ParseException {
        String str = "nonExistingFile.txt";
        TypeHandler.openFile(str);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_notImplemented() throws ParseException {
        String str = "testFile1.txt,testFile2.txt,testFile3.txt";
        TypeHandler.createFiles(str);
    }
}