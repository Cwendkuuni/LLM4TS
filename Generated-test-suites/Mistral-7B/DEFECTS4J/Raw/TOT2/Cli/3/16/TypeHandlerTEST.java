import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.TypeHandler;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValueNullString() {
        String str = null;
        Object obj = String.class;
        TypeHandler.createValue(str, obj);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInvalidClassForString() {
        String str = "test";
        Object obj = Integer.class;
        TypeHandler.createValue(str, obj);
    }

    // Add test cases for other data types and null obj
}