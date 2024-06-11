import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testAttributeConstruction() {
        String key = "testKey";
        String value = "testValue";
        Attribute attribute = new Attribute(key, value);

        Assert.assertEquals(key, attribute.getKey());
        Assert.assertEquals(value, attribute.getValue());
    }
}