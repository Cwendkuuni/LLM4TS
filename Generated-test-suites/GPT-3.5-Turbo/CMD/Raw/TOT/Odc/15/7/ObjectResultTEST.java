
import org.junit.Test;
import static org.junit.Assert.*;

public class ObjectResultTest {
    
    @Test
    public void testObjectResultConstructorTypical() {
        ObjectResult objectResult = new ObjectResult("testSchema", "testName", "testType");
        assertNotNull(objectResult);
        assertEquals("testSchema", objectResult.getSchema());
        assertEquals("testName", objectResult.getName());
        assertEquals("testType", objectResult.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorNullValues() {
        new ObjectResult(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testObjectResultConstructorInvalidType() {
        new ObjectResult("testSchema", "testName", "");
    }
}
