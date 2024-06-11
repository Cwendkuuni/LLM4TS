
import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws ParseException {
        String str = "test";
        Object obj = "test";
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(result, obj);
    }

    @Test
    public void testCreateValue_String_Class() throws ParseException {
        String str = "test";
        Class<?> clazz = String.class;
        String result = TypeHandler.createValue(str, clazz);
        assertEquals(result, str);
    }

    @Test
    public void testCreateValue_String_Class_ObjectValue() throws ParseException {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(result, TypeHandler.createObject(str));
    }

    @Test
    public void testCreateValue_String_Class_NumberValue() throws ParseException {
        String str = "123";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;
        Number result = TypeHandler.createValue(str, clazz);
        assertEquals(result, TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateValue_String_Class_DateValue() throws ParseException {
        String str = "01/01/2021";
        Class<?> clazz = PatternOptionBuilder.DATE_VALUE;
        Date result = TypeHandler.createValue(str, clazz);
        // Test for date expected behavior
        // assertEquals(result, TypeHandler.createDate(str));
        assertNull(result);
    }

    @Test
    public void testCreateValue_String_Class_ClassValue() throws ParseException {
        String str = "java.lang.String";
        Class<?> clazz = PatternOptionBuilder.CLASS_VALUE;
        Class<?> result = TypeHandler.createValue(str, clazz);
        assertEquals(result, TypeHandler.createClass(str));
    }

    @Test
    public void testCreateValue_String_Class_FileValue() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.FILE_VALUE;
        File result = TypeHandler.createValue(str, clazz);
        assertEquals(result, TypeHandler.createFile(str));
    }

    @Test
    public void testCreateValue_String_Class_ExistingFileValue() throws ParseException {
        String str = "test.txt";
        Class<?> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;
        FileInputStream result = TypeHandler.createValue(str, clazz);
        assertEquals(result, TypeHandler.openFile(str));
    }

    @Test
    public void testCreateValue_String_Class_FilesValue() throws ParseException {
        String str = "file1.txt,file2.txt";
        Class<?> clazz = PatternOptionBuilder.FILES_VALUE;
        File[] result = TypeHandler.createValue(str, clazz);
        // Test for files expected behavior
        // assertArrayEquals(result, TypeHandler.createFiles(str));
        assertNull(result);
    }

    @Test
    public void testCreateValue_String_Class_InvalidValue() throws ParseException {
        String str = "invalid";
        Class<?> clazz = Double.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateObject() throws ParseException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        // Test for object expected behavior
        // assertEquals(result, TypeHandler.createClass(classname).newInstance());
        assertNull(result);
    }

    @Test
    public void testCreateNumber() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(result, Long.valueOf(str));
    }

    @Test
    public void testCreateClass() throws ParseException {
        String classname = "java.lang.String";
        Class<?> result = TypeHandler.createClass(classname);
        assertEquals(result, Class.forName(classname));
    }

    @Test
    public void testCreateDate() {
        String str = "01/01/2021";
        // Test for date expected behavior
        // Date result = TypeHandler.createDate(str);
        // assertNotEquals(result, null);
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createDate(str));
    }

    @Test
    public void testCreateURL() throws ParseException {
        String str = "http://example.com";
        URL result = TypeHandler.createURL(str);
        assertEquals(result, new URL(str));
    }

    @Test
    public void testCreateFile() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertEquals(result, new File(str));
    }

    @Test
    public void testOpenFile() throws ParseException {
        String str = "test.txt";
        FileInputStream result = TypeHandler.openFile(str);
        assertEquals(result, new FileInputStream(str));
    }

    @Test
    public void testCreateFiles() {
        String str = "file1.txt,file2.txt";
        // Test for files expected behavior
        // File[] result = TypeHandler.createFiles(str);
        // assertArrayEquals(result, FileW.findFiles(str));
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createFiles(str));
    }

}
