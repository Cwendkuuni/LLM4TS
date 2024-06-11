package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        // Create FormElement for testing
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        // Test elements() method
        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        // Test addElement() method
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertSame(element, elements.first());
    }

    @Test
    public void testRemoveChild() {
        // Test removeChild() method
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        formElement.removeChild(element);
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());
    }

    @Test
    public void testSubmit() {
        // Test submit() method - when action attribute is present
        formElement.attr("action", "http://example.com/action");
        formElement.attr("method", "post");
        Jsoup.connect("http://example.com/action").data("");

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connect.Method.POST, connection.method());
        assertEquals("http://example.com/action", connection.request().url().toString());

        // Test submit() method - when action attribute is not present
        formElement.attributes().remove("action");
        connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connect.Method.GET, connection.method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitNoBaseUri() {
        // Test submit() method when baseUri is not set
        FormElement formElement = new FormElement(Tag.valueOf("form"), "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        // Test formData() method
        Element inputElement = new Element(Tag.valueOf("input"), "", new Attributes());
        inputElement.attr("name", "input");
        inputElement.val("inputValue");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "", new Attributes());
        selectElement.attr("name", "select");
        Element optionElement = new Element(Tag.valueOf("option"), "", new Attributes());
        optionElement.attr("value", "optionValue");
        selectElement.addElement(optionElement);
        formElement.addElement(selectElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), "", new Attributes());
        checkboxElement.attr("name", "checkbox");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);

        List<Connection.KeyVal> data = formElement.formData();
        assertNotNull(data);
        assertEquals(3, data.size());
        assertEquals("input", data.get(0).key());
        assertEquals("inputValue", data.get(0).value());
        assertEquals("select", data.get(1).key());
        assertEquals("optionValue", data.get(1).value());
        assertEquals("checkbox", data.get(2).key());
        assertEquals("on", data.get(2).value());
    }
}