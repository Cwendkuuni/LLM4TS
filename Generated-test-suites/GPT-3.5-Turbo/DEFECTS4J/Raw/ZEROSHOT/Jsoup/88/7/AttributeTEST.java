import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Test;

public class AttributeTest {

    @Test
    public void testConstructor() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNull(attribute.parent);
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("key1", "value");
        Assert.assertEquals("key1", attribute.getKey());

        attribute.setKey("key2");
        Assert.assertEquals("key2", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("key", "value1");
        Assert.assertEquals("value1", attribute.getValue());

        String oldValue = attribute.setValue("value2");
        Assert.assertEquals("value2", attribute.getValue());
        Assert.assertEquals("value1", oldValue);
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("key", "value");
        String html = attribute.html();
        Assert.assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("key", "value");
        String toString = attribute.toString();
        Assert.assertEquals("key=\"value\"", toString);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "value&amp;");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value&", attribute.getValue());
        Assert.assertNull(attribute.parent);
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute1 = new Attribute("data-key", "value");
        Assert.assertTrue(attribute1.isDataAttribute());
        
        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertFalse(attribute2.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        Document.OutputSettings outputSettings1 = new Document.OutputSettings();
        Assert.assertFalse(attribute1.shouldCollapseAttribute(outputSettings1));

        Attribute attribute2 = new Attribute("key", "key");
        Document.OutputSettings outputSettings2 = new Document.OutputSettings();
        Assert.assertTrue(attribute2.shouldCollapseAttribute(outputSettings2));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("key", "value");
        Assert.assertFalse(attribute1.isBooleanAttribute());

        Attribute attribute2 = new Attribute("checked", "checked");
        Assert.assertTrue(attribute2.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");
        Attribute attribute3 = new Attribute("key", "value2");

        Assert.assertEquals(attribute1, attribute2);
        Assert.assertNotEquals(attribute1, attribute3);
    }

    @Test
    public void testHashCode() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = new Attribute("key", "value");

        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute1 = new Attribute("key", "value");
        Attribute attribute2 = attribute1.clone();

        Assert.assertEquals(attribute1, attribute2);
    }
}