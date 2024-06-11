package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.ElementParser;
import org.jsoup.parser.TagParser;
import org.jsoup.select.Elements;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        attributes.put("id", "myForm");
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        assertEquals("form", formElement.tagName());
        assertEquals("http://example.com", formElement.baseUri());
        assertEquals("myForm", formElement.id());
    }

    @Test
    public void testFormElementElements() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);
        Elements elements = formElement.elements();
        assertSame(formElement, elements.parent());
        assertFalse(elements.isEmpty());
    }

    @Test
    public void testFormElementAddElement() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);

        Tag inputTag = new Tag("input");
        Attributes inputAttributes = new Attributes();
        inputAttributes.put("type", "text");
        inputAttributes.put("name", "username");
        Element inputElement = new Element("input", inputAttributes);

        formElement.addElement(inputElement);
        Elements formElements = formElement.elements();
        assertTrue(formElements.contains(inputElement));
    }

    @Test
    public void testFormElementRemoveChild() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "http://example.com", attributes);

        Tag inputTag = new Tag("input");
        Attributes inputAttributes = new Attributes();
        inputAttributes.put("type", "text");
        inputAttributes.put("name", "username");
        Element inputElement = new Element("input", inputAttributes);
        formElement.addElement(inputElement);

        Elements formElements = formElement.elements();
        assertEquals(1, formElements.size());

        formElement.removeChild(inputElement);
        formElements = formElement.elements();
        assertEquals(0, formElements.size());
    }

    @Test
    public void testSubmitForm() throws Exception {
        String html = "<html><body><form action='http://example.com' method='post'><input type='text' name='username' value='test'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals("POST", connection.method().name());
        assertEquals("http://example.com", connection.url().toString());
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("username", keyVal.key());
        assertEquals("test", keyVal.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitFormNoAction() {
        String html = "<html><body><form method='post'><input type='text' name='username' value='test'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        formElement.submit();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitFormEmptyAction() {
        String html = "<html><body><form action='' method='post'><input type='text' name='username' value='test'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        formElement.submit();
    }

    @Test
    public void testFormDataSelect() {
        String html = "<html><body><form><select name='color'><option value='red' selected></option><option value='green'></option></select></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("color", keyVal.key());
        assertEquals("red", keyVal.value());
    }

    @Test
    public void testFormDataCheckbox() {
        String html = "<html><body><form><input type='checkbox' name='agree' value='yes' checked></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("agree", keyVal.key());
        assertEquals("yes", keyVal.value());
    }

    @Test
    public void testFormDataRadio() {
        String html = "<html><body><form><input type='radio' name='gender' value='male' checked><input type='radio' name='gender' value='female'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("gender", keyVal.key());
        assertEquals("male", keyVal.value());
    }

    @Test
    public void testFormDataEmpty() {
        String html = "<html><body><form></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }
}