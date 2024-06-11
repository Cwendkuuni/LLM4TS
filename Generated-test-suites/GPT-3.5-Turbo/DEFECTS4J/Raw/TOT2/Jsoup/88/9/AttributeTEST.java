import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeTest {
    @Test
    public void testAttributeConstructor() {
        Attribute attr = new Attribute("key", "value");
        assertEquals("key", attr.getKey());
        assertEquals("value", attr.getValue());
    }

    // Other test cases will be added here for each method
}