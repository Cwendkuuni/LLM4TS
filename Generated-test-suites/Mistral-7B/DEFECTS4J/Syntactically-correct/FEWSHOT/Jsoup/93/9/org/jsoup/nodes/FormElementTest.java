package org.jsoup.nodes;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        assertTrue(formElement instanceof FormElement);
        assertEquals(tag, formElement.tag());
    }

    @Test
    public void testGetElements() {
        Tag tag = new Tag("form");
        FormElement formElement = new FormElement(tag, "", new Attributes());
        Elements elements = formElement.elements();
        assertTrue(elements instanceof Elements);
        assertEquals(0, elements.size());
    }

    @Test
    public void testAddElement() {
        Tag tagForm = new Tag("form");
        Tag tagInput = new Tag("input");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        Element element = new Element(tagInput, "", new Attributes());

        formElement.addElement(element);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element, elements.get(0));
    }

    @Test
    public void testRemoveChild() {
        Tag tagForm = new Tag("form");
        Tag tagInput1 = new Tag("input");
        Tag tagInput2 = new Tag("input");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        Element element1 = new Element(tagInput1, "", new Attributes());
        Element element2 = new Element(tagInput2, "", new Attributes());

        formElement.addElement(element1);
        formElement.addElement(element2);

        formElement.removeChild(element1);
        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertEquals(element2, elements.get(0));
    }

    @Test
    public void testSubmit() {
        String baseUri = "http://example.com";
        Tag tagForm = new Tag("form", "action", baseUri);
        FormElement formElement = new FormElement(tagForm, baseUri, new Attributes());

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertEquals(Connection.Method.GET, connection.method());
        assertEquals(baseUri, connection.url());
    }

    @Test(expected = IllegalStateException.class)
    public void testSubmitWithoutBaseUri() {
        Tag tagForm = new Tag("form", "action", "");
        FormElement formElement = new FormElement(tagForm, "", new Attributes());
        formElement.submit();
    }

    @Test
    public void testFormData() {
        String baseUri = "http://example.com";
        Tag tagForm = new Tag("form", "action", baseUri);

        // Add text input
        Tag tagInputText = new Tag("input", "type", "text", "name", "nameText", "value", "valueText");
        Element elementText = new Element(tagInputText, "", new Attributes());
        FormElement formElement = new FormElement(tagForm, baseUri, new Attributes());
        formElement.addElement(elementText);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(1, formData.size());
        Connection.KeyVal keyValText = formData.get(0);
        assertEquals("nameText", keyValText.key());
        assertEquals("valueText", keyValText.value());

        // Add Select input
        Tag tagSelect = new Tag("select", "name", "nameSelect");
        Element elementSelect = new Element(tagSelect, "", new Attributes());
        Elements options = new Elements();
        options.add(new Element(new Tag("option", "value", "valueOption1"), "", new Attributes()));
        options.add(new Element(new Tag("option", "value", "valueOption2"), "", new Attributes()));
        elementSelect.add(options);
        formElement.addElement(elementSelect);
        formData = formElement.formData();
        assertEquals(2, formData.size());
        Connection.KeyVal keyValSelect1 = formData.get(0);
        assertEquals("nameSelect", keyValSelect1.key());
        assertEquals("valueOption1", keyValSelect1.value());
        Connection.KeyVal keyValSelect2 = formData.get(1);
        assertEquals("nameSelect", keyValSelect2.key());
        assertEquals("valueOption2", keyValSelect2.value());

        // Add checkbox input
        Tag tagCheckbox = new Tag("input", "type", "checkbox", "name", "nameCheckbox");
        Element elementCheckbox = new Element(tagCheckbox, "", new Attributes());
        formElement.addElement(elementCheckbox);
        formElement.addElement(elementCheckbox);
        formData = formElement.formData();
        assertEquals(3, formData.size());
        Connection.KeyVal keyValCheckbox1 = formData.get(2);
        assertEquals("nameCheckbox", keyValCheckbox1.key());
        assertTrue(keyValCheckbox1.value().equals("on"));
    }
}