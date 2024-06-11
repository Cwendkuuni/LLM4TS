import org.junit.*;
import static org.junit.Assert.*;

public class AttributeTest {

    @Test
    public void testGetKey() {
        Attribute attribute = new Attribute("name", "value");
        assertEquals("name", attribute.getKey());
    }

    @Test
    public void testSetKey() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setKey("newName");
        assertEquals("newName", attribute.getKey());
    }

    @Test
    public void testGetValue() {
        Attribute attribute = new Attribute("name", "value");
        assertEquals("value", attribute.getValue());
    }

    @Test
    public void testSetValue() {
        Attribute attribute = new Attribute("name", "value");
        attribute.setValue("newValue");
        assertEquals("newValue", attribute.getValue());
    }

    @Test
    public void testHtml() {
        Attribute attribute = new Attribute("name", "value");
        String expectedHtml = "name=\"value\"";
        assertEquals(expectedHtml, attribute.html());
    }

    @Test
    public void testToString() {
        Attribute attribute = new Attribute("name", "value");
        String expectedString = "name=\"value\"";
        assertEquals(expectedString, attribute.toString());
    }

    @Test
    public void testCreateFromEncoded() {
        Attribute attribute = Attribute.createFromEncoded("name", "&quot;value&quot;");
        String expectedValue = "\"value\"";
        assertEquals(expectedValue, attribute.getValue());
    }

    @Test
    public void testIsDataAttribute() {
        Attribute attribute = new Attribute("data-name", "value");
        assertTrue(attribute.isDataAttribute());
    }

    @Test
    public void testShouldCollapseAttribute_withEmptyValue() {
        Attribute attribute = new Attribute("name", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttribute_withSameValueAsKey() {
        Attribute attribute = new Attribute("name", "name");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        assertTrue(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testShouldCollapseAttribute_withDifferentValue() {
        Attribute attribute = new Attribute("name", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.syntax(Document.OutputSettings.Syntax.html);
        assertFalse(attribute.shouldCollapseAttribute(outputSettings));
    }

    @Test
    public void testIsBooleanAttribute() {
        Attribute attribute = new Attribute("checked", null);
        assertTrue(attribute.isBooleanAttribute());
    }

    // Add more test cases including edge cases and error scenarios

}