package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.ValidationException;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FormElementTest {
    @Test
    public void testConstructor() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        assertEquals(tag, formElement.tag());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void testElements() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Elements elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(0, elements.size());

        Element element = new Element(Tag.valueOf("input"), baseUri);
        formElement.addElement(element);

        elements = formElement.elements();
        assertNotNull(elements);
        assertEquals(1, elements.size());
        assertEquals(element, elements.first());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithNoActionThrowsException() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        formElement.submit();
    }

    @Test
    public void testSubmitWithEmptyForm() throws Exception {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com");
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Connection connection = formElement.submit();

        assertEquals(Connection.Method.GET, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
    }

    @Test
    public void testSubmitWithFormData() throws Exception {
        Tag tag = Tag.valueOf("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "http://example.com");
        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element inputElement = new Element(Tag.valueOf("input"), baseUri);
        inputElement.attr("name", "username");
        inputElement.attr("value", "jsmith");
        formElement.addElement(inputElement);

        Element checkboxElement = new Element(Tag.valueOf("input"), baseUri);
        checkboxElement.attr("name", "rememberMe");
        checkboxElement.attr("type", "checkbox");
        checkboxElement.attr("checked", "");
        formElement.addElement(checkboxElement);

        Connection connection = formElement.submit();

        assertEquals(Connection.Method.POST, connection.request().method());
        assertEquals("http://example.com", connection.request().url().toString());
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("jsmith", formData.get(0).value());
        assertEquals("rememberMe", formData.get(1).key());
        assertEquals("on", formData.get(1).value());
    }
}