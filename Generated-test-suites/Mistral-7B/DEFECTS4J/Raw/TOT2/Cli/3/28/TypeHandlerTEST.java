import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "Hello World";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals("Hello World", result);
    }
}