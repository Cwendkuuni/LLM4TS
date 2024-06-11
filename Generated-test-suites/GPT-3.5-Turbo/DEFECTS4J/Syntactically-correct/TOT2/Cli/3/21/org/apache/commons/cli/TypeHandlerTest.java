import static org.junit.Assert.*;

import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueForObject() {
        Object obj = TypeHandler.createValue("test", Object.class);
        assertEquals("test", obj);
    }

    @Test
    public void testCreateValueForClass() {
        Object obj = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", obj);
    }

    @Test
    public void testCreateObject() {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertNotNull(obj);
    }

    @Test
    public void testCreateNumber() {
        Number number = TypeHandler.createNumber("10");
        assertEquals(10, number);
    }

    @Test
    public void testCreateClass() {
        Class clazz = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, clazz);
    }

    @Test
    public void testCreateDate() {
        assertNull(TypeHandler.createDate("invalidDate"));
    }

    @Test
    public void testCreateURL() {
        assertNull(TypeHandler.createURL("invalidURL"));
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
    }

    @Test
    public void testCreateFiles() {
        assertNull(TypeHandler.createFiles("test"));
    }
}