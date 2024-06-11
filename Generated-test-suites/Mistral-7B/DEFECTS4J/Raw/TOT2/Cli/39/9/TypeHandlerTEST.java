import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue() throws ParseException
    {
        Object result = TypeHandler.createValue("hello", String.class);
        assertEquals("hello", result);

        result = TypeHandler.createValue("123", Integer.class);
        assertEquals(123, result);

        result = TypeHandler.createValue("45.6", Double.class);
        assertEquals(45.6, result);

        result = TypeHandler.createValue("2022-03-31", Date.class);
        // Assuming Date is properly implemented in the test environment
        Date expectedDate = new Date(2022 - 1900, 2 - 1, 31);
        assertEquals(expectedDate, result);

        result = TypeHandler.createValue("com.example.MyClass", MyClass.class);
        // Assuming MyClass is properly defined in the test environment
        MyClass expectedObject = new MyClass();
        assertEquals(expectedObject, result);
    }
}