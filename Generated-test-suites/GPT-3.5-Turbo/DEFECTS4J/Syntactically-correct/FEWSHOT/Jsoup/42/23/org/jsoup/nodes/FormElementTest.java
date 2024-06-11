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
import org.junit.Test;

public class FormElementTest {
    @Test
    public void testElements() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Elements elements = formElement.elements();
        assertEquals(0, elements.size());

        Element element1 = new Element(Tag.valueOf("input"), "http://example.com");
        formElement.addElement(element1);
        assertEquals(1, elements.size());
        assertEquals(element1, elements.get(0));

        Element element2 = new Element(Tag.valueOf("select"), "http://example.com");
        formElement.addElement(element2);
        assertEquals(2, elements.size());
        assertEquals(element2, elements.get(1));
    }

    @Test
    public void testAddElement() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element element = new Element(Tag.valueOf("input"), "http://example.com");

        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testSubmit() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.attr("action", "http://example.com");
        formElement.attr("method", "POST");

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals(Connection.Method.POST, connection.request().method());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmit_missingActionUrl() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        FormElement formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        inputElement.attr("name", "input1");
        inputElement.val("value1");
        formElement.addElement(inputElement);

        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com");
        selectElement.attr("name", "select1");
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com");
        optionElement.attr("selected", "");
        optionElement.val("option1");
        selectElement.appendChild(optionElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("input1", formData.get(0).key());
        assertEquals("value1", formData.get(0).value());
        assertEquals("select1", formData.get(1).key());
        assertEquals("option1", formData.get(1).value());
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
        assertTrue(formElement1.equals(formElement1));
        assertFalse(formElement1.equals(null));
        assertFalse(formElement1.equals("formElement1"));
        assertFalse(formElement1.equals(formElement2));
    }
}