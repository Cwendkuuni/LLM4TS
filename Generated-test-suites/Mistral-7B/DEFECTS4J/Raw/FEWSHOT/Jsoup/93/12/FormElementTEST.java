package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormElementTest {
    @Test
    public void testFormElementCreation() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit");

        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tag().name());
        assertEquals("/submit", formElement.attr("action"));
    }

    @Test
    public void testElements() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        formElement.addElement(inputElement);

        Elements elements = formElement.elements();
        assertEquals(1, elements.size());
        assertTrue(elements.get(0) instanceof Element);
        assertEquals("input", elements.get(0).tag().name());
        assertEquals("username", elements.get(0).attr("name"));
    }

    @Test
    public void testRemoveChild() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        formElement.addElement(inputElement);

        Element buttonElement = new Element("button");
        formElement.addElement(buttonElement);

        formElement.removeChild(inputElement);
        assertEquals(1, formElement.elements().size());
        assertNull(formElement.elements().get(0).tag());
    }

    @Test
    public void testSubmit() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        attributes.put("action", "/submit");

        FormElement formElement = new FormElement(tag, baseUri, attributes);

        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        formElement.addElement(inputElement);

        Connection connection = formElement.submit();
        assertNotNull(connection);
        assertNotNull(connection.url());
        assertTrue(connection.url().startsWith(baseUri + "/submit"));
    }

    @Test
    public void testFormData() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Element inputElement1 = new Element("input");
        inputElement1.attr("type", "text");
        inputElement1.attr("name", "username");
        inputElement1.attr("value", "John");
        formElement.addElement(inputElement1);

        Element inputElement2 = new Element("input");
        inputElement2.attr("type", "text");
        inputElement2.attr("name", "password");
        inputElement2.attr("value", "Password");
        formElement.addElement(inputElement2);

        Element selectElement = new Element("select");
        selectElement.attr("name", "country");
        Element optionElement1 = new Element("option");
        optionElement1.attr("value", "USA");
        optionElement1.text("United States");
        selectElement.addElement(optionElement1);

        Element optionElement2 = new Element("option");
        optionElement2.attr("value", "Canada");
        optionElement2.text("Canada");
        selectElement.addElement(optionElement2);

        formElement.addElement(selectElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(6, formData.size());
        assertEquals("username", formData.get(0).key());
        assertEquals("John", formData.get(0).value());
        assertEquals("password", formData.get(1).key());
        assertEquals("Password", formData.get(1).value());
        assertEquals("country", formData.get(2).key());
        assertEquals("USA", formData.get(2).value());

        formElement.removeChild(inputElement1);
        formData = formElement.formData();
        assertEquals(5, formData.size());
        assertNull(formData.get(0).value());
    }

    @Test
    public void testFormDataWithDisabledInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        inputElement.attr("disabled", "disabled");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithEmptyInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Element inputElement = new Element("input");
        inputElement.attr("type", "text");
        inputElement.attr("name", "username");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        inputElement.attr("value", "");
        formData = formElement.formData();
        assertEquals(0, formData.size());
    }

    @Test
    public void testFormDataWithSelectElement() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Element selectElement = new Element("select");
        selectElement.attr("name", "country");
        formElement.addElement(selectElement);

        Element optionElement1 = new Element("option");
        optionElement1.attr("value", "USA");
        optionElement1.text("United States");
        selectElement.addElement(optionElement1);

        Element optionElement2 = new Element("option");
        optionElement2.attr("value", "Canada");
        optionElement2.text("Canada");
        selectElement.addElement(optionElement2);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        selectElement.select("option[selected]").first().attr("selected", "selected");
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("country", formData.get(0).key());
        assertEquals("USA", formData.get(0).value());
    }

    @Test
    public void testFormDataWithCheckboxOrRadioInput() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes formAttributes = new Attributes();
        formAttributes.put("action", "/submit");

        FormElement formElement = new FormElement(formTag, baseUri, formAttributes);

        Element inputElement = new Element("input");
        inputElement.attr("type", "checkbox");
        inputElement.attr("name", "option1");
        formElement.addElement(inputElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(0, formData.size());

        inputElement.attr("checked", "checked");
        formData = formElement.formData();
        assertEquals(1, formData.size());
        assertEquals("option1", formData.get(0).key());
        assertEquals("on", formData.get(0).value());
    }
}