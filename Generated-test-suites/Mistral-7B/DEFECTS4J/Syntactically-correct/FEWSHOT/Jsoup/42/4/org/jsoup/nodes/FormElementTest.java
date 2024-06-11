package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class FormElementTest {

    @Test
    public void newFormElementTest() {
        Tag tag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(tag, baseUri, attributes);
        assertEquals("form", formElement.tagName());
        assertEquals(baseUri, formElement.baseUri());
        assertEquals(attributes, formElement.attributes());
    }

    @Test
    public void getElementsTest() {
        Tag formTag = new Tag("form");
        String baseUri = "http://example.com";
        Attributes attributes = new Attributes();
        FormElement formElement = new FormElement(formTag, baseUri, attributes);
        Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "inputName"));
        Element textareaElement = new Element("textarea", new Attributes().put("name", "textareaName"));
        formElement.addElement(inputElement);
        formElement.addElement(textareaElement);

        Elements elements = formElement.elements();
        assertEquals(2, elements.size());
        assertSame(inputElement, elements.get(0));
        assertSame(textareaElement, elements.get(1));
    }

    @Test
    public void submitTest() {
        String baseUri = "http://example.com";
        String action = "http://example.com/submit";
        Attributes attributes = new Attributes()
                .put("action", action)
                .put("method", "POST");
        Tag formTag = new Tag("form", attributes);

        FormElement formElement = new FormElement(formTag, baseUri, attributes.clone());

        Element inputElement = new Element("input", new Attributes().put("type", "text").put("name", "inputName"));
        Element textareaElement = new Element("textarea", new Attributes().put("name", "textareaName"));
        formElement.addElement(inputElement);
        formElement.addElement(textareaElement);

        inputElement.attr("value", "inputValue");
        textareaElement.attr("value", "textareaValue");

        Connection connection = formElement.submit();
        assertEquals(Connection.Method.POST, connection.method());
        assertEquals(action, connection.url());
        List<Connection.KeyVal> formData = connection.data();
        assertEquals(3, formData.size());
        assertEquals("inputName", formData.get(0).key());
        assertEquals("inputValue", formData.get(0).value());
        assertEquals("textareaName", formData.get(1).key());
        assertEquals("textareaValue", formData.get(1).value());
        assertNull(formData.get(2).key());
        assertNull(formData.get(2).value());
    }

    @Test
    public void formDataTest() {
        String baseUri = "http://example.com";
        String action = "http://example.com/submit";
        Attributes attributes = new Attributes()
                .put("action", action)
                .put("method", "POST");
        Tag formTag = new Tag("form", attributes);

        FormElement formElement = new FormElement(formTag, baseUri, attributes.clone());

        Element inputElement1 = new Element("input", new Attributes().put("type", "text").put("name", "inputName1")
                .put("value", "inputValue1"));
        Element inputElement2 = new Element("input", new Attributes().put("type", "text").put("name", "inputName2")
                .put("value", "inputValue2"));
        Element selectElement = new Element("select", new Attributes().put("name", "selectName"));
        Element optionElement1 = new Element("option", new Attributes().put("value", "optionValue1")
                .put("selected", "selected"));
        Element optionElement2 = new Element("option", new Attributes().put("value", "optionValue2"));
        selectElement.add(optionElement1);
        selectElement.add(optionElement2);
        Element textareaElement = new Element("textarea", new Attributes().put("name", "textareaName")
                .put("value", "textareaValue"));
        formElement.addElement(inputElement1);
        formElement.addElement(inputElement2);
        formElement.addElement(selectElement);
        formElement.addElement(textareaElement);

        List<Connection.KeyVal> formData = formElement.formData();
        assertEquals(6, formData.size());
        assertEquals("inputName1", formData.get(0).key());
        assertEquals("inputValue1", formData.get(0).value());
        assertEquals("inputName2", formData.get(1).key());
        assertEquals("inputValue2", formData.get(1).value());
        assertEquals("selectName", formData.get(2).key());
        assertEquals("optionValue1", formData.get(2).value());
        assertEquals("textareaName", formData.get(3).key());
        assertEquals("textareaValue", formData.get(3).value());
        assertNull(formData.get(4).key());
        assertNull(formData.get(4).value());
        assertNull(formData.get(5).key());
        assertNull(formData.get(5).value());
    }

    @Test
    public void equalsTest() {
        Tag tag1 = new Tag("form");
        String baseUri1 = "http://example.com";
        Attributes attributes1 = new Attributes();

        FormElement formElement1 = new FormElement(tag1, baseUri1, attributes1);

        Tag tag2 = new Tag("form");
        String baseUri2 = "http://example.com";
        Attributes attributes2 = new Attributes();

        FormElement formElement2 = new FormElement(tag2, baseUri2, attributes2);

        FormElement formElement3 = new FormElement(tag1, baseUri1, attributes1);

        assertEquals(formElement1, formElement3);
        assertFalse(formElement1.equals(formElement2));
    }
}