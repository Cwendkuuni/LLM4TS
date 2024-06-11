import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        Object obj1 = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertTrue(obj1 instanceof String);
        
        Object obj2 = TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(obj2);
        assertTrue(obj2 instanceof Object);
        
        Object obj3 = TypeHandler.createValue("10", PatternOptionBuilder.NUMBER_VALUE);
        assertNotNull(obj3);
        assertTrue(obj3 instanceof Number);
        
        Object obj4 = TypeHandler.createValue("2021-09-01", PatternOptionBuilder.DATE_VALUE);
        assertNotNull(obj4);
        assertTrue(obj4 instanceof Date);
        
        Object obj5 = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE);
        assertNotNull(obj5);
        assertTrue(obj5 instanceof Class);
        
        Object obj6 = TypeHandler.createValue("file.txt", PatternOptionBuilder.FILE_VALUE);
        assertNotNull(obj6);
        assertTrue(obj6 instanceof File);
        
        Object obj7 = TypeHandler.createValue("file.txt", PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNotNull(obj7);
        assertTrue(obj7 instanceof File);
        
        Object obj8 = TypeHandler.createValue("file1.txt,file2.txt", PatternOptionBuilder.FILES_VALUE);
        assertNotNull(obj8);
        assertTrue(obj8 instanceof File[]);
        
        Object obj9 = TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        assertNotNull(obj9);
        assertTrue(obj9 instanceof URL);
        
        Object obj10 = TypeHandler.createValue("invalidValue", "InvalidClass");
        assertNull(obj10);
    }

    @Test
    public void testCreateObject() {
        Object obj = TypeHandler.createObject("java.lang.Integer");
        assertNotNull(obj);
        assertTrue(obj instanceof Integer);
    }

    @Test
    public void testCreateNumber() {
        Number num = TypeHandler.createNumber("10");
        assertNotNull(num);
        assertTrue(num instanceof Number);
        
        Number invalidNum = TypeHandler.createNumber("invalidNumber");
        assertNull(invalidNum);
    }

    @Test
    public void testCreateClass() {
        Class clazz = TypeHandler.createClass("java.lang.String");
        assertNotNull(clazz);
        assertTrue(clazz.equals(String.class));
        
        Class invalidClazz = TypeHandler.createClass("InvalidClass");
        assertNull(invalidClazz);
    }

    @Test
    public void testCreateDate() {
        Date date = TypeHandler.createDate("2021-09-01");
        assertNotNull(date);
        assertTrue(date instanceof Date);
        
        Date invalidDate = TypeHandler.createDate("invalidDate");
        assertNull(invalidDate);
    }

    @Test
    public void testCreateURL() {
        URL url = TypeHandler.createURL("http://www.example.com");
        assertNotNull(url);
        assertTrue(url instanceof URL);
        
        URL invalidUrl = TypeHandler.createURL("invalidUrl");
        assertNull(invalidUrl);
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("file.txt");
        assertNotNull(file);
        assertTrue(file instanceof File);
    }

    @Test
    public void testCreateFiles() {
        File[] files = TypeHandler.createFiles("file1.txt,file2.txt");
        assertNotNull(files);
        assertTrue(files instanceof File[]);
    }
}