import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import java.util.HashMap;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        Attribute attr = new Attribute("key", "value");
        Assert.assertEquals("key", attr.getKey());
        Assert.assertEquals("value", attr.getValue());

        Attribute attrWithParent = new Attribute("key", "value", new Attributes());
        Assert.assertEquals("key", attrWithParent.getKey());
        Assert.assertEquals("value", attrWithParent.getValue());
        Assert.assertNull(attrWithParent.parent);
    }

    @Test
    public void testAttributeSetters() {
        Attribute attr = new Attribute("key", "value");
        Attribute attrWithParent = new Attribute("key", "value", new Attributes());

        String newKey = "newKey";
        String newValue = "newValue";

        attr.setKey(newKey);
        Assert.assertEquals(newKey, attr.getKey());

        attrWithParent.setKey(newKey);
        Assert.assertEquals(newKey, attrWithParent.getKey());
        Assert.assertEquals(newValue, attrWithParent.setValue("newValue"));
        Assert.assertEquals(newValue, attrWithParent.getValue());

        String oldKey = "oldKey";
        String oldValue = "oldValue";
        Assert.assertEquals(oldValue, attrWithParent.setValue(oldValue));
        Assert.assertEquals(oldValue, attrWithParent.getValue());
        Assert.assertEquals(newKey, attrWithParent.getKey());
    }

    @Test
    public void testAttributeMethods() {
        Attribute attr = new Attribute("key", "value");

        Attribute clone = attr.clone();
        Assert.assertNotSame(attr, clone);
        Assert.assertEquals(attr.getKey(), clone.getKey());
        Assert.assertEquals(attr.getValue(), clone.getValue());

        Map<String, Attribute> map = new HashMap<>();
        map.put(attr.getKey(), attr);
        Attributes parent = new Attributes(map);
        attr.setParent(parent);

        Assert.assertEquals(parent, attr.getParent());
        attr.setValue("newValue");
        Assert.assertEquals("newValue", parent.get(attr.getKey()).getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNullKey() {
        new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForEmptyKey() {
        new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNullValue() {
        new Attribute("key", null);
    }
}