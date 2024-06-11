import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AttributeTest {
    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("testKey", "testValue");
        assertEquals("testKey", attribute.getKey());
        assertEquals("testValue", attribute.getValue());
    }
}