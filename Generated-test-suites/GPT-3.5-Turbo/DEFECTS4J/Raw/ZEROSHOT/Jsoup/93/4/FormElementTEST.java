package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    private FormElement formElement;

    @Before
    public void setUp() {
        formElement = new FormElement(Tag.valueOf("form"), "http://example.com", new Attributes());
    }

    @Test
    public void testElements() {
        assertEquals(new Elements(), formElement.elements());
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        assertEquals(formElement, formElement.addElement(element));
        assertEquals(1, formElement.elements().size());
        assertEquals(element, formElement.elements().get(0));
    }

    @Test
    public void testRemoveChild() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        formElement.addElement(element);
        assertEquals(1, formElement.elements().size());
        formElement.removeChild(element);
        assertEquals(0, formElement.elements().size());
    }

    @Test
    public void testSubmit() {
        Element element = new Element(Tag.valueOf("input"), "http://example.com");
        element.attr("name", "username");
        element.attr("value", "testuser");
        formElement.addElement(element);
        Connection connection = formElement.submit();
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals("http://example.com", connection.request().url().toString());
        assertEquals("username=testuser", connection.request().data());
    }

    @Test
    public void testFormData() {
        Element inputElement = new Element(Tag.valueOf("input"), "http://example.com");
        inputElement.attr("name", "username");
        inputElement.attr("value", "testuser");
        Element selectElement = new Element(Tag.valueOf("select"), "http://example.com");
        selectElement.attr("name", "gender");
        Element optionElement = new Element(Tag.valueOf("option"), "http://example.com");
        optionElement.attr("value", "male");
        optionElement.attr("selected", "");
        selectElement.appendChild(optionElement);
        formElement.addElement(inputElement);
        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("testuser", formData.get(0).value());
        assertEquals("gender", formData.get(1).key());
        assertEquals("male", formData.get(1).value());
    }

}