import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AttributeTest {

    private Attribute attribute;

    @BeforeEach
    public void setup() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testGetKey() {
        Assertions.assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        Assertions.assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Assertions.assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        Assertions.assertEquals("value", oldValue);
        Assertions.assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Assertions.assertEquals("key=\"value\"", attribute.html());
    }

    @Test
    public void testFromString() {
        Attribute fromString = Attribute.createFromEncoded("key", "value&amp;");
        Assertions.assertEquals("key", fromString.getKey());
        Assertions.assertEquals("value&", fromString.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Assertions.assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        Assertions.assertTrue(dataAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        // Empty value
        Assertions.assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));

        // Same as key value
        attribute.setKey("collapsed");
        attribute.setValue("collapsed");
        Assertions.assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));

        // Non-boolean attribute
        attribute.setKey("href");
        attribute.setValue("example.com");
        Assertions.assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));

        // Boolean attribute
        attribute.setKey("checked");
        attribute.setValue("");
        Assertions.assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        Assertions.assertFalse(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", "");
        Assertions.assertTrue(booleanAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute equalAttribute = new Attribute("key", "value");
        Assertions.assertTrue(attribute.equals(equalAttribute));

        Attribute differentKey = new Attribute("differentKey", "value");
        Assertions.assertFalse(attribute.equals(differentKey));

        Attribute differentValue = new Attribute("key", "differentValue");
        Assertions.assertFalse(attribute.equals(differentValue));

        Attribute nullValue = new Attribute("key", null);
        Assertions.assertFalse(attribute.equals(nullValue));

        Assertions.assertTrue(attribute.equals(attribute));
    }

    @Test
    public void testHashCode() {
        Attribute equalAttribute = new Attribute("key", "value");
        Assertions.assertEquals(attribute.hashCode(), equalAttribute.hashCode());

        Attribute differentKey = new Attribute("differentKey", "value");
        Assertions.assertNotEquals(attribute.hashCode(), differentKey.hashCode());

        Attribute differentValue = new Attribute("key", "differentValue");
        Assertions.assertNotEquals(attribute.hashCode(), differentValue.hashCode());

        Attribute nullValue = new Attribute("key", null);
        Assertions.assertNotEquals(attribute.hashCode(), nullValue.hashCode());

        Assertions.assertEquals(attribute.hashCode(), attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        Assertions.assertEquals(attribute.getKey(), clonedAttribute.getKey());
        Assertions.assertEquals(attribute.getValue(), clonedAttribute.getValue());
        Assertions.assertNotSame(attribute, clonedAttribute);
    }
}