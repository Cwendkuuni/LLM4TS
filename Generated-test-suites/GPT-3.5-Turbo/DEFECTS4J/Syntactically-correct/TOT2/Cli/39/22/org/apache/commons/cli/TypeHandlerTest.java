import static org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueFromClass() {
        try {
            TypeHandler.createValue("test", String.class);
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateValueFromObject() {
        try {
            TypeHandler.createValue("test", PatternOptionBuilder.OBJECT_VALUE);
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateValueFromNumber() {
        try {
            TypeHandler.createValue("42", PatternOptionBuilder.NUMBER_VALUE);
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateValueFromURL() {
        try {
            TypeHandler.createValue("http://www.example.com", PatternOptionBuilder.URL_VALUE);
        } catch (ParseException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateValueFromInvalidClass() {
        try {
            TypeHandler.createValue("test", TestClass.class);
            fail("Exception should be thrown for invalid class");
        } catch (ParseException e) {
            // Expected exception
        }
    }
}