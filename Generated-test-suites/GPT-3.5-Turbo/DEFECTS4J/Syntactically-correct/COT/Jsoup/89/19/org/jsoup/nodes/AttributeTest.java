import org.jsoup.SerializationException;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junut.framework.TestCase;
import org.junit.Assert;

public class AttributeTest {

    // Test getKey() method
    public void testGetKey() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name", attribute.getKey());
    }

    // Test setKey() method
    public void testSetKey() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setKey("newName");
        Assert.assertEquals("newName", attribute.getKey());
    }

    // Test getValue() method
    public void testGetValue() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("value", attribute.getValue());
    }

    // Test setValue() method
    public void testSetValue() {
        Attribute attribute = new Attribute("name", "value");
        String oldValue = attribute.setValue("newValue");
        Assert.assertEquals("value", oldValue);
        Assert.assertEquals("newValue", attribute.getValue());
    }

    // Test html() method
    public void testHtml() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name=\"value\"", attribute.html());
    }

    // Test toString() method
    public void testToString() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name=\"value\"", attribute.toString());
    }

    // Test createFromEncoded() method
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("name", "value");
        Assert.assertEquals("name", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    // Test isDataAttribute() method
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    // Test shouldCollapseAttribute() method
    public void testShouldCollapseAttribute() {
        Attribute attribute1 = new Attribute("name", "name");
        Assert.assertTrue(attribute1.shouldCollapseAttribute());

        Attribute attribute2 = new Attribute("name", "");
        Assert.assertTrue(attribute2.shouldCollapseAttribute());

        Attribute attribute3 = new Attribute("name", "value");
        Assert.assertFalse(attribute3.shouldCollapseAttribute());
    }

    // Test isBooleanAttribute() method
    public void testIsBooleanAttribute() {
        Attribute attribute1 = new Attribute("allowfullscreen", "value");
        Assert.assertTrue(attribute1.isBooleanAttribute());

        Attribute attribute2 = new Attribute("name", "value");
        Assert.assertFalse(attribute2.isBooleanAttribute());
    }

    // Test equals() method
    public void testEquals() {
        Attribute attribute1 = new Attribute("name", "value1");
        Attribute attribute2 = new Attribute("name", "value1");
        Attribute attribute3 = new Attribute("name", "value2");

        Assert.assertTrue(attribute1.equals(attribute2));
        Assert.assertFalse(attribute1.equals(attribute3));
    }

    // Test hashCode() method
    public void testHashCode() {
        Attribute attribute1 = new Attribute("name", "value1");
        Attribute attribute2 = new Attribute("name", "value1");
        Attribute attribute3 = new Attribute("name", "value2");

        Assert.assertEquals(attribute1.hashCode(), attribute2.hashCode());
        Assert.assertNotEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    // Test clone() method
    public void testClone() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = attribute1.clone();

        Assert.assertEquals(attribute1, attribute2);
    }
}