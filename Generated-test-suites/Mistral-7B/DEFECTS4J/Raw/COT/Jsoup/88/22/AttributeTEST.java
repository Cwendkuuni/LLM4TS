import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attributes.AttributesNotFound;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

public class AttributeTest {

    @Test
    public void testAttributeCreationWithKeyAndValue() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testAttributeCreationWithKeyValueAndParent() {
        Attributes parent = new Attributes();
        Attribute attribute = new Attribute("key", "value", parent);
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
        Assert.assertNotNull(attribute.parent);
        Assert.assertEquals(parent, attribute.parent);
    }

    @Test
    public void testSetKeyMethod() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        Assert.assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyMethodWithNullKey() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey(null);
    }

    @Test
    public void testSetValueMethod() {
        Attribute attribute = new Attribute("key", "value");
        String newValue = "newValue";
        String oldValue = attribute.setValue(newValue);
        Assert.assertEquals(newValue, attribute.getValue());
        Assert.assertEquals("value", oldValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueMethodWithNullValue() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue(null);
    }

    @Test
    public void testHtmlMethodWithEmptyAppendable() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);
        Assert.assertEquals("", sb.toString());
        StringUtil.releaseBuilder(sb);
    }

    @Test
    public void testHtmlMethod() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, null);
        Assert.assertEquals("key=\"value\"", sb.toString());
        StringUtil.releaseBuilder(sb);
    }

    @Test
    public void testHtmlMethodWithDocumentOutputSettings() {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        attribute.html(sb, outputSettings);
        Assert.assertEquals("key=\"value\"", sb.toString());
        StringUtil.releaseBuilder(sb);
    }

    @Test
    public void testCreateFromEncodedMethod() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        Assert.assertEquals("key", attribute.getKey());
        Assert.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsDataAttributeMethod() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.isDataAttribute());

        Attribute dataAttribute = new Attribute(Attributes.dataPrefix + "dataKey", "value");
        Assert.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttributeMethodWithNullOut() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertFalse(attribute.shouldCollapseAttribute(null));
    }

    @Test
    public void testShouldCollapseAttributeMethodWithEmptyOut() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeMethodWithNonBooleanAttribute() {
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeMethodWithBooleanAttributeAndEmptyValue() {
        Attribute attribute = new Attribute(Attributes.checked, null);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeMethodWithBooleanAttributeAndSameValueAsKey() {
        Attribute attribute = new Attribute(Attributes.checked, "checked");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeMethodWithBooleanAttributeAndDifferentValueAsKey() {
        Attribute attribute = new Attribute(Attributes.checked, "notChecked");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Assert.assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttributeMethod() {
        Attribute attribute = new Attribute("key", null);
        Assert.assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("booleanAttribute", null);
        Assert.assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("nonBooleanAttribute", "value");
        Assert.assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEqualsMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        Assert.assertFalse(attribute1.equals(attribute2));
        Assert.assertTrue(attribute1.equals(attribute3));
    }

    @Test
    public void testHashCodeMethod() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key2", "value2");
        Attribute attribute3 = new Attribute("key1", "value1");

        Assert.assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
        Assert.assertEquals(attribute1.hashCode(), attribute3.hashCode());
    }

    @Test
    public void testCloneMethod() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = attribute.clone();

        Assert.assertEquals(attribute, clonedAttribute);
        Assert.assertNotSame(attribute, clonedAttribute);
        Assert.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assert.assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneMethodWithException() {
        Attribute attribute = new Attribute("key", "value");
        Attribute clonedAttribute = (Attribute) attribute.clone();
    }

    @Test
    public void testToStringMethod() {
        Attribute attribute = new Attribute("key", "value");
        Assert.assertEquals("key=\"value\"", attribute.toString());
    }
}