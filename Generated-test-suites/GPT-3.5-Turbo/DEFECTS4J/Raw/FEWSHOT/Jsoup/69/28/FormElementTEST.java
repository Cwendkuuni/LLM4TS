
package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * A HTML Form Element provides ready access to the form fields/controls that are associated with it. It also allows a
 * form to easily be submitted.
 */
public class FormElementTest {

    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.form, "http://example.com", new Attributes());
        assertEquals(0, formElement.elements().size());

        Element element1 = new Element(Tag.input, "http://example.com", new Attributes());
        Element element2 = new Element(Tag.select, "http://example.com", new Attributes());

        formElement.addElement(element1);
        formElement.addElement(element2);
        assertEquals(2, formElement.elements().size());
        assertEquals(element1, formElement.elements().get(0));
        assertEquals(element2, formElement.elements().get(1));
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.form, "http://example.com", new Attributes());
        Element element1 = new Element(Tag.input, "http://example.com", new Attributes());
        Element element2 = new Element(Tag.select, "http://example.com", new Attributes());

        formElement.addElement(element1);
        assertEquals(1, formElement.elements().size());
        assertEquals(element1, formElement.elements().get(0));

        formElement.addElement(element2);
        assertEquals(2, formElement.elements().size());
        assertEquals(element2, formElement.elements().get(1));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.form, "http://example.com", new Attributes());

        Connection connection = formElement.submit();
        assertNull(connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());

        formElement.attr("action", "/submit");
        formElement.attr("method", "POST");

        connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com/submit", connection.request().url().toString());
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.form, "http://example.com", new Attributes());

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        Element inputElement = new Element(Tag.input, "http://example.com", new Attributes());
        inputElement.attr("name", "username");
        inputElement.val("johndoe");

        formElement.addElement(inputElement);

        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("johndoe", formData.get(0).value());
    }
}
