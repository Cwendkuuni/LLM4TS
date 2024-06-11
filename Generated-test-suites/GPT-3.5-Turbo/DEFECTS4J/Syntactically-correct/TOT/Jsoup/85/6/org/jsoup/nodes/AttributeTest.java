import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("testKey", "testValue");
    }

    @Test
    public void getKey_normalCase() {
        assertEquals("testKey", attribute.getKey());
    }

    @Test
    public void setKey_normalCase() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = NullPointerException.class)
    public void setKey_nullKey() {
        attribute.setKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setKey_emptyKey() {
        attribute.setKey("");
    }

    @Test
    public void setKey_parentIsNull() {
        Attribute attributeWithParent = new Attribute("testKey", "testValue", null);

        attributeWithParent.setKey("newKey");
        assertEquals("newKey", attributeWithParent.getKey());
    }

    @Test
    public void setValue_normalCase() {
        assertEquals("testValue", attribute.setValue("newValue"));
    }

    @Test
    public void setValue_parentIsNull() {
        Attribute attributeWithParent = new Attribute("testKey", "testValue", null);

        assertEquals("testValue", attributeWithParent.setValue("newValue"));
    }

    @Test
    public void html_normalCase() {
        assertEquals("testKey=\"testValue\"", attribute.html());
    }

    @Test
    public void html_keyAndValueContainSpecialCharacters() {
        attribute = new Attribute("key\"&<>", "value\"&<>");
        assertEquals("key\"&amp;&lt;&gt;=\"value\"&amp;&lt;&gt;\"", attribute.html());
    }

    @Test
    public void createFromEncoded_normalCase() {
        Attribute encodedAttribute = Attribute.createFromEncoded("testKey", "testValue&amp;&lt;&gt;");
        assertEquals("testValue&<>", encodedAttribute.getValue());
    }

    @Test
    public void createFromEncoded_specialCharactersInKeyAndValue() {
        Attribute encodedAttribute = Attribute.createFromEncoded("key\"&<>", "value\"&<>");
        assertEquals("value\"&<>", encodedAttribute.getValue());
    }

    @Test
    public void isDataAttribute_normalKey() {
        assertFalse(attribute.isDataAttribute());
    }

    @Test
    public void isDataAttribute_dataAttributeKey() {
        attribute = new Attribute("data-test", "testValue");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void shouldCollapseAttribute_normalCase() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void shouldCollapseAttribute_emptyValue() {
        attribute = new Attribute("testKey", "");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void shouldCollapseAttribute_sameKeyAndValue() {
        attribute = new Attribute("testKey", "testKey");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void isBooleanAttribute_normalCase() {
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void isBooleanAttribute_booleanAttributeKey() {
        attribute = new Attribute("checked", "testValue");
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void equals_sameAttributes() {
        Attribute attribute2 = new Attribute("testKey", "testValue");
        assertTrue(attribute.equals(attribute2));
    }

    @Test
    public void equals_attributesWithDifferentKey() {
        Attribute attribute2 = new Attribute("differentKey", "testValue");
        assertFalse(attribute.equals(attribute2));
    }

    @Test
    public void equals_attributesWithDifferentValue() {
        Attribute attribute2 = new Attribute("testKey", "differentValue");
        assertFalse(attribute.equals(attribute2));
    }

    @Test
    public void equals_attributesWithNullKey() {
        Attribute attribute1 = new Attribute(null, "testValue");
        Attribute attribute2 = new Attribute(null, "testValue");
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void equals_attributesWithNullValue() {
        Attribute attribute1 = new Attribute("testKey", null);
        Attribute attribute2 = new Attribute("testKey", null);
        assertTrue(attribute1.equals(attribute2));
    }

    @Test
    public void hashCode_sameAttributes() {
        Attribute attribute2 = new Attribute("testKey", "testValue");
        assertEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void hashCode_attributesWithDifferentKey() {
        Attribute attribute2 = new Attribute("differentKey", "testValue");
        assertNotEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void hashCode_attributesWithDifferentValue() {
        Attribute attribute2 = new Attribute("testKey", "differentValue");
        assertNotEquals(attribute.hashCode(), attribute2.hashCode());
    }

    @Test
    public void clone_sameAttributes() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
    }

    @Test
    public void clone_changeKey() {
        Attribute clonedAttribute = attribute.clone();
        clonedAttribute.setKey("newKey");
        assertNotEquals(attribute, clonedAttribute);
    }

    @Test
    public void clone_changeValue() {
        Attribute clonedAttribute = attribute.clone();
        clonedAttribute.setValue("newValue");
        assertNotEquals(attribute, clonedAttribute);
    }
}