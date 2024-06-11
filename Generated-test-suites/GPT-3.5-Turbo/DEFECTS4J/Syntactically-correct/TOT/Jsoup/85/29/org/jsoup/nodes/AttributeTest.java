import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

public class AttributeTest {
    private Attribute attribute;
    private Attributes parent;

    @Before
    public void setUp() {
        parent = new Attributes();
    }

    @Test
    public void testConstructor_withValidKeyAndValue_success() {
        attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNullKey_throwIllegalArgumentException() {
        attribute = new Attribute(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withEmptyKey_throwIllegalArgumentException() {
        attribute = new Attribute("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKey_withNullKey_throwIllegalArgumentException() {
        attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKey_withEmptyKey_throwIllegalArgumentException() {
        attribute = new Attribute("key", "value");
        attribute.setKey("");
    }

    @Test
    public void testSetKey_withValidKey_success() {
        attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue_success() {
        attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        Assert.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml_success() {
        attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testToString_success() {
        attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded_success() {
        attribute = Attribute.createFromEncoded("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute_withDataAttributeKey_returnTrue() {
        attribute = new Attribute("data-key", "value");
        Assert.assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsDataAttribute_withNonDataAttributeKey_returnFalse() {
        attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute_withEmptyValue_returnTrue() {
        attribute = new Attribute("key", "");
        Assert.assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttribute_withSameValueAsKey_returnTrue() {
        attribute = new Attribute("key", "key");
        Assert.assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttribute_withNonBooleanAttribute_returnFalse() {
        attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute_withBooleanAttribute_returnTrue() {
        attribute = new Attribute("required", null);
        Assert.assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsBooleanAttribute_withNonBooleanAttribute_returnFalse() {
        attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals_withEqualAttribute_returnTrue() {
        attribute = new Attribute("key", "value");
        Attribute otherAttribute = new Attribute("key", "value");
        Assert.assertEquals(attribute, otherAttribute);
    }

    @Test
    public void testEquals_withDifferentAttribute_returnFalse() {
        attribute = new Attribute("key", "value");
        Attribute otherAttribute = new Attribute("key", "newValue");
        Assert.assertNotEquals(attribute, otherAttribute);
    }

    @Test
    public void testHashCode_withEqualAttribute_returnEqualHashCodes() {
        attribute = new Attribute("key", "value");
        Attribute otherAttribute = new Attribute("key", "value");
        Assert.assertEquals(attribute.hashCode(), otherAttribute.hashCode());
    }

    @Test
    public void testHashCode_withDifferentAttribute_returnDifferentHashCodes() {
        attribute = new Attribute("key", "value");
        Attribute otherAttribute = new Attribute("key", "newValue");
        Assert.assertNotEquals(attribute.hashCode(), otherAttribute.hashCode());
    }

    @Test
    public void testClone_withAttribute_returnClonedAttribute() {
        attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}