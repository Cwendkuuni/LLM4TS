import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        Assert.assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        Assert.assertEquals("value", oldValue);
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testhtml() {
        String expected = "key=\"value\"";
        Assert.assertEquals(expected, attribute.html());
    }

    @Test
    public void testToString() {
        String expected = "key=\"value\"";
        Assert.assertEquals(expected, attribute.toString());
    }

    @Test
    public void testcreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key", "encoded%20value");
        Assert.assertEquals("key", encodedAttribute.getKey());
        Assert.assertEquals("encoded value", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Assert.assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Assert.assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        Attribute booleanAttribute = new Attribute("checked", "");
        Assert.assertTrue(booleanAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
        Attribute emptyValueAttribute = new Attribute("key", "");
        Assert.assertTrue(emptyValueAttribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Assert.assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", "");
        Assert.assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertTrue(attribute.equals(attribute2));
    }

    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("key", "value");
        Assert.assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}