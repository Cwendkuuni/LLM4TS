package org.jsoup.nodes;

import static org.junit.Assert.*;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test class for Attribute.java
 */
public class AttributeTest {

    @Test
    public void testAttributeCreation() {
        // Test creating an Attribute with raw key and value
        Attribute attribute = new Attribute("key", "value");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());

        // Test creating an Attribute with raw key, value and parent
        Attributes attributes = new Attributes();
        Attribute attributeParent = new Attribute("parentKey", "parentValue", attributes);
        Attribute newAttribute = new Attribute("newKey", "newValue", attributeParent);
        assertEquals("newKey", newAttribute.getKey());
        assertEquals("newValue", newAttribute.getValue());
        assertEquals(newAttribute, attributeParent.put("newKey", "newValue"));
        assertEquals(newAttribute, attributeParent.get("newKey"));
    }

    @Test
    public void testAttributeKeySet() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());

        Attributes attributes = new Attributes();
        Attribute attributeParent = new Attribute("parentKey", "parentValue", attributes);
        Attribute attributeToUpdate = new Attribute("keyToUpdate", "valueToUpdate", attributeParent);
        attributeToUpdate.setKey("newKeyToUpdate");
        assertEquals("newKeyToUpdate", attributeToUpdate.getKey());
        assertEquals(attributeToUpdate, attributeParent.get("newKeyToUpdate"));
    }

    @Test
    public void testAttributeValueSet() {
        Attribute attribute = new Attribute("key", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());

        Attributes attributes = new Attributes();
        Attribute attributeParent = new Attribute("parentKey", "parentValue", attributes);
        Attribute attributeToUpdate = new Attribute("keyToUpdate", "valueToUpdate", attributeParent);
        attributeToUpdate.setValue("newValueToUpdate");
        assertEquals("newValueToUpdate", attributeToUpdate.getValue());
        assertEquals(attributeToUpdate, attributeParent.get("keyToUpdate"));
    }

    @Test
    public void testAttributeHtml() throws IOException {
        Attribute attribute = new Attribute("key", "value");
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", sb.toString());
    }

    @Test
    public void testAttributeCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("booleanAttribute", "");
        assertTrue(attribute.isBooleanAttribute());

        Attribute attributeNotBoolean = new Attribute("nonBooleanAttribute", "value");
        assertFalse(attributeNotBoolean.isBooleanAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        Attribute attribute = new Attribute("booleanAttribute", "");
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        Attribute attributeXml = new Attribute("booleanAttribute", "");
        assertFalse(attributeXml.shouldCollapseAttribute(outputSettings));

        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        Attribute attributeHtml = new Attribute("booleanAttribute", "");
        assertTrue(attributeHtml.shouldCollapseAttribute(outputSettings));

        Attribute attributeNotBoolean = new Attribute("nonBooleanAttribute", "value");
        assertFalse(attributeNotBoolean.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testClone() {
        Attribute originalAttribute = new Attribute("key", "value");
        Attribute clonedAttribute = originalAttribute.clone();
        assertEquals(originalAttribute, clonedAttribute);
        assertNotSame(originalAttribute, clonedAttribute);
    }

    @Test
    public void testEqualsAndHashCode() {
        Attribute attribute1 = new Attribute("key1", "value1");
        Attribute attribute2 = new Attribute("key1", "value1");
        Attribute attribute3 = new Attribute("key2", "value2");

        assertTrue(attribute1.equals(attribute1));
        assertTrue(attribute1.equals(attribute2));
        assertFalse(attribute1.equals(attribute3));

        int hashCode1 = attribute1.hashCode();
        int hashCode2 = attribute2.hashCode();
        int hashCode3 = attribute3.hashCode();
        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
    }
}