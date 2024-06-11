package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class FormElementTest {
    private FormElement formElement;
    
    @Before
    public void setup() {
        formElement = new FormElement(Tag.form("form"), "http://example.com", new Attributes());

        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.attr("value", "testuser");

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com", new Attributes());
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com", new Attributes());
        optionElement.attr("value", "option1");
        optionElement.attr("selected", "");
        selectElement.appendChild(optionElement);

        formElement.addElement(inputElement);
        formElement.addElement(selectElement);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();

        assertNotNull(elements);
        assertEquals(2, elements.size());
    }

    @Test
    public void testAddElement() {
        Element newElement = new Element(Tag.valueOf("input"), "http://example.com", new Attributes());
        newElement.attr("type", "password");
        newElement.attr("name", "password");

        FormElement returnedFormElement = formElement.addElement(newElement);

        assertNotNull(returnedFormElement);
        assertEquals(3, formElement.elements().size());
    }

    @Test
    public void testRemoveChild() {
        Element inputElement = formElement.elements().get(0);
        formElement.removeChild(inputElement);

        assertEquals(1, formElement.elements().size());
        assertFalse(formElement.elements().contains(inputElement));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMissingActionUrl() {
        formElement.submit();
    }

    @Test
    public void testSubmitPost() {
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "post");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test
    public void testSubmitGet() {
        formElement.attr("action", "http://example.com/submit");
        formElement.attr("method", "get");

        Connection connection = formElement.submit();

        assertNotNull(connection);
        assertEquals("http://example.com/submit", connection.request().url().toString());
        assertEquals(Connection.Method.GET, connection.request().method());
    }

    @Test
    public void testFormData() {
        List<Connection.KeyVal> formData = formElement.formData();

        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());
        assertEquals("option1", formData.get(1).key());
    }
}