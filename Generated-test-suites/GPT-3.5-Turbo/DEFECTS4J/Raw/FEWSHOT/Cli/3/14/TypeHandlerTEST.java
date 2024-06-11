import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValue_StringObject_String() {
        Object obj = "String";
        assertEquals("String", TypeHandler.createValue("String", obj));
    }

    @Test
    public void testCreateValue_StringClass_String() {
        Class clazz = String.class;
        assertEquals("String", TypeHandler.createValue("String", clazz));
    }

    @Test
    public void testCreateValue_ObjectValue_String() {
        assertEquals("String", TypeHandler.createValue("String", PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValue_NumberValue_String() {
        assertEquals(NumberUtils.createNumber("123"), TypeHandler.createValue("123", PatternOptionBuilder.NUMBER_VALUE));
    }

    @Test
    public void testCreateValue_DateValue_String() {
        Date date = new Date();
        assertEquals(date, TypeHandler.createValue(date.toString(), PatternOptionBuilder.DATE_VALUE));
    }

    @Test
    public void testCreateValue_ClassValue_String() {
        Class clazz = String.class;
        assertEquals(clazz, TypeHandler.createValue(clazz.getName(), PatternOptionBuilder.CLASS_VALUE));
    }

    @Test
    public void testCreateValue_FileValue_String() {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        assertEquals(file, TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE));
    }

    @Test
    public void testCreateValue_ExistingFileValue_String() {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        assertEquals(file, TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE));
    }

    @Test
    public void testCreateValue_FilesValue_String() {
        String filePaths = "file1.txt,file2.txt,file3.txt";
        assertNull(TypeHandler.createValue(filePaths, PatternOptionBuilder.FILES_VALUE));
    }

    @Test
    public void testCreateValue_URLValue_String() {
        String urlString = "http://www.example.com";
        try {
            URL url = new URL(urlString);
            assertEquals(url, TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateValue_UnknownValue_String() {
        assertNull(TypeHandler.createValue("Unknown", "UnknownValue"));
    }

    @Test
    public void testCreateObject() {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertEquals(String.class, obj.getClass());
    }

    @Test
    public void testCreateNumber() {
        Number number = TypeHandler.createNumber("123");
        assertEquals(NumberUtils.createNumber("123"), number);
    }

    @Test
    public void testCreateNumber_InvalidNumber() {
        assertNull(TypeHandler.createNumber("Invalid"));
    }

    @Test
    public void testCreateClass() {
        Class clazz = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, clazz);
    }

    @Test
    public void testCreateClass_ClassNotFound() {
        assertNull(TypeHandler.createClass("InvalidClass"));
    }

    @Test
    public void testCreateDate() {
        Date date = new Date();
        assertEquals(date, TypeHandler.createDate(date.toString()));
    }

    @Test
    public void testCreateDate_InvalidDate() {
        assertNull(TypeHandler.createDate("InvalidDate"));
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        try {
            URL url = new URL(urlString);
            assertEquals(url, TypeHandler.createURL(urlString));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateURL_InvalidURL() {
        assertNull(TypeHandler.createURL("InvalidURL"));
    }

    @Test
    public void testCreateFile() {
        String filePath = "testFile.txt";
        File file = new File(filePath);
        assertEquals(file, TypeHandler.createFile(filePath));
    }

    @Test
    public void testCreateFiles() {
        String filePaths = "file1.txt,file2.txt,file3.txt";
        assertNull(TypeHandler.createFiles(filePaths));
    }
}