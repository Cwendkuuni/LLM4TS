import static org.junit.Assert.*;
import org.junit.Test;
import java.text.ParseException;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
        assertEquals("bar", TypeHandler.createValue("bar", PatternOptionBuilder.STRING_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValueStringClass() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
        assertEquals(123, TypeHandler.createValue("123", Integer.class));
        assertNull(TypeHandler.createValue(null, String.class));
    }

    @Test
    public void testCreateObject() throws ParseException {
        assertNotNull(TypeHandler.createObject("java.lang.String"));
        assertNotNull(TypeHandler.createObject("java.util.Date"));
        assertNull(TypeHandler.createObject("non.existing.Class"));
    }

    @Test
    public void testCreateNumber() throws ParseException {
        assertEquals(123, TypeHandler.createNumber("123"));
        assertEquals(3.14, TypeHandler.createNumber("3.14"));
        assertNull(TypeHandler.createNumber(null));
    }

    @Test
    public void testCreateClass() throws ParseException {
        assertNotNull(TypeHandler.createClass("java.lang.String"));
        assertNotNull(TypeHandler.createClass("java.util.Date"));
        assertNull(TypeHandler.createClass("non.existing.Class"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() {
        TypeHandler.createDate("2022-01-01");
    }

    @Test
    public void testCreateURL() throws ParseException {
        assertEquals(new URL("http://www.example.com"), TypeHandler.createURL("http://www.example.com"));
        assertNull(TypeHandler.createURL(null));
    }

    @Test
    public void testCreateFile() {
        assertNotNull(TypeHandler.createFile("path/to/file.txt"));
        assertNull(TypeHandler.createFile(null));
    }

    // testCreateFiles() method cannot be tested as it throws an UnsupportedOperationException

}