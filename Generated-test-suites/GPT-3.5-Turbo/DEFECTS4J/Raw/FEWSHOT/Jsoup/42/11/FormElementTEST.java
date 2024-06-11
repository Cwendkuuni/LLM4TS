package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void setUp() {
        Tag tag = Tag.valueOf("form");
        String baseUri = "https://www.example.com";
        Attributes attributes = new Attributes();
        formElement = new FormElement(tag, baseUri, attributes);
    }

    @Test
    public void testElements() {
        Elements elements = formElement.elements();
        assertTrue(elements.isEmpty());

        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testAddElement() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(element));
    }

    @Test
    public void testSubmit() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        element.attr("name", "username");
        element.attr("value", "user123");

        element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        element.attr("name", "password");
        element.attr("value", "pass123");

        Connection connection = formElement.submit();
        assertEquals("https://www.example.com", connection.request().url());
        assertEquals(Connection.Method.POST, connection.request().method());
        assertTrue(connection.request().data().contains(new HttpConnection.KeyVal("username", "user123")));
        assertTrue(connection.request().data().contains(new HttpConnection.KeyVal("password", "pass123")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitWithoutAction() {
        formElement.submit();
    }

    @Test
    public void testFormData() {
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        element.attr("name", "username");
        element.attr("value", "user123");

        element = new Element(Tag.valueOf("input"), "", new Attributes());
        formElement.addElement(element);
        element.attr("name", "password");
        element.attr("value", "pass123");

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());
        assertTrue(formData.contains(HttpConnection.KeyVal.create("username","user123")));
        assertTrue(formData.contains(HttpConnection.KeyVal.create("password","pass123")));
    }

    @Test
    public void testEquals() {
        FormElement formElement1 = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        FormElement formElement2 = new FormElement(Tag.valueOf("form"), "https://www.example.com", new Attributes());
        assertTrue(formElement1.equals(formElement2));

        formElement2.addElement(new Element(Tag.valueOf("input"), "", new Attributes()));
        assertFalse(formElement1.equals(formElement2));
    }
}