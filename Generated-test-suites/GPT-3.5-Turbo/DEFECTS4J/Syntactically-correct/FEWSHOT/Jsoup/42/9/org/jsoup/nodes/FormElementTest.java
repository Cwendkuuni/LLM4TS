package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * A HTML Form Element provides ready access to the form fields/controls that are associated with it. It also allows a
 * form to easily be submitted.
 */
public class FormElementTest {
    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        FormElement result = formElement.addElement(element);

        assertEquals(1, result.elements().size());
        assertEquals(element, result.elements().get(0));
    }

    @Test
    public void testSubmit() {
        Connection connection = formElement.submit();

        assertEquals("http://example.com", connection.request().url());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        Element element1 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element1.attr("name", "username");
        element1.attr("value", "john.doe");

        Element element2 = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        element2.attr("name", "password");
        element2.attr("value", "pass123");

        FormElement result = formElement.addElement(element1).addElement(element2);
        List<Connection.KeyVal> formData = result.formData();

        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("john.doe", formData.get(0).value());
        assertEquals("password", formData.get(1).key());
        assertEquals("pass123", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertEquals(formElement, formElement2);
    }
}