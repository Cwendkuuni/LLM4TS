import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ParseException;

@RunWith(JUnit4.class)
public class TypeHandlerTest {

    @Test
    public void testCreateValueWithObject() {
        try {
            Object obj = TypeHandler.createValue("test", Object.class);
            assertEquals("test", obj);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateValueWithClass() {
        try {
            String str = "String";
            Class<?> clazz = TypeHandler.createClass(str);
            assertEquals(String.class, clazz);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateNumberLong() {
        try {
            Number num = TypeHandler.createNumber("10");
            assertEquals(10L, num);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateNumberDouble() {
        try {
            Number num = TypeHandler.createNumber("10.5");
            assertEquals(10.5, num);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("http://www.example.com");
            assertNotNull(url);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testCreateFile() {
        File file = TypeHandler.createFile("test.txt");
        assertNotNull(file);
    }

    @Test
    public void testOpenFile() {
        try {
            FileInputStream fis = TypeHandler.openFile("test.txt");
            assertNotNull(fis);
        } catch (ParseException e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = ParseException.class)
    public void testOpenFileFileNotFoundException() throws ParseException {
        TypeHandler.openFile("nonexistent.txt");
    }

}