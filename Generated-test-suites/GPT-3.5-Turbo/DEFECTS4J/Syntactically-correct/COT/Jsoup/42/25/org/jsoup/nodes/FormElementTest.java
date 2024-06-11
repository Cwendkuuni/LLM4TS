
package org.jsoup.nodes;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        Elements elements = formElement.elements();

        assertNotNull(elements);
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com");

        FormElement result = formElement.addElement(element);

        assertEquals(formElement, result);
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attributes().put("action", "/submit");
        formElement.attributes().put("method", "POST");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_noActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        inputElement.attributes().put("name", "username");
        inputElement.attributes().put("type", "text");
        inputElement.attr("value", "test");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> data = formElement.formData();

        assertNotNull(data);
        assertEquals(1, data.size());
        assertEquals("username", data.get(0).key());
        assertEquals("test", data.get(0).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());

        assertTrue(formElement.equals(formElement));
        assertFalse(formElement.equals(null));
        assertFalse(formElement.equals(new Object()));
    }
}

