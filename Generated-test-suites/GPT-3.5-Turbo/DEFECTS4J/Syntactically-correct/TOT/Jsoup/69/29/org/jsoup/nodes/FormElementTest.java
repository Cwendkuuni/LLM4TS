import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        // Test empty elements list
        assertTrue(formElement.elements().isEmpty());

        // Test adding elements to the list
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element1);
        assertEquals(1, formElement.elements().size());
        assertTrue(formElement.elements().contains(element1));

        Element element2 = new Element(Tag.valueOf("textarea"), "", new Attributes());
        formElement.addElement(element2);
        assertEquals(2, formElement.elements().size());
        assertTrue(formElement.elements().contains(element2));
    }

    @Test
    public void testSubmit() {
        // Test submit with absolute action URL
        formElement.attr("action", "http://example.com/form");
        formElement.attr("method", "POST");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com/form", connection.request().url().toString());

        // Test submit with relative action URL
        formElement.removeAttr("action");
        Connection connection2 = formElement.submit();
        assertNotNull(connection2);
        assertEquals(Connection.Method.GET, connection2.request().method());
        assertEquals("http://example.com", connection2.request().url().toString());

        // Test submit without action URL
        formElement.removeAttr("method");
        try {
            formElement.submit();
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Could not determine a form action URL for submit. Ensure you set a base URI when parsing.", e.getMessage());
        }
    }

    @Test
    public void testFormData() {
        // Test empty form data
        assertEquals(0, formElement.formData().size());

        // Test adding form data for different form control elements
        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "select");
        selectElement.append("<option value=\"1\" selected>Option 1</option>");
        selectElement.append("<option value=\"2\">Option 2</option>");
        formElement.addElement(selectElement);
        assertEquals(1, formElement.formData().size());
        assertEquals("select", formElement.formData().get(0).key());
        assertEquals("1", formElement.formData().get(0).value());

        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "checked");
        formElement.addElement(checkboxElement);
        assertEquals(2, formElement.formData().size());
        assertEquals("checkbox", formElement.formData().get(1).key());
        assertEquals("on", formElement.formData().get(1).value());

        Element radioElement = new Element(Tag.valueOf("input"), "", new Attributes());
        radioElement.attr("name", "radio");
        radioElement.attr("type", "radio");
        formElement.addElement(radioElement);
        assertEquals(2, formElement.formData().size());

        radioElement.attr("checked", "checked");
        assertEquals(3, formElement.formData().size());
        assertEquals("radio", formElement.formData().get(2).key());
        assertEquals("", formElement.formData().get(2).value());

        radioElement.removeAttr("checked");
        assertEquals(2, formElement.formData().size());

        Element textElement = new Element(Tag.valueOf("input"), "", new Attributes());
        textElement.attr("name", "text");
        textElement.attr("type", "text");
        textElement.attr("value", "Hello World");
        formElement.addElement(textElement);
        assertEquals(3, formElement.formData().size());
        assertEquals("text", formElement.formData().get(2).key());
        assertEquals("Hello World", formElement.formData().get(2).value());
    }
}