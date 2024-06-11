import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TypeHandlerTest {
    @Test
    public void testCreateValueStringObject() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", PatternOptionBuilder.STRING_VALUE));
        assertEquals("1", TypeHandler.createValue("1", PatternOptionBuilder.STRING_VALUE));
        assertEquals("", TypeHandler.createValue("", PatternOptionBuilder.STRING_VALUE));
        assertNull(TypeHandler.createValue(null, PatternOptionBuilder.STRING_VALUE));
    }

    @Test
    public void testCreateValueStringClass() throws ParseException {
        assertEquals("foo", TypeHandler.createValue("foo", String.class));
        assertEquals("1", TypeHandler.createValue("1", String.class));
        assertEquals("", TypeHandler.createValue("", String.class));
        assertNull(TypeHandler.createValue(null, String.class));
    }

    @Test
    public void testCreateValueObject() throws ParseException {
        assertEquals(new Object(), TypeHandler.createObject("java.lang.Object"));
        assertEquals(new TypeHandler(), TypeHandler.createObject("org.apache.commons.cli.TypeHandler"));
        assertNull(TypeHandler.createObject(null));
    }

    @Test
    public void testCreateNumber() throws ParseException {
        assertEquals(1, TypeHandler.createNumber("1"));
        assertEquals(1.234, TypeHandler.createNumber("1.234"));
        assertEquals(0, TypeHandler.createNumber("0"));
        assertNull(TypeHandler.createNumber(null));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberException() throws ParseException {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertEquals(TypeHandler.class, TypeHandler.createClass("org.apache.commons.cli.TypeHandler"));
    }

    @Test(expected = ParseException.class)
    public void testCreateClassException() throws ParseException {
        TypeHandler.createClass("InvalidClassName");
    }

    @Test
    public void testCreateDate() throws ParseException {
        assertEquals(null, TypeHandler.createDate("1/1/2021"));
    }

    @Test
    public void testCreateURL() throws ParseException {
        assertEquals(new URL("http://www.example.com"), TypeHandler.createURL("http://www.example.com"));
        assertEquals(new URL("https://www.example.com"), TypeHandler.createURL("https://www.example.com"));
    }

    @Test(expected = ParseException.class)
    public void testCreateURLException() throws ParseException {
        TypeHandler.createURL("invalid url");
    }

    @Test
    public void testCreateFile() {
        assertEquals(new File("path/to/file"), TypeHandler.createFile("path/to/file"));
        assertEquals(new File("file.txt"), TypeHandler.createFile("file.txt"));
    }

    @Test
    public void testOpenFile() throws ParseException {
        assertEquals(new FileInputStream("file.txt"), TypeHandler.openFile("file.txt"));
    }

    @Test(expected = ParseException.class)
    public void testOpenFileException() throws ParseException {
        TypeHandler.openFile("invalid path");
    }

    @Test
    public void testCreateFiles() {
        assertEquals(null, TypeHandler.createFiles("path/to/files"));
    }
}