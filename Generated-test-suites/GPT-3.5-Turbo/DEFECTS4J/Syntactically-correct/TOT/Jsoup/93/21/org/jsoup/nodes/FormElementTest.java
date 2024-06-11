import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setup() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        // Typical use case
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertTrue(elements.isEmpty());

        // Edge case: Add a form control element
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test
    public void testAddElement() {
        // Typical use case
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement result = formElement.addElement(element);
        assertEquals(formElement, result);
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().first());

        // Error scenario: Add null element
        try {
            formElement.addElement(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testSubmit() {
        // Typical use case
        formElement.attr("action", "http://example.com/submit");
        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());

        // Typical use case: POST method
        formElement.attr("method", "POST");
        connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.POST, connection.request().method());

        // Edge case: No action attribute
        formElement.removeAttr("action");
        try {
            formElement.submit();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testFormData() {
        // Typical use case
        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element1);
        element1.attr("name", "username");
        element1.val("JohnDoe");

        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element2);
        element2.attr("name", "password");
        element2.val("12345");

        List<Connection.KeyVal> data = formElement.formData();
        assertEquals(2, data.size());
        assertEquals(HttpConnection.KeyVal.create("username", "JohnDoe"), data.get(0));
        assertEquals(HttpConnection.KeyVal.create("password", "12345"), data.get(1));

        // Edge case: disabled form input
        Element element3 = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element3);
        element3.attr("name", "age");
        element3.attr("disabled", "disabled");
        data = formElement.formData();
        assertEquals(2, data.size());
        assertEquals(HttpConnection.KeyVal.create("username", "JohnDoe"), data.get(0));
        assertEquals(HttpConnection.KeyVal.create("password", "12345"), data.get(1));
    }
}