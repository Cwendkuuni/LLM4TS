package org.jsoup.nodes;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class FormElementTest {

    @Test
    public void testFormElementConstructor() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        assertNotNull(formElement);
        assertEquals("form", formElement.tagName());
    }

    @Test
    public void testElementsMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        assertNotNull(formElement.elements());
    }

    @Test
    public void testAddElementMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        Element element = new Element(Tag.valueOf("input"), "", new Attributes());
        FormElement returnedFormElement = formElement.addElement(element);
        assertNotNull(returnedFormElement);
        assertTrue(returnedFormElement.hasText());
    }

    @Test
    public void testSubmitMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test
    public void testFormDataMethod() {
        Tag tag = Tag.valueOf("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        List<Connection.KeyVal> formData = formElement.formData();
        assertNotNull(formData);
    }

}