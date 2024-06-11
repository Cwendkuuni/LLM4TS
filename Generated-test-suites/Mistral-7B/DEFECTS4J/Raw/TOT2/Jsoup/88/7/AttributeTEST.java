import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {
    @Test
    public void testAttributeConstructor_StringKeyStringValue() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);
        assertNotNull(attribute);
        assertEquals(key, attribute.getKey());
        assertEquals(value, attribute.getValue());
    }
}