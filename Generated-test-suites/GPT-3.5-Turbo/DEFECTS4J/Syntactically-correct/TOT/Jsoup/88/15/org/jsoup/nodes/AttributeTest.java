
import org.junit.Test;
import org.junit.Before;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.helper.Validate;
import org.jsoup.SerializationException;
import java.io.IOException;
import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;
    private Attributes parent;

    @Before
    public void setUp() {
        parent = new Attributes();
        attribute = new Attribute("key", "value", parent);
    }

    // Test getKey() method
    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    // Test setKey() method
    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    // Test setValue() method
    @Test
    public void testSetValue() {
        String oldValue = attribute.setValue("newValue");
        assertEquals("value", oldValue);
        assertEquals("newValue", attribute.getValue());
    }

    // Test html() method
    @Test
    public void testHtml() {
        assertEquals("key=\"value\"", attribute.html());
    }

    // Test createFromEncoded() method
    @Test
    public void testCreateFromEncoded() {
        Attribute decodedAttribute = Attribute.createFromEncoded("key", "value");
        assertEquals(attribute.getKey(), decodedAttribute.getKey());
        assertEquals(attribute.getValue(), decodedAttribute.getValue());
    }

    // Test isDataAttribute() method
    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());
        Attribute dataAttribute = new Attribute("data-key", "value");
        assertTrue(dataAttribute.isDataAttribute());
    }

    // Test shouldCollapseAttribute() method
    @Test
    public void testShouldCollapseAttribute() {
        attribute.setValue("");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        attribute.setValue("key");
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
        attribute.setValue(null);
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    // Test isBooleanAttribute() method
    @Test
    public void testIsBooleanAttribute() {
        assertTrue(attribute.isBooleanAttribute());
        Attribute booleanAttribute = new Attribute("checked", null);
        assertTrue(booleanAttribute.isBooleanAttribute());
        Attribute nonBooleanAttribute = new Attribute("nonBoolean", "value");
        assertFalse(nonBooleanAttribute.isBooleanAttribute());
    }

    // Test equals() method
    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("key", "value", parent);
        Attribute differentAttribute = new Attribute("key2", "value2", parent);
        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
    }

    // Test clone() method
    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
        assertEquals(attribute.parent, clonedAttribute.parent);
    }

    // TODO: Add more test cases to cover all methods and edge cases

}

