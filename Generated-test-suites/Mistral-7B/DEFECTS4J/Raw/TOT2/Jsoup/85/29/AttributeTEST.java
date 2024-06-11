import org.junit.Test;
import org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
        assertNull(attribute.getParent());
    }
}