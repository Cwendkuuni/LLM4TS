
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AttributeTest {

    private Attribute attribute;

    @Mock
    private Attributes parent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        attribute = new Attribute("key", "value", parent);
    }

    @Test
    public void testGetKey() {
        assertEquals("key", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newKey");
        assertEquals("newKey", attribute.getKey());
    }

    @Test
    public void testSetValue() {
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        String html = attribute.html();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testHtmlWithEmptyValue() {
        attribute.setValue("");
        String html = attribute.html();
        assertEquals("key", html);
    }

    @Test
    public void testHtmlWithNullValue() {
        attribute.setValue(null);
        String html = attribute.html();
        assertEquals("key", html);
    }

    @Test
    public void testToString() {
        String html = attribute.toString();
        assertEquals("key=\"value\"", html);
    }

    @Test
    public void testToStringWithEmptyValue() {
        attribute.setValue("");
        String html = attribute.toString();
        assertEquals("key", html);
    }

    @Test
    public void testToStringWithNullValue() {
        attribute.setValue(null);
        String html = attribute.toString();
        assertEquals("key", html);
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute newAttribute = Attribute.createFromEncoded("key", "&lt;value&gt;");
        assertEquals("value", newAttribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testIsNotDataAttribute() {
        Attribute newAttribute = new Attribute("notDataKey", "value", parent);
        assertFalse(newAttribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithEmptyValue() {
        attribute.setValue("");
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testShouldCollapseAttributeWithNullValue() {
        attribute.setValue(null);
        assertTrue(attribute.shouldCollapseAttribute(new Document.OutputSettings()));
    }

    @Test
    public void testIsBooleanAttribute() {
        assertTrue(attribute.isBooleanAttribute());
    }

    @Test
    public void testIsNotBooleanAttribute() {
        Attribute newAttribute = new Attribute("notBooleanKey", "value", parent);
        assertFalse(newAttribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute newAttribute = new Attribute("key", "value", parent);
        assertEquals(attribute, newAttribute);
    }

    @Test
    public void testHashCode() {
        Attribute newAttribute = new Attribute("key", "value", parent);
        assertEquals(attribute.hashCode(), newAttribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();
        assertEquals(attribute, clonedAttribute);
        assertNotSame(attribute, clonedAttribute);
    }

}

