package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;
import org.jsoup.nodes.Tag;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementConstruction() {
        Tag tag = new Tag("form");
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, "", attributes);
        assertEquals("form", formElement.tagName());
        assertTrue(formElement instanceof FormElement);
    }

    @Test
    public void testElementsMethod() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", formAttributes);

        Element inputElement = new Element("input");
        Element textareaElement = new Element("textarea");
        Element selectElement = new Element("select");

        formElement.addElement(inputElement);
        formElement.addElement(textareaElement);
        formElement.addElement(selectElement);

        Elements elements = formElement.elements();
        assertEquals(3, elements.size());
        assertTrue(elements.contains(inputElement));
        assertTrue(elements.contains(textareaElement));
        assertTrue(elements.contains(selectElement));
    }

    @Test
    public void testRemoveChildMethod() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", formAttributes);

        Element inputElement = new Element("input");
        Element textareaElement = new Element("textarea");
        Element selectElement = new Element("select");

        formElement.addElement(inputElement);
        formElement.addElement(textareaElement);
        formElement.addElement(selectElement);

        formElement.removeChild(textareaElement);
        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertFalse(elements.contains(textareaElement));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubmitMethodNullAction() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", formAttributes);
        formElement.submit();
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testSubmitMethodValidAction() {
        String html = "<html><head></head><body><form action='/submit' method='post'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        Connection connection = formElement.submit();
        assertNotNull(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormDataMethodNullForm() {
        List<Connection.KeyVal> formData = FormElement.formData(null);
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testFormDataMethodEmptyForm() {
        Tag formTag = new Tag("form");
        Attributes formAttributes = new Attributes();
        FormElement formElement = new FormElement(formTag, "", formAttributes);
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataMethodSingleInput() {
        String html = "<html><head></head><body><form><input name='name' value='value'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value", keyVal.value());
    }

    @Test
    public void testFormDataMethodMultipleInputs() {
        String html = "<html><head></head><body><form><input name='name1' value='value1'><input name='name2' value='value2'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(2, formData.size());

        Connection.KeyVal keyVal1 = formData.get(0);
        assertEquals("name1", keyVal1.key());
        assertEquals("value1", keyVal1.value());

        Connection.KeyVal keyVal2 = formData.get(1);
        assertEquals("name2", keyVal2.key());
        assertEquals("value2", keyVal2.value());
    }

    @Test
    public void testFormDataMethodSelectElement() {
        String html = "<html><head></head><body><form><select name='name'><option value='value1'>Option 1</option><option value='value2' selected>Option 2</option></select></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertEquals("value2", keyVal.value());
    }

    @Test
    public void testFormDataMethodCheckboxRadio() {
        String html = "<html><head></head><body><form><input type='checkbox' name='name' value='value' checked><input type='checkbox' name='name' value='value'></form></body></html>";
        Document doc = Jsoup.parse(html);
        FormElement formElement = (FormElement) doc.select("form").first();
        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyVal = formData.get(0);
        assertEquals("name", keyVal.key());
        assertTrue(keyVal.value().equals("value") || keyVal.value().equals("on"));
    }
}