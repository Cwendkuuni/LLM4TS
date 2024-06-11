
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new Object(); // any object
        String str = "testValue";

        // Positive test case
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result.toString());

        // Null object test case
        assertNull(TypeHandler.createValue(str, null));

        // Invalid object type test case
        assertNull(TypeHandler.createValue(str, int.class));
    }

    @Test
    public void testCreateValueStringClass() {
        String str = "testValue";
        Class<String> stringClass = String.class;

        // Positive test case
        String result = (String) TypeHandler.createValue(str, stringClass);
        assertEquals(str, result);

        // Null class test case
        assertNull(TypeHandler.createValue(str, null));

        // Invalid class type test case
        assertNull(TypeHandler.createValue(str, int.class));
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidString() {
        String str = "";
        Object obj = new Object(); // any object

        try {
            TypeHandler.createValue(str, obj.getClass());
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateValueNullString() {
        Object obj = new Object(); // any object

        try {
            TypeHandler.createValue(null, obj.getClass());
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() {
        String str = "testValue";
        Class<Integer> intClass = Integer.class;

        try {
            TypeHandler.createValue(str, intClass);
            fail("Expected ParseException");
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectClassNotFound() {
        String classname = "InvalidClass";

        try {
            TypeHandler.createObject(classname);
            fail("Expected ClassNotFoundException");
        } catch (ClassNotFoundException e) {
            // Expected
        }
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObjectInstantiationException() {
        String classname = "java.lang.String";

        try {
            TypeHandler.createObject(classname);
            fail("Expected InstantiationException");
        } catch (InstantiationException e) {
            // Expected
        }
    }

    @Test
    public void testCreateObject() {
        String classname = "java.lang.String";

        // Positive test case
        Object result = TypeHandler.createObject(classname);
        assertInstanceof(String.class, result);
        assertEquals("", (String) result);

        // Null classname test case
        assertNull(TypeHandler.createObject(null));

        // Invalid classname test case
        assertNull(TypeHandler.createObject("InvalidClass"));
    }
}