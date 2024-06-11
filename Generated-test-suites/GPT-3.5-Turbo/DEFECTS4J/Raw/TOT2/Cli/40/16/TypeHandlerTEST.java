import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() {
        Object value = TypeHandler.createValue("test", Object.class);
        assertEquals("test", value);
    }

    @Test
    public void testCreateValueWithStringNumber() {
        Number value = TypeHandler.createValue("10", Number.class);
        assertEquals(10, value);
    }

    @Test
    public void testCreateValueWithInvalidClass() {
        Object value = TypeHandler.createValue("InvalidClass", Object.class);
        assertNull(value);
    }

    @Test
    public void testCreateObjectValidClass() {
        try {
            Object obj = TypeHandler.createObject("java.lang.String");
            assertNotNull(obj);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateObjectInvalidClass() {
        try {
            Object obj = TypeHandler.createObject("InvalidClass");
            fail("Should throw ParseException");
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateNumberWithInteger() {
        try {
            Number number = TypeHandler.createNumber("10");
            assertEquals(10, number);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateNumberWithDouble() {
        try {
            Number number = TypeHandler.createNumber("10.5");
            assertEquals(10.5, number);
        } catch (ParseException e) {
            fail("Should not throw ParseException");
        }
    }

    @Test
    public void testCreateNumberWithInvalidInput() {
        try {
            Number number = TypeHandler.createNumber("abc");
            fail("Should throw ParseException");
        } catch (ParseException e) {
            // Expected Exception
        }
    }

    // Add more test cases for other methods...

}