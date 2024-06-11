package org.jsoup.nodes;

import org.jsoup.SerializationException;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class AttributeTest {
    private Attribute attribute1;
    private Attribute attribute2;

    @Before
    public void setUp() {
        attribute1 = new Attribute("key1", "value1");
        attribute2 = new Attribute("key2", "value2");
    }

    @Test
    public void createAttribute_keyAndValue_attributeCreated() {
        assertEquals("key1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());
    }

    @Test
    public void createAttribute_keyValueAndParent_attributeCreatedWithParent() {
        Attributes parent = new Attributes();
        attribute1 = new Attribute("key1", "value1", parent);
        assertEquals("key1", attribute1.getKey());
        assertEquals("value1", attribute1.getValue());
        assertEquals(parent, attribute1.getParent());
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAttribute_nullKey_exceptionThrown() {
        attribute1 = new Attribute(null, "value1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createAttribute_emptyKey_exceptionThrown() {
        attribute1 = new Attribute("", "value1");
    }

    @Test
    public void setKey_newKey_keyChanged() {
        attribute1.setKey("newKey");
        assertEquals("newKey", attribute1.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setKey_nullKey_exceptionThrown() {
        attribute1.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setKey_emptyKey_exceptionThrown() {
        attribute1.setKey("");
    }

    @Test
    public void setValue_newValue_valueChanged() {
        attribute1.setValue("newValue");
        assertEquals("newValue", attribute1.getValue());
    }

    @Test
    public void setValue_newValue_previousValueReturned() {
        String oldValue = attribute1.getValue();
        assertEquals("value1", attribute1.setValue("newValue"));
        assertNotEquals(oldValue, attribute1.getValue());
    }

    @Test
    public void html_htmlStringReturned() {
        assertEquals("key1=\"value1\"", attribute1.html());
    }
    
    @Test(expected = SerializationException.class)
    public void html_ioExceptionThrown() throws IOException {
        attribute1.html(null, null);
    }
    
    @Test
    public void html_appendableProvided_htmlStringAppended() throws IOException {
        StringBuilder sb = new StringBuilder();
        attribute1.html(sb, null);
        assertEquals("key1=\"value1\"", sb.toString());
    }

    @Test
    public void toString_htmlStringReturned() {
        assertEquals("key1=\"value1\"", attribute1.toString());
    }

    @Test
    public void createFromEncoded_unencodedKeyAndEncodedValue_attributeCreated() {
        Attribute attribute = Attribute.createFromEncoded("key1", "value1");
        assertEquals("key1", attribute.getKey());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void isDataAttribute_keyIsDataAttribute_trueReturned() {
        assertTrue(attribute1.isDataAttribute());
    }

    @Test
    public void shouldCollapseAttribute_attributeEmptyOrSameAsName_trueReturned() {
        assertTrue(attribute1.shouldCollapseAttribute(null));
    }

    @Test
    public void isBooleanAttribute_keyIsBooleanAttribute_trueReturned() {
        assertTrue(attribute1.isBooleanAttribute());
    }

    @Test
    public void equals_equalAttributes_trueReturned() {
        assertTrue(attribute1.equals(attribute1));
    }

    @Test
    public void equals_differentAttributes_falseReturned() {
        assertNotEquals(attribute1, attribute2);
    }

    @Test
    public void hashCode_equalAttributes_sameHashCodes() {
        assertEquals(attribute1.hashCode(), attribute1.hashCode());
    }

    @Test
    public void hashCode_differentAttributes_differentHashCodes() {
        assertNotEquals(attribute1.hashCode(), attribute2.hashCode());
    }

    @Test
    public void clone_attributeCloned() {
        Attribute clonedAttribute = attribute1.clone();
        assertEquals(attribute1.getKey(), clonedAttribute.getKey());
        assertEquals(attribute1.getValue(), clonedAttribute.getValue());
    }
}