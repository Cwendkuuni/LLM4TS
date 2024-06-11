import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringAndObject() {
        String str = "testValue";
        Object obj = new Object();

        Object result = TypeHandler.createValue(str, obj.getClass());

        assertEquals(str, result.toString());
    }
}