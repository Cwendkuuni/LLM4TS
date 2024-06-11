import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attribute;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setKey("newName");
        Assert.assertEquals("newName", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name=\"value\"", attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertEquals("name=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("name", "%20value");
        Assert.assertEquals("name", attribute.getKey());
        Assert.assertEquals(" value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Attribute attribute = new Attribute("name", "name");
        Assert.assertTrue(attribute.shouldCollapseAttribute());

        Attribute booleanAttribute = new Attribute("disabled", null);
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute());

        Attribute nonBooleanAttribute = new Attribute("name", "value");
        Assert.assertFalse(nonBooleanAttribute.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("name", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());

        Attribute booleanAttribute = new Attribute("disabled", null);
        Assert.assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute1 = new Attribute("name", "value");
        Attribute attribute2 = new Attribute("name", "value");
        Assert.assertEquals(attribute1, attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute = new Attribute("name", "value");
        int expectedHashCode = 31 * "name".hashCode() + "value".hashCode();
        Assert.assertEquals(expectedHashCode, attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute attribute = new Attribute("name", "value");
        Attribute clone = attribute.clone();
        Assert.assertEquals(attribute, clone);
    }
}