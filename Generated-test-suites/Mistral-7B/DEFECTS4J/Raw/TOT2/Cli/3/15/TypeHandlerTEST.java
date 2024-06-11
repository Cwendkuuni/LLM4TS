import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new String("test");
        String str = "test";
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(obj, result);
    }
}