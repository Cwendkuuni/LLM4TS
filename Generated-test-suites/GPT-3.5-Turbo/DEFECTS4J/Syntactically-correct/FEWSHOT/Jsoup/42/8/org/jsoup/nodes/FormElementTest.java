package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

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
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        assertEquals(0, formElement.elements.size());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input1"));
        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input2"));

        formElement.addElement(element1);
        formElement.addElement(element2);

        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertEquals(element1, elements.get(0));
        assertEquals(element2, elements.get(1));
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        assertEquals(0, formElement.elements.size());

        Element element = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input"));
        formElement.addElement(element);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("action", "https://example.com");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();
        assertEquals("https://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitWithoutAction() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();
        assertEquals("", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutBaseUri() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.attr("action", "relative-url");
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());

        Element element1 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input1").put("value", "value1"));
        Element element2 = new Element(Tag.valueOf("input"), "", new Attributes().put("name", "input2").put("value", "value2"));

        formElement.addElement(element1);
        formElement.addElement(element2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("input2", formData.get(1).key());
        assertEquals("value2", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "", new Attributes());
        FormElement formElement3 = new FormElement(Tag.valueOf("input"), "", new Attributes());

        assertEquals(formElement1, formElement2);
        assertNotEquals(formElement1, formElement3);
        assertEquals(formElement1, formElement1);
        assertNotEquals(formElement1, null);
    }
}