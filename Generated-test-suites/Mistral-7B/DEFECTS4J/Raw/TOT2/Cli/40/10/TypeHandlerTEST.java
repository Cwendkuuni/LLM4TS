import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_String() throws ParseException {
        String str = "example";
        Class<String> clazz = String.class;

        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);
    }

    // Add edge cases and exception handling test cases here
}