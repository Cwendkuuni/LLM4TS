import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValue() {
        String str = "Hello World";
        String expectedStr = "Hello World";
        Object obj = new String("Hello World");

        Object result = TypeHandler.createValue(str, obj.getClass());

        assertEquals(expectedStr, result);
    }
}