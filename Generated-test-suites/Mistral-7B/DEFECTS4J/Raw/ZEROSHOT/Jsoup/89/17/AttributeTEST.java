import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributeTest {

    Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("key", "value");
    }

    @Test
    public void testCreate() {
        assertNotNull(attribute);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetKeyNull() {
        attribute.setKey(null);
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueNull() {
        attribute.setValue(null);
    }

    @Test
    public void testHtml() throws Exception {
        StringBuilder sb = StringUtil.borrowBuilder();
        attribute.html(sb, new Document("").outputSettings());
        assertEquals("key=\"value\"", StringUtil.releaseBuilder(sb));
    }

    @Test
    public void testIsBooleanAttributeTrue() {
        assertTrue(Attribute.isBooleanAttribute("checked"));
    }

    @Test
    public void testIsBooleanAttributeFalse() {
        assertFalse(Attribute.isBooleanAttribute("data-test"));
    }

    @Test
    public void testShouldCollapseAttributeTrue() throws Exception {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttributeFalse() throws Exception {
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.xml);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attributeEncoded = Attribute.createFromEncoded("key", "key=\"value\"");
        assertEquals("key", attributeEncoded.getKey());
        assertEquals("value", attributeEncoded.getValue());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertEquals("key", clonedAttribute.getKey());
        assertEquals("value", clonedAttribute.getValue());
    }
}