import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {
    @Test
    public void testAttributeCreation() {
        Attribute attribute = new Attribute("key", "value");
        String expectedKey = "key";
        String expectedValue = "value";

        Assert.assertEquals(expectedKey, attribute.getKey());
        Assert.assertEquals(expectedValue, attribute.getValue());
    }
}