import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key1", "value1");
    }

    @Test
    public void testGetKey() {
        Assert.assertEquals("key1", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        String expectedHtml = "key1=\"value1\"";
        Assert.assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        String expectedString = "key1=\"value1\"";
        Assert.assertEquals(expectedString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key2", "value&amp;");
        Assert.assertEquals("value&", encodedAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        boolean isData = attribute.isDataAttribute();
        Assert.assertFalse(isData);
    }

    @Test
    public void testShouldCollapseAttribute() {
        boolean shouldCollapse = attribute.shouldCollapseAttribute(new Document.OutputSettings());
        Assert.assertFalse(shouldCollapse);
    }

    @Test
    public void testEquals() {
        Attribute attribute2 = new Attribute("key1", "value1");
        Assert.assertEquals(attribute, attribute2);
    }

    @Test
    public void testHashCode() {
        Attribute attribute2 = new Attribute("key1", "value1");
        Assert.assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        Assert.assertNotSame(attribute, clonedAttribute);
        Assert.assertEquals(attribute, clonedAttribute);
    }
}