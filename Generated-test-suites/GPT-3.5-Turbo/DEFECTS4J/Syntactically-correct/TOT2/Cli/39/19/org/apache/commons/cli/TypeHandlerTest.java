import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() {
        try {
            Object obj = TypeHandler.createValue("test", String.class);
            assertEquals("test", obj);
        } catch (ParseException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateObject() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertNotNull(obj);
        } catch (ParseException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateNumber() {
        try {
            Number num = TypeHandler.createNumber("10");
            assertEquals(10L, num);
        } catch (ParseException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateClass() {
        try {
            Class<?> clazz = TypeHandler.createClass("java.lang.String");
            assertEquals(String.class, clazz);
        } catch (ParseException e) {
            fail("Exception not expected here");
        }
    }

    @Test
    public void testCreateURL() {
        try {
            URL url = TypeHandler.createURL("https://www.example.com");
            assertNotNull(url);
        } catch (ParseException e) {
            fail("Exception not expected here");
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateInvalidURL() throws ParseException {
        TypeHandler.createURL("invalidurl");
    }
    
    // Other test cases for additional methods can be added here
    
}