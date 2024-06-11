
import org.jsoup.nodes.Attribute;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeTest {

    private Attribute attribute;

    @Before
    public void setUp() {
        attribute = new Attribute("name", "value");
    }

    @Test
    public void testGetKey() {
        assertEquals("name", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        attribute.setKey("newName");
        assertEquals("newName", attribute.getKey());

        attribute.setKey("");
        assertEquals("", attribute.getKey());

        attribute.setKey(" ");
        assertEquals(" ", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        assertEquals("value", attribute.setValue("newValue"));
        assertEquals("newValue", attribute.getValue());

        attribute.setValue("");
        assertEquals("", attribute.getValue());

        attribute.setValue(" ");
        assertEquals(" ", attribute.getValue());
    }

    @Test
    public void testHtml() {
        assertEquals("name=\"value\"", attribute.html());

        attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.html());

        attribute = new Attribute("name", "");
        assertEquals("name=\"\"", attribute.html());

        attribute = new Attribute("name", null);
        assertEquals("name=\"\"", attribute.html());
    }

    @Test
    public void testToString() {
        assertEquals("name=\"value\"", attribute.toString());

        attribute = new Attribute("href", "index.html");
        assertEquals("href=\"index.html\"", attribute.toString());

        attribute = new Attribute("name", "");
        assertEquals("name=\"\"", attribute.toString());

        attribute = new Attribute("name", null);
        assertEquals("name=\"\"", attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        attribute = Attribute.createFromEncoded("name", "attribute &lt;value&gt;");
        assertEquals("name", attribute.getKey());
        assertEquals("attribute <value>", attribute.getValue());

        attribute = Attribute.createFromEncoded("name", "");
        assertEquals("name", attribute.getKey());
        assertEquals("", attribute.getValue());

        attribute = Attribute.createFromEncoded("name", null);
        assertEquals("name", attribute.getKey());
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data-name", "value");
        assertTrue(attribute.isDataAttribute());

        attribute = new Attribute("data-", "value");
        assertFalse(attribute.isDataAttribute());

        attribute = new Attribute("data","");
        assertTrue(attribute.isDataAttribute());

        attribute = new Attribute("data",null);
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute() {
        assertFalse(attribute.shouldCollapseAttribute());

        attribute = new Attribute("autofocus", "");
        assertTrue(attribute.shouldCollapseAttribute());

        attribute = new Attribute("checked", "checked");
        assertTrue(attribute.shouldCollapseAttribute());

        attribute = new Attribute("readonly", "readonly");
        assertFalse(attribute.shouldCollapseAttribute());

        attribute = new Attribute("disabled", "");
        assertTrue(attribute.shouldCollapseAttribute());

        attribute = new Attribute("required", "required");
        assertFalse(attribute.shouldCollapseAttribute());
    }

    @Test
    public void testIsBooleanAttribute() {
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("async", null);
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("multiple", "multiple");
        assertFalse(attribute.isBooleanAttribute());

        attribute = new Attribute("hidden", "hidden");
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("formnovalidate", "");
        assertTrue(attribute.isBooleanAttribute());

        attribute = new Attribute("typemustmatch", "typemustmatch");
        assertFalse(attribute.isBooleanAttribute());
    }

    @Test
    public void testEquals() {
        Attribute sameAttribute = new Attribute("name", "value");
        Attribute differentAttribute = new Attribute("name", "differentValue");
        Attribute nullValueAttribute = new Attribute("name", null);
        Attribute nullAttribute = new Attribute(null, null);

        assertTrue(attribute.equals(sameAttribute));
        assertFalse(attribute.equals(differentAttribute));
        assertFalse(attribute.equals(nullValueAttribute));
        assertFalse(attribute.equals(nullAttribute));
    }

    @Test
    public void testHashCode() {
        Attribute sameAttribute = new Attribute("name", "value");

        assertEquals(sameAttribute.hashCode(), attribute.hashCode());
    }

    @Test
    public void testClone() {
        Attribute clonedAttribute = attribute.clone();

        assertNotSame(attribute, clonedAttribute);
        assertEquals(attribute.getKey(), clonedAttribute.getKey());
        assertEquals(attribute.getValue(), clonedAttribute.getValue());
    }
}
